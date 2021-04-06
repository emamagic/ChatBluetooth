package com.emamagic.android_peermessanger.ui.availabledevices

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emamagic.android_peermessanger.base.BaseFragment
import com.emamagic.android_peermessanger.databinding.FragmentAvailableDevicesBinding
import com.emamagic.android_peermessanger.util.toasty

class AvailableDevicesFragment : BaseFragment<FragmentAvailableDevicesBinding>(),
    AvailableDevicesAdapter.Interaction {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var availableDevicesAdapter: AvailableDevicesAdapter? = null
    private var listItems: ArrayList<BluetoothDevice>? = null

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAvailableDevicesBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listItems = ArrayList()
        availableDevicesAdapter = AvailableDevicesAdapter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.recyclerItemDevices?.adapter = availableDevicesAdapter

        if (requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            bluetoothAdapter?.enable()

            scanDevices()
            val intentFilter = IntentFilter()
            intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            requireActivity().registerReceiver(bluetoothDeviceReceiver ,intentFilter)
            enableDiscoverableDevices(300)
        }


    }

    private fun enableDiscoverableDevices(second: Int) {
        if (bluetoothAdapter?.scanMode != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            val discoveryIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, second)
            startActivity(discoveryIntent)
        }
    }

    private fun scanDevices() {
        if (bluetoothAdapter?.isDiscovering!!) bluetoothAdapter?.cancelDiscovery()
        bluetoothAdapter?.startDiscovery()
    }


    private val bluetoothDeviceReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device?.bondState != BluetoothDevice.BOND_BONDED) {
                    listItems?.add(device!!)
                    availableDevicesAdapter?.submitList(listItems!!)
                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                if (listItems?.size == 0)  toasty("No new Devices found")

            } else {
                toasty("click on the device to start the chat")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().unregisterReceiver(bluetoothDeviceReceiver)
    }

    private fun pairDevice(device: BluetoothDevice) {
        try {
            val btDeviceInstance = Class.forName(BluetoothDevice::class.java.canonicalName)
            val method = btDeviceInstance.getMethod("createBond")
            method.invoke(device)
        } catch (e: Exception) {
            Log.e("TAG", "pairDevice: ${e.message} ${e.cause}")
            toasty("${e.message} ${e.cause}")
        }
    }

    override fun onItemSelected(position: Int) {
        pairDevice(listItems?.get(position)!!)
    }
}