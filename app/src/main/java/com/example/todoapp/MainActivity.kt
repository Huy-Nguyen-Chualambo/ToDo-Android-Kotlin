package com.example.todoapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.databinding.DialogTaskBinding
import com.example.todoapp.ui.TodoAdapter
import com.example.todoapp.viewmodel.TodoViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TodoViewModel
    private lateinit var adapter: TodoAdapter
    private var selectedDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TodoViewModel::class.java]

        setupRecyclerView()
        setupSearch()
        setupFilterChips()
        setupFab()
        observeTodos()
    }

    private fun setupRecyclerView() {
        adapter = TodoAdapter(
            onToggleDone = { todo ->
                viewModel.toggleDone(todo)
                showSnackbar("Task ${if (todo.isDone) "unmarked" else "marked"} as done")
            },
            onDelete = { todo ->
                viewModel.deleteTodo(todo)
                showSnackbar("Task deleted")
            },
            onItemClick = { todo ->
                showEditTaskDialog(todo)
            }
        )

        binding.rvTodos.adapter = adapter
        binding.rvTodos.layoutManager = LinearLayoutManager(this)
    }

    private fun setupSearch() {
        binding.searchEditText.setOnEditorActionListener { _, _, _ ->
            val query = binding.searchEditText.text.toString()
            viewModel.searchTodos(query)
            true
        }
    }

    private fun setupFilterChips() {
        binding.filterChipGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chipAll -> viewModel.filterTodos(TodoFilter.ALL)
                R.id.chipActive -> viewModel.filterTodos(TodoFilter.ACTIVE)
                R.id.chipCompleted -> viewModel.filterTodos(TodoFilter.COMPLETED)
            }
        }
    }

    private fun setupFab() {
        binding.fabAddTask.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun observeTodos() {
        viewModel.todos.observe(this) { todos ->
            adapter.submitList(todos)
            binding.emptyView.visibility = if (todos.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun showAddTaskDialog() {
        showTaskDialog(null)
    }

    private fun showEditTaskDialog(todo: Todo) {
        showTaskDialog(todo)
    }

    private fun showTaskDialog(todo: Todo?) {
        val dialogBinding = DialogTaskBinding.inflate(LayoutInflater.from(this))
        selectedDate = todo?.deadline

        // Pre-fill data if editing
        todo?.let {
            dialogBinding.editTextTitle.setText(it.title)
            dialogBinding.editTextDescription.setText(it.description)
            dialogBinding.editTextDeadline.setText(it.deadline?.let { date ->
                SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(date)
            })
        }

        // Setup deadline picker
        dialogBinding.editTextDeadline.setOnClickListener {
            showDateTimePicker { date ->
                selectedDate = date
                dialogBinding.editTextDeadline.setText(
                    SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(date)
                )
            }
        }

        AlertDialog.Builder(this)
            .setTitle(if (todo == null) "Add New Task" else "Edit Task")
            .setView(dialogBinding.root)
            .setPositiveButton(if (todo == null) "Add" else "Save") { _, _ ->
                val title = dialogBinding.editTextTitle.text.toString()
                val description = dialogBinding.editTextDescription.text.toString()
                
                if (title.isNotBlank()) {
                    if (todo == null) {
                        viewModel.insertTodo(title, description, selectedDate)
                        showSnackbar("Task added")
                    } else {
                        viewModel.updateTodo(todo.copy(
                            title = title,
                            description = description,
                            deadline = selectedDate
                        ))
                        showSnackbar("Task updated")
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDateTimePicker(onDateTimeSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance()
        
        DatePickerDialog(
            this,
            { _, year, month, day ->
                TimePickerDialog(
                    this,
                    { _, hour, minute ->
                        calendar.set(year, month, day, hour, minute)
                        onDateTimeSelected(calendar.time)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}