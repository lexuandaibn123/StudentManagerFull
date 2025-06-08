package com.example.studentmanagerfull

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var studentId: String = "",
    var email: String = "",
    var phone: String = ""
) : Serializable