package com.melagroup.veritas.ui.home.Books.daily

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.melagroup.veritas.R
import com.melagroup.veritas.ui.home.Books.BookOnClickListener

class DailyBooksAdapter (private val dataSet: Array<String>,
                         val itemClickListener: BookOnClickListener
) :
    RecyclerView.Adapter<DailyBooksAdapter.ViewHolder>() {

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            init {
                // Define click listener for the ViewHolder's View.
            }
            fun bind(user: String, clickListener: BookOnClickListener)
            {
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
            viewHolder.bind("a$position", itemClickListener)
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size

    }