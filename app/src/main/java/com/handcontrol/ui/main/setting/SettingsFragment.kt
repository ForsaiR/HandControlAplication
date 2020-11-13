package com.handcontrol.ui.main.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.handcontrol.R

class SettingsFragment : Fragment() {

    private lateinit var settingViewModel: SettingViewModel
//    private val PERMISSIONS_WRITE_EXTERNAL_STORAGE = 300

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingViewModel =
            ViewModelProvider(this).get(SettingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        val textView: TextView = root.findViewById(R.id.text_setting)
        settingViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }

    //  Вставьте фрагмент кода, когда пользователь захочет сохранить график
//    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    == PackageManager.PERMISSION_DENIED
//    ) {
//        // Запрос разрешения
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//            PERMISSIONS_WRITE_EXTERNAL_STORAGE
//        )
//    } else {
//        //исполняемый код
//    }
}