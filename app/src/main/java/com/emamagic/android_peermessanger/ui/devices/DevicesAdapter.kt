package com.emamagic.android_peermessanger.ui.devices

import android.bluetooth.BluetoothDevice
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.emamagic.android_peermessanger.R
import com.emamagic.android_peermessanger.databinding.ItemDevicesListBinding

class DevicesAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BluetoothDevice>() {

        override fun areItemsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice) = oldItem.address == newItem.address

        override fun areContentsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice) = oldItem == newItem

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemDevicesListBinding.inflate(LayoutInflater.from(parent.context) ,parent ,false)
        return DevicesViewHolder(binding ,interaction)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DevicesViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<BluetoothDevice>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    class DevicesViewHolder
    constructor(
        private val binding: ItemDevicesListBinding,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION)
                    interaction?.onItemSelected(position)
            }
        }

        fun bind(item: BluetoothDevice){
            binding.apply {
                txtDevicesFName.text = item.name
                txtDevicesFId.text = item.address
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int)
    }
}
