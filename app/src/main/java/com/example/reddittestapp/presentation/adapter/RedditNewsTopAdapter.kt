package com.example.reddittestapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.reddittestapp.R
import com.example.reddittestapp.data.network.model.RedditGetTopResponse
import com.example.reddittestapp.databinding.ItemTopNewsBinding
import com.example.reddittestapp.util.toTimeAgo
import com.squareup.picasso.Picasso

class RedditNewsTopAdapter(
    private var clickListener: NewsRedditViewHolder.OnImageViewClickListener,
    private var clickDownloadListener: NewsRedditViewHolder.OnDownloadClickListener,
) :
    PagingDataAdapter<RedditGetTopResponse.DataChildren.Children, NewsRedditViewHolder>(
        RedditNewsDiffCallBack()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsRedditViewHolder {
        return NewsRedditViewHolder(
            ItemTopNewsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), clickListener,
            clickDownloadListener
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
    private val binding: ItemTopNewsBinding,
    private var clickListener: OnImageViewClickListener,
    private var clickDownloadListener: OnDownloadClickListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(path: RedditGetTopResponse.DataChildren.Children?) {
        path?.let { itChildren ->


            if (path.data?.thumbnail != null) {

                binding.imgThumbnail.setOnClickListener {
                    clickListener.onClickImageViewListener(itChildren)
                }

                binding.downloadImages .setOnClickListener {
                    clickDownloadListener.onDownloadViewListener(itChildren)
                }

                Picasso.get()
                    .load(path.data?.thumbnail)
                    .fit()
                    .centerCrop()
                    .into(binding.imgThumbnail)
            } else {
                binding.imgThumbnail.setImageResource(R.drawable.img)
            }

            binding.description.text = itChildren.data?.title
            binding.author.text = itChildren.data?.author
            binding.comments.text = "${itChildren.data?.num_comments} comments"
            binding.time.text = itChildren.data?.created?.toTimeAgo()

        }
    }

    interface OnImageViewClickListener {
        fun onClickImageViewListener(
            item: RedditGetTopResponse.DataChildren.Children
        ) {
        }
    }
    interface OnDownloadClickListener {
        fun onDownloadViewListener(
            item: RedditGetTopResponse.DataChildren.Children
        ) {
        }
    }

}
