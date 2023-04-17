package com.comunidadedevspace.taskbeats.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

//Responsável por acessar os dados dentro da base de dados
@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

     //Selecionar da tabela Task
    //"Selecione da Tabela Task todos"
    @Query ("Select * from Task")
    fun getAll(): LiveData<List<Task>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(task: Task)

    //Deletar todos
    @Query("Delete from task")
    fun deleteAll()

    //Deletar pelo Id
    @Query("Delete from task where id = :id")
    fun deleteById(id : Int)
}