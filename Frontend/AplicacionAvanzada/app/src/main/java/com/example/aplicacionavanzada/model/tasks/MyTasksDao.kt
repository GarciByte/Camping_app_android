package com.example.aplicacionavanzada.model.tasks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MyTasksDao {
    @Query("SELECT * FROM camping_tasks_v2")
    fun getAll(): Flow<List<MyTask>>

    @Query("SELECT COUNT(*) FROM camping_tasks_v2 WHERE done = 0")
    fun getPendingTaskCount(): Flow<Int>

    @Query("UPDATE camping_tasks_v2 SET done = :done WHERE id = :taskId")
    suspend fun updateTaskStatus(taskId: Int, done: Boolean)

    @Insert
    suspend fun insertTask(myTask: MyTask)

    @Delete
    suspend fun deleteTask(myTask: MyTask)

    @Delete
    suspend fun deleteAllMyTasks(allMyTasks: List<MyTask>)
}