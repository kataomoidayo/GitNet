package com.putu.gitnet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.putu.gitnet.databinding.UserListBinding
import com.putu.gitnet.model.UserModel

class FollowersAdapter(private val followersList: List<UserModel>) : RecyclerView.Adapter<FollowersAdapter.ViewHolder>(){

    inner class ViewHolder(val userListBinding: UserListBinding) : RecyclerView.ViewHolder(userListBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = followersList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val follower = followersList[position]

        with(holder.userListBinding) {
            Glide.with(root.context)
                .load(follower.avatarUrl)
                .circleCrop()
                .into(imgAvatar)

            tvUsername.text = follower.login
        }
    }
}