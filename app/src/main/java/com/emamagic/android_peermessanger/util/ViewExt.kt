package com.emamagic.android_peermessanger.util

import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import com.emamagic.android_peermessanger.R
import com.emamagic.android_peermessanger.util.Constants.MODE_TOAST_ERROR
import com.emamagic.android_peermessanger.util.Constants.MODE_TOAST_SUCCESS
import com.emamagic.android_peermessanger.util.Constants.MODE_TOAST_WARNING


inline fun SearchView.onQueryTextListener(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String?) = true

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })

}

fun Fragment.toasty(@StringRes titleId: Int, selectedMode: Int? = null) {
    val layout = layoutInflater.inflate(
        R.layout.toast_layout,
        requireView().findViewById(R.id.toast_root)
    )
    when (selectedMode) {

        MODE_TOAST_SUCCESS -> {
            layout.findViewById<ImageView>(R.id.toast_img)
                .setImageResource(R.drawable.ic_corroct_toast)
            layout.findViewById<ConstraintLayout>(R.id.toast_root)
                .setBackgroundResource(R.drawable.bg_corroct_toast)
        }
        MODE_TOAST_WARNING -> {
            layout.findViewById<ImageView>(R.id.toast_img)
                .setImageResource(R.drawable.ic_warning_toast)
            layout.findViewById<ConstraintLayout>(R.id.toast_root)
                .setBackgroundResource(R.drawable.bg_warning_toast)
            layout.findViewById<TextView>(R.id.toast_txt)
                .setTextColor(resources.getColor(R.color.black))
        }
        MODE_TOAST_ERROR -> {
            layout.findViewById<ImageView>(R.id.toast_img)
                .setImageResource(R.drawable.ic_error_toast)
            layout.findViewById<ConstraintLayout>(R.id.toast_root)
                .setBackgroundResource(R.drawable.bg_error_toast)
        }
        else -> {
            Toast.makeText(requireContext(), resources.getString(titleId), Toast.LENGTH_LONG).show()
        }

    }

    layout.findViewById<TextView>(R.id.toast_txt).text = resources.getString(titleId)
    if (selectedMode != null) {
        Toast(requireActivity()).apply {
            setGravity(Gravity.BOTTOM, 0, 100)
            duration = Toast.LENGTH_LONG
            view = layout
        }.show()
    }
}

fun Fragment.toasty(text: String, selectedMode: Int? = null) {
    val layout = layoutInflater.inflate(
        R.layout.toast_layout,
        requireView().findViewById(R.id.toast_root)
    )
    when (selectedMode) {

        MODE_TOAST_SUCCESS -> {
            layout.findViewById<ImageView>(R.id.toast_img)
                .setImageResource(R.drawable.ic_corroct_toast)
            layout.findViewById<ConstraintLayout>(R.id.toast_root)
                .setBackgroundResource(R.drawable.bg_corroct_toast)
        }
        MODE_TOAST_WARNING -> {
            layout.findViewById<ImageView>(R.id.toast_img)
                .setImageResource(R.drawable.ic_warning_toast)
            layout.findViewById<ConstraintLayout>(R.id.toast_root)
                .setBackgroundResource(R.drawable.bg_warning_toast)
            layout.findViewById<TextView>(R.id.toast_txt)
                .setTextColor(resources.getColor(R.color.black))
        }
        MODE_TOAST_ERROR -> {
            layout.findViewById<ImageView>(R.id.toast_img)
                .setImageResource(R.drawable.ic_error_toast)
            layout.findViewById<ConstraintLayout>(R.id.toast_root)
                .setBackgroundResource(R.drawable.bg_error_toast)
        }
        else -> {
            Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
        }

    }

    layout.findViewById<TextView>(R.id.toast_txt).text = text
    if (selectedMode != null) {
        Toast(requireActivity()).apply {
            setGravity(Gravity.BOTTOM, 0, 100)
            duration = Toast.LENGTH_LONG
            view = layout
        }.show()
    }
}

val <T> T.exhaustive: T
    get() = this
