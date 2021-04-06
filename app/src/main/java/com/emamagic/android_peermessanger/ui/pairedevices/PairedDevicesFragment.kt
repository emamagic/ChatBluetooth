package com.emamagic.android_peermessanger.ui.pairedevices

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.emamagic.android_peermessanger.R
import com.emamagic.android_peermessanger.base.BaseFragment
import com.emamagic.android_peermessanger.databinding.FragmentPairedDevicesBinding
import kotlin.collections.ArrayList

class PairedDevicesFragment: BaseFragment<FragmentPairedDevicesBinding>() ,PairedDevicesAdapter.Interaction {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var devicesAdapter: PairedDevicesAdapter? = null
    private var listItems: ArrayList<BluetoothDevice>? = null
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPairedDevicesBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        listItems = ArrayList()
        devicesAdapter = PairedDevicesAdapter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.recyclerItemDevices?.adapter = devicesAdapter
        if (requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)){
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            bluetoothAdapter?.enable()
        }



    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bluetooth_setting, menu)
        if (bluetoothAdapter == null)
            menu.findItem(R.id.btn_settings).isEnabled = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return when (id) {
            R.id.btn_settings -> {
                val intent = Intent()
                intent.action = Settings.ACTION_BLUETOOTH_SETTINGS
                startActivity(intent)
                true
            }
            R.id.btn_available_devices -> {
                if (hasPermissions(requireContext() ,*permissions)) {
                    val navigation = PairedDevicesFragmentDirections.actionPairedDevicesFragmentToAvailableDevicesFragment()
                    findNavController().navigate(navigation)
                }else { getPermission() }

                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setUpRecycler()
        if (bluetoothAdapter == null)
            binding?.txtDevicesFBluetoothStatus?.text = resources.getString(R.string.bluetooth_not_support)
        else if (!bluetoothAdapter!!.isEnabled)
            binding?.txtDevicesFBluetoothStatus?.text = resources.getString(R.string.bluetooth_disable)
        else if (listItems?.size == 0)
            binding?.txtDevicesFBluetoothStatus?.text = resources.getString(R.string.bluetooth_devices_not_found)
    }

    private fun setUpRecycler(){
        listItems?.clear()
        bluetoothAdapter?.let {
            for (device in it.bondedDevices)
                if (device.type != BluetoothDevice.DEVICE_TYPE_LE)
                    listItems?.add(device)

            devicesAdapter?.submitList(listItems!!)
        }
    }

    override fun onItemSelected(position: Int) {
        val navigateToChatFragment = PairedDevicesFragmentDirections.actionPairedDevicesFragmentToChatFragment(listItems?.get(position)?.address!!)
        findNavController().navigate(navigateToChatFragment)
    }

    private fun getPermission() {
        val allPermissions = 1
        if (!hasPermissions(requireContext(), *permissions)) {
            ActivityCompat.requestPermissions(requireActivity(), permissions, allPermissions)
        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

}