package com.example.ibeacon_check

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private val REQUEST_ENABLE_BT:Int = 1
    //Empty array for discovered devices
    private var devices = ArrayList<String>()
    //Create an ArrayAdapter adapter to bind an array to a ListView
    var adapter: ArrayAdapter<Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)

        //BluetoothAdapter class instance creation
        val bluetooth: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        //ListView class instance creation
        val  listView: ListView=findViewById(R.id.listView)

        devices = ArrayList()
        adapter= ArrayAdapter(this, android.R.layout.simple_list_item_1, devices as List<Any>)
        //Bind the array through the adapter to the ListView
        listView.adapter = adapter

        //Bluetooth is on or off?
        if (bluetooth.isEnabled) {
           val  mydevice_adress: String= bluetooth.address
            val  mydevice_name: String= bluetooth.name
            val status = mydevice_adress+mydevice_name
            // Bluetooth is on. Show a pop-up notification with the name and address of our device
            Toast.makeText(this,status,Toast.LENGTH_LONG).show()
        }
        else
        {
            // Bluetooth is off. Propose the user to enable it.
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        // Search for bluetooth devices
        bluetooth.startDiscovery()

    }


    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            //When a new device is found
            when(intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice object and its info from the Intent.
                    val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                    val dev_name= "$deviceName $deviceHardwareAddress"
                    //Add name and address to array adapter to show in ListView
                    devices.add(0, dev_name);
                    adapter?.notifyDataSetChanged();
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
