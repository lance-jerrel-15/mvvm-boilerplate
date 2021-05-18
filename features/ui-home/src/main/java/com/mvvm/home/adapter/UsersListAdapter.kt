package com.mvvm.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.commons.State
import com.mvvm.data.responses.UsersResponse
import com.mvvm.ui_home.R
import com.mvvm.ui_home.databinding.ItemUsersBinding


class UsersListAdapter(private val retry: () -> Unit,
                        private val onClickCallback: (id: Int) -> Unit)
    : PagedListAdapter<UsersResponse.Data, RecyclerView.ViewHolder>(UsersDiffCallback) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) getViewHolder(parent) else ListFooterViewHolder.create(retry, parent)
    }

    private fun getViewHolder(
        parent: ViewGroup
    ): RecyclerView.ViewHolder =
                DataBindingUtil.inflate<ItemUsersBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_users,
                    parent,
                    false
                ).run {
                    UsersViewHolder(onClickCallback, this)
                }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as UsersViewHolder).bind(getItem(position))
        else (holder as ListFooterViewHolder).bind(state)
    }


    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    companion object {
        val UsersDiffCallback = object : DiffUtil.ItemCallback<UsersResponse.Data>() {
            override fun areItemsTheSame(oldItem: UsersResponse.Data, newItem: UsersResponse.Data): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UsersResponse.Data, newItem: UsersResponse.Data): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }
}