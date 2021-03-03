package com.bit.temperatureapps.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val data: String,
    val time: String,
    val temperature: String
)