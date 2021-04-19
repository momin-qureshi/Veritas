package com.melagroup.veritas.ui.home.Books

import com.melagroup.veritas.data.model.Book

interface BookOnClickListener {
    fun onItemClick(book: Book, position: Int)
}