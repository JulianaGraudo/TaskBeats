package com.comunidadedevspace.taskbeats.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.data.Task
import com.google.android.material.snackbar.Snackbar

class TaskDetailActivity : AppCompatActivity() {

    private var task: Task? = null
    private lateinit var btnDone : Button

    companion object {
        // o valor dessa constante não pode mudar durante a execução do programa
        // Quando uma constante é declarada como "private", ela só pode ser acessada dentro da classe em que foi declarada
       private const val TASK_DETAIL_EXTRA = "task.extra.detail"

        //Garantir que quem for abrir a TaskDetailActivity passe uma tarefa para ser mostrada
        //Context é quem vai me abrir
        // A própria tela que vai ser aberta tem a responsabilidade de ter certeza que tem os itens que são necessários para abrí-la
        fun start (context: Context, task: Task?) : Intent{
            val intent = Intent(context, TaskDetailActivity::class.java)
                .apply {
                    putExtra(TASK_DETAIL_EXTRA,task) // Como a data class Tarefa é um serializable agora, o putExtra aceita
                }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        setSupportActionBar(findViewById(R.id.toolbar))

        //Recuperar a Task
        task = intent.getSerializableExtra(TASK_DETAIL_EXTRA) as Task?

        val edtTitle : EditText = findViewById(R.id.edt_task_title)
        val edtDescription : EditText = findViewById(R.id.edt_task_description)
        btnDone = findViewById(R.id.btn_done)

        if(task != null){
            edtTitle.setText(task!!.title)
            edtDescription.setText(task!!.description)
        }

        btnDone.setOnClickListener {
            val title = edtTitle.text.toString()
            val desc = edtDescription.text.toString()
            if (title.isNotEmpty() && desc.isNotEmpty()){
                if (task == null){
                    addOrUpdateTask(0,title,desc, ActionType.CREATE)
                } else {
                    addOrUpdateTask(task!!.id,title,desc, ActionType.UPDATE)
                }
            }else{
                showMessage(it,"Preencha todos os campos!")
            }

        }
        //Recuperar campo do XML
        //tvTitle = findViewById(R.id.tv_task_title_detail)

        // Setar um novo texto na tela
        //tvTitle.text = task?.title ?: "Adicione uma tarefa"
    }

    private fun addOrUpdateTask(
        id : Int,
        title : String,
        description : String,
        actionType: ActionType
    ){
        val task = Task(id,title,description)
        returnAction(task,actionType)
    }

    // Criar um Menu mas ainda nada acontece se clicar
    //Essa função  é usada para inflar o menu de opções a partir de um arquivo XML
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_task_detail,menu)
        return true
    }
    // Checar qual opção foi clicada no menu de acordo com o Id
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.delete_task ->{
                if (task != null){
                    // as !! é para garantir que passe uma task, reafirmar, caso contrário dará crash
                    returnAction(task!!, ActionType.DELETE)
                }else{
                    showMessage(btnDone,"Item não encontrado")
                }


                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun returnAction(task: Task, actionType : ActionType){
        val intent = Intent()
            .apply {
                val taskAction = TaskAction(task,actionType.name)
                putExtra(TASK_ACTION_RESULT, taskAction)
            }
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

    private fun showMessage(view: View, message: String){
        Snackbar.make(view,message, Snackbar.LENGTH_LONG)
            .setAction("Action",null)
            .show()
    }

}
