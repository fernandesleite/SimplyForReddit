package me.fernandesleite.simplyforreddit.ui.submission

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.fernandesleite.simplyforreddit.R
import net.dean.jraw.models.Submission

class SubmissionsAdapter(private val clickListener: OnClickListener) :
    ListAdapter<Submission, SubmissionsAdapter.ViewHolder>(DiffCallback()) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val author: TextView = view.findViewById(R.id.author)
        val title: TextView = view.findViewById(R.id.title)
        val thumbnail: ImageView = view.findViewById(R.id.thumbnail)
        val score: TextView = view.findViewById(R.id.score)
        val commentCount: TextView = view.findViewById(R.id.commentCount)
        val subreddit: TextView = view.findViewById(R.id.subreddit)
    }

    interface OnClickListener {
        fun onSubmissionClick(submission: Submission)
    }

    private val list = mutableListOf<Submission>()
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
        holder.title.text = getItem(position).title
        holder.author.text = "u/${getItem(position).author}"
        holder.commentCount.text = "${getItem(position).commentCount.toString()} comments"
        holder.score.text = getItem(position).score.toString()
        holder.subreddit.text = "r/${getItem(position).subreddit}"
        holder.thumbnail.setImageDrawable(null)
        if (URLUtil.isValidUrl(getItem(position).thumbnail)) {
            Glide.with(holder.thumbnail.context)
                .load(getItem(position).thumbnail)
                .into(holder.thumbnail)
            holder.thumbnail.visibility = View.VISIBLE
        } else {
            Glide.with(holder.thumbnail.context).clear(holder.thumbnail)
            holder.thumbnail.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            clickListener.onSubmissionClick(getItem(position))
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Submission>() {
        override fun areItemsTheSame(oldItem: Submission, newItem: Submission): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Submission, newItem: Submission): Boolean {
            return oldItem.id == newItem.id
        }
    }
}