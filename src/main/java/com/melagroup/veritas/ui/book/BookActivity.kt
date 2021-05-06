package com.melagroup.veritas.ui.book

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ethanhua.skeleton.Skeleton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.like.LikeButton
import com.like.OnLikeListener
import com.melagroup.veritas.R
import com.melagroup.veritas.data.model.Book
import com.melagroup.veritas.data.model.Genre
import com.melagroup.veritas.ui.book.genres.GenreListAdapter
import com.melagroup.veritas.ui.login.LoginViewModel
import com.melagroup.veritas.ui.login.LoginViewModelFactory

class BookActivity : AppCompatActivity() {

    private lateinit var bookViewModel: BookViewModel
    private val db = Firebase.firestore
    private val storageReference = Firebase.storage("gs://veritas-ae735.appspot.com")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        bookViewModel = ViewModelProvider(this, BookViewModelFactory())
                .get(BookViewModel::class.java)

        val title = findViewById<TextView>(R.id.label_book_name)
        val synopsis = findViewById<TextView>(R.id.description_synopsis)
        val coverImage = findViewById<ImageView>(R.id.book_cover)
        val author = findViewById<TextView>(R.id.label_author)

        val readBookButton = findViewById<Button>(R.id.button_read)
        val favoritesButton = findViewById<LikeButton>(R.id.button_mylist)

        val skeletonScreen = Skeleton.bind(findViewById<View>(R.id.activity_book))
            .load(R.layout.activity_book_skeleton)
            .show()

        val bookId = intent.getStringExtra("book_id")

        Log.d("Book", "start: $bookId")
        if (bookId != null) {
            db.collection("books").document(bookId).get().addOnSuccessListener {

                val book = Book(
                        book_id = bookId as String,
                        title =  it["title"] as String,
                        photo_url = it["photo_url"] as String,
                        authors = null, synopsis = null, tags = null
                )
                bookViewModel.init(book)

                title.text = it["title"] as String
                synopsis.text = it["synopsis"] as String
                var authorNames = ""
                for (auth in it["authors"] as ArrayList<*>)
                    authorNames = authorNames.plus(auth).plus(", ")

                author.text = authorNames.subSequence(0, authorNames.length-1)

                val url = "books/covers/${it["photo_url"] as String}"
                val ref = storageReference.reference
                Log.d("Book", url)
                Glide.with(this /* context */)
                    .load(ref.child(url))
                    .listener(object : RequestListener<Drawable> {

                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            skeletonScreen.hide()
                            return false
                        }
                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            //do something when picture already loaded
                            skeletonScreen.hide()

                            val genreRecyclerView: RecyclerView = findViewById(R.id.genre_list)
                            val genre_list = mutableListOf<Genre>()
                            for (genre in it["genres"] as ArrayList<*>)
                                genre_list.add(Genre("", genre as String?))
                            genreRecyclerView.adapter =  GenreListAdapter(genre_list)
                            genreRecyclerView.layoutManager = LinearLayoutManager(
                                baseContext,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            return false
                        }
                    })
                    .into(coverImage)

            }.addOnFailureListener{
                Log.d("Book", "failed: $bookId")
            }


        }

        bookViewModel.isInFaves.observe(this@BookActivity, Observer {
            favoritesButton.isLiked = it
        })

        favoritesButton.setOnLikeListener(object: OnLikeListener {
            override fun liked(likeButton: LikeButton?) {
                bookViewModel.toggleFavorite()
            }
            override fun unLiked(likeButton: LikeButton?) {
                bookViewModel.toggleFavorite()
            }
        })

        readBookButton.setOnClickListener {
            bookViewModel.readBook()
        }

    }
}