package com.emamagic.android_peermessanger.ui.availabledevices

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emamagic.android_peermessanger.base.BaseFragment
import com.emamagic.android_peermessanger.databinding.FragmentAvailableDevicesBinding

class AvailableDevicesFragment: BaseFragment<FragmentAvailableDevicesBinding>() {

    private var bluetoothAdapter: BluetoothAdapter? = null

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAvailableDevicesBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)){
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            bluetoothAdapter?.enable()
        }

        enableDiscoverableDevices(300)
        scanDevices()

    }

    private fun enableDiscoverableDevices(second: Int){
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


    private val bluetoothDeviceReceiver: BroadcastReceiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)


            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().unregisterReceiver(bluetoothDeviceReceiver)
    }
}