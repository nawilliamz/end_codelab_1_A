package com.example.android.architecture.blueprints.todoapp.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.runBlocking

class FakeTestRepository:TasksRepository {

    //This LinkedHashMap represents the data that comes back from the database/network.
    var tasksServiceData:LinkedHashMap<String, Task> = LinkedHashMap()

    //This LiveData is here so that we can return something from observeTasks() method below
    private val observableTasks = MutableLiveData<Result<List<Task>>>()

    //***Implement this one
    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        return Result.Success(tasksServiceData.values.toList())
    }

    //**Implement this one
    override suspend fun refreshTasks() {
        observableTasks.value = getTasks()
    }

    //**Implement this one
    override fun observeTasks(): LiveData<Result<List<Task>>> {
        runBlocking { refreshTasks() }
        return observableTasks
    }

    override suspend fun refreshTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override fun observeTask(taskId: String): LiveData<Result<Task?>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun saveTask(task: Task) {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

    //addTasks() method moves all tasks from remote DB to localDB so the tasks lists is equal b/n them
    //Use this method instead of using the saveTask() method above multiple times
    //Often, when testing, you're going to want some way to modify the state of the repository.
    //Recall that tasksServiceData represents the data returned from our DB/network
    fun addTasks (vararg tasks:Task) {
        for (task in tasks) {
            //This code picks task out of our list of result tasks, and re-sets it to that same task.
            //Then it performs this action for every task in tasks
            tasksServiceData[task.id] = task
        }
        //refreshTasks() runs updateTaskFromRemoteDataSource() which deletes all tasks present in local
        //DB and the saves all tasks currently in remote data source to local data source
        runBlocking { refreshTasks() }
    }
}