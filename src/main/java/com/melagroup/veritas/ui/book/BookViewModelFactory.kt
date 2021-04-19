package com.melagroup.veritas.ui.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.melagroup.veritas.data.BookDataSource
import com.melagroup.veritas.data.BookRepository
import com.melagroup.veritas.ui.login.LoginViewModel

class BookViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return BookViewModel(
                bookRepository = BookRepository(
                    dataSource = BookDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}