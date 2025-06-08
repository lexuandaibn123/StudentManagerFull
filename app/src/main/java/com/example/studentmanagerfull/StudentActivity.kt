package com.example.studentmanagerfull

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.studentmanagerfull.databinding.ActivityStudentBinding

class StudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentBinding
    private var isEdit = false
    private lateinit var student: StudentEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (isEdit) getString(R.string.update_student) else getString(R.string.add_student)

        student = intent.getSerializableExtra("student") as? StudentEntity ?: StudentEntity()
        isEdit = intent.getBooleanExtra("isEdit", false)
        binding.student = student

        binding.btnSave.setOnClickListener {
            if (isAnyFieldEmpty()) {
                Toast.makeText(this, getString(R.string.enter_all_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val resultIntent = Intent().apply {
                putExtra("student", student)
                putExtra("isEdit", isEdit)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun isAnyFieldEmpty(): Boolean {
        return student.name.isBlank() || student.studentId.isBlank() || student.email.isBlank() || student.phone.isBlank()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}