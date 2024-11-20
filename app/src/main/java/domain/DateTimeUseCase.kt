package domain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate

class DateTimeUseCase {

    suspend fun getWeekDays(date : LocalDate) : List<LocalDate> {
        val dayOfWeek = mutableListOf<LocalDate>()
        withContext(Dispatchers.Default) {
            val firstDayOfWeek = date.with(DayOfWeek.MONDAY)
            for (i in 0..6) {
                dayOfWeek.add(firstDayOfWeek.plusDays(i.toLong()))
            }
        }
        return dayOfWeek.toList()
    }
}