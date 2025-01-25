package com.example.aplicacionavanzada.model.tasks

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "camping_tasks_v2")
data class MyTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "task_name")
    val name: String,
    @ColumnInfo(name = "done")
    val done: Boolean = false
)