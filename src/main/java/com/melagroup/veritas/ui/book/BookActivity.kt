package com.melagroup.veritas.ui.book

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.melagroup.veritas.R
import com.melagroup.veritas.data.model.Book
import com.melagroup.veritas.ui.login.LoginViewModel
import com.melagroup.veritas.ui.login.LoginViewModelFactory

class BookActivity : AppCompatActivity() {

    private lateinit var bookViewModel: BookViewModel
    val db = Firebase.firestore
    val storageReference = Firebase.storage("gs://veritas-ae735.appspot.com")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        val title = findViewById<TextView>(R.id.label_book_name)
        val synopsis = findViewById<TextView>(R.id.description_synopsis)
        val coverImage = findViewById<ImageView>(R.id.book_cover)

        val book_id = intent.getStringExtra("book_id")
        Log.d("Book", "start: $book_id")
        if (book_id != null) {
            db.collection("books").document(book_id).get().addOnSuccessListener {
                Log.d("Book", "success: $book_id")
                title.text = it["title"] as String
                synopsis.text = it["synopsis"] as String
                val url = "books/covers/${it["photo_url"] as String}"
                val ref = storageReference.reference

//                Log.d("Book", url)
//                Glide.with(this /* context */)
//                    .load(ref.child(url))
//                    .into(coverImage)


            }.addOnFailureListener{
                Log.d("Book", "failed: $book_id")
            }
        }
    }
}