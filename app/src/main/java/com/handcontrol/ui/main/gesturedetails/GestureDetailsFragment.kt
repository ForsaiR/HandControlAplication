package com.handcontrol.ui.main.gesturedetails

import android.os.Bundle
import android.view.*
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.LinearLayoutManager
import com.handcontrol.R
import com.handcontrol.api.SetPositions
import com.handcontrol.base.BaseFragment
import com.handcontrol.base.BaseRecyclerAdapter
import com.handcontrol.databinding.FragmentGestureDetailsBinding
import com.handcontrol.model.Action
import com.handcontrol.model.ExecutableItem
import com.handcontrol.model.Gesture
import com.handcontrol.ui.main.action.ActionFragment
import com.handcontrol.ui.main.action.ActionFragment.Companion.ARG_ACTION_POSITION
import com.handcontrol.ui.main.gestures.ExecutableItemListener
import kotlinx.android.synthetic.main.fragment_gesture_details.*

class GestureDetailsFragment : BaseFragment<FragmentGestureDetailsBinding, GestureDetailsViewModel>(
    GestureDetailsViewModel::class.java,
    R.layout.fragment_gesture_details
) {

//    private val PERMISSIONS_RECORD_AUDIO = 200

    override val viewModel: GestureDetailsViewModel by navGraphViewModels(R.id.nav_graph_gesture) {
        GestureDetailsViewModelFactory(
            arguments?.getSerializable(ARG_GESTURE_KEY) as? Gesture
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        //todo
        activity?.actionBar?.title = viewModel.name.value ?: NEW_GESTURE_NAME
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(actionsRecycler) {
            adapter = BaseRecyclerAdapter<Action, ExecutableItemListener>(
                R.layout.list_item_executable,
                viewModel.actions.value!!,
                object : ExecutableItemListener {
                    override fun onClick(item: ExecutableItem, position: Int) {
                        val navController =
                            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        navController.navigate(R.id.nav_graph_action, Bundle().apply {
                            putSerializable(ActionFragment.ARG_ACTION_KEY, item)
                            putSerializable(ARG_ACTION_POSITION, position)
                        })
                    }

                    override fun onPlay(item: ExecutableItem, position: Int) {
                        if (viewModel.playedPosition != null) {
                            (adapter as BaseRecyclerAdapter<Action, ExecutableItemListener>).getItem(
                                viewModel.playedPosition!!
                            ).isExecuted = false
                            adapter?.notifyItemChanged(viewModel.playedPosition!!)

                        }

                        SetPositions().invoke(item as Action)
                        item.isExecuted = !item.isExecuted
                        viewModel.playedPosition = position
                        adapter?.notifyItemChanged(position)
                    }

                }
            )

            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.editable_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController()
        return when (item.itemId) {
            R.id.app_bar_edit -> {
                navController.navigate(R.id.navigation_gesture_details_editor)
                true
            }
            else -> item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val ARG_GESTURE_KEY = "gesture"
        const val NEW_GESTURE_NAME = "Новый жест"
        const val ARG_MODE_CREATE_KEY = "create_mode"
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