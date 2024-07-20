package ch.makery.todo.util

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object DateTimeUtil {
  val DATE_PATTERN = "dd.MM.yyyy"
  val TIME_PATTERN = "HH:mm"

  val DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN)
  val TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN)

  implicit class DateFormatter(val date: LocalDate) {

    def asDateString: String = {
      if (date == null) {
        return null
      }
      DATE_FORMATTER.format(date)
    }
  }

  implicit class TimeFormatter(val time: LocalTime) {

    def asTimeString: String = {
      if (time == null) {
        return null
      }
      TIME_FORMATTER.format(time)
    }
  }

  implicit class StringFormatter(val data: String) {

    def parseLocalDate: LocalDate = {
      try {
        LocalDate.parse(data, DATE_FORMATTER)
      } catch {
        case e: DateTimeParseException => null
      }
    }

    def parseLocalTime: LocalTime = {
      try {
        LocalTime.parse(data, TIME_FORMATTER)
      } catch {
        case e: DateTimeParseException => null
      }
    }

    def isValidDate: Boolean = {
      data.parseLocalDate != null
    }

    def isValidTime: Boolean = {
      data.parseLocalTime != null
    }
  }
}
