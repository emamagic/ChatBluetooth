package com.emamagic.android_peermessanger.ui.chat

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.emamagic.android_peermessanger.R
import com.emamagic.android_peermessanger.base.BaseFragment
import com.emamagic.android_peermessanger.bluetooth.SerialListener
import com.emamagic.android_peermessanger.bluetooth.SerialService
import com.emamagic.android_peermessanger.bluetooth.SerialSocket
import com.emamagic.android_peermessanger.databinding.FragmentChatBinding
import com.emamagic.android_peermessanger.util.TextUtil

class ChatFragment : BaseFragment<FragmentChatBinding>(), ServiceConnection, SerialListener {

    private val args: ChatFragmentArgs by navArgs()

    private var service: SerialService? = null
    private var connected = Connected.False
    private var initialStart = true
    private val newline = TextUtil.newline_crlf
    private var pendingNewline = false

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentChatBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            imgChatFSend.setOnClickListener {
                send(edtChatFSend.text.toString())
            }
        }


    }

    private fun connect() {
        try {
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice(args.deviceAddress)
            Log.e("TAG", "connecting ...")
            connected = Connected.Pending
            val socket = SerialSocket(requireActivity().applicationContext, device)
            service?.connect(socket)
        } catch (e: java.lang.Exception) {
            onSerialConnectError(e)
        }
    }

    private fun disconnect() {
        connected = Connected.False
        service?.disconnect()
    }

    private fun send(str: String) {
        if (connected != Connected.True) {
            Toast.makeText(activity, "not connected", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val msg: String = str
            val data: ByteArray = (str + newline).toByteArray()

            val spn = SpannableStringBuilder("$msg ".trimIndent())
            spn.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.colorSendText)),
                0,
                spn.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding?.txtChatFReceive?.append(spn)
            service?.write(data)
        } catch (e: java.lang.Exception) {
            onSerialIoError(e)
        }
    }

    private fun receive(data: ByteArray) {
        var msg = String(data)
        if (newline == TextUtil.newline_crlf && msg.isNotEmpty()) {
            msg = msg.replace(TextUtil.newline_crlf, TextUtil.newline_lf)
            if (pendingNewline && msg[0] == '\n') {
                val edt: Editable? = binding?.txtChatFReceive?.editableText
                if (edt != null && edt.length > 1) edt.replace(edt.length - 2, edt.length, "")
            }
            pendingNewline = msg[msg.length - 1] == '\r'
        }
        binding?.txtChatFReceive?.append(TextUtil.toCaretString(msg, newline.isNotEmpty()))

    }

    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
        service = (binder as SerialService.SerialBinder).service
        service?.attach(this)
        if (initialStart && isResumed) {
            initialStart = false
            requireActivity().runOnUiThread { connect() }
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        service = null
    }

    override fun onSerialConnect() {
        Log.e("TAG", "connected ...")
        connected = Connected.True
    }

    override fun onSerialConnectError(e: Exception?) {
        Log.e("TAG", "disconnected ... Exception -> ${e?.message}")
        disconnect()
    }

    override fun onSerialRead(data: ByteArray?) {
        Log.e("TAG", "receive message -> $data")
        receive(data!!)
    }

    override fun onSerialIoError(e: Exception?) {
        Log.e("TAG", "disconnected ... Exception -> ${e?.message}")
        disconnect()
    }

    override fun onDestroy() {
        if (connected != Connected.False) disconnect()
        requireActivity().stopService(Intent(activity, SerialService::class.java))
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        if (service != null) service?.attach(this) else requireActivity().startService(
            Intent(
                activity,
                SerialService::class.java
            )
        )
    }

    override fun onStop() {
        if (service != null && !requireActivity().isChangingConfigurations) service?.detach()
        super.onStop()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        requireActivity().bindService(
            Intent(getActivity(), SerialService::class.java),
            this,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onDetach() {
        try {
            requireActivity().unbindService(this)
        } catch (ignored: java.lang.Exception) {
        }
        super.onDetach()
    }

    override fun onResume() {
        super.onResume()
        if (initialStart && service != null) {
            initialStart = false
            requireActivity().runOnUiThread { connect() }
        }
    }


    private enum class Connected {
        False, Pending, True
    }
}