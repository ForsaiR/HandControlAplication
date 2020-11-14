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

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        with(gestureRecycler) {
//            adapter = BaseRecyclerAdapter<Gesture, ExecutableItemListener>(
//                R.layout.list_item_executable,
//                viewModel.listData.value!!,
//                object : ExecutableItemListener {
//                    override fun onClick(item: Gesture) {
//                        val navController =
//                            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
//                        navController.navigate(R.id.navigation_gesture_details, Bundle().apply {
//                            putSerializable(ARG_GESTURE_KEY, item)
//                        })
//                    }
//
//                    override fun onPlay(item: Gesture) {
//                        //TODO("Not yet implemented")
//                    }
//
//                }
//            )
//
//            layoutManager = LinearLayoutManager(context).apply {
//                orientation = LinearLayoutManager.VERTICAL
//            }
//        }
//    }


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