package com.example.android.architecture.blueprints.todoapp.taskdetail


import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.Data.Source.FakeAndroidTestRepository
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ServiceLocator
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


//We're annotating the whole class with @ExperimentalCoroutinesAPI since we're running multiple runTests throughout the
//class
@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class TaskDetailFragmentTest{

    private lateinit var repository: TasksRepository

    @Before
    fun initRepository() {
        repository = FakeAndroidTestRepository()
        //Here's where we swap the "real repository" with the fake one.
        ServiceLocator.tasksRepository = repository
    }

    //One thing you need to ensure is that the repository is completely reset between tests and properly cleaned up.
    //Since resetRepository() runs a coroutine, we're going to use runTest here
    @After
    fun cleanupDB () = runTest {
        ServiceLocator.resetRepository()
    }



    //By adding runTest below, we ensure that the codein the code block is run with a coroutine
    @Test
    fun activeTaskDetails_DisplayedInUi() = runTest{



        //GIVEN - Add active (incomplete) task to DB
        //This is the task that we want to see displayed to the fragment
        val activeTask = Task("Active Task", "AndroidX Rocks", false)

        repository.saveTask(activeTask)

        //WHEN - DetailsFragment launched to display task
        val bundle = TaskDetailFragmentArgs(activeTask.id).toBundle()

        //The reason it has been given the AppTheme is because, when using this launchFragmentInContainer
        //the fragment is launched in an empty activity.Because fragments usually inherit their theme from
        //their parent activity, we want to make sure we have the correct theme here
        launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.AppTheme)


        // THEN - Task details are displayed on the screen
        // make sure that the title/description are both shown and correct
        onView(withId(R.id.task_detail_title_text)).check(matches(isDisplayed()))
        onView(withId(R.id.task_detail_title_text)).check(matches(withText("Active Task")))
        onView(withId(R.id.task_detail_description_text)).check(matches(isDisplayed()))
        onView(withId(R.id.task_detail_description_text)).check(matches(withText("AndroidX Rocks")))
        // and make sure the "active" checkbox is shown unchecked
        onView(withId(R.id.task_detail_complete_checkbox)).check(matches(isDisplayed()))
        onView(withId(R.id.task_detail_complete_checkbox)).check(matches(not(isChecked())))

        Thread.sleep(10000)
    }
}