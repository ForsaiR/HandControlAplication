package com.handcontrol.ui.start.prothesis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.Chart.LOG_TAG
import com.handcontrol.R
import com.handcontrol.ui.main.Navigation
import kotlinx.android.synthetic.main.fragment_choise.*


class ChoiseFragment : Fragment() {
    val devices = arrayOf(
        "Proto_1", "Proto_2", "Proto_3",
    )
    private var mAdapter: ArrayAdapter<String>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_choise, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        mAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1, devices
        )
        val listView: ListView = rootView.findViewById(R.id.list) as ListView
        listView.setAdapter(mAdapter)


        return rootView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            val intent = Intent(context, Navigation::class.java)
            startActivity(intent)
        })


    }
}
