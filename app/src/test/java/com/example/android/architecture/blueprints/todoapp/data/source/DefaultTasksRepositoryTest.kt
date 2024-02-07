package com.example.android.architecture.blueprints.todoapp.data.source

//import org.junit.jupiter.api.Assertions.*
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.core.IsEqual
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DefaultTasksRepositoryTest {

    private val task1 = Task("Title1", "Description1")
    private val task2 = Task("Title2", "Description2")
    private val task3 = Task("Title3", "Description3")
    private val remoteTasks = listOf(task1, task2).sortedBy { it.id }
    private val localTasks = listOf(task3).sortedBy { it.id }
    private val newTasks = listOf(task3).sortedBy { it.id }


    //these face data sources are using our FakeDataSource test class
    private lateinit var tasksRemoteDataSource: FakeDataSource
    private lateinit var tasksLocalDataSource: FakeDataSource

    //Class under test
    private lateinit var tasksRepository: DefaultTasksRepository

    @Before
    fun createRepository() {

        //***This is the crucial step in the test. remoteTasks, which is substituting for our list of tasks in
        //RemoteDataSource, is used in the constructor of FakeDataSource. And we know this List<Task> passed into the
        //constructor is the same list that is returned by the getTasks() method wrapped in a Result [Result<List<Task>>]
        //
        tasksRemoteDataSource = FakeDataSource(remoteTasks.toMutableList())
        tasksLocalDataSource = FakeDataSource(localTasks.toMutableList())

        tasksRepository = DefaultTasksRepository(tasksRemoteDataSource, tasksLocalDataSource,
            Dispatchers.Unconfined)

    }

    //Here, we're creating a real DefaultTasksRepository but initializing it with fake data sources.
    //So, we're verifying that calling getTasks [since forcedUpdate is set to true, we're running updateTasksFromRemoteDataSource
    //by default] from our DefaultTasksRepository returns the same list of
    //tasks as when we create the tasks lists above manually
    @ExperimentalCoroutinesApi
    @Test
    fun getTasks_requestAllTasksFromRemoteDataSource() = runTest {
        //When tasks are requested from the tasks repository (or we basically force a refresh)

        //getTasks() method is the face method as defined in FakdDataSource. It returns a Result. We have to case
        //as a Success since the "fake" method returns a Result without specifying which type
        val tasks = tasksRepository.getTasks(true) as Result.Success

        //**This test will be successful if it passes because I'm able to verify that, since the actual getTasks()
        //method simply retrieves all Tasks current in the remote data source and transfers them to local data source
        //(i.e. it "refreshes" the tasks list in the local data source with those from remote data source.
        // assertThat checks that the "fake"

        //**The "fake" getTasks() method above works well as a stand-in because 1.) it retrieves the tasks in the remote data source,
        //which we have added manually above. This combined with fact that remoteTasks of type List<Task> is fed into the constructor
        //for FakeDataSource above which means that the getTasks() method that is run in tasksRepository above will be the same list
        //of tasks as remoteTasks (or that is what we would expect).

        //Important Note: since remoteTasks variable above is being inputted into FakeDataSource
        //constructor, it is acting as the lists of tasks that gets wrapped in the Result.Success
        //and, therefore, will equal the data property in the Result.Success
        assertThat(tasks.data, IsEqual(remoteTasks))

        //****So the OVERARCHING LOGIC OF THIS TEST is we're evaluating the data property of
        //the Result.Success obtained from running the "fake" getTasks() method in FaceDataSource
        //as compared with the remote tasks (list of Tasks) we get by adding tasks manually into
        //the remoteTasks variable above in this class. They should be equal to each other since we
        //are using remoteTasks as the argument in constructor for FakeDataSource and getTasks() takes
        //that argument and wrappes in a Result.Success which, by the way Result.Success has been
        //defineed means that the argument ultimately equals the Result.Success data property
        //returned by getTasks().
        //****Also, note that using true in getTasks as the boolean arg (along with casting the Result as
        //a Success) forces a refresh of local data and thus also forces the method to return the tasks
        //list that is in remoted data source at the time getTasks is run. So we expect getTasks() to
        //return the tasks list defined above as remote data source rather than local data source. If it
        //were to return the local tasks list as defined above, then the test would fail.


    }

}