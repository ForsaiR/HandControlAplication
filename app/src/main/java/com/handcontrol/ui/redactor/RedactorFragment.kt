package com.handcontrol.ui.redactor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.handcontrol.R

class RedactorFragment : Fragment() {

    private lateinit var redactorViewModel: RedactorViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        redactorViewModel =
            ViewModelProvider(this).get(RedactorViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_redactor, container, false)
        val textView: TextView = root.findViewById(R.id.text_redactor)
        redactorViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}