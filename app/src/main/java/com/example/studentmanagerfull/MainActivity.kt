package com.example.studentmanagerfull

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentmanagerfull.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), StudentAdapter.OnItemActionListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: StudentViewModel
    private lateinit var adapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        viewModel = ViewModelProvider(this).get(StudentViewModel::class.java)
        adapter = StudentAdapter(viewModel.students.value.orEmpty(), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.students.observe(this) { list ->
            adapter = StudentAdapter(list, this)
            binding.recyclerView.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                startAddEdit(null, false)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startAddEdit(student: StudentEntity?, isEdit: Boolean) {
        val intent = Intent(this, StudentActivity::class.java).apply {
            student?.let { putExtra("student", it) }
            putExtra("isEdit", isEdit)
        }
        addEditLauncher.launch(intent)
    }

    private val addEditLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult
            val student = data.getSerializableExtra("student") as StudentEntity
            val isEdit = data.getBooleanExtra("isEdit", false)
            if (isEdit) {
                viewModel.updateStudent(student)
                Log.d("MainActivity", "Updated student: $student")
            } else {
                viewModel.addStudent(student)
                Log.d("MainActivity", "Added student: $student")
            }
        }
    }

    override fun onAction(viewType: StudentAdapter.ActionType, student: StudentEntity) {
        when (viewType) {
            StudentAdapter.ActionType.UPDATE -> startAddEdit(student, true)
            StudentAdapter.ActionType.DELETE -> AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_student))
                .setMessage(getString(R.string.confirm_delete, student.name))
                .setPositiveButton(getString(R.string.yes)) { _, _ -> viewModel.deleteStudent(student) }
                .setNegativeButton(getString(R.string.no), null)
                .show()
            StudentAdapter.ActionType.CALL -> {
                val dial = Intent(Intent.ACTION_DIAL).apply {
                    data = android.net.Uri.parse("tel:${student.phone}")
                }
                startActivity(dial)
            }
            StudentAdapter.ActionType.EMAIL -> {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = android.net.Uri.parse("mailto:${student.email}")
                }
                startActivity(emailIntent)
            }
        }
    }
}