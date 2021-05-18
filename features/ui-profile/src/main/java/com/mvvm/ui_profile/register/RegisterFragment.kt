package com.mvvm.ui_profile.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.mvvm.commons.base.BaseFragment
import com.mvvm.commons.base.SharedViewModel
import com.mvvm.commons.util.getPref
import com.mvvm.commons.util.updateEmail
import com.mvvm.commons.util.updateName
import com.mvvm.ui_profile.databinding.FragmentRegisterBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    private val viewModel by inject<RegisterViewModel>()

    private val sharedViewModel by sharedViewModel<SharedViewModel>()

    override fun createBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentRegisterBinding = FragmentRegisterBinding.inflate(inflater, container, false)

    override fun onViewCreated(binding: FragmentRegisterBinding, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initView()
    }

    private fun initView() {
        requireBinding().btnSave.setOnClickListener {
            validateAndSave()
        }
    }

    private fun validateAndSave() {
        if (requireBinding().edtName.text.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Enter Name", Toast.LENGTH_SHORT).show()
            return
        }

        if (requireBinding().edtEmail.text.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Enter Email", Toast.LENGTH_SHORT).show()
            return
        }

        val name = requireBinding().edtName.text.toString()
        val email = requireBinding().edtEmail.text.toString()
        requireContext().getPref().updateName(name)
        requireContext().getPref().updateEmail(email)
        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
    }
}