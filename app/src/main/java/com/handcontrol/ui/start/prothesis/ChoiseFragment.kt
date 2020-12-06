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
import com.google.android.material.snackbar.Snackbar
import com.handcontrol.R
import com.handcontrol.adapter.AdapterClass
import com.handcontrol.api.Api
import com.handcontrol.repository.SearchQuery
import com.handcontrol.ui.main.Navigation
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.launch
import java.util.logging.Logger


class ChoiseFragment : Fragment() {
    lateinit var list: ListView
    var adapter: AdapterClass? = null
    var editsearch: SearchView? = null
    var searchQueries: Array<String> = arrayOf(
           "Proto_1", "Proto_2", "Proto_3",
    )

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
            var proto = Api.getGrpcHandler().getProto()
            proto = proto.replace("[", "")
            proto = proto.replace("]", "")
            val parts = proto.split(", ")
                // Logger.getLogger(ChoiseFragment::class.java.name).warning(proto)
            for (searchQuery in parts) {
                val searchQuery1 = SearchQuery(searchQuery)
                arraylist.add(searchQuery1)
            }
        }catch (e: StatusRuntimeException){
            e.printStackTrace()
        }
        }

        list = rootView.findViewById(R.id.list) as ListView
        adapter = AdapterClass(this, arraylist)
        list.adapter = adapter
        editsearch = rootView.findViewById(R.id.search) as SearchView
        editsearch!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter!!.filter(newText)
                return false
            }
        })

        return rootView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.setOnItemClickListener(OnItemClickListener { _, _, _, id ->
            val intent = Intent(context, Navigation::class.java)
            Api.saveProthesis(id.toString())
            startActivity(intent)
        })
    }
}
