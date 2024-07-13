package com.example.hataru.domain.entity

data class FAQItem(val question: String, val answer: String, var isExpanded: Boolean = false)
