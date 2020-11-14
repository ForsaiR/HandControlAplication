package com.handcontrol.ui.main.gestures.gesturedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.handcontrol.R
import com.handcontrol.base.BaseFragment
import com.handcontrol.databinding.FragmentGestureDetailsBinding
import com.handcontrol.model.Gesture

class GestureDetailsFragment : BaseFragment<FragmentGestureDetailsBinding, GestureDetailsViewModel>(
    GestureDetailsViewModel::class.java,
    R.layout.fragment_gesture_details
) {

//    private val PERMISSIONS_RECORD_AUDIO = 200

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelFactory = GestureDetailsViewModelFactory(
            arguments?.getSerializable(ARG_GESTURE_KEY) as Gesture
        )
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        val ARG_GESTURE_KEY = "gesture"
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