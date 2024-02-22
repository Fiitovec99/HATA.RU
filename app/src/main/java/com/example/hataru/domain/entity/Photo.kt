package com.example.hataru.domain.entity

data class Photo(
    val account_id: Int,
    val create_date: String,
    val file_name: String,
    val id: Int,
    val mime_type: String,
    val name: String,
    val order: Int,
    val roomtype_id: Int,
    val size: Int,
    val thumb: String,
    val update_date: String,
    val url: String
)