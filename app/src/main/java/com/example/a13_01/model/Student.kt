package com.example.a13_01.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Data class representing a Student from the API
 */
data class Student(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("mssv")
    val mssv: String,
    
    @SerializedName("hoten")
    val hoten: String,
    
    @SerializedName("ngaysinh")
    val ngaysinh: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("thumbnail")
    val thumbnail: String,
    
    @SerializedName("created_at")
    val createdAt: String,
    
    @SerializedName("updated_at")
    val updatedAt: String
) : Serializable {
    
    /**
     * Get full thumbnail URL
     */
    fun getFullThumbnailUrl(): String {
        return if (thumbnail.isNotEmpty()) {
            "https://lebavui.io.vn$thumbnail"
        } else {
            ""
        }
    }
    
    /**
     * Format date of birth for display (YYYY-MM-DD to DD/MM/YYYY)
     */
    fun getFormattedBirthday(): String {
        return try {
            val parts = ngaysinh.split("-")
            if (parts.size == 3) {
                "${parts[2]}/${parts[1]}/${parts[0]}"
            } else {
                ngaysinh
            }
        } catch (e: Exception) {
            ngaysinh
        }
    }
}
