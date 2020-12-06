package com.handcontrol.ui.start.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.handcontrol.R
import com.handcontrol.api.Api
import com.handcontrol.api.HandlingType
import io.grpc.StatusRuntimeException
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.registrationButton
import kotlinx.android.synthetic.main.fragment_registration.*
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
        val name: EditText = view.findViewById(R.id.name) as EditText
        val login: EditText = view.findViewById(R.id.new_login) as EditText
        val password: EditText = view.findViewById(R.id.new_password) as EditText
        registrationButton.setOnClickListener {
            Api.setHandlingType(HandlingType.GRPC)
            if (!name.text.isBlank() &&!login.text.isBlank() && !password.text.isBlank()) {
                lifecycleScope.launch {
                    try {
                        Snackbar.make(it, "wait...", Snackbar.LENGTH_INDEFINITE).show()
                        Api.getGrpcHandler()
                            .registration(name.text.toString(), login.text.toString(), password.text.toString())
                        Snackbar.make(it, "Registrated", Snackbar.LENGTH_SHORT).show()
                        if (Api.isRegistrated()) {
                            findNavController().navigate(R.id.action_registrationFragment_to_choiseFragment)
                        } else {
                            Snackbar.make(it, "Not registrated", Snackbar.LENGTH_SHORT).show()
                        }
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