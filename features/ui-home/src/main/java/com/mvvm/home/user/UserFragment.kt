package com.mvvm.home.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mvvm.commons.base.BaseFragment
import com.mvvm.commons.view.Result
import com.mvvm.ui_home.databinding.FragmentUserBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFragment : BaseFragment<FragmentUserBinding>() {

    private val args: UserFragmentArgs by navArgs()

    private val viewModel: UserViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentUserBinding = FragmentUserBinding.inflate(inflater, container, false)

    override fun onViewCreated(binding: FragmentUserBinding, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initView()
        subscribeUi()
    }

    private fun initView() {
        viewModel.getUserId(args.itemId)

        binding!!.onClickBack = View.OnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun subscribeUi() {
        viewModel.userEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                when(result) {
                    is Result.Loading -> return@observe
                    is Result.Success -> {

                    }
                    is Result.Error -> {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}