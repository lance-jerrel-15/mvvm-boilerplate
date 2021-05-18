package com.mvvm.ui_doggo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mvvm.commons.autoCleared
import com.mvvm.commons.base.BaseFragment
import com.mvvm.ui_doggo.adapter.LoaderDoggoImageAdapter
import com.mvvm.ui_doggo.adapter.LoaderStateAdapter
import com.mvvm.ui_doggo.databinding.FragmentDoggoBinding
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

@ExperimentalPagingApi
class DoggoFragment : BaseFragment<FragmentDoggoBinding>() {

    private val viewModel by inject<DoggoViewModel>()
    private val adapter by lazy { LoaderDoggoImageAdapter() }
    private val adapterLoader by lazy { LoaderStateAdapter { adapter.retry() } }
    private var swipeLayout: SwipeRefreshLayout by autoCleared()

    override fun createBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentDoggoBinding = FragmentDoggoBinding.inflate(inflater, container, false)

    override fun onViewCreated(binding: FragmentDoggoBinding, savedInstanceState: Bundle?) {
        binding.rvDoggo.applySystemWindowInsetsToPadding(top = true)
        binding.adapter = adapter
        binding.lifecycleOwner = this
        initView()
        setupViews()
        onRefresh()
    }

    private fun initView() {
        viewModel.doggoEvent.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                adapter.submitData(it)
            }
        }
    }

    private fun setupViews() {
        requireBinding().rvDoggo.adapter = adapter.withLoadStateFooter(adapterLoader)
        requireBinding().rvDoggo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun onRefresh() {
        requireBinding().rvDoggo.recycledViewPool.clear()
        adapter.notifyDataSetChanged()
        swipeLayout = requireBinding().refresher
        swipeLayout.setOnRefreshListener {
            adapter.refresh()
            swipeLayout.isRefreshing = false
        }
    }
}