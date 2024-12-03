package data.source
import config.AppConstant
import data.local.AppLocalDatabase
import data.local.dao.TaskDao
import data.local.entity.Task
import data.result.Result
import domain.DateTimeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.SortedSet
import java.util.TreeSet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskDataSource @Inject constructor(
    private val db : AppLocalDatabase,
    private val taskDao : TaskDao) {

    private val cachedDates : SortedSet<Long> = TreeSet()
    private val cachedTasks = mutableMapOf<Long, MutableList<Task>>()

    private fun cacheLoadedDay(dates : List<Long>) = synchronized(cachedDates) {
        cachedDates.addAll(dates)
    }

    private fun cacheMapOfTasks(date : Map<Long, List<Task>>?) = date?.forEach {
        cachedTasks[it.key] = it.value.toMutableList()
    }

    private fun cacheListOfTasks(date : Long, data : List<Task>?) = data?.let {
        cachedTasks[date] = it.toMutableList()
    }

    suspend fun getTaskById(id : String) : Task? {
        cachedTasks.values.forEach {
            it.find { it.id == id }?.let {
                return it
            }
        }
        return withContext(Dispatchers.IO) {
            return@withContext taskDao.getTaskById(id)
        }
    }

    suspend fun getWeekTask(userId : String, weekDay : LocalDate) = withContext(Dispatchers.IO) {
        val dates = DateTimeUseCase().getWeekDays(weekDay).map {
            DateTimeUseCase().convertDateStringIntoLong("${it.dayOfMonth}/${it.monthValue}/${it.year}")
        }
        val loadRange = filterDateRange(dates)
        fillDateRange(loadRange)
        if(loadRange.isEmpty()) {
            return@withContext getTasksFromCacheInRangeIfAvailable(dates)
        }
        taskDao.getTasksInDateRange(userId, loadRange, loadRange[0], loadRange.size, AppConstant.MILLISECOND_IN_DAY)?.let {
            cacheMapOfTasks(it)
            cacheLoadedDay(loadRange)
        }
        return@withContext getTasksFromCacheInRangeIfAvailable(dates)
    }

    private fun getTasksFromCacheInRangeIfAvailable(range: List<Long>) : Map<Long, List<Task>> {
        val result = mutableMapOf<Long, List<Task>>()
        for(date in range) {
            result[date] = cachedTasks[date] ?: emptyList()
        }
        return result.toMap()
    }

    private fun fillDateRange(range : MutableList<Long>) {
        if (range.size < 2) return
        while (range[range.size - 1] - range[range.size - 2] > AppConstant.MILLISECOND_IN_DAY) {
            val nextDay = range[range.size - 2].plus(AppConstant.MILLISECOND_IN_DAY)
            range.add(range.size - 1, nextDay)
        }
    }

    private fun filterDateRange(dateRange : List<Long>) : MutableList<Long> {
        val result = mutableListOf<Long>()
        var left = -1
        var step = 0
        while(step < dateRange.size) {
            if(!cachedDates.contains(dateRange[step])) {
                left = step
                result.add(dateRange[step])
                break
            }
            step++
        }
        if (left == -1) return result
        step = dateRange.size - 1
        while (step > 0) {
            if(step == left) break
            if(!cachedDates.contains(dateRange[step])) {
                result.add(dateRange[step])
                break
            }
            step--
        }
        return result
    }

    suspend fun getTaskByDate(userId : String, date : Long) : MutableList<Task>? {
        var result : List<Task>? = null
        if(!cachedTasks.containsKey(date)) {
            result = taskDao.getTasksAtDate(userId, date)
            coroutineScope {
                launch {
                    cacheListOfTasks(date, result)
                    cacheLoadedDay(listOf(date))
                }
            }
        }
        return result?.toMutableList() ?: cachedTasks[date]
    }

    suspend fun addTask(task: Task) = withContext(Dispatchers.IO) {
        try {
            db.runInTransaction {
                taskDao.addTask(task)
            }
            val range = DateTimeUseCase().getDateBetween(task.startDate, task.endDate)
            for(date in range) {
                if(cachedTasks.containsKey(date)) {
                    cachedTasks[date]?.add(task)
                    continue
                }
                if(!cachedDates.contains(date)) cachedDates.add(date)
                cachedTasks[date] = mutableListOf(task)
            }
            return@withContext Result.Success
        } catch (e : Exception) {
            return@withContext Result.Error(e.message)
        }
    }

    suspend fun getOnGoingTaskAtDate(userId : String, date : Long) : MutableList<Task> {
        val result = mutableListOf<Task>()
        val resultAtDate = getTaskByDate(userId, date)
        resultAtDate?.let {
            reorderTask(resultAtDate, result)
        }
        return result
    }

    private fun reorderTask(task : MutableList<Task>, resultContainer : MutableList<Task>) {
        val highPriority = mutableListOf<Task>()
        val mediumPriority = mutableListOf<Task>()
        val lowPriority = mutableListOf<Task>()
        val now = LocalDateTime.now()
        task.forEach {
            when(it.priority) {
                AppConstant.PRIORITY_HIGH -> addTaskToOnGoingList(now, it, highPriority)
                AppConstant.PRIORITY_MEDIUM -> addTaskToOnGoingList(now, it, mediumPriority)
                AppConstant.PRIORITY_LOW -> addTaskToOnGoingList(now, it, lowPriority)
            }
        }
        resultContainer.addAll(lowPriority)
        resultContainer.addAll(resultContainer.size, mediumPriority)
        resultContainer.addAll(resultContainer.size, highPriority)
    }

    private fun addTaskToOnGoingList(now : LocalDateTime, target : Task, list : MutableList<Task>) {
        val targetEndTime = DateTimeUseCase().combineDateAndTimeFromTask(target.endDate, target.endTime)
        val targetDiff = DateTimeUseCase().calculateTimeDiff(now, targetEndTime)
        var insertIndex = 0;
        var endTime : LocalDateTime
        if(target.state == AppConstant.TASK_STATE_FINISHED) return
        if(targetEndTime.isBefore(now) || targetEndTime.isEqual(now)) return
        for (i in list.indices) {
            endTime = DateTimeUseCase().combineDateAndTimeFromTask(list[i].endDate, list[i].endTime)
            val timeDiff = DateTimeUseCase().calculateTimeDiff(now, endTime)
            insertIndex = if(targetDiff < timeDiff) i + 1 else break
        }
        list.add(insertIndex, target)
    }

    private fun updateTaskInCache(oldTask : Task, newTask: Task) {
        cachedTasks.values.forEach {
            if(it.contains(oldTask)) {
                val index = it.indexOf(oldTask)
                it[index] = newTask
            }
        }
    }

    suspend fun finishTask(taskId: String) = withContext(Dispatchers.IO) {
        try {
            val _task = getTaskById(taskId)!!
            val task = _task.copy(state = AppConstant.TASK_STATE_FINISHED)
            db.runInTransaction {
                taskDao.updateTask(task)
            }
            updateTaskInCache(_task, task)
            return@withContext Result.Success
        } catch (e : Exception) {
            return@withContext Result.Error(e.message)
        }
    }

    suspend fun deleteTask(taskId : String) = withContext(Dispatchers.IO) {
        try {
            db.runInTransaction {
                taskDao.deleteTask(taskId)
            }
            deleteTaskFromCache(taskId)
            return@withContext Result.Success
        } catch (e : Exception) {
            return@withContext Result.Error(e.message)
        }
    }

    private fun deleteTaskFromCache(taskId : String) {
        cachedTasks.values.forEach {
            it.removeIf {task ->
                taskId == task.id
            }
        }
    }

    fun clearCacheDataOnSignOut() {
        cachedDates.clear()
        cachedTasks.clear()
    }
}