package com.codetutor.unittestingbasics.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class Country(
    @PrimaryKey val name: String,
    @ColumnInfo(name = "drive_side") val driveSide: String
)

enum class DriveSide { LEFT, RIGHT }