package com.handcontrol.repository

import com.handcontrol.model.Action
import com.handcontrol.model.Gesture

object GestureRepository {
    private var repo: Gesture? = null

    fun initGesture(gesture: Gesture?) {
        repo = gesture ?: Gesture(null, "", false, true, null, mutableListOf())
    }

    fun saveAction(action: Action, position: Int? = null) {
        if (position == null) {
            addAction(action)
        } else {
            editAction(action, position)
        }
    }

    fun deleteAction(action: Action) {
        repo!!.actions.remove(action)
    }

    private fun editAction(action: Action, position: Int) {
        repo!!.actions[position] = action
    }

    private fun addAction(action: Action) {
        with(repo!!.actions) {
            add(action.apply {
                name = "action" + (last().name?.get(6)?.toString()?.toInt()?.plus(1))
            })
        }
    }
}