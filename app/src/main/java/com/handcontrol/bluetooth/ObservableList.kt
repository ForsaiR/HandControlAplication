package com.handcontrol.bluetooth

import java.util.*

class ObservableList<T> : Observable() {
    val value = mutableListOf<T>()

    fun notifyChanged() {
        setChanged()
        notifyObservers(value)
    }
}