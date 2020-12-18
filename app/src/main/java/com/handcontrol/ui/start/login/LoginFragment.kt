package com.handcontrol.ui.start.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
        val login: EditText = view.findViewById(R.id.login) as EditText
        val password: EditText = view.findViewById(R.id.password) as EditText
        loginButton.setOnClickListener {
            if (login.text.isNotBlank() && password.text.isNotBlank()) {
                lifecycleScope.launch {
                    try {
                        Snackbar.make(it, "wait...", Snackbar.LENGTH_INDEFINITE).show()
                        Api.getGrpcHandler()
                            .authorization(login.text.toString(), password.text.toString())
                        Snackbar.make(it, "authorized", Snackbar.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_loginFragment_to_choiseFragment)
                    } catch (e: StatusRuntimeException) {
                        e.printStackTrace()
                        Snackbar.make(it, "error", Snackbar.LENGTH_SHORT).show()
                    }
                }
            } else {
                Snackbar.make(it, "Login or password is empty", Snackbar.LENGTH_SHORT).show()
            }
        }
        registrationButton.setOnClickListener (
            Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registrationFragment)
        )
    }
}