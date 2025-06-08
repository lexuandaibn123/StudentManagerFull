package com.example.studentmanagerfull

import androidx.room.*

@Dao
interface StudentDao {
    @Query("SELECT * FROM students")
    fun getAllStudents(): List<StudentEntity>

    @Insert
    fun insertStudent(student: StudentEntity): Long

    @Update
    fun updateStudent(student: StudentEntity)

    @Delete
    fun deleteStudent(student: StudentEntity)
}