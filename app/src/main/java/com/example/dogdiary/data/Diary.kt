package com.example.dogdiary.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary")
data class Diary(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name="feed")
    val feedTotal: Double,
    @ColumnInfo(name="walkhour")
    val walkHour: Int,
    @ColumnInfo(name="walkminute")
    val walkMinute: Int,
    @ColumnInfo(name="bowelscount")
    val bowelsCount: Int,
    @ColumnInfo(name="memo")
    val memo: String,
)
