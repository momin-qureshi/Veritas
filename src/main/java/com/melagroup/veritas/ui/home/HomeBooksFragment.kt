package com.melagroup.veritas.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.melagroup.veritas.R
import com.melagroup.veritas.data.model.Book
import com.melagroup.veritas.ui.book.BookActivity
import com.melagroup.veritas.ui.home.Books.BookOnClickListener
import com.melagroup.veritas.ui.home.Books.daily.DailyBooksAdapter
import com.melagroup.veritas.ui.home.Books.mylist.MyListBooksAdapter
import com.melagroup.veritas.ui.main.HomeBooksViewModel

class HomeBooksFragment : Fragment(), BookOnClickListener {

    companion object {
        fun newInstance() = HomeBooksFragment()
    }

    val db = Firebase.firestore
    private lateinit var viewModel: HomeBooksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_books_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dailyRecyclerView: RecyclerView = view.findViewById(R.id.recyclerview_daily) as RecyclerView
        val dailyBooksAdapter = DailyBooksAdapter(Array<String>(6) { "a" }, this)

        dailyRecyclerView.adapter =  dailyBooksAdapter
        dailyRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )


        val mylistRecyclerView: RecyclerView = view.findViewById(R.id.recyclerview_mylist) as RecyclerView

        val arr = emptyList<Book>()
        val mylistBooksAdapter = MyListBooksAdapter(arr , this)
        mylistRecyclerView.adapter =  mylistBooksAdapter
        mylistRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        val recommendationsRecyclerView: RecyclerView = view.findViewById(R.id.recyclerview_recommendations) as RecyclerView
        recommendationsRecyclerView.adapter =  DailyBooksAdapter(Array<String>(6) { "a" }, this)
        recommendationsRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        db.collection("books").get().addOnSuccessListener {
            val list : List<Book>  = it.documents.map{
                Book(book_id = it.id, title= it["title"] as String?, photo_url = it["photo_url"] as String?,
                    authors = null, tags = null, synopsis = it["synopsis"] as String?)}
            mylistBooksAdapter.dataSet = list
            mylistBooksAdapter.notifyDataSetChanged()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeBooksViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onItemClick(book: Book, position: Int) {
        Toast.makeText(context, book.title, Toast.LENGTH_SHORT).show()

        val intent = Intent(context, BookActivity::class.java)
        intent.putExtra("book_id", book.book_id)
        startActivity(intent)
    }

}