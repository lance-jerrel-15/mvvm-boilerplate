package com.mvvm.login

import android.content.Context.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.mvvm.commons.base.BaseFragment
import com.mvvm.commons.base.SharedViewModel
import com.mvvm.commons.util.getPref
import com.mvvm.commons.util.isLoggedIn
import com.mvvm.commons.util.updateToken
import com.mvvm.commons.view.Result
import com.mvvm.login.databinding.FragmentLoginBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel by inject<LoginViewModel>()

    private val sharedViewModel by sharedViewModel<SharedViewModel>()

    override fun createBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (requireContext().getPref().isLoggedIn()) {
            sharedViewModel.setAuthenticationState(SharedViewModel.AuthenticationState.AUTHENTICATED)
        }
    }

    override fun onViewCreated(binding: FragmentLoginBinding, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        subscribeUi()
    }

    private fun subscribeUi() {
        viewModel.output.loginEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is Result.Loading -> return@observe
                    is Result.Success -> {
                        val token = result.data!!.token
                        requireContext().getPref().updateToken(token)
                        hideKeyboard()
                        sharedViewModel.setAuthenticationState(
                            SharedViewModel.AuthenticationState.AUTHENTICATED
                        )
                    }
                    is Result.Error -> {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}
