package com.yunusbedir.userlist.util

import com.yunusbedir.userlist.data.Person
import androidx.recyclerview.widget.DiffUtil

class GenericDiffUtil<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return when (oldItem) {
            is Person -> {
                oldItem.id == (newItem as Person).id
            }
            else -> throw Exception("Not supported")
        }
    }
}