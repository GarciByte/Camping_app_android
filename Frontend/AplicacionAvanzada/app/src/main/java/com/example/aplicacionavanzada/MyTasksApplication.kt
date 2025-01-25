package com.example.aplicacionavanzada

import android.app.Application
import com.example.aplicacionavanzada.model.tasks.MyTasksContainer

class MyTasksApplication : Application() {
    lateinit var container: MyTasksContainer

    override fun onCreate() {
        super.onCreate()
        container = MyTasksContainer(this)
    }
}