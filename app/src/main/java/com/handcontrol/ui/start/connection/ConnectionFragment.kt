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
import com.handcontrol.adapter.ConnectionItemAdapter
import com.handcontrol.bluetooth.BluetoothService
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class ConnectionFragment : Fragment() {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private lateinit var mPairedDevices: Set<BluetoothDevice>
    private var blocked = false

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

        updateDeviceList()

        refreshButton.setOnClickListener {
            it.animation = animAlpha
            updateDeviceList()
        }
    }

    /**
     * updateDeviceList - функция обновления списка Bluetooth устройст для возможного сопряжения
     */
    private fun updateDeviceList() {
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
        devList.divider = null
        devList.isClickable = !blocked
        devList.adapter = ConnectionItemAdapter(this.requireContext(),listDevicesName)
        devList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            runBlocking {
                conn(listDevices[position])
            }
        }
    }

    /**
     * conn - подключение к выбранному Bluetooth устройству
     */
    private suspend fun conn(device: BluetoothDevice) {
        blocked = true
        updateDeviceList()

        Toast.makeText(context, "Соединение", Toast.LENGTH_SHORT).show()

        var attempt = 0
        val bluetoothService = BluetoothService(device.address).apply { start() }

        while (!bluetoothService.isConnected()!!) {
            if (attempt < 50) {
                attempt += 1
                delay(100)
            } else {
                Toast.makeText(context, "Не удалось подключиться", Toast.LENGTH_LONG).show()
                bluetoothService.close()
                blocked = false
                updateDeviceList()
                return
            }
        }

        Api.setHandlingType(HandlingType.BLUETOOTH)
        Api.setBluetoothAddress(device.address)
        Api.setApiHandler(BluetoothHandler(bluetoothService))

        Toast.makeText(context, "Устройство подключено", Toast.LENGTH_LONG).show()

        transition()
    }

    /**
     * transition - функция перехода на новый экран в новигации
     */
    private fun transition() {
        activity?.finish()
        findNavController().navigate(R.id.action_global_navigation)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK)
                updateDeviceList()
        } else super.onActivityResult(requestCode, resultCode, data)
    }
}