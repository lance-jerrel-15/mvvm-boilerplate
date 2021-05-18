package com.mvvm.boilerplate.ui.mainnav

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.mvvm.boilerplate.R
import com.mvvm.boilerplate.databinding.FragmentMainBinding
import com.mvvm.boilerplate.util.BottomNavigationHelper
import com.mvvm.commons.autoCleared
import com.mvvm.commons.base.BaseFragment
import com.mvvm.commons.base.SharedViewModel
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding
import dev.chrisbanes.insetter.setEdgeToEdgeSystemUiFlags
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment<FragmentMainBinding>(), NavController.OnDestinationChangedListener {

    private val sharedViewModel by sharedViewModel<SharedViewModel>()
    private var navigationHelper by autoCleared<BottomNavigationHelper>()

    private val viewModel: MainViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)

    override fun onViewCreated(binding: FragmentMainBinding, savedInstanceState: Bundle?) {
        binding.navHostContainer.applySystemWindowInsetsToPadding(top = true)
        binding.bottomNavView.applySystemWindowInsetsToPadding(bottom = true)
        binding.mainRoot.setEdgeToEdgeSystemUiFlags(true)
        binding.viewModel = viewModel
        initHelper()
        if (savedInstanceState == null) setupBottomNavigationBar()
    }

    private fun initHelper() {
        val bottomNavigationView = binding!!.bottomNavView
        val navGraphIds = listOf(
            R.navigation.bottom_nav_one,
            R.navigation.bottom_nav_two,
            R.navigation.bottom_nav_three
        )

        navigationHelper = BottomNavigationHelper(
            bottomNavigationView = bottomNavigationView,
            navGraphIds = navGraphIds,
            fragmentManager = childFragmentManager,
            containerId = R.id.nav_host_container,
            intent = requireActivity().intent,
            onMenuItemClickedCallback = menuItemClickedCallback,
            destinationChangedListener = this
        )
    }

    private val menuItemClickedCallback: (Int) -> Unit = { itemId ->
        when (itemId) {
            R.id.bottom_nav_one -> {
            }
            else -> {
            }
        }
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        // BottomNav visibility
        when (destination.id) {
            in NO_BOTTOM_NAV_IDS -> {
                showBottomNav(false)
            }
            else -> {
                showBottomNav(true)
            }
        }

        // Toolbar Back visibility
        when (destination.id) {
            in HAS_BACK_IDS -> {
            }
            else -> {
            }
        }
    }

    private fun showToolbar(show: Boolean) {
        if (false) binding!!.header.root.visibility = View.VISIBLE
        else binding!!.header.root.visibility = View.GONE
    }

    @SuppressLint("RestrictedApi")
    private fun showBack(show: Boolean) {
        binding!!.header.ivBack.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showBottomNav(show: Boolean) {
        with(binding!!.bottomNavView) {
            if (show && visibility == View.VISIBLE || !show && visibility == View.GONE) return

//            val transition: Transition = Slide(Gravity.BOTTOM)
//            transition.duration = 600
//            transition.addTarget(this)
//
//            TransitionManager.beginDelayedTransition(binding!!.root as ViewGroup, transition)
            visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        viewLifecycleOwner.lifecycle.removeObserver(navigationHelper)
        viewLifecycleOwner.lifecycle.addObserver(navigationHelper)
        // Setup the bottom navigation view with a list of navigation graphs
        val controller = navigationHelper.setupWithNavController()

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(viewLifecycleOwner) {
            // do nothing
            sharedViewModel.currentNavController = controller
            //            binding!!.header.toolbar.setupWithNavController(navController)
            //            binding!!.header.toolbar.setOnMenuItemClickListener {
            //                viewModel.logout()
            //                true
            //            }
        }
        sharedViewModel.currentNavController = controller
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.currentNavController = null
    }

    companion object {
        private val HAS_BACK_IDS = arrayOf(
            R.id.userFragment
        )

        private val NO_BOTTOM_NAV_IDS = arrayOf(
            R.id.loginFragment,
            R.id.userFragment
        )
    }
}
