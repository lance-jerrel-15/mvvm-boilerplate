package com.mvvm.home.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.mvvm.data.responses.UsersResponse
import com.mvvm.ui_home.databinding.ItemUsersBinding

class UsersViewHolder(private val onClickCallback: (id: Int) -> Unit,
                      private val binding: ItemUsersBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(users: UsersResponse.Data?) {
        if (users != null) {
            binding.txtUsersId.text = users.id.toString()
            binding.txtUsersName.text = users.firstName
            binding.txtUsersLname.text = users.lastName
            binding.txtUsersEmail.text = users.email
            if (users.avatar.isNotEmpty()) binding.imgUsersBanner.load(users.avatar)

            binding.imgUsersBanner.setOnClickListener {
                onClickCallback.invoke(users.id)
            }
        }
    }
}
