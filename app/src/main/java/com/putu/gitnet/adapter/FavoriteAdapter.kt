package com.putu.gitnet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.putu.gitnet.database.UserEntity
import com.putu.gitnet.databinding.UserListBinding
import com.putu.gitnet.model.UserModel

class FavoriteAdapter (private val favoriteList: List<UserEntity>): RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(var userListBinding: UserListBinding): RecyclerView.ViewHolder(userListBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val userListBinding = UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(userListBinding)
    }

    override fun getItemCount() : Int  = favoriteList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = favoriteList[position]

        val user = UserModel (
            favorite.id,
            favorite.bio,
            favorite.login,
            favorite.company,
            favorite.publicRepos,
            favorite.followers,
            favorite.avatarUrl,
            favorite.following,
            favorite.name,
            favorite.htmlUrl
        )

        Glide.with(holder.itemView.context)
            .load(favorite.avatarUrl)
            .circleCrop()
            .into(holder.userListBinding.imgAvatar)

        holder.userListBinding.tvUsername.text = favorite.login

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(user)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserModel)
    }
}