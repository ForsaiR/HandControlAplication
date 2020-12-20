package com.handcontrol.ui.start.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.handcontrol.R
import com.handcontrol.api.Api
import com.handcontrol.api.HandlingType
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
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        val login: EditText = view.findViewById(R.id.login) as EditText
        val password: EditText = view.findViewById(R.id.password) as EditText
        loginButton.setOnClickListener {
            loginButton.isEnabled = false;
            Api.setHandlingType(HandlingType.GRPC)
            if (!login.text.isBlank() && !password.text.isBlank()) {
                lifecycleScope.launch {
                    try {
                        Snackbar.make(it, "wait...", Snackbar.LENGTH_INDEFINITE).show()
                        Api.getGrpcHandler()
                            .authorization(login.text.toString(), password.text.toString())
                        Snackbar.make(it, "authorized", Snackbar.LENGTH_SHORT).show()
                        if (Api.isAuthorized()) {
                           val proto = Api.getGrpcHandler().getProto()
                            Api.saveProtos(proto)
                            findNavController().navigate(R.id.action_loginFragment_to_choiseFragment)
                        } else {
                            Snackbar.make(it, "Not authorized", Snackbar.LENGTH_SHORT).show()
                        }
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