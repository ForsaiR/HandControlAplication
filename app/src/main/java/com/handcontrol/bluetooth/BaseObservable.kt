package com.handcontrol.bluetooth

import java.util.*

class BaseObservable<T>(value: T) : Observable() {
    private var mValue = value
    var value: T
        get() = mValue
    set(value) {
        mValue = value
        setChanged()
        notifyObservers(mValue)
    }
}