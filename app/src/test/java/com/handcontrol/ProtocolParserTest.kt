package com.handcontrol

import com.handcontrol.bluetooth.Packet
import com.handcontrol.bluetooth.ProtocolParser
import org.junit.Assert.assertEquals
import org.junit.Test

class ProtocolParserTest {
    @Test
    fun allData() {
        val expectedValue = listOf(
            Packet(Packet.Type.TELEMETRY, listOf(0x34.toByte(), 0xDF.toByte())),
            Packet(Packet.Type.ERR, emptyList())
        )
        assertEquals(expectedValue, ProtocolParser().append(data, data.size))
    }

    @Test
    fun perByte() {
        val result = mutableListOf<Packet>()
        val parser = ProtocolParser()
        data.forEach { result.addAll(parser.append(byteArrayOf(it), 1)) }
        assertEquals(expectedValue, result)
    }

    @Test
    fun randomCount() {
        val result = mutableListOf<Packet>()
        val parser = ProtocolParser()
        val dataList = data.toList()
        var size: Int
        result.addAll(parser.append(dataList.subList(0, 4).apply { size = this.size }.toByteArray(), size))
        result.addAll(parser.append(dataList.subList(4, 12).apply { size = this.size }.toByteArray(), size))
        result.addAll(parser.append(dataList.subList(12, 26).apply { size = this.size }.toByteArray(), size))
        result.addAll(parser.append(dataList.subList(26, 37).apply { size = this.size }.toByteArray(), size))
        result.addAll(parser.append(dataList.subList(37, 43).apply { size = this.size }.toByteArray(), size))
        result.addAll(parser.append(dataList.subList(43, 45).apply { size = this.size }.toByteArray(), size))
        result.addAll(parser.append(dataList.subList(45, 52).apply { size = this.size }.toByteArray(), size))
        assertEquals(expectedValue, result)
    }

    companion object {
        private val data = byteArrayOf(  // size = 52 bytes
            0x34.toByte(),
            0x53.toByte(),
            0x76.toByte(),
            0x76.toByte(),
            0x76.toByte(),
            0x76.toByte(),
            0x76.toByte(),
            0x76.toByte(),
            0x76.toByte(),
            0xFF.toByte(),
            0xFD.toByte(),  //SFD start
            0xBA.toByte(),
            0xDC.toByte(),
            0x01.toByte(),
            0x50.toByte(),
            0xB4.toByte(),
            0x11.toByte(),
            0xFF.toByte(),  //SFD end
            0x03.toByte(),  //type
            0x02.toByte(),  //size
            0x00.toByte(),
            0x34.toByte(),  //payload
            0xdf.toByte(),
            0x34.toByte(),  //incorrect crc
            0xFF.toByte(),  //trash
            0xFD.toByte(),  //SFD start
            0xBA.toByte(),
            0xDC.toByte(),
            0x01.toByte(),
            0x50.toByte(),
            0xB4.toByte(),
            0x11.toByte(),
            0xFF.toByte(),  //SFD end
            0x03.toByte(),  //type
            0x02.toByte(),  //size
            0x00.toByte(),
            0x34.toByte(),  //payload
            0xdf.toByte(),
            0x70.toByte(),  //correct crc
            0xFD.toByte(),  //SFD start
            0xBA.toByte(),
            0xDC.toByte(),
            0x01.toByte(),
            0x50.toByte(),
            0xB4.toByte(),
            0x11.toByte(),
            0xFF.toByte(),  //SFD end
            0x02.toByte(),  //type
            0x00.toByte(),  //size
            0x00.toByte(),
            0xEF.toByte(),  //correct crc
            0xCA.toByte()  //trash
        )

        private val expectedValue = listOf(
            Packet(Packet.Type.TELEMETRY, listOf(0x34.toByte(), 0xDF.toByte())),
            Packet(Packet.Type.ERR, emptyList())
        )
    }
}