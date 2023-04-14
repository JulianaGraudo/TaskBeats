package com.comunidadedevspace.taskbeats
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Task(
    //mais fácil de manipular pq o Id vai ser único por tarefa
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title : String,
    val description : String
    ) : Serializable
