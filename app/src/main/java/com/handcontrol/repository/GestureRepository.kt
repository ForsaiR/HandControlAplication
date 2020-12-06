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

    private fun editAction(action: Action, position: Int) {
        repo!!.actions[position] = action
    }

    private fun addAction(action: Action) {
        repo!!.actions.add(action)
    }
}