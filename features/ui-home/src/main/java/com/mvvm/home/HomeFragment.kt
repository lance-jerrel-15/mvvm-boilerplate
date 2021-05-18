package com.mvvm.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mvvm.commons.State
import com.mvvm.commons.autoCleared
import com.mvvm.commons.base.BaseFragment
import com.mvvm.commons.base.SharedViewModel
import com.mvvm.commons.navigation.defaultNavAnimation
import com.mvvm.commons.navigation.userDeepLink
import com.mvvm.home.adapter.UsersListAdapter
import com.mvvm.ui_home.R
import com.mvvm.ui_home.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.progress_bar
import kotlinx.android.synthetic.main.fragment_home.txt_error
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel by inject<HomeViewModel>()
    private val sharedViewModel: SharedViewModel by sharedViewModel()

    private val usersListAdapter by lazy {
        UsersListAdapter({
            viewModel.invalidate()
        }, {
            findNavController().navigate(userDeepLink(it))
        })
    }

    private var swipeLayout: SwipeRefreshLayout by autoCleared()

    override fun createBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(binding: FragmentHomeBinding, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initAdapter()
        initState()

        swipeLayout = binding.refresher
        swipeLayout.setOnRefreshListener(this)
    }

    private fun initAdapter() {
        binding!!.adapter = usersListAdapter
        viewModel.output.usersEvent.observe(viewLifecycleOwner) {
            usersListAdapter.submitList(it)
        }
        usersListAdapter.notifyDataSetChanged()
    }

    private fun initState() {
        txt_error.setOnClickListener { viewModel.invalidate() }
        viewModel.getState().observe(viewLifecycleOwner) { state ->
            progress_bar.visibility = if (viewModel.listIsEmpty() && state == State.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (viewModel.listIsEmpty() && state == State.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                usersListAdapter.setState(state ?: State.DONE)
            }
        }
    }

    override fun onRefresh() {
        invalidate()
    }
    
    private fun invalidate() {
        usersListAdapter.submitList(null)
        binding!!.recyclerView.recycledViewPool.clear()
        usersListAdapter.notifyDataSetChanged()
        viewModel.invalidate()
        swipeLayout.isRefreshing = false
    }
}