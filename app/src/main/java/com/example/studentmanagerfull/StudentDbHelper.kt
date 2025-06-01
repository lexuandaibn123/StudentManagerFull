package com.example.studentmanagerfull

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StudentDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "students.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "students"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_STUDENT_ID = "studentId"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PHONE = "phone"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_STUDENT_ID TEXT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_PHONE TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    fun getAllStudents(): List<Student> {
        val students = mutableListOf<Student>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val studentId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
            students.add(Student(id, name, studentId, email, phone))
        }
        cursor.close()
        db.close()
        return students
    }

    fun insertStudent(student: Student): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.name)
            put(COLUMN_STUDENT_ID, student.studentId)
            put(COLUMN_EMAIL, student.email)
            put(COLUMN_PHONE, student.phone)
        }
        val id = db.insert(TABLE_NAME, null, values).toInt()
        db.close()
        return id
    }

    fun updateStudent(student: Student) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.name)
            put(COLUMN_STUDENT_ID, student.studentId)
            put(COLUMN_EMAIL, student.email)
            put(COLUMN_PHONE, student.phone)
        }
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(student.id.toString()))
        db.close()
    }

    fun deleteStudent(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }
}