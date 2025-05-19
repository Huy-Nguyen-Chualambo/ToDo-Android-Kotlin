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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class TodoAdapter(
    private val onToggleDone: (Todo) -> Unit,
    private val onDelete: (Todo) -> Unit,
    private val onItemClick: (Todo) -> Unit
): ListAdapter<Todo, TodoAdapter.TodoViewHolder>(DiffCallBack()) {

    private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm", Locale.getDefault())

    class TodoViewHolder(val binding: ItemTodoBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = getItem(position)
        holder.binding.apply {
            checkboxDone.isChecked = todo.isDone
            textTitle.text = todo.title
            textTitle.paintFlags = if (todo.isDone) {
                textTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                textTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            // Handle description visibility
            if (todo.description.isNotBlank()) {
                textDescription.visibility = View.VISIBLE
                textDescription.text = todo.description
            } else {
                textDescription.visibility = View.GONE
            }

            // Handle deadline visibility and formatting
            if (todo.deadline != null) {
                textDeadline.visibility = View.VISIBLE
                textDeadline.text = todo.deadline.format(dateFormatter)
                
                // Highlight deadline if it's approaching or overdue
                val now = LocalDateTime.now()
                if (todo.deadline.isBefore(now)) {
                    textDeadline.setTextColor(root.context.getColor(R.color.error))
                } else if (todo.deadline.isBefore(now.plusDays(1))) {
                    textDeadline.setTextColor(root.context.getColor(R.color.warning))
                } else {
                    textDeadline.setTextColor(root.context.getColor(R.color.text_secondary))
                }
            } else {
                textDeadline.visibility = View.GONE
            }

            // Set up click listeners
            checkboxDone.setOnClickListener {
                onToggleDone(todo)
            }

            buttonDelete.setOnClickListener {
                onDelete(todo)
            }

            root.setOnClickListener {
                onItemClick(todo)
            }

            // Add animation
            root.startAnimation(AnimationUtils.loadAnimation(root.context, R.anim.item_animation))
        }
    }

    class DiffCallBack: DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Todo, newItem: Todo) = oldItem == newItem
    }
}
