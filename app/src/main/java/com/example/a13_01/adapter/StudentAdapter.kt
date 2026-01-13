package com.example.a13_01.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.a13_01.R
import com.example.a13_01.model.Student

/**
 * RecyclerView Adapter for displaying student list
 */
class StudentAdapter(
    private var students: List<Student>,
    private val onItemClick: (Student) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnailImage: ImageView = itemView.findViewById(R.id.imgThumbnail)
        val nameText: TextView = itemView.findViewById(R.id.tvName)
        val mssvText: TextView = itemView.findViewById(R.id.tvMssv)
        
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(students[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        
        holder.nameText.text = student.hoten
        holder.mssvText.text = "MSSV: ${student.mssv}"
        
        // Load thumbnail with Glide
        val thumbnailUrl = student.getFullThumbnailUrl()
        if (thumbnailUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(thumbnailUrl)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .circleCrop()
                )
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.thumbnailImage)
        } else {
            holder.thumbnailImage.setImageResource(R.drawable.ic_placeholder)
        }
    }

    override fun getItemCount(): Int = students.size

    /**
     * Update the student list (used for search filtering)
     */
    fun updateList(newList: List<Student>) {
        students = newList
        notifyDataSetChanged()
    }
}
