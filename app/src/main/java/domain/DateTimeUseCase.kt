package domain
import android.annotation.SuppressLint
import android.util.Range
import androidx.core.util.rangeTo
import env_variable.AppConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date

class DateTimeUseCase {

    @SuppressLint("SimpleDateFormat")
    fun convertDateStringIntoLong(input : String) : Long {
        val date = SimpleDateFormat(AppConstant.PATTERN_DATE).parse(input)
        return date!!.time
    }

    fun getDateRangeFromStrings(begin : String, end : String) : Range<LocalDate> {
        val beginDate = LocalDate.parse(begin, DateTimeFormatter.ofPattern(AppConstant.PATTERN_DATE))
        val endDate = LocalDate.parse(end, DateTimeFormatter.ofPattern(AppConstant.PATTERN_DATE))
        val range = beginDate.rangeTo(endDate)
        return range 
    }

    @SuppressLint("SimpleDateFormat")
    fun convertLongToDateString(time : Long) : String {
        val date = Date(time)
        val format = SimpleDateFormat(AppConstant.PATTERN_DATE)
        return format.format(date)
    }

    fun convertLongDayOfWeek(key: Long): String {
        val date = LocalDate.parse(convertLongToDateString(key), DateTimeFormatter.ofPattern(
            AppConstant.PATTERN_DATE))
        return date.dayOfWeek.toString().substring(0, 3)
    }

    private fun convertDateInToString(date : LocalDate) : String {
        return "${date.dayOfMonth}/${date.monthValue}/${date.year}"
    }

    fun convertDateIntoLong(date : LocalDate) : Long {
        return convertDateStringIntoLong(convertDateInToString(date))
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

    fun getDateBetween(begin : Long, end : Long) : List<Long> {
        var beginDate = LocalDate.parse(convertLongToDateString(begin), DateTimeFormatter.ofPattern(
            AppConstant.PATTERN_DATE))
        val endDate = LocalDate.parse(convertLongToDateString(end), DateTimeFormatter.ofPattern(
            AppConstant.PATTERN_DATE))
        val dates = mutableListOf(convertDateStringIntoLong(convertDateInToString(beginDate)))
        while (beginDate.isBefore(endDate)) {
            val date = beginDate.plusDays(1)
            dates.add(convertDateStringIntoLong(convertDateInToString(date)))
            beginDate = date
        }
        return dates
    }

    fun combineDateAndTimeFromTask(date : Long, time : String) : LocalDateTime {
        val localDate = LocalDate.parse(convertLongToDateString(date), DateTimeFormatter.ofPattern(
            AppConstant.PATTERN_DATE))
        val localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern(AppConstant.PATTERN_TIME))
        return LocalDateTime.of(localDate, localTime)
    }

    fun calculateTimeDiff(startDate : LocalDateTime, endDate : LocalDateTime) : Long {
        return Duration.between(startDate, endDate).toSeconds()
    }
}