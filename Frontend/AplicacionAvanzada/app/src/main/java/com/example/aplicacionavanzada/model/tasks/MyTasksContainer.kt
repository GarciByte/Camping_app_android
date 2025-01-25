package com.example.aplicacionavanzada.model.tasks

import android.content.Context

class MyTasksContainer(private val context: Context) {
    val myTasksRepository: MyTasksRepository by lazy {
        MyTasksRepository(MyTasksDatabase.getMyTasksDatabase(context).myTasksDao())
    }
}