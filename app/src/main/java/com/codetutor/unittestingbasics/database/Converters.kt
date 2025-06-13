package com.codetutor.unittestingbasics.database

import androidx.room.TypeConverter
import com.codetutor.unittestingbasics.model.DriveSide

class Converters {
    @TypeConverter
    fun fromSide(d: DriveSide) = d.name

    @TypeConverter
    fun toSide(s: String) = DriveSide.valueOf(s)
}
