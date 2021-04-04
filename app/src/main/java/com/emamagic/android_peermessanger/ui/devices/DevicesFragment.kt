package com.emamagic.android_peermessanger.ui.devices

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.*
import com.emamagic.android_peermessanger.R
import com.emamagic.android_peermessanger.base.BaseFragment
import com.emamagic.android_peermessanger.databinding.FragmentDevicesBinding
import kotlin.collections.ArrayList

class DevicesFragment: BaseFragment<FragmentDevicesBinding>() ,DevicesAdapter.Interaction {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var devicesAdapter: DevicesAdapter? = null
    private var listItems: ArrayList<BluetoothDevice>? = null

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDevicesBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        listItems = ArrayList()
        devicesAdapter = DevicesAdapter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH))
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bluetooth_setting, menu)
        if (bluetoothAdapter == null)
            menu.findItem(R.id.btn_settings).isEnabled = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.btn_settings) {
            val intent = Intent()
            intent.action = Settings.ACTION_BLUETOOTH_SETTINGS
            startActivity(intent)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        if (bluetoothAdapter == null)
            binding?.txtDevicesFBluetoothStatus?.text = resources.getString(R.string.bluetooth_not_support)
        else if (!bluetoothAdapter!!.isEnabled)
            binding?.txtDevicesFBluetoothStatus?.text = resources.getString(R.string.bluetooth_disable)
        else
            binding?.txtDevicesFBluetoothStatus?.text = resources.getString(R.string.bluetooth_devices_not_found)
        setUpRecycler()

    }

    private fun setUpRecycler(){
        listItems?.clear()
        bluetoothAdapter?.let {
            for (device in it.bondedDevices){
                if (device.type != BluetoothDevice.DEVICE_TYPE_LE)
                    listItems?.add(device)
            }
            devicesAdapter?.submitList(listItems?.toList()!!)
        }
    }

    override fun onItemSelected(position: Int) {

    }

}