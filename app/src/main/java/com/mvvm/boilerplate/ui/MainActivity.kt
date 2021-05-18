package com.mvvm.boilerplate.ui

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.mvvm.boilerplate.R
import com.mvvm.commons.base.SharedViewModel
import com.mvvm.commons.livedatabus.Event
import com.mvvm.commons.livedatabus.LiveDataBus
import com.mvvm.commons.util.clear
import com.mvvm.commons.util.getPref
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModel<SharedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        subscribeUi()
    }

    private fun subscribeUi() {
        LiveDataBus.subscribe(Event.UNAUTHORIZED, this) {
            it.runAndConsume {
                /**
                 * TODO Clear Data
                 * **/
                mainViewModel.refuseAuthentication()
                this.getPref().clear()
            }
        }

        mainViewModel.authenticationState.observe(this) { state ->
            when (state) {
                SharedViewModel.AuthenticationState.UNAUTHENTICATED -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.action_to_login)
                }
                SharedViewModel.AuthenticationState.AUTHENTICATED -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.action_to_main)
                }
                else -> {
                }
            }
        }

        mainViewModel.navigateEvent.observe(this) {
            it.getContentIfNotHandled()?.let { event ->
                findNavController(R.id.nav_host_fragment)
                    .navigate(event.actionId, event.args, event.navOptions, event.extras)
            }
        }
        mainViewModel.deepLinkEvent.observe(this) {
            it.getContentIfNotHandled()?.let { event ->
                findNavController(R.id.nav_host_fragment)
                    .navigate(event.deepLink, event.navOptions, event.extras)
            }
        }
    }

    // back pressed action
    override fun onSupportNavigateUp(): Boolean {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        return mainViewModel.currentNavController?.value?.navigateUp()
            ?: findNavController(R.id.nav_host_fragment).navigateUp()
    }
}