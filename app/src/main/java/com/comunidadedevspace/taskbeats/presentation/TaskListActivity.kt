package com.comunidadedevspace.taskbeats.presentation


import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.result.ActivityResult
import androidx.lifecycle.Observer
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.data.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable

class TaskListActivity : AppCompatActivity() {


    private lateinit var ctnContent: LinearLayout

    //adapter
    private val adapter: TaskListAdapter = TaskListAdapter(::onListItemClicked)

    private val viewModel: TaskListViewModel by lazy {
        TaskListViewModel.create(application)
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                //Pegando resultado
                val data = result.data
                val taskAction = data?.getSerializableExtra(TASK_ACTION_RESULT) as TaskAction
                viewModel.execute(taskAction)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        setSupportActionBar(findViewById(R.id.toolbar))

        ctnContent = findViewById(R.id.ctn_content)

        //RecyclerView
        val rvTasks: RecyclerView = findViewById(R.id.rv_task_list)
        rvTasks.adapter = adapter

        val fab = findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            openTaskListDetail(null)
        }

    }

    override fun onStart() {
        super.onStart()
        //Somente aqui é necessário ter o listFromDataBase(), haverá atualizações automáticas na base de dados devido ao live data
        listFromDataBase()
    }


    private fun deleteAll() {
        val taskAction = TaskAction(null,ActionType.DELETE_ALL.name)
        viewModel.execute(taskAction)
    }

    private fun listFromDataBase() {

        //Observer
        //Toda atualização do LiveData vai cair sempre aqui:
        val listObserver = Observer<List<Task>> { listTasks ->
            if(listTasks.isEmpty()){
                ctnContent.visibility = View.VISIBLE
            }else{
                ctnContent.visibility = View.GONE
            }
            adapter.submitList(listTasks)
        }

        //Live Data (atrelando ao Observer)
        //Toda vez que tiver uma mudança na base de dados não precisará chamar o listFromDataBse novamente
        viewModel.taskListLiveData.observe(this@TaskListActivity,listObserver)

    }

    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }

    private fun onListItemClicked(task: Task) {
        openTaskListDetail(task)

    }

    // task: Task? = null é um default argument
    private fun openTaskListDetail(task: Task?) {
        val intent = TaskDetailActivity.start(this, task)
        startForResult.launch(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_task_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_task -> {
                //Deletar todas as tarefas
                deleteAll()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}

enum class ActionType {
    DELETE,
    DELETE_ALL,
    UPDATE,
    CREATE

}

data class TaskAction(val task: Task?, val actionType: String) : Serializable

const val TASK_ACTION_RESULT = "TASK_ACTION_RESULT"