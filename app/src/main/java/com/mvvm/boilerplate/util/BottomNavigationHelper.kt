package com.mvvm.boilerplate.util

import android.content.Intent
import android.util.SparseArray
import androidx.core.util.forEach
import androidx.core.util.set
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationHelper(
    private var bottomNavigationView: BottomNavigationView?,
    private val navGraphIds: List<Int>,
    private val fragmentManager: FragmentManager,
    private val containerId: Int,
    private val intent: Intent?,
    private val onMenuItemClickedCallback: ((Int) -> Unit)? = null,
    private val destinationChangedListener: NavController.OnDestinationChangedListener? = null
) : LifecycleObserver {

    private var isOnFirstFragment = true
    private var firstFragmentTag = ""
    private var firstFragmentGraphId = 0
    private val _selectedNavController = MutableLiveData<NavController>()
    val selectedNavController: LiveData<NavController> = _selectedNavController

    /**
     * Manages the various graphs needed for a [BottomNavigationView].
     *
     * This sample is a workaround until the Navigation Component supports multiple back stacks.
     */
    fun setupWithNavController(): LiveData<NavController> {

        // Map of tags
        val graphIdToTagMap = SparseArray<String>()
        // Result. Mutable live data with the selected controlled

        // First create a NavHostFragment for each NavGraph ID
        navGraphIds.forEachIndexed { index, navGraphId ->
            val fragmentTag = getFragmentTag(index)

            // Find or create the Navigation host fragment
            val navHostFragment = obtainNavHostFragment(
                fragmentManager,
                fragmentTag,
                navGraphId,
                containerId
            )

            // Obtain its id
            val graphId = navHostFragment.navController.graph.id

            if (index == 0/*0*/) { // TODO uncomment 0 if dashboard will be available
                // ****** changed to 1 so that back pressing go backs to Explore tab
                firstFragmentGraphId = graphId
            }

            // Save to the map
            graphIdToTagMap[graphId] = fragmentTag

            // Attach or detach nav host fragment depending on whether it's the selected item.
            if (bottomNavigationView?.selectedItemId == graphId) {
                // Update livedata with the selected graph
                _selectedNavController.value = navHostFragment.navController
                // TODO uncomment 0 if dashboard will be available
                attachNavHostFragment(fragmentManager, navHostFragment, index == 0/*0*/)
            } else {
                detachNavHostFragment(fragmentManager, navHostFragment)
            }
        }

        // Now connect selecting an item with swapping Fragments
        var selectedItemTag = graphIdToTagMap[bottomNavigationView?.selectedItemId ?: 0]
        firstFragmentTag = graphIdToTagMap[firstFragmentGraphId]
        isOnFirstFragment = selectedItemTag == firstFragmentTag

        // When a navigation item is selected
        bottomNavigationView?.setOnNavigationItemSelectedListener { item ->
            // Don't do anything if the state is state has already been saved.
            if (fragmentManager.isStateSaved) {
                false
            } else {
                val newlySelectedItemTag = graphIdToTagMap[item.itemId]
                if (selectedItemTag != newlySelectedItemTag) {
                    // Pop everything above the first fragment (the "fixed start destination")
                    fragmentManager.popBackStack(
                        firstFragmentTag,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    val selectedFragment = fragmentManager.findFragmentByTag(newlySelectedItemTag)
                            as NavHostFragment

                    // Exclude the first fragment tag because it's always in the back stack.
                    if (firstFragmentTag != newlySelectedItemTag) {
                        // Commit a transaction that cleans the back stack and adds the first fragment
                        // to it, creating the fixed started destination.
                        fragmentManager.beginTransaction()
//                            .setCustomAnimations(
//                                R.anim.nav_default_enter_anim,
//                                R.anim.nav_default_exit_anim,
//                                R.anim.nav_default_pop_enter_anim,
//                                R.anim.nav_default_pop_exit_anim
//                            )
                            .attach(selectedFragment)
                            .setPrimaryNavigationFragment(selectedFragment)
                            .apply {
                                // Detach all other Fragments
                                graphIdToTagMap.forEach { _, fragmentTagIter ->
                                    if (fragmentTagIter != newlySelectedItemTag) {
                                        detach(fragmentManager.findFragmentByTag(firstFragmentTag)!!)
                                    }
                                }
                            }
                            .addToBackStack(firstFragmentTag)
                            .setReorderingAllowed(true)
                            .commit()
                    }
                    selectedItemTag = newlySelectedItemTag
                    isOnFirstFragment = selectedItemTag == firstFragmentTag

                    destinationChangedListener?.let {
                        _selectedNavController.value?.removeOnDestinationChangedListener(it)
                    }
                    _selectedNavController.value = selectedFragment.navController
                    destinationChangedListener?.let {
                        _selectedNavController.value?.addOnDestinationChangedListener(it)
                    }
                    onMenuItemClickedCallback?.invoke(item.itemId)

                    true
                } else {
                    false
                }
            }
        }

        // Optional: on item reselected, pop back stack to the destination of the graph
        setupItemReselected(graphIdToTagMap, fragmentManager)

        // Handle deep link
        setupDeepLinks(navGraphIds, fragmentManager, containerId, intent)

//    // Finally, ensure that we update our BottomNavigationView when the back stack changes
        fragmentManager.addOnBackStackChangedListener(backStackChangedListener)
        return _selectedNavController
    }

    private val backStackChangedListener = FragmentManager.OnBackStackChangedListener {
        if (!isOnFirstFragment && !fragmentManager.isOnBackStack(firstFragmentTag)) {
            bottomNavigationView?.selectedItemId = firstFragmentGraphId
        }

        // Reset the graph if the currentDestination is not valid (happens when the back
        // stack is popped after using the back button).
        _selectedNavController.value?.let { controller ->
            if (controller.currentDestination == null) {
                controller.navigate(controller.graph.id)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        bottomNavigationView = null
        fragmentManager.removeOnBackStackChangedListener(backStackChangedListener)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        destinationChangedListener?.let {
            selectedNavController.value?.removeOnDestinationChangedListener(it)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        destinationChangedListener?.let {
            selectedNavController.value?.addOnDestinationChangedListener(it)
        }
    }

    private fun setupDeepLinks(
        navGraphIds: List<Int>,
        fragmentManager: FragmentManager,
        containerId: Int,
        intent: Intent?
    ) {
        navGraphIds.forEachIndexed { index, navGraphId ->
            val fragmentTag = getFragmentTag(index)

            // Find or create the Navigation host fragment
            val navHostFragment = obtainNavHostFragment(
                fragmentManager,
                fragmentTag,
                navGraphId,
                containerId
            )
            // Handle Intent
            if (navHostFragment.navController.handleDeepLink(intent) &&
                bottomNavigationView?.selectedItemId != navHostFragment.navController.graph.id
            ) {
                bottomNavigationView?.selectedItemId = navHostFragment.navController.graph.id
            }
        }
    }

    private fun setupItemReselected(
        graphIdToTagMap: SparseArray<String>,
        fragmentManager: FragmentManager
    ) {
        bottomNavigationView?.setOnNavigationItemReselectedListener { item ->
            val newlySelectedItemTag = graphIdToTagMap[item.itemId]
            val selectedFragment = fragmentManager.findFragmentByTag(newlySelectedItemTag)
                    as NavHostFragment
            val navController = selectedFragment.navController
            // Pop the back stack to the start destination of the current navController graph
            navController.popBackStack(
                navController.graph.startDestination, false
            )
        }
    }

    private fun detachNavHostFragment(
        fragmentManager: FragmentManager,
        navHostFragment: NavHostFragment
    ) {
        fragmentManager.beginTransaction()
            .detach(navHostFragment)
            .commitNow()
    }

    private fun attachNavHostFragment(
        fragmentManager: FragmentManager,
        navHostFragment: NavHostFragment,
        isPrimaryNavFragment: Boolean
    ) {
        fragmentManager.beginTransaction()
            .attach(navHostFragment)
            .apply {
                if (isPrimaryNavFragment) {
                    setPrimaryNavigationFragment(navHostFragment)
                }
            }
            .commitNow()
    }

    private fun obtainNavHostFragment(
        fragmentManager: FragmentManager,
        fragmentTag: String,
        navGraphId: Int,
        containerId: Int
    ): NavHostFragment {
        // If the Nav Host fragment exists, return it
        val existingFragment = fragmentManager.findFragmentByTag(fragmentTag) as NavHostFragment?
        existingFragment?.let { return it }

        // Otherwise, create it and return it.
        val navHostFragment = NavHostFragment.create(navGraphId)
        fragmentManager.beginTransaction()
            .add(containerId, navHostFragment, fragmentTag)
            .commitNow()
        return navHostFragment
    }

    private fun FragmentManager.isOnBackStack(backStackName: String): Boolean {
        val backStackCount = backStackEntryCount
        for (index in 0 until backStackCount) {
            if (getBackStackEntryAt(index).name == backStackName) {
                return true
            }
        }
        return false
    }

    private fun getFragmentTag(index: Int) = "bottomNavigation#$index"
}
