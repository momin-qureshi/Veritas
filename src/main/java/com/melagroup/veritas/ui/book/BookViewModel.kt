package com.melagroup.veritas.ui.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.melagroup.veritas.data.model.Book
import com.melagroup.veritas.data.BookRepository

class BookViewModel (private val bookRepository: BookRepository) : ViewModel(){
    private val _bookResult = MutableLiveData<Book>()
    val bookResult: LiveData<Book> = _bookResult

    val db = Firebase.firestore
    private val auth : FirebaseAuth = Firebase.auth

    private var book: Book? = null
    private val _isInFaves = MutableLiveData<Boolean>()
    private var disableFavesButton = false
    val isInFaves: LiveData<Boolean> = _isInFaves
    private var isInList: Boolean = false

    fun init(book: Book?){
        isInList = false
        _isInFaves.value = false
        this.book = book
        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener { res->
            if(res["MyBooks"] != null) {
                @Suppress("UNCHECKED_CAST")
                val books = res["MyBooks"] as ArrayList<HashMap<String, String>>
                for (res_book in books)
                    if (res_book["book_id"] == book?.book_id) {
                        isInList = true
                        break
                    }
            }
            if(res["Favorites"] != null){
                @Suppress("UNCHECKED_CAST")
                val books = res["Favorites"] as ArrayList<HashMap<String, String>>
                for (res_book in books)
                    if (res_book["book_id"] == book?.book_id) {
                        _isInFaves.value = true
                        break
                    }
            }
        }
    }

    fun readBook(){
        if(!isInList)
            addToMyList(book)
    }

    private fun addToMyList(book: Book?){
        val user = auth.currentUser?.uid
        val data = hashMapOf(
                "book_id" to book?.book_id,
                "title" to book?.title,
                "photo_url" to book?.photo_url
        )
        if(user != null)
            db.collection("users").document(user).update(
                    "MyBooks", FieldValue.arrayUnion(data)
            )
    }

    fun toggleFavorite() {
        if(disableFavesButton)
            return
        disableFavesButton = true
        val user = auth.currentUser?.uid
        val data = hashMapOf(
                "book_id" to book?.book_id,
                "title" to book?.title,
                "photo_url" to book?.photo_url
        )
        val fieldValue: FieldValue
        if(_isInFaves.value!!){
           fieldValue = FieldValue.arrayRemove(data)
        }
        else{
            fieldValue = FieldValue.arrayUnion(data)
        }
        if(user != null)
            db.collection("users").document(user).update("Favorites", fieldValue).addOnSuccessListener {
                _isInFaves.value = !_isInFaves.value!!
                disableFavesButton = false
            }
    }
}