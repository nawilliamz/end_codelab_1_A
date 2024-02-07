package com.example.android.architecture.blueprints.todoapp.data.source

import androidx.lifecycle.LiveData
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import java.lang.Error
import kotlin.Exception

class FakeDataSource(var tasks:MutableList<Task>? = mutableListOf()):TasksDataSource {
    override fun observeTasks(): LiveData<Result<List<Task>>> {
        TODO("Not yet implemented")
    }


    //Important test note here: assuming let() is run, tasks (which comes from the FakeDataSource
    //constructor, become equal to the data property for Result

    override suspend fun getTasks(): Result<List<Task>> {
        //Remember, here the tasks list is being "inserted" into an ArrayList by adding it in
        //to the ArrayList constructor
        tasks?.let { return Result.Success(ArrayList(it)) }
        return Result.Error(Exception("Tasks not found"))
    }

    override suspend fun refreshTasks() {
        TODO("Not yet implemented")
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(taskId: String): Result<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshTask(taskId: String) {
        TODO("Not yet implemented")
    }


    override suspend fun saveTask(task: Task) {
        tasks?.add(task)
    }

    override suspend fun completeTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun completeTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearCompletedTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTasks() {
        tasks?.clear()
    }

    override suspend fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }


}