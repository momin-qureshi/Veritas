package com.melagroup.veritas.ui.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melagroup.veritas.data.model.Book
import com.melagroup.veritas.data.BookRepository
import com.melagroup.veritas.data.Result

class BookViewModel (private val bookRepository: BookRepository) : ViewModel(){
    private val _bookResult = MutableLiveData<Book>()
    val bookResult: LiveData<Book> = _bookResult

    fun loadBook(book_id: String){
        var result = bookRepository.getBook(book_id);
        if (result is Result.Success){

        }
    }
}