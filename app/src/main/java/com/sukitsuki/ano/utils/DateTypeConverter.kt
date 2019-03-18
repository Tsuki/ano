package com.sukitsuki.ano.utils

import androidx.room.TypeConverter
import java.sql.Date

class DateTypeConverter {
  @TypeConverter
  fun toDate(value: Date): Long {
    return value.time
  }

  @TypeConverter
  fun toLong(value: Long): Date {
    return Date(value)
  }
}
