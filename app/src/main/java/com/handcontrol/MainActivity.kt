package com.handcontrol

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private val PERMISSIONS_REQUEST_BLUETOOTH = 100
    private val PERMISSIONS_REQUEST_INTERNET = 101

    //обработка подключения соединения по bluetooth
    fun bluetooth_connection(view: View?){
        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.BLUETOOTH)
            == PackageManager.PERMISSION_DENIED
        ) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSIONS_REQUEST_BLUETOOTH);
        } else {
            val intent = Intent(view!!.getContext(), Navigation::class.java)
            startActivityForResult(intent, 0)
        }
    }
    //обработка подключения интернет-соединения
    fun internet_connection(view: View?) {
        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.INTERNET)
            == PackageManager.PERMISSION_DENIED
        ) {
            // Запрос разрешения
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.INTERNET),
                PERMISSIONS_REQUEST_INTERNET
            )
        } else {
            val intent = Intent(view!!.getContext(), Navigation::class.java)
            startActivityForResult(intent, 0)
        }
    }

}


