package com.handcontrol.ui.start.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.handcontrol.R
import com.handcontrol.api.Api
import io.grpc.StatusRuntimeException
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton.setOnClickListener {
            lifecycleScope.launch {
                try {
                    Snackbar.make(it, "wait...", Snackbar.LENGTH_INDEFINITE).show()
                    Api.getGrpcHandler().setTestToken()
                    Snackbar.make(it, "authorized", Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_choiseFragment)
                } catch (e: StatusRuntimeException) {
                    e.printStackTrace()
                    Snackbar.make(it, "error", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        registrationButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registrationFragment)
        )
    }
}