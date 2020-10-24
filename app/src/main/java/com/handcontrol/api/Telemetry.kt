package com.handcontrol.api

import kotlin.random.Random

class Telemetry {
    private var lastValue = MAX_VAL / 2

    fun getValue(): Int {
        val avg = MAX_VAL / 2
        val correction = (avg - lastValue) * VARIATION / avg
        lastValue += Random.nextInt(-VARIATION + correction, VARIATION + correction)
        return lastValue
    }

    companion object {
        private const val MAX_VAL = 255
        private const val VARIATION = 10
    }
}