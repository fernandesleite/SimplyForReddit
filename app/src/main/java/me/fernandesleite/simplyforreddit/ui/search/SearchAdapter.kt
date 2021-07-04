package me.fernandesleite.simplyforreddit.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.fernandesleite.simplyforreddit.R
import net.dean.jraw.models.SubredditSearchResult

class SearchAdapter: ListAdapter<SubredditSearchResult, SearchAdapter.ViewHolder>(DiffCallback()) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val subredditName: TextView = view.findViewById(R.id.subredditName)
    }

    private class DiffCallback : DiffUtil.ItemCallback<SubredditSearchResult>() {
        override fun areItemsTheSame(oldItem: SubredditSearchResult, newItem: SubredditSearchResult): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: SubredditSearchResult, newItem: SubredditSearchResult): Boolean {
            return oldItem.name == newItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_search_subreddit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.subredditName.text = getItem(position).name

    }
}