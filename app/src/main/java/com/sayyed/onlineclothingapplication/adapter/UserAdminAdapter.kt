package com.sayyed.onlineclothingapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.sayyed.onlineclothingapplication.R
import com.sayyed.onlineclothingapplication.eventlistener.OnAdminUserClickListener
import com.sayyed.onlineclothingapplication.models.Users
import com.sayyed.onlineclothingapplication.utils.FileUpload

class UserAdminAdapter(
    val context: Context,
    private val userList: MutableList<Users>,
    private val onAdminUserClickListener: OnAdminUserClickListener
): RecyclerView.Adapter<UserAdminAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.show_all_layout, parent, false)
        return UserAdminAdapter.UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val userProfile = userList[position]
        "${userProfile.firstName} ${userProfile.lastName}".also { holder.tvItemName.text = it }
        val admin: Boolean = userProfile.isAdmin
        if(admin) {
            holder.tvItemQty.text = "Admin"
        } else {
            holder.tvItemQty.visibility = View.GONE
        }

        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(600, 120)
        Glide.with(context)
            .load(FileUpload.checkImageString(userProfile.image))
            .apply(requestOptions)
            .into(holder.imgProductItem)

        holder.ivItemEdit.visibility = View.GONE

        holder.ivItemDelete.setOnClickListener {
            onAdminUserClickListener.onProductDeleteClick(position, "${userProfile._id}")
        }
    }

    override fun getItemCount(): Int = userList.size

    class UserViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var tvItemName: TextView = itemView.findViewById(R.id.tvItemName)
        var tvItemQty: TextView = itemView.findViewById(R.id.tvItemQty)
        var imgProductItem: ImageView = itemView.findViewById(R.id.imgProductItem)
        var ivItemEdit: ImageView = itemView.findViewById(R.id.ivItemEdit)
        var ivItemDelete: ImageView = itemView.findViewById(R.id.ivItemDelete)
    }
}