package me.fernandesleite.simplyforreddit.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.fernandesleite.simplyforreddit.R
import net.dean.jraw.models.Submission

class SubmissionsAdapter : ListAdapter<Submission, SubmissionsAdapter.ViewHolder>(DiffCallback()) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val author: TextView = view.findViewById(R.id.author)
    }
    val list = mutableListOf<Submission>()


    fun addItems(items: MutableList<Submission>) {
        if (!currentList.containsAll(items)) {
            list.addAll(items)
            submitList(list)
            notifyDataSetChanged()
        }
    }

    fun removeItems() {
        list.clear()
        submitList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_submission, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.author.text = getItem(position).author
    }

    private class DiffCallback : DiffUtil.ItemCallback<Submission>() {
        override fun areItemsTheSame(oldItem: Submission, newItem: Submission): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Submission, newItem: Submission): Boolean {
            return oldItem.id == newItem.id
        }
    }
}