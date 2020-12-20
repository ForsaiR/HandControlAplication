package com.handcontrol.ui.start.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.handcontrol.R
import com.handcontrol.api.Api
import io.grpc.StatusRuntimeException
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.launch

class RegistrationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val login: EditText = view.findViewById(R.id.new_login) as EditText
        val password: EditText = view.findViewById(R.id.new_password) as EditText
        registrationButton.setOnClickListener {
            Api.setHandlingType(HandlingType.GRPC)
            registrationButton.isEnabled = false;
            if (!login.text.isBlank() && !password.text.isBlank()) {
                lifecycleScope.launch {
                    try {
                        Snackbar.make(it, "wait...", Snackbar.LENGTH_INDEFINITE).show()
                        Api.getGrpcHandler()
                            .registration(login.text.toString(), password.text.toString())
                        Snackbar.make(it, "Registrated", Snackbar.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_registrationFragment_to_choiseFragment)
                    } catch (e: StatusRuntimeException) {
                        e.printStackTrace()
                        Snackbar.make(it, "error", Snackbar.LENGTH_SHORT).show()
                    }
                }
            } else {
                Snackbar.make(it, "Name, login or password is empty", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}