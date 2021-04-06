package com.emamagic.android_peermessanger.ui.messagelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emamagic.android_peermessanger.base.BaseFragment
import com.emamagic.android_peermessanger.databinding.FragmentMessageListBinding

class MessageListFragment: BaseFragment<FragmentMessageListBinding>() {


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMessageListBinding.inflate(inflater ,container ,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}