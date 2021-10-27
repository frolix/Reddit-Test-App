package com.example.reddittestapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.reddittestapp.data.network.model.RedditGetTopResponse
import com.example.reddittestapp.databinding.ItemTopNewsBinding
import com.squareup.picasso.Picasso

class RedditNewsTopAdapter(private val clicked: (String) -> Unit) :
    PagingDataAdapter<RedditGetTopResponse.DataChildren.Children, NewsRedditViewHolder>(
        RedditNewsDiffCallBack()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsRedditViewHolder {
        return NewsRedditViewHolder(
            ItemTopNewsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NewsRedditViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }
}

class RedditNewsDiffCallBack : DiffUtil.ItemCallback<RedditGetTopResponse.DataChildren.Children>() {
    override fun areItemsTheSame(
        oldItem: RedditGetTopResponse.DataChildren.Children,
        newItem: RedditGetTopResponse.DataChildren.Children
    ): Boolean {
        return oldItem.data?.title == newItem.data?.title
    }

    override fun areContentsTheSame(
        oldItem: RedditGetTopResponse.DataChildren.Children,
        newItem: RedditGetTopResponse.DataChildren.Children
    ): Boolean {
        return oldItem == newItem
    }
}

class NewsRedditViewHolder(
    private val binding: ItemTopNewsBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(path: RedditGetTopResponse.DataChildren.Children?) {
        path?.let {
            if (path.data?.thumbnail != null) {
                Picasso.get()
                    .load(path.data?.thumbnail)
                    .fit()
                    .centerCrop()
                    .into(binding.imgThumbnail)
            } else {
                binding.imgThumbnail.setImageBitmap(null)
            }

            binding.description.text = it.data?.title
            binding.author.text = it.data?.author
            binding.comments.text = "${it.data?.num_comments} comments"
            binding.time.text = it.data?.created.toString()

        }
    }


}
