package com.example.studentmanagerfull

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper: StudentDbHelper = StudentDbHelper(application)
    private val _students = MutableLiveData<MutableList<Student>>()
    val students: LiveData<MutableList<Student>> = _students

    init {
        _students.value = dbHelper.getAllStudents().toMutableList()
    }

    fun addStudent(student: Student) {
        val id = dbHelper.insertStudent(student)
        student.id = id
        val list = _students.value ?: mutableListOf()
        list.add(student)
        _students.value = list
    }

    fun updateStudent(updated: Student) {
        dbHelper.updateStudent(updated)
        val list = _students.value ?: return
        val index = list.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            list[index] = updated
            _students.value = list
        }
    }

    fun deleteStudent(student: Student) {
        dbHelper.deleteStudent(student.id)
        val list = _students.value ?: return
        list.removeAll { it.id == student.id }
        _students.value = list
    }
}