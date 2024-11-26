package domain
import android.annotation.SuppressLint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Date

class DateTimeUseCase {

    @SuppressLint("SimpleDateFormat")
    fun convertLongToDateString(time : Long) : String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy")
        return format.format(date)
    }

    fun convertDateInToString(date : LocalDate) : String {
        return "${date.dayOfMonth}/${date.monthValue}/${date.year}"
    }

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