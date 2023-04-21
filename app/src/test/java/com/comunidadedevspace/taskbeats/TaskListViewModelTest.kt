package com.comunidadedevspace.taskbeats

import com.comunidadedevspace.taskbeats.data.Task
import com.comunidadedevspace.taskbeats.data.TaskDao
import com.comunidadedevspace.taskbeats.presentation.ActionType
import com.comunidadedevspace.taskbeats.presentation.TaskAction
import com.comunidadedevspace.taskbeats.presentation.TaskListViewModel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class TaskListViewModelTest {
    private val taskDao : TaskDao = mock() // permite fazer testes dublês

    private val undertest : TaskListViewModel by lazy {
        TaskListViewModel(taskDao, UnconfinedTestDispatcher())
    }

    //Testes cases Delete_All

    @Test
    // Para testar quando tem Courotines tem que usar o runTest
    fun delete_all() = runTest{
        //Given
        val taskAction = TaskAction(task = null, actionType = ActionType.DELETE_ALL.name)
        //When
        undertest.execute(taskAction)
        //Then
        verify(taskDao).deleteAll()
    }

    @Test
    fun update_task() = runTest{

        //Given
        val task = Task(1, title = "title", description = "description")

        val taskAction = TaskAction(
            task = task,
            actionType = ActionType.UPDATE.name)
        //When
        undertest.execute(taskAction)
        //Then
        verify(taskDao).update(task)
    }

    @Test
    fun delete_task() = runTest{

        //Given
        val task = Task(1, title = "title", description = "description")

        val taskAction = TaskAction(
            task = task,
            actionType = ActionType.DELETE.name)
        //When
        undertest.execute(taskAction)
        //Then
        verify(taskDao).deleteById(task.id)
    }

    @Test
    fun create_task() = runTest{
        //Given
        val task = Task(1, title = "title", description = "description")
        val taskAction = TaskAction(
            task = task,
            actionType = ActionType.CREATE.name)
        //When
        undertest.execute(taskAction)

        //Then
        verify(taskDao).insert(task)
    }
}