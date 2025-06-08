package com.example.studentmanagerfull

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    private val db: AppDatabase = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "students.db"
    ).build()
    private val dao: StudentDao = db.studentDao()
    private val _students = MutableLiveData<MutableList<StudentEntity>>()
    val students: LiveData<MutableList<StudentEntity>> = _students

    init {
        loadStudents()
    }

    private fun loadStudents() {
        viewModelScope.launch(Dispatchers.IO) {
            val studentsList = dao.getAllStudents().toMutableList()
            _students.postValue(studentsList)
        }
    }

    fun addStudent(student: StudentEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = dao.insertStudent(student)
            val updatedStudent = student.copy(id = id.toInt())
            val list = _students.value ?: mutableListOf()
            list.add(updatedStudent)
            _students.postValue(list)
        }
    }

    fun updateStudent(updated: StudentEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateStudent(updated)
            val list = _students.value ?: return@launch
            val index = list.indexOfFirst { it.id == updated.id }
            if (index != -1) {
                list[index] = updated
                _students.postValue(list)
            }
        }
    }

    fun deleteStudent(student: StudentEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteStudent(student)
            val list = _students.value ?: return@launch
            list.removeAll { it.id == student.id }
            _students.postValue(list)
        }
    }
}