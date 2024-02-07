/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith




//Because we're now passing in a tasksRepository into our view model rather than an application, we're
//we actually are no longer using that AndroidX Test application provider code. So I can remove the
//@RunWith(AndroidUnit4::class runner annotation code. Since I'm no longer using the AndroidX Test
//runner to simulate an Android environment,I'll notice that my tests measurably speed up when I
//run them. (recall that you need these component of Application code to run the tests previously which
//is why the runner code was needed)
//Notice that we aren't using Robelectric anymore either. So, even after the big refactor of making
//the repository and using that instead, the code still works. The fake repository doesn't contain any
//of the data sources or any of that complexity. 

//What exactly am I replacing with this fake repository?
//@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {

    private lateinit var tasksRepository:FakeTestRepository

    // Subject under test
    private lateinit var tasksViewModel: TasksViewModel

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        tasksRepository = FakeTestRepository()
        val task1 = Task("title1", "description1")
        val task2 = Task("title2", "description2", true)
        val task3 = Task("title3", "description3", true)
        tasksRepository.addTasks(task1, task2, task3)

        //Note how we are directly constructing our view model here in our rest so there's no need
        // for a delegate property
        tasksViewModel = TasksViewModel(tasksRepository)
    }


    @Test
    fun addNewTask_setsNewTaskEvent() {
        // When adding a new task
        tasksViewModel.addNewTask()

        // Then the new task event is triggered
        val value = tasksViewModel.newTaskEvent.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled(), not(nullValue()))


    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible() {
        // When the filter type is ALL_TASKS
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)

        // Then the "Add task" action is visible
        assertThat(tasksViewModel.tasksAddViewVisible.getOrAwaitValue(), `is`(true))
    }

}
