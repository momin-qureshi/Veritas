package com.melagroup.veritas.data.model

data class Review(
        val user_id: String?,
        val book_id: String?,
        val book_title: String?,
        val photo_url: String?,
        val text: String?,
        val isPositive: Boolean?
)