package com.melagroup.veritas.data

import com.melagroup.veritas.data.model.Book
import com.melagroup.veritas.data.model.LoggedInUser
import java.io.IOException
import java.util.*

class BookDataSource {
    fun getBook(book_id: String) : Result<Book> {
        val syn = "The novel follows the character development of Elizabeth Bennet, the dynamic " +
                "protagonist of the book who learns about the repercussions of hasty judgments and " +
                "comes to appreciate the difference between superficial goodness and actual goodness. " +
                "Its humour lies in its honest depiction of manners, education, marriage, and money " +
                "during the Regency era in Great Britain."
        return Result.Success<Book>(
            Book(null, null, "Pride & Prejudice", listOf("Me", "You"),
            listOf("Crime", "Drama"), syn)
        )
    }
}