package com.example.todoapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoFilter
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.databinding.DialogTaskBinding
import com.example.todoapp.ui.TodoAdapter
import com.example.todoapp.viewmodel.TodoViewModel
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TodoViewModel
    private lateinit var adapter: TodoAdapter
    private var selectedDateTime: LocalDateTime? = null
    private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm", Locale.getDefault())

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

        binding.rvTodos.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupSearch() {
        binding.searchEditText.setOnEditorActionListener { _, _, _ ->
            viewModel.searchTodos(binding.searchEditText.text.toString())
            false
        }
    }

    private fun setupFilterChips() {
        binding.filterChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val filter = when (checkedId) {
                binding.chipAll.id -> TodoFilter.ALL
                binding.chipActive.id -> TodoFilter.ACTIVE
                binding.chipCompleted.id -> TodoFilter.COMPLETED
                else -> TodoFilter.ALL
            }
            viewModel.filterTodos(filter)
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
        val dialogBinding = DialogTaskBinding.inflate(layoutInflater)
        selectedDateTime = null

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add New Task")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { _, _ ->
                val title = dialogBinding.editTextTitle.text.toString()
                val description = dialogBinding.editTextDescription.text.toString()
                if (title.isNotBlank()) {
                    viewModel.insertTodo(title, description, selectedDateTime)
                    showSnackbar("Task added")
                } else {
                    Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialogBinding.buttonSetDeadline.setOnClickListener {
            showDateTimePicker { dateTime ->
                selectedDateTime = dateTime
                dialogBinding.textDeadline.text = dateTime.format(dateFormatter)
            }
        }

        dialog.show()
    }

    private fun showEditTaskDialog(todo: Todo) {
        val dialogBinding = DialogTaskBinding.inflate(layoutInflater)
        selectedDateTime = todo.deadline

        dialogBinding.apply {
            editTextTitle.setText(todo.title)
            editTextDescription.setText(todo.description)
            textDeadline.text = todo.deadline?.format(dateFormatter) ?: "No deadline"
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Edit Task")
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                val title = dialogBinding.editTextTitle.text.toString()
                val description = dialogBinding.editTextDescription.text.toString()
                if (title.isNotBlank()) {
                    viewModel.updateTodo(todo.copy(
                        title = title,
                        description = description,
                        deadline = selectedDateTime
                    ))
                    showSnackbar("Task updated")
                } else {
                    Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialogBinding.buttonSetDeadline.setOnClickListener {
            showDateTimePicker { dateTime ->
                selectedDateTime = dateTime
                dialogBinding.textDeadline.text = dateTime.format(dateFormatter)
            }
        }

        dialog.show()
    }

    private fun showDateTimePicker(onDateTimeSelected: (LocalDateTime) -> Unit) {
        val calendar = Calendar.getInstance()
        
        DatePickerDialog(
            this,
            { _, year, month, day ->
                TimePickerDialog(
                    this,
                    { _, hour, minute ->
                        val dateTime = LocalDateTime.of(year, month + 1, day, hour, minute)
                        onDateTimeSelected(dateTime)
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