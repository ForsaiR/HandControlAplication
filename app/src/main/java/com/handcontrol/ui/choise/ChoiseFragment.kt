package com.handcontrol.ui.choise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.handcontrol.R

class ChoiseFragment : Fragment() {

    private lateinit var choiseViewModel: ChoiseViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        choiseViewModel =
            ViewModelProvider(this).get(ChoiseViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_choise, container, false)
        val textView: TextView = root.findViewById(R.id.text_choise)
        choiseViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}