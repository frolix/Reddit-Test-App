package com.example.reddittestapp.presentation.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reddittestapp.data.network.model.RedditGetTopResponse
import com.example.reddittestapp.databinding.FragmentTopNewsBinding
import com.example.reddittestapp.domain.GetTopNewsRedditVM
import com.example.reddittestapp.presentation.adapter.NewsRedditViewHolder
import com.example.reddittestapp.presentation.adapter.RedditNewsTopAdapter
import com.example.reddittestapp.util.saveToInternalStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class TopNewsFragment : Fragment(), NewsRedditViewHolder.OnImageViewClickListener,
    NewsRedditViewHolder.OnDownloadClickListener {

    private lateinit var adapter: RedditNewsTopAdapter
    private val getTopNewsRedditVM: GetTopNewsRedditVM by viewModels()
    private lateinit var binding: FragmentTopNewsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopNewsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onClickImageViewListener(
        item: RedditGetTopResponse.DataChildren.Children
    ) {
        super.onClickImageViewListener(item)
        if (item.data?.url_overridden_by_dest != null) {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(item.data?.url_overridden_by_dest))
            startActivity(browserIntent)
        }
    }


    override fun onDownloadViewListener(item: RedditGetTopResponse.DataChildren.Children) {
        super.onDownloadViewListener(item)
        if (item.data?.thumbnail != null) {
            lifecycleScope.launchWhenStarted {
                val uri = item.data?.thumbnail
                uri?.saveToInternalStorage(requireContext())
            }
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        startSearch()
    }


    private fun initAdapter() {
        adapter = RedditNewsTopAdapter(this, this)

        binding.newsTopRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        binding.newsTopRecyclerView.adapter = adapter

        adapter.addLoadStateListener { loadState ->
            if (loadState.mediator?.refresh is LoadState.Loading) {
                if (adapter.snapshot().isEmpty()) {
                    binding.progress.isVisible = true
                }
                binding.errorTxt.isVisible = false
            } else {
                binding.progress.isVisible = false
                val error = when {
                    loadState.mediator?.prepend is LoadState.Error -> loadState.mediator?.prepend as LoadState.Error
                    loadState.mediator?.append is LoadState.Error -> loadState.mediator?.append as LoadState.Error
                    loadState.mediator?.refresh is LoadState.Error -> loadState.mediator?.refresh as LoadState.Error
                    else -> null
                }
                error?.let {
                    if (adapter.snapshot().isEmpty()) {
                        binding.errorTxt.isVisible = true
                        binding.errorTxt.text = it.error.localizedMessage
                    }
                }
            }
        }
    }

    private fun startSearch() {
        Log.d("startSearch", "startSearch: ")
        lifecycleScope.launch {
            getTopNewsRedditVM.searchTopNewsLiveData.collectLatest {
                adapter.submitData(this@TopNewsFragment.lifecycle, it)
            }

            /*
            //this code using livedata
           getTopNewsRedditVM.searchTopNewsLiveData().observe(viewLifecycleOwner, {
               adapter.submitData(this@TopNewsFragment.lifecycle, it)
           })
             */
        }
    }

}