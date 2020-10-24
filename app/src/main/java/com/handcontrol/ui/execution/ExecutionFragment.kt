package com.handcontrol.ui.execution

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.handcontrol.R

class ExecutionFragment : Fragment() {

    private lateinit var executionViewModel: ExecutionViewModel
//    private val PERMISSIONS_RECORD_AUDIO = 200

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        executionViewModel =
            ViewModelProvider(this).get(ExecutionViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_execution, container, false)
        val textView: TextView = root.findViewById(R.id.text_execution)
        executionViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

//  Вставьте фрагмент кода, когда пользователь захочет использовать голосовые команды
//    if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
//    == PackageManager.PERMISSION_DENIED
//    ) {
//        // Запрос разрешения
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(Manifest.permission.RECORD_AUDIO),
//            PERMISSIONS_RECORD_AUDIO
//        )
//    } else {
//        //исполняемый код
//    }
}