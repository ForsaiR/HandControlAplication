package com.handcontrol.ui.main.gestures

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handcontrol.R
import com.handcontrol.base.BaseFragment
import com.handcontrol.base.BaseRecyclerAdapter
import com.handcontrol.base.BaseViewHolder
import com.handcontrol.databinding.FragmentGesturesBinding
import com.handcontrol.databinding.ListItemExecutableBinding
import com.handcontrol.model.ExecutableItem
import com.handcontrol.model.Gesture
import com.handcontrol.ui.main.gesturedetails.GestureDetailsFragment
import com.handcontrol.ui.main.gesturedetails.GestureDetailsFragment.Companion.ARG_NEW_GESTURE_NAME
import kotlinx.android.synthetic.main.fragment_gestures.*

class GesturesFragment : BaseFragment<FragmentGesturesBinding, GesturesViewModel>(
    GesturesViewModel::class.java,
    R.layout.fragment_gestures
) {

    override val viewModel: GesturesViewModel by lazy { ViewModelProvider(this).get(viewModelClass) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback {
            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
            alertDialogBuilder
                .setMessage("Выйти из приложения?")
                .setPositiveButton("Да") { _, _ ->
                    activity?.finish()
                }
                .setNegativeButton("Нет") { dialog, _ -> dialog.cancel() }
            val alertDialog: AlertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateGestures()
    //    gestureRecycler.adapter?.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        floatingActionButton.setOnClickListener {

            val navController = findNavController()
            navController.navigate(R.id.nav_graph_gesture,
                Bundle().apply {
                    putString("title", ARG_NEW_GESTURE_NAME)
                })
        }

        with(gestureRecycler) {
            adapter = BaseRecyclerAdapter<Gesture, ExecutableItemListener>(
                R.layout.list_item_executable,
                viewModel.listData.value ?: mutableListOf(),
                object : ExecutableItemListener {
                    override fun onClick(item: ExecutableItem, position: Int) {
                        val navController =
                            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        navController.navigate(R.id.nav_graph_gesture, Bundle().apply {
                            putSerializable(GestureDetailsFragment.ARG_GESTURE_KEY, item)
                            putString("title", item.name)
                        })
                    }

                    override fun onPlay(item: ExecutableItem, position: Int) {
                        //todo get gesture state from api
                        item.isExecuted = !item.isExecuted
                        viewModel.performGesture(item as Gesture)
                        adapter?.notifyItemChanged(position)
                    }

                }

            )

            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }

            viewModel.listData.observe(viewLifecycleOwner) {
                (adapter as BaseRecyclerAdapter<Gesture, ExecutableItemListener>).dataSet = (it)
            }
        }

        ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    viewModel.deleteGesture(((viewHolder as BaseViewHolder).binding as ListItemExecutableBinding).item?.id!!)
                }
            }
        ).attachToRecyclerView(gestureRecycler)

        viewModel.errorConnection.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
            }
        }
    }

}