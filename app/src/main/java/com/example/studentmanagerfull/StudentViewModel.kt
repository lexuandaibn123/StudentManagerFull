package com.example.studentmanagerfull

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StudentViewModel : ViewModel() {
    private val _students = MutableLiveData<MutableList<Student>>(mutableListOf())
    val students: LiveData<MutableList<Student>> = _students

    fun addStudent(student: Student) {
        val list = _students.value ?: mutableListOf()
        student.id = if (list.isEmpty()) 1 else list.maxOf { it.id } + 1
        list.add(student)
        _students.value = list
    }

    fun updateStudent(updated: Student) {
        val list = _students.value ?: return
        val index = list.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            list[index] = updated
            _students.value = list
        }
    }

    fun deleteStudent(student: Student) {
        val list = _students.value ?: return
        list.removeAll { it.id == student.id }
        _students.value = list
    }
}