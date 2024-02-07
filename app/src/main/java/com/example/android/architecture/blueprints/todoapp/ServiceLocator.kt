package com.example.android.architecture.blueprints.todoapp

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.example.android.architecture.blueprints.todoapp.data.source.DefaultTasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.local.TasksLocalDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.local.ToDoDatabase
import com.example.android.architecture.blueprints.todoapp.data.source.remote.TasksRemoteDataSource
import kotlinx.coroutines.runBlocking


//Convert to singleton by changing to object

object ServiceLocator {

    private val lock = Any()

    private var database:ToDoDatabase? = null

    //Marking it with @Volatile prevents multiple threads from accessing the object simultaneously
    @Volatile
    var tasksRepository: TasksRepository? = null
        //@VisibleForTesting marks the setter for the tasksRepository as visible for testing
        @VisibleForTesting set





    init {


    }

    //A context will be needed to set up the database
    fun provideTasksRepository(context: Context):TasksRepository {

        //Wrapping the code in synchronized block prevents it from being accessed by multiple threads
        //and preventing creation of multiple repositories
        synchronized(this) {
            return tasksRepository ?: createTasksRepository(context)
        }

    }

    private fun createTasksRepository(context:Context):TasksRepository {
        val newRepo = DefaultTasksRepository(TasksRemoteDataSource, createTaskLocalDataSource(context))

        tasksRepository = newRepo
        return newRepo

    }

    private fun createTaskLocalDataSource(context: Context):TasksDataSource {
        val database = database ?: createDataBase(context)
        return TasksLocalDataSource(database.taskDao())
    }

    private fun createDataBase(context: Context): ToDoDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            ToDoDatabase::class.java,
            "Tasks.db"
        ).build()
        database = result
        return result
    }

    //We've marked this as visible for testing because you're only going to ever want to reset the repository
    //from within tests
    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            //Note that we're using runBlocking and not runBlockingTest here because this is inside of a test double
            // and not a test class.
            runBlocking {
                TasksRemoteDataSource.deleteAllTasks()
            }
            //Clear all data to avoid test pollutions
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            tasksRepository = null
        }


    }

}