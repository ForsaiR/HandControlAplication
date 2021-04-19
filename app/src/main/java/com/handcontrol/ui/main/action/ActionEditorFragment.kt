package com.handcontrol.ui.main.main.action

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.navigation.ui.onNavDestinationSelected
import com.handcontrol.R
import com.handcontrol.base.BaseFragment
import com.handcontrol.databinding.FragmentActionDetailsEditorBinding
import com.handcontrol.model.Action
import com.handcontrol.ui.main.main.action.ActionFragment.Companion.ARG_ACTION_KEY
import kotlinx.android.synthetic.main.fragment_action_details_editor.*

class ActionEditorFragment : BaseFragment<FragmentActionDetailsEditorBinding, ActionViewModel>(
    ActionViewModel::class.java,
    R.layout.fragment_action_details_editor
) {

    override val viewModel: ActionViewModel by navGraphViewModels(R.id.nav_graph_action) {
        ActionViewModelFactory(
            arguments?.getSerializable(ARG_ACTION_KEY) as? Action,
            arguments?.getSerializable(ActionFragment.ARG_ACTION_POSITION) as? Int?
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
                if (validate()) {
                    viewModel.saveAction()
                    navController.popBackStack()
                    return true
                }
                Toast.makeText(requireContext(), R.string.finger_error_toast, Toast.LENGTH_SHORT)
                    .show()
                false
            }
            else -> item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        }
    }

    private fun validate(): Boolean {
        if (editFinger1.error.isNullOrEmpty() &&
            editFinger2.error.isNullOrEmpty() &&
            editFinger3.error.isNullOrEmpty() &&
            editFinger4.error.isNullOrEmpty() &&
            editFinger5.error.isNullOrEmpty()
        ) return true

        return false
    }
}