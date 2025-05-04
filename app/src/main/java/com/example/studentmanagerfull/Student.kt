package com.example.studentmanagerfull

import java.io.Serializable

data class Student(
    var id: Int = 0,
    var name: String = "",
    var studentId: String = "",
    var email: String = "",
    var phone: String = ""
) : Serializable