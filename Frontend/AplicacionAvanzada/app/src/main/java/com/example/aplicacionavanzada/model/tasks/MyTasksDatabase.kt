package com.example.aplicacionavanzada.model.tasks

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MyTask::class], version = 2)
abstract class MyTasksDatabase: RoomDatabase() {
    abstract fun myTasksDao(): MyTasksDao

    companion object {
        @Volatile
        private var Instance: MyTasksDatabase? = null

        fun getMyTasksDatabase(context: Context): MyTasksDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    klass = MyTasksDatabase::class.java,
                    name = "camping_tasks_v2"
                )
                    .build()
                    .also { Instance = it }
            }
        }
    }
}