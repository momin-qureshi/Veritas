package com.melagroup.veritas.data

import com.melagroup.veritas.data.model.Book

class BookRepository(val dataSource: BookDataSource) {

    fun getBook(book_id: String) : Result<Book> {
        return dataSource.getBook(book_id)
    }
}