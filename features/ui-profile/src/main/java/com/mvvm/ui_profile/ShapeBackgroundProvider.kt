package com.mvvm.ui_profile

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.mvvm.commons.util.getThemeColor

internal class ShapeBackgroundProvider(private val context: Context) {

    fun getAddressBackground(
        active: Boolean,
        roundedBottomLeft: Boolean = false,
        roundedBottomRight: Boolean = false
    ) = MaterialShapeDrawable(
        ShapeAppearanceModel.builder()
            .also {
                if (roundedBottomLeft) {
                    it.setBottomLeftCorner(
                        CornerFamily.ROUNDED,
                        cornerSize
                    )
                }
                if (roundedBottomRight) {
                    it.setBottomRightCorner(
                        CornerFamily.ROUNDED,
                        cornerSize
                    )
                }
            }
            .build()
    ).apply {
        fillColor = ColorStateList.valueOf(
            context.getThemeColor(
                if (active) R.attr.colorSurface
                else R.attr.colorPrimarySurface
            )
        )
    }

    fun getInterestBackground(active: Boolean) = MaterialShapeDrawable(
        ShapeAppearanceModel.builder()
            .setTopRightCorner(
                CornerFamily.ROUNDED,
                cornerSize
            ).build()
    ).apply {
        fillColor = ColorStateList.valueOf(
            context.getThemeColor(
                if (active) R.attr.colorSurface
                else R.attr.colorPrimarySurface
            )
        )
    }

    fun getPersonalInfoBackground(active: Boolean) = MaterialShapeDrawable(
        ShapeAppearanceModel.builder()
            .setTopLeftCorner(
                CornerFamily.ROUNDED,
                cornerSize
            ).build()
    ).apply {
        fillColor = ColorStateList.valueOf(
            context.getThemeColor(
                if (active) R.attr.colorSurface
                else R.attr.colorPrimarySurface
            )
        )
    }

    fun getBottomSheetBackground(@ColorInt color: Int) =
        MaterialShapeDrawable(
            ShapeAppearanceModel.builder()
                .setTopLeftCorner(
                    CornerFamily.ROUNDED,
                    cornerSize
                )
                .setTopRightCorner(
                    CornerFamily.ROUNDED,
                    cornerSize
                )
                .build()
        ).apply {
            fillColor = ColorStateList.valueOf(
                color
            )
        }

    private val cornerSize = context.resources.getDimension(R.dimen.cornerSizeBottomSheet)
}
