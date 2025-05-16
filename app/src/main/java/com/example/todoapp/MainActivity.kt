package com.example.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.databinding.ItemTodoBinding
import com.example.todoapp.ui.TodoAdapter
import com.example.todoapp.viewmodel.TodoViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TodoViewModel
    private lateinit var adapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TodoViewModel::class.java]

        adapter = TodoAdapter(
            onToggleDone = { viewModel.toggleDone(it) },
            onDelete = { viewModel.deleteTodo(it) }
        )

        binding.rvTodos.adapter = adapter
        binding.rvTodos.layoutManager = LinearLayoutManager(this)

        binding.btnAdd.setOnClickListener {
          val text = binding.eTodo.editText?.text.toString()
            if(text.isNotBlank()){
                viewModel.insertTodo(text)
                binding.eTodo.editText?.text?.clear()
            }

    }

        viewModel.todos.observe(this) { todos ->
            adapter.submitList(todos)
        }
    }
}