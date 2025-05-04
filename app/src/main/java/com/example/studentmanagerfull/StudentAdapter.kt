package com.example.studentmanagerfull

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.studentmanagerfull.R
import com.example.studentmanagerfull.Student
import com.example.studentmanagerfull.databinding.ItemStudentBinding

class StudentAdapter(
    private val students: List<Student>,
    private val listener: OnItemActionListener
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    enum class ActionType { UPDATE, DELETE, CALL, EMAIL }

    interface OnItemActionListener {
        fun onAction(viewType: ActionType, student: Student)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding: ItemStudentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_student, parent, false
        )
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.binding.student = student

        holder.binding.btnMore.setOnClickListener { view ->
            // Hiển thị PopupMenu và trả ActionType
            PopupMenu(view.context, view).apply {
                menuInflater.inflate(R.menu.menu_item_student, menu)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_update -> listener.onAction(ActionType.UPDATE, student)
                        R.id.action_delete -> listener.onAction(ActionType.DELETE, student)
                        R.id.action_call   -> listener.onAction(ActionType.CALL, student)
                        R.id.action_email  -> listener.onAction(ActionType.EMAIL, student)
                        else -> return@setOnMenuItemClickListener false
                    }
                    true
                }
            }.show()
        }
    }

    override fun getItemCount(): Int = students.size

    inner class StudentViewHolder(val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root)
}