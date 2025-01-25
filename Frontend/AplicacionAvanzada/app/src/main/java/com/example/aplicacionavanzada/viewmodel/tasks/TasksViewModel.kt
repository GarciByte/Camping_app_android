package com.example.aplicacionavanzada.viewmodel.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.aplicacionavanzada.MyTasksApplication
import com.example.aplicacionavanzada.model.tasks.MyTask
import com.example.aplicacionavanzada.model.tasks.MyTasksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TasksViewModel(private val myTasksRepository: MyTasksRepository) : ViewModel() {
    fun getAllTasks(): Flow<List<MyTask>> = myTasksRepository.getAll()

    fun getPendingTaskCount(): Flow<Int> = myTasksRepository.getPendingTaskCount()

    fun addTask(taskName: String) = viewModelScope.launch {
        myTasksRepository.insertTask(MyTask(name = taskName, done = false))
    }

    fun updateTaskStatus(taskId: Int, done: Boolean) = viewModelScope.launch {
        myTasksRepository.updateTaskStatus(taskId, done)
    }

    fun deleteTask(myTask: MyTask) = viewModelScope.launch {
        myTasksRepository.deleteTask(myTask)
    }

    fun deleteAllTasks(allMyTasks: List<MyTask>) = viewModelScope.launch {
        myTasksRepository.deleteAllMyTasks(allMyTasks)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MyTasksApplication)
                TasksViewModel(application.container.myTasksRepository)
            }
        }
    }
}