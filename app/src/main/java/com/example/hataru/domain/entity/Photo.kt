package com.example.hataru.domain.entity

import java.util.Date

data class Photo(
    val id: Double,
    val name: String,
    val fileName: String,
    val mimeType: String,
    val size: Double,
    val accountId: Double,
    val createDate: Date,
    val updateDate: Date,
    val roomtypeId: Double,
    val order: Double,
    val url: String?,
    val thumb: String
)