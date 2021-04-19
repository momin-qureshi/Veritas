package com.melagroup.veritas.ui.home.Books.mylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.melagroup.veritas.R
import com.melagroup.veritas.data.model.Book
import com.melagroup.veritas.ui.home.Books.BookOnClickListener

class MyListBooksAdapter (var dataSet: List<Book>,
                          private val itemClickListener: BookOnClickListener) :
    RecyclerView.Adapter<MyListBooksAdapter.ViewHolder>() {


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        val title: TextView = view.findViewById(R.id.title)

        init {
            // Define click listener for the ViewHolder's View.
        }
        fun bind(book: Book, position: Int, clickListener: BookOnClickListener)
        {
            view.setOnClickListener {
                clickListener.onItemClick(book, position)
            }
        }
        fun setImage(photo_url: String?){

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.book_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        var title = dataSet[position].title
        if (title != null) {
            if(title.length > 20){
                title = "${title.subSequence(0, 17)}..."
            }
        }
        viewHolder.title.text = title
        viewHolder.setImage(dataSet[position].photo_url)
        viewHolder.bind(dataSet[position], position, itemClickListener)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}