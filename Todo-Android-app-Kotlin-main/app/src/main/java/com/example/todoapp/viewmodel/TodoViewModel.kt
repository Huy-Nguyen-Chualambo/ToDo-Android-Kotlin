package com.example.todoapp.viewmodel

import android.app.Application
import android.icu.text.CaseMap.Title
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoDatabase
import kotlinx.coroutines.launch
import java.sql.Time

class TodoViewModel(application: Application):AndroidViewModel(application) {
    private val dao = TodoDatabase.getDatabase(application).todoDao()

    val todos: LiveData<List<Todo>> = dao.getAllTodo()

    fun insertTodo(title: String) = viewModelScope.launch {
        dao.insert(Todo(title = title))
    }
    fun toggleDone(todo: Todo) = viewModelScope.launch {
        dao.update(todo.copy(isDone = !todo.isDone))
    }

    fun deleteTodo(todo:Todo) = viewModelScope.launch {
        dao.delete(todo )
    }
}