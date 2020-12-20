package com.handcontrol.ui.start.prothesis

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.handcontrol.R
import com.handcontrol.adapter.SearchAdapter
import com.handcontrol.api.Api
import com.handcontrol.repository.SearchQuery
import com.handcontrol.ui.main.Navigation
import io.grpc.StatusRuntimeException
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.coroutines.launch


class ChoiseFragment : Fragment() {
    lateinit var list: ListView
    var adapter: SearchAdapter? = null
    var editsearch: SearchView? = null
    var proto:String = ""
    var parts:Array<String> = emptyArray()
    var arraylist = ArrayList<SearchQuery>()
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?

    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_choise, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        Api.clearProthesis()

        lifecycleScope.launch {

            try{
                proto = Api.getGrpcHandler().getProto()
           //     proto = Api.getProtos().toString()
                var device = proto
                device = device.replace("[", "")
                device = device.replace("]", "")
                parts = device.split(", ").toTypedArray()
                for (searchQuery in parts) {
                    val searchQuery1 = SearchQuery(searchQuery)
                    arraylist.add(searchQuery1)
                }
            } catch (e: StatusRuntimeException){
                e.printStackTrace()
            }
        }

        list = rootView.findViewById(R.id.list) as ListView
        adapter = SearchAdapter(this, arraylist)
        list.adapter = adapter
        editsearch = rootView.findViewById(R.id.search) as SearchView

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.setOnItemClickListener(OnItemClickListener { adapter, view, item, id ->
            val intent = Intent(context, Navigation::class.java)
            val device = view.text1.text
            //println("proto" + device)
            Api.saveProthesis(device as String)
            startActivity(intent)
        })
        editsearch!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter!!.filter(newText)
                return false
            }
        })
    }

}
