package com.emamagic.android_peermessanger.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.emamagic.android_peermessanger.R

abstract class BaseFragment<VB: ViewBinding>: Fragment() {

    private  var _binding: VB? = null
    protected val binding get() = _binding
    private lateinit var loading: FrameLayout
    private var callback: OnBackPressedCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getFragmentBinding(inflater ,container)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading = binding?.root?.rootView?.findViewById(R.id.my_loading)!!
    }

    protected fun showLoading(isDim: Boolean = false){
        if (loading.visibility != View.VISIBLE){
            if (isDim) loading.setBackgroundColor(resources.getColor(R.color.dim))
            loading.visibility = View.VISIBLE
        }
    }

    protected fun hideLoading(){
        if (loading.visibility != View.GONE){
            loading.visibility = View.GONE
        }
    }

    abstract fun getFragmentBinding(inflater: LayoutInflater ,container: ViewGroup?): VB


    fun onMyBackPressed(owner: LifecycleOwner, call: () -> Unit) {
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                call()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(owner, callback!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (callback != null){
            callback?.isEnabled = false
            callback?.remove()
        }
    }

}

