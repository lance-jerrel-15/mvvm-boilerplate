package com.mvvm.ui_profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.get
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.mvvm.commons.autoCleared
import com.mvvm.commons.base.BaseFragment
import com.mvvm.commons.base.SharedViewModel
import com.mvvm.commons.navigation.registerDeepLink
import com.mvvm.commons.util.getScreenDisplayMetrics
import com.mvvm.commons.util.getThemeColor
import com.mvvm.commons.view.Result
import com.mvvm.data.responses.UsersResponse
import com.mvvm.ui_profile.databinding.FragmentProfileBinding
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding
import dev.chrisbanes.insetter.setEdgeToEdgeSystemUiFlags
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.math.absoluteValue


class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val viewModel by inject<ProfileViewModel>()
    private val sharedViewModel: SharedViewModel by sharedViewModel()
    private var sheetBehavior by autoCleared<BottomSheetBehavior<LinearLayout>>()
    private var shapeBackgroundProvider by autoCleared<ShapeBackgroundProvider>()

    override fun createBinding(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(binding: FragmentProfileBinding, savedInstanceState: Bundle?) {
        binding.mainRoot.setEdgeToEdgeSystemUiFlags(true)
        binding.lifecycleOwner = this
        initBottomSheet()
        initView()

        binding.btnRegisterNow.setOnClickListener {
             findNavController().navigate(registerDeepLink())
        }
    }

    private fun initBottomSheet() {
        shapeBackgroundProvider = ShapeBackgroundProvider(requireContext())
        sheetBehavior = BottomSheetBehavior.from(requireBinding().contentLayout)
        sheetBehavior.isFitToContents = true
        sheetBehavior.isHideable = false
        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        val screenPixels = requireActivity().getScreenDisplayMetrics().heightPixels
        sheetBehavior.peekHeight = (screenPixels * BOTTOM_SHEET_COLLAPSED_THRESHOLD).toInt()

        val background = shapeBackgroundProvider.getBottomSheetBackground(
            requireContext().getThemeColor(R.attr.colorAccent)
        )
        ViewCompat.setBackground(requireBinding().contentLayout, background)

        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                requireBinding().headerContainer.alpha = slideOffset
                requireBinding().containerDetails.alpha = (slideOffset - 1f).absoluteValue
            }

            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }
        })
    }

    private fun initView() {


        viewModel.choiceEvent.observe(viewLifecycleOwner) {
            it.let { result ->
                when (result) {
                    is Result.Loading -> {
                    }
                    is Result.Success -> {
                        val data = result.data!!.data
                        chipView(data)
                    }
                }

            }
        }
        requireBinding().chipGroup.setOnCheckedChangeListener { group, checkedId ->

        }
    }

    private fun chipView(data: List<UsersResponse.Data>) {
        data.forEach {
            val chip = Chip(requireContext())
            val drawable = ChipDrawable.createFromAttributes(
                requireContext(),
                null, 0, R.style.Widget_MaterialComponents_Chip_Choice
            )
            chip.setChipDrawable(drawable)
            chip.text = it.firstName
            chip.isCheckable = true
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    Toast.makeText(requireContext(), it.firstName + it.id.toString(), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(),"Unchecked: " + it.firstName + it.id.toString(), Toast.LENGTH_SHORT).show()
                }

            }

            requireBinding().chipGroup.addView(chip)
        }
    }

    companion object {
        private const val BOTTOM_SHEET_COLLAPSED_THRESHOLD = 0.50f
    }
}