package com.melagroup.veritas.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.melagroup.veritas.R
import com.melagroup.veritas.data.model.Book
import com.melagroup.veritas.ui.book.BookActivity
import com.melagroup.veritas.ui.home.Books.BookOnClickListener
import com.melagroup.veritas.ui.home.Books.mylist.MyListBooksAdapter
import com.melagroup.veritas.ui.main.HomeBooksViewModel

class HomeBooksFragment : Fragment(), BookOnClickListener {

    companion object {
        fun newInstance() = HomeBooksFragment()
    }

    val db = Firebase.firestore
    val auth : FirebaseAuth = Firebase.auth
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
        val dailyBooksAdapter = MyListBooksAdapter(emptyList<Book>(), this)
        val dailySkeleton = initBooksRecyclerView(dailyRecyclerView, dailyBooksAdapter)
        db.collection("books").get().addOnSuccessListener { res ->
            val list : List<Book>  = res.documents.map{
                Book(book_id = it.id, title= it["title"] as String?, photo_url = it["photo_url"] as String?,
                        authors = null, tags = null, synopsis = null)}
            dailyBooksAdapter.dataSet = list
            dailyBooksAdapter.notifyDataSetChanged()
            dailySkeleton?.hide()
        }

        val recommendationsRecyclerView: RecyclerView = view.findViewById(R.id.recyclerview_recommendations) as RecyclerView
        val recommendationsBooksAdapter = MyListBooksAdapter(emptyList<Book>(), this)
        val recommendationsSkeleton = initBooksRecyclerView(recommendationsRecyclerView, recommendationsBooksAdapter)
        db.collection("books").get().addOnSuccessListener { res ->
            val list : List<Book>  = res.documents.map{
                Book(book_id = it.id, title= it["title"] as String?, photo_url = it["photo_url"] as String?,
                        authors = null, tags = null, synopsis = null)}
            recommendationsBooksAdapter.dataSet = list
            recommendationsBooksAdapter.notifyDataSetChanged()
            recommendationsSkeleton?.hide()
        }

        val mylistRecyclerView: RecyclerView = view.findViewById(R.id.recyclerview_mylist) as RecyclerView
        val mylistBooksAdapter = MyListBooksAdapter(emptyList<Book>(), this)
        val mylistSkeleton = initBooksRecyclerView(mylistRecyclerView, mylistBooksAdapter)

        val favoritesRecyclerView: RecyclerView = view.findViewById(R.id.recyclerview_favorites) as RecyclerView
        val favoritesBooksAdapter = MyListBooksAdapter(emptyList<Book>(), this)
        val favoritesSkeleton = initBooksRecyclerView(favoritesRecyclerView, favoritesBooksAdapter)

        db.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener { res->
            if(res["MyBooks"] != null) {
                @Suppress("UNCHECKED_CAST")
                val books = res["MyBooks"] as ArrayList<HashMap<String, String>>
                val list: List<Book> = books.map {
                    Book(book_id = it["book_id"], title = it["title"], photo_url = it["photo_url"],
                            authors = null, tags = null, synopsis = null)}.reversed()

                view.findViewById<TextView>(R.id.label_mylist).visibility = if(list.isEmpty()) View.INVISIBLE else View.VISIBLE
                mylistBooksAdapter.dataSet = list
                mylistBooksAdapter.notifyDataSetChanged()
            }
            else{
                view.findViewById<TextView>(R.id.label_mylist).visibility = View.INVISIBLE
            }
            if(res["Favorites"] != null) {
                @Suppress("UNCHECKED_CAST")
                val books = res["Favorites"] as ArrayList<HashMap<String, String>>
                val list: List<Book> = books.map {
                    Book(book_id = it["book_id"], title = it["title"], photo_url = it["photo_url"],
                            authors = null, tags = null, synopsis = null)}.reversed()
                view.findViewById<TextView>(R.id.label_favorites).visibility = if(list.isEmpty()) View.INVISIBLE else View.VISIBLE
                favoritesBooksAdapter.dataSet = list
                favoritesBooksAdapter.notifyDataSetChanged()
            }
            else{
                view.findViewById<TextView>(R.id.label_favorites).visibility = View.INVISIBLE
            }


            favoritesSkeleton?.hide()
            mylistSkeleton?.hide()
        }


        db.collection("users").document(auth.currentUser!!.uid).addSnapshotListener { snapshot, e ->

            if (e != null) {
                Log.w("BookFragmentListener", "Listen failed.", e)
                return@addSnapshotListener
            }

            val res = snapshot?.data
            if(res?.get("MyBooks") != null) {
                @Suppress("UNCHECKED_CAST")
                val books = res["MyBooks"] as ArrayList<HashMap<String, String>>
                val list: List<Book> = books.map {
                    Book(book_id = it["book_id"], title = it["title"], photo_url = it["photo_url"],
                            authors = null, tags = null, synopsis = null)}.reversed()
                view.findViewById<TextView>(R.id.label_mylist).visibility = if(list.isEmpty()) View.INVISIBLE else View.VISIBLE
                mylistBooksAdapter.dataSet = list
                mylistBooksAdapter.notifyDataSetChanged()
            }
            if(res?.get("Favorites") != null) {
                @Suppress("UNCHECKED_CAST")
                val books = res["Favorites"] as ArrayList<HashMap<String, String>>
                val list: List<Book> = books.map {
                    Book(book_id = it["book_id"], title = it["title"], photo_url = it["photo_url"],
                            authors = null, tags = null, synopsis = null)}.reversed()
                view.findViewById<TextView>(R.id.label_favorites).visibility = if(list.isEmpty()) View.INVISIBLE else View.VISIBLE
                favoritesBooksAdapter.dataSet = list
                favoritesBooksAdapter.notifyDataSetChanged()
            }

            favoritesSkeleton?.hide()
            mylistSkeleton?.hide()
        }

    }
 private fun initBooksRecyclerView(recyclerView: RecyclerView, adapter: MyListBooksAdapter): RecyclerViewSkeletonScreen? {

        recyclerView.adapter =  adapter
        recyclerView.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
        )
        return Skeleton.bind(recyclerView)
                .adapter(adapter)
                .load(R.layout.book_skeleton)
                .color(R.color.shimmer_color_for_image)
                .show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeBooksViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onItemClick(book: Book, position: Int) {
        val intent = Intent(context, BookActivity::class.java)
        intent.putExtra("book_id", book.book_id)
        startActivity(intent)
    }

}