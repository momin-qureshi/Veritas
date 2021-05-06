package com.melagroup.veritas.ui.book.reviews


import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.like.LikeButton
import com.like.Utils
import com.melagroup.veritas.R
import com.melagroup.veritas.data.model.Genre
import com.melagroup.veritas.data.model.Review
import kotlin.coroutines.coroutineContext

class ReviewsRecyclerViewAdapter (var dataSet: List<Review>) :
        RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ViewHolder>() {


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        val text: TextView = view.findViewById(R.id.text_review)
        val like: ImageView = view.findViewById(R.id.thumbsup)
        var drawables : ArrayList<Drawable?>? = null

        init {

            drawables = arrayListOf(ResourcesCompat.getDrawable(view.resources, R.drawable.thumb_on, null),
                    ResourcesCompat.getDrawable(view.resources, R.drawable.thumbs_down, null))
        }
        fun bind(review: Review, position: Int)
        {
            view.setOnClickListener {
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.review_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.text.text = dataSet[position].text
        if(viewHolder.drawables != null && dataSet[position].isPositive != null)
            viewHolder.like.setImageDrawable(viewHolder.drawables!![if(dataSet[position].isPositive!!) 0 else 1])

        // viewHolder.bind(dataSet[position], position, itemClickListener)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}