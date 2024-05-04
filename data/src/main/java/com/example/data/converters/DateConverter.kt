package com.example.data.converters

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun timeStampToDate(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }
    @TypeConverter
    fun dateToTimeStamp(date: Date?): Long? {
        return date?.time
    }
}