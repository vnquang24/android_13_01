package com.example.a13_01

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.a13_01.databinding.ActivityStudentDetailBinding
import com.example.a13_01.model.Student

class StudentDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentDetailBinding

    companion object {
        const val EXTRA_STUDENT = "extra_student"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Enable back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Thông tin sinh viên"

        // Get student data from intent
        @Suppress("DEPRECATION")
        val student = intent.getSerializableExtra(EXTRA_STUDENT) as? Student

        student?.let {
            displayStudentInfo(it)
        }
    }

    private fun displayStudentInfo(student: Student) {
        binding.apply {
            tvDetailName.text = student.hoten
            tvDetailMssv.text = student.mssv
            tvDetailBirthday.text = student.getFormattedBirthday()
            tvDetailEmail.text = student.email

            // Load thumbnail with Glide
            val thumbnailUrl = student.getFullThumbnailUrl()
            if (thumbnailUrl.isNotEmpty()) {
                Glide.with(this@StudentDetailActivity)
                    .load(thumbnailUrl)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.ic_placeholder)
                            .error(R.drawable.ic_placeholder)
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imgDetailThumbnail)
            } else {
                imgDetailThumbnail.setImageResource(R.drawable.ic_placeholder)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
