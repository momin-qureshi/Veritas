package com.melagroup.veritas.data.model

data class Book(
    val book_id: String?,
    val photo_url: String?,
    val title: String?,
    val authors: List <String>?,
    val tags: List <String>?,
    val synopsis: String?
)