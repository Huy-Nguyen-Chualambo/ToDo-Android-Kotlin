package com.example.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoDatabase
import com.example.todoapp.data.TodoFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TodoViewModel(application: Application): AndroidViewModel(application) {
    private val dao = TodoDatabase.getDatabase(application).todoDao()
    
    private val _searchQuery = MutableStateFlow("")
    private val _currentFilter = MutableStateFlow(TodoFilter.ALL)
    private val _sortOrder = MutableStateFlow(SortOrder.CREATED_AT)

    private val allTodos = dao.getAllTodo()

    val todos: LiveData<List<Todo>> = combine(
        allTodos,
        _searchQuery,
        _currentFilter,
        _sortOrder
    ) { todos, query, filter, sortOrder ->
        todos.filter { todo ->
            val matchesSearch = todo.title.contains(query, ignoreCase = true) ||
                    todo.description.contains(query, ignoreCase = true)
            val matchesFilter = when (filter) {
                TodoFilter.ALL -> true
                TodoFilter.ACTIVE -> !todo.isDone
                TodoFilter.COMPLETED -> todo.isDone
            }
            matchesSearch && matchesFilter
        }.sortedWith(
            when (sortOrder) {
                SortOrder.CREATED_AT -> compareByDescending { it.createdAt }
                SortOrder.DEADLINE -> compareBy { it.deadline ?: LocalDateTime.MAX }
            }
        )
    }.asLiveData()

    fun insertTodo(title: String, description: String = "", deadline: LocalDateTime? = null) = viewModelScope.launch {
        dao.insert(Todo(
            title = title,
            description = description,
            deadline = deadline,
            createdAt = LocalDateTime.now()
        ))
    }

    fun updateTodo(todo: Todo) = viewModelScope.launch {
        dao.update(todo)
    }

    fun toggleDone(todo: Todo) = viewModelScope.launch {
        dao.update(todo.copy(isDone = !todo.isDone))
    }

    fun deleteTodo(todo: Todo) = viewModelScope.launch {
        dao.delete(todo)
    }

    fun searchTodos(query: String) {
        _searchQuery.value = query
    }

    fun filterTodos(filter: TodoFilter) {
        _currentFilter.value = filter
    }

    fun setSortOrder(sortOrder: SortOrder) {
        _sortOrder.value = sortOrder
    }
}

enum class SortOrder {
    CREATED_AT,
    DEADLINE
}