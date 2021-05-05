package com.handcontrol.ui.start.connection

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.handcontrol.R
import com.handcontrol.api.Api
import com.handcontrol.api.BluetoothHandler
import com.handcontrol.api.HandlingType
import android.widget.Button

import android.view.animation.Animation
import android.view.animation.AnimationUtils


class ConnectionFragment : Fragment() {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private lateinit var mPairedDevices: Set<BluetoothDevice>
    private var pairedDevice: BluetoothDevice? = null

    companion object {
        private const val REQUEST_ENABLE_BLUETOOTH = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_connection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        val refreshButton: Button = view.findViewById(R.id.refreshButton) as Button
        val animAlpha: Animation = AnimationUtils.loadAnimation(this.requireContext(), R.anim.alpha)

        if(bluetoothAdapter == null) {
            refreshButton.visibility = View.INVISIBLE
            Toast.makeText(context, "Устройство не поддерживает Bluetooth", Toast.LENGTH_LONG).show()
            return
        }

        if(!bluetoothAdapter.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }

        pairedDeviceList()

        refreshButton.setOnClickListener {
            it.animation = animAlpha
            pairedDeviceList()
        }
    }

    private fun pairedDeviceList() {
        mPairedDevices = bluetoothAdapter!!.bondedDevices
        val listDevices : ArrayList<BluetoothDevice> = ArrayList()
        val listDevicesName : ArrayList<String> = ArrayList()

        if (mPairedDevices.isNotEmpty()) {
            for (device: BluetoothDevice in mPairedDevices) {
                listDevices.add(device)
                listDevicesName.add(device.name)
                Log.i("device", ""+device)
            }
        } else {
            Toast.makeText(context, "Bluetooth устройства не найдены", Toast.LENGTH_LONG).show()
        }

        val devList: ListView = view?.findViewById(R.id.device_list) as ListView

        val adapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1, listDevicesName)

        devList.adapter = adapter
        devList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            choice(listDevices[position])
        }
    }

    private fun choice(device: BluetoothDevice) {
        val bluetoothHandler = BluetoothHandler(device.address)

        while (!bluetoothHandler.isConnected()) {
            print("Connecting")
        }

        Api.setHandlingType(HandlingType.BLUETOOTH)
        Api.setBluetoothAddress(device.address)
        Api.setApiHandler(bluetoothHandler)

        transfer()
    }

    private fun transfer() {
        activity?.finish()
        findNavController().navigate(R.id.action_global_navigation)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK)
                pairedDeviceList()
        } else super.onActivityResult(requestCode, resultCode, data)
    }
}