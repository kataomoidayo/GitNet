package com.putu.gitnet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.putu.gitnet.databinding.UserListBinding
import com.putu.gitnet.model.UserModel

class FollowingAdapter(private val followingList: List<UserModel>) : RecyclerView.Adapter<FollowingAdapter.ViewHolder>() {

    inner class ViewHolder(val userListBinding: UserListBinding) : RecyclerView.ViewHolder(userListBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = followingList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val following = followingList[position]

        with(holder.userListBinding) {
            Glide.with(root.context)
                .load(following.avatarUrl)
                .circleCrop()
                .into(imgAvatar)

            tvUsername.text = following.login
        }
    }
}