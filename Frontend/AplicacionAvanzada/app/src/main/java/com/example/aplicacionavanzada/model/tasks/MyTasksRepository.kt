package com.example.aplicacionavanzada.model.tasks

class MyTasksRepository(private val myTasksDao: MyTasksDao) {
    fun getAll() = myTasksDao.getAll()

    fun getPendingTaskCount() = myTasksDao.getPendingTaskCount()

    suspend fun insertTask(myTask: MyTask) = myTasksDao.insertTask(myTask)

    suspend fun updateTaskStatus(taskId: Int, done: Boolean) = myTasksDao.updateTaskStatus(taskId, done)

    suspend fun deleteTask(myTask: MyTask) = myTasksDao.deleteTask(myTask)

    suspend fun deleteAllMyTasks(allMyTasks: List<MyTask>) = myTasksDao.deleteAllMyTasks(allMyTasks)
}