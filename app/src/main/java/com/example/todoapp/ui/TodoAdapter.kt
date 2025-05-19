package com.example.todoapp.ui

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.Todo
import com.example.todoapp.databinding.ItemTodoBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TodoAdapter(
    private val onToggleDone: (Todo) -> Unit,
    private val onDelete: (Todo) -> Unit,
    private val onItemClick: (Todo) -> Unit
): ListAdapter<Todo, TodoAdapter.TodoViewHolder>(DiffCallBack()) {

    private val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())

    class TodoViewHolder(val binding: ItemTodoBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = getItem(position)
        with(holder.binding) {
            textviewTitle.text = todo.title
            textviewDescription.text = todo.description
            textviewDescription.visibility = if (todo.description.isBlank()) View.GONE else View.VISIBLE
            
            textviewDeadline.text = todo.deadline?.let { dateFormat.format(it) }
            textviewDeadline.visibility = if (todo.deadline == null) View.GONE else View.VISIBLE
            
            checkboxDone.isChecked = todo.isDone
            textviewTitle.paintFlags = if (todo.isDone)
                textviewTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else
                textviewTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            checkboxDone.setOnCheckedChangeListener { _, _ -> onToggleDone(todo) }
            buttonDelete.setOnClickListener { onDelete(todo) }
            root.setOnClickListener { onItemClick(todo) }

            // Add animation
            root.startAnimation(AnimationUtils.loadAnimation(root.context, R.anim.item_animation))
        }
    }

    class DiffCallBack: DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Todo, newItem: Todo) = oldItem == newItem
    }
}
