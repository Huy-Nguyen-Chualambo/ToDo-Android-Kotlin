package com.example.todoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String = "",
    val isDone: Boolean = false,
    val deadline: LocalDateTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
