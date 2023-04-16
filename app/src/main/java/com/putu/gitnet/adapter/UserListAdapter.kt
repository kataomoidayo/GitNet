package com.putu.gitnet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.putu.gitnet.databinding.UserListBinding
import com.putu.gitnet.model.UserModel

class UserListAdapter(private val userList: List<UserModel>) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(var userListBinding: UserListBinding): RecyclerView.ViewHolder(userListBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val userListBinding = UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(userListBinding)
    }

    override fun getItemCount() : Int  = userList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]

        Glide.with(holder.itemView.context)
            .load(user.avatarUrl)
            .circleCrop()
            .into(holder.userListBinding.imgAvatar)

        holder.userListBinding.tvUsername.text = user.login

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(userList[holder.adapterPosition])
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserModel)
    }
}

