package com.handcontrol.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.handcontrol.R

class ConnectionItemAdapter(context: Context, devicesName: MutableList<String>) :
    BaseAdapter() {
    var inflater: LayoutInflater
    private var devicesName: MutableList<String>? = null

    init {
        this.devicesName = devicesName
        inflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return devicesName!!.size
    }

    override fun getItem(position: Int): String {
        return devicesName!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val listItem: View = inflater.inflate(R.layout.list_item_connection, null)
        val text: TextView = listItem.findViewById(R.id.device_name)
        text.text = devicesName?.get(position)
        return listItem
    }
}