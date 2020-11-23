package com.handcontrol.ui.main.action

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.navigation.ui.onNavDestinationSelected
import com.handcontrol.R
import com.handcontrol.base.BaseFragment
import com.handcontrol.databinding.FragmentActionDetailsBinding
import com.handcontrol.model.Action

class ActionFragment : BaseFragment<FragmentActionDetailsBinding, ActionViewModel>(
    ActionViewModel::class.java,
    R.layout.fragment_action_details
) {

    override val viewModel: ActionViewModel by navGraphViewModels(R.id.nav_graph_action) {
        ActionViewModelFactory(
            arguments?.getSerializable(ARG_ACTION_KEY) as? Action
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (arguments?.getSerializable(ARG_ACTION_KEY) as? Action).let {
            if (it == null) {
                findNavController().navigate(R.id.navigation_action_details_editor)
            }
        }
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.editable_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController()
        return when (item.itemId) {
            R.id.app_bar_edit -> {
                navController.navigate(
                    R.id.navigation_action_details_editor,
//                    Bundle().apply {
//                    putSerializable(
//                        ARG_ACTION_KEY, Action(
//                            viewModel.id,
//                            viewModel.name,
//                            false,
//                            viewModel.thumbFinger.value?.toInt() ?: 0,
//                            viewModel.pointerFinger.value?.toInt() ?: 0,
//                            viewModel.middleFinger.value?.toInt() ?: 0,
//                            viewModel.ringFinger.value?.toInt() ?: 0,
//                            viewModel.littleFinger.value?.toInt() ?: 0
//                        )
//                    )
//                }
                )
                true
            }
            else -> item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val ARG_ACTION_KEY = "action"
        const val ARG_MODE_CREATE_KEY = "create_mode"
    }

}