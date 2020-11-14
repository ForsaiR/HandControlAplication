package com.handcontrol.ui.main.gestures

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.handcontrol.R
import com.handcontrol.base.BaseFragment
import com.handcontrol.base.BaseRecyclerAdapter
import com.handcontrol.databinding.FragmentGesturesBinding
import com.handcontrol.model.Gesture
import kotlinx.android.synthetic.main.fragment_gestures.*

class GesturesFragment : BaseFragment<FragmentGesturesBinding, GesturesViewModel>(
    GesturesViewModel::class.java,
    R.layout.fragment_gestures
) {

//    private val PERMISSIONS_RECORD_AUDIO = 200


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(gestureRecycler) {
            adapter = BaseRecyclerAdapter<Gesture, GestureListener>(
                R.layout.list_item_gesture,
                viewModel.listData.value!!,
                object : GestureListener {
                    override fun onClick(item: Gesture) {
                        //TODO("Not yet implemented")
                    }

                    override fun onPlay(item: Gesture) {
                        //TODO("Not yet implemented")
                    }

                }
            )

            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        }
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