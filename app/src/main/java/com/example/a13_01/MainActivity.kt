package com.example.a13_01

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a13_01.adapter.StudentAdapter
import com.example.a13_01.api.RetrofitClient
import com.example.a13_01.databinding.ActivityMainBinding
import com.example.a13_01.model.Student
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StudentAdapter
    private var studentList: List<Student> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearch()
        fetchStudents()
    }

    private fun setupRecyclerView() {
        adapter = StudentAdapter(emptyList()) { student ->
            // Navigate to detail activity
            val intent = Intent(this, StudentDetailActivity::class.java)
            intent.putExtra(StudentDetailActivity.EXTRA_STUDENT, student)
            startActivity(intent)
        }
        
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            
            override fun afterTextChanged(s: Editable?) {
                filterStudents(s.toString())
            }
        })
        
        binding.btnClearSearch.setOnClickListener {
            binding.etSearch.text.clear()
        }
    }

    private fun filterStudents(query: String) {
        val filteredList = if (query.isEmpty()) {
            studentList
        } else {
            studentList.filter { student ->
                student.hoten.lowercase().contains(query.lowercase()) ||
                student.mssv.contains(query)
            }
        }
        
        adapter.updateList(filteredList)
        
        // Show empty state if no results
        if (filteredList.isEmpty() && studentList.isNotEmpty()) {
            binding.tvEmptyState.text = "Không tìm thấy sinh viên"
            binding.tvEmptyState.visibility = View.VISIBLE
        } else {
            binding.tvEmptyState.visibility = View.GONE
        }
    }

    private fun fetchStudents() {
        showLoading(true)
        
        RetrofitClient.apiService.getStudents().enqueue(object : Callback<List<Student>> {
            override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {
                showLoading(false)
                
                if (response.isSuccessful) {
                    response.body()?.let { students ->
                        studentList = students
                        adapter.updateList(students)
                        
                        // Update student count
                        binding.tvStudentCount.text = "Tổng số: ${students.size} sinh viên"
                        
                        if (students.isEmpty()) {
                            binding.tvEmptyState.text = "Không có dữ liệu"
                            binding.tvEmptyState.visibility = View.VISIBLE
                        }
                    }
                } else {
                    showError("Lỗi: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                showLoading(false)
                showError("Không thể kết nối: ${t.message}")
            }
        })
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        binding.tvEmptyState.text = message
        binding.tvEmptyState.visibility = View.VISIBLE
    }
}
