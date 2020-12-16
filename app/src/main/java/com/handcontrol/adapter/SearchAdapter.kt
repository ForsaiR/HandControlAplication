package com.handcontrol.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.handcontrol.R
import com.handcontrol.repository.SearchQuery
import com.handcontrol.ui.start.prothesis.ChoiseFragment
import java.util.*
import kotlin.collections.ArrayList


class SearchAdapter(context: ChoiseFragment, searchQueries: MutableList<SearchQuery>?) :
        BaseAdapter() {
    var inflater: LayoutInflater
    private var searchQueries: MutableList<SearchQuery>? = null
    private val arraylist: ArrayList<SearchQuery>

    inner class ViewHolder {
        var devices: TextView? = null
    }

    override fun getCount(): Int {
        return searchQueries!!.size
    }

    override fun getItem(position: Int): SearchQuery {
        return searchQueries!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var view: View? = view
        val holder: ViewHolder
        if (view == null) {
            holder = ViewHolder()
            view = inflater.inflate(R.layout.list_item, null)
            holder.devices = view.findViewById(R.id.text1)
            view.setTag(holder)
        } else {
            holder = view.getTag() as ViewHolder
        }
        holder.devices!!.setText(searchQueries!![position].query)
        return view!!
    }

    fun filter(charText: String) {
        charText.toLowerCase(Locale.getDefault())
        searchQueries!!.clear()
        if (charText.length == 0) {
            searchQueries!!.addAll(arraylist)
        } else {
            for (wp in arraylist) {
                if (wp.query.toLowerCase(Locale.getDefault()).contains(charText)) {
                    searchQueries!!.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }

    init {
        this.searchQueries = searchQueries
        inflater = LayoutInflater.from(context.context)
        arraylist = ArrayList()
        arraylist.addAll(searchQueries!!)
    }
}