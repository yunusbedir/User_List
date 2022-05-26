package com.yunusbedir.userlist.ui.main

import com.yunusbedir.userlist.data.Person
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yunusbedir.userlist.databinding.ItemViewUserListBinding
import com.yunusbedir.userlist.util.GenericDiffUtil

class UserListAdapter(private val userListAdapterCallback: UserListAdapterCallback) : ListAdapter<Person, UserListAdapter.UserListViewHolder>(GenericDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        return UserListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind(getItem(position))
        if (currentList.size - 1 == position) {
            userListAdapterCallback.onRefresh()
        }
    }

    class UserListViewHolder(private val binding: ItemViewUserListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(person: Person) {
            binding.userTextView.text = "${person.fullName} (${person.id})"
        }

        companion object {
            fun from(parent: ViewGroup): UserListViewHolder {
                val binding = ItemViewUserListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return UserListViewHolder(binding)
            }
        }
    }

}