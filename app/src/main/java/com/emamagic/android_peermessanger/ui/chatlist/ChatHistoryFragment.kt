package com.emamagic.android_peermessanger.ui.chatlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.emamagic.android_peermessanger.base.BaseFragment
import com.emamagic.android_peermessanger.databinding.FragmentChatHistoryBinding

class ChatHistoryFragment: BaseFragment<FragmentChatHistoryBinding>() {


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentChatHistoryBinding.inflate(inflater ,container ,false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            btnFloatingNewChat.setOnClickListener {
                findNavController().navigate(ChatHistoryFragmentDirections.actionChatHistoryFragmentToPairedDevicesFragment())
            }
        }

    }


}