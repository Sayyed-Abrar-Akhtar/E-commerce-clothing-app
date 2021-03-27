package com.sayyed.onlineclothingapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.models.Category
import com.sayyed.onlineclothingapplication.models.Review

class ReviewAdapter(
    val context: Context,
    val reviewsList: List<Review>
): RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {


    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tvReviewerName: TextView = view.findViewById(R.id.tvReviewerName)
        var rbReviewerRating: RatingBar = view.findViewById(R.id.rbReviewerRating)
        var tvReviewerComment: TextView = view.findViewById(R.id.tvReviewerComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.reviews_layout, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val reviewerDetail = reviewsList[position]

        holder.tvReviewerName.text = reviewerDetail.name
        holder.tvReviewerComment.text = reviewerDetail.comment
        holder.rbReviewerRating.rating = reviewerDetail.rating.toFloat()
    }

    override fun getItemCount(): Int = reviewsList.size
}