package com.comunidadedevspace.taskbeats.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.data.Task

//essa classe tem objetivo de ter a funcionalidade do Adapter - transformar uma lista de tarefas na UI para usar na recyclerView

class TaskListAdapter(
    private val openTaskDetailView : (task : Task) -> Unit
    ) : ListAdapter<Task, TaskListViewHolder>(TaskListAdapter) {
    // O que é um viewHolder: ele pega o item do xml item_task, vai segurar essa view, pra quando tiver fazendo
    // o Bind dessa view, for possivel setar no textView


    //Função para criar o viewHolder(segurar o que tem dentro da Task e transformar na UI, nesse caso o item_task.xml)
    //Dessa forma é atrelado o ViewHolder ao layout item_task
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        val view : View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_task, parent,false)

        return TaskListViewHolder(view)
    }

    // Função para atrelar cada tarefa (task) ao ViewHolder > para poder colocar o valor disso dentro dos textViews
    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task, openTaskDetailView)
    }

    companion object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.title == newItem.title && oldItem.description == newItem.description
        }

    }
}
// essa view que é necessária para fazer o findViewById é criada ali em cima inflando o Layout do item_task

class TaskListViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
    private val tvTaskTitle = view.findViewById<TextView>(R.id.tv_task_title)
    private val tvTaskDescription = view.findViewById<TextView>(R.id.tv_task_description)

    fun bind(task: Task, openTaskDetailView : (task : Task) -> Unit){
        tvTaskTitle.text = task.title
        tvTaskDescription.text = task.description

        view.setOnClickListener{
            openTaskDetailView.invoke(task)
        }
    }
}