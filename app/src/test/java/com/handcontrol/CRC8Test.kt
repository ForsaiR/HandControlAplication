package com.handcontrol

import com.handcontrol.bluetooth.CRC8
import org.junit.Assert.assertEquals
import org.junit.Test

class CRC8Test {
    @Test
    fun test() {
        val crc = CRC8()
        val data = listOf(
            0x04.toByte(),
            0x03.toByte(),
            0xFB.toByte(),
            0xAD.toByte(),
            0xFF.toByte(),
            0xBD.toByte()
        )
        val expectedCrc = 0xBF.toByte()
        crc.add(data)
        assertEquals(expectedCrc, crc.calculate())
    }
}