package com.handcontrol.ui.start.connection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.handcontrol.R
import kotlinx.android.synthetic.main.fragment_connection.*

class ConnectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_connection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bluetoothButton.setOnClickListener {
            activity?.finish()
            it.findNavController().navigate(R.id.action_global_navigation)
        }
        grpcButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_connectionFragment_to_loginFragment)
        )
    }
}