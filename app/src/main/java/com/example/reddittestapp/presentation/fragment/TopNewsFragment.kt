package com.example.reddittestapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reddittestapp.databinding.FragmentTopNewsBinding
import com.example.reddittestapp.domain.GetTopNewsRedditVM
import com.example.reddittestapp.presentation.adapter.RedditNewsTopAdapter
import com.example.reddittestapp.util.autoCleared
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class TopNewsFragment : Fragment() {

    private val adapter =
        RedditNewsTopAdapter { name: String -> newsTopClickedPlayer(name) }
    private val getTopNewsRedditVM: GetTopNewsRedditVM by viewModels()
    private var binding: FragmentTopNewsBinding by autoCleared()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        startSearch()
//        CoroutineScope(Dispatchers.Main).launch {
//            delay(2000)
            initAdapter()
            startSearch()
//        }


    }


    private fun newsTopClickedPlayer(name: String) {
        val parentLayout = requireView().findViewById<View>(android.R.id.content)
        Snackbar.make(parentLayout, name, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun initAdapter() {
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
//                binding.progress.isVisible = false
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
         lifecycleScope.launch {
            getTopNewsRedditVM.searchTopNewsLiveData().observe(viewLifecycleOwner, {
                adapter.submitData(this@TopNewsFragment.lifecycle, it)
            })
        }
    }

}