package com.comunidadedevspace.taskbeats.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comunidadedevspace.taskbeats.TaskBeatsApplication
import com.comunidadedevspace.taskbeats.data.Task
import com.comunidadedevspace.taskbeats.data.TaskDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Função de fazer as operações relacionadas a UI, para não deixar nada na UI que não seja mostrar dados para o usuário
class TaskListViewModel(private val taskDao: TaskDao, private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : ViewModel() {

    //Live Data no View Model
    val taskListLiveData : LiveData<List<Task>> = taskDao.getAll()

    fun execute(taskAction: TaskAction){
        when (taskAction.actionType) {
            ActionType.DELETE.name -> deleteById(taskAction.task!!.id)

            ActionType.DELETE_ALL.name-> deleteAll()

            ActionType.CREATE.name -> insertIntoDataBase(taskAction.task!!)

            ActionType.UPDATE.name -> updateIntoDataBase(taskAction.task!!)
        }
    }
    private fun deleteById(id: Int) {
        viewModelScope.launch(dispatcher) {
            taskDao.deleteById(id)
        }
    }

    private fun insertIntoDataBase(task: Task) {
        //Significa que é feito em paralelo
        viewModelScope.launch(dispatcher) {
            taskDao.insert(task)
        }

    }

    private fun updateIntoDataBase(task: Task) {
        viewModelScope.launch(dispatcher) {
            taskDao.update(task)
        }

    }

    private fun deleteAll() {
        viewModelScope.launch(dispatcher) {
            taskDao.deleteAll()
        }
    }

    // Consegue chamar o View Model e ter acesso a fun sem ter que criar um novo View Model
    companion object{
        // é passado o TaskBeatsApplication no construtor para poder ter acesso ao data base
        fun create(application: Application): TaskListViewModel{
            val dataBaseInstance = (application as TaskBeatsApplication).getAppDataBase()
            val dao = dataBaseInstance.taskDao()
            return TaskListViewModel(dao)
        }

    }
}