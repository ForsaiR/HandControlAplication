package com.handcontrol.ui.main.action

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.navigation.ui.onNavDestinationSelected
import com.handcontrol.R
import com.handcontrol.base.BaseFragment
import com.handcontrol.databinding.FragmentActionDetailsEditorBinding
import com.handcontrol.model.Action
import com.handcontrol.ui.main.action.ActionFragment.Companion.ARG_ACTION_KEY

class ActionEditorFragment : BaseFragment<FragmentActionDetailsEditorBinding, ActionViewModel>(
    ActionViewModel::class.java,
    R.layout.fragment_action_details_editor
) {

    override val viewModel: ActionViewModel by navGraphViewModels(R.id.nav_graph_action) {
        ActionViewModelFactory(
            arguments?.getSerializable(ARG_ACTION_KEY) as? Action,
            arguments?.getInt(ActionFragment.ARG_ACTION_POSITION)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.storable_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController()
        return when (item.itemId) {
            R.id.app_bar_save -> {
                viewModel.saveAction()
                navController.popBackStack()
                true
            }
            else -> item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        }
    }
}