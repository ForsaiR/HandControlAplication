package com.handcontrol.bluetooth

import java.util.*
import kotlin.math.min

class ProtocolParser {
    private val mBuffer = LinkedList<Byte>()
    private var mState = State.SFD
    private val mCrc by lazy { CRC8() }
    private var mType: Byte = 0
    private var mSize = 0
    private var mPayload = LinkedList<Byte>()

    fun append(data: ByteArray, size: Int): LinkedList<Packet> {
        repeat(size) { mBuffer.add(data[it]) }
        return parse()
    }

    private fun parse(): LinkedList<Packet> {
        val packets = LinkedList<Packet>()
        var next = true
        while (next) {
            next = false
            if (mState == State.SFD) {
                while (mBuffer.size >= 8) {
                    if (mBuffer.subList(0, 8) == SFD) {
                        mState = State.TYPE
                        repeat(8) { mBuffer.poll() }
                        mCrc.clear()
                        break
                    } else {
                        mBuffer.poll()
                    }
                }
            }
            if (mState == State.TYPE) {
                if (mBuffer.size >= 1) {
                    mState = State.SIZE
                    mType = mBuffer.poll()!!
                    mCrc.add(mType)
                }
            }
            if (mState == State.SIZE) {
                if (mBuffer.size >= 2) {
                    mState = State.PAYLOAD
                    val size0 = mBuffer.poll()!!
                    val size1 = mBuffer.poll()!!
                    mSize = size1.toUnsignedInt() shl 8
                    mSize = mSize or size0.toUnsignedInt()
                    mCrc.add(size0)
                    mCrc.add(size1)
                    mPayload = LinkedList()
                }
            }
            if (mState == State.PAYLOAD) {
                val size = min(mSize, mBuffer.size)
                repeat(size) { mPayload.add(mBuffer.poll()!!) }
                mSize -= size
                if (mSize == 0) {
                    mState = State.CRC8
                    mCrc.add(mPayload)
                }
            }
            if (mState == State.CRC8) {
                if (mBuffer.size >= 1) {
                    mState = State.SFD
                    val crc = mBuffer.poll()!!
                    if (mCrc.check(crc)) {
                        packets.add(Packet(Packet.Type.values()[mType.toUnsignedInt()], mPayload))
                    }
                    next = true
                }
            }
        }
        return packets
    }

    private enum class State {
        SFD,
        TYPE,
        SIZE,
        PAYLOAD,
        CRC8
    }

    companion object {
        private val SFD = listOf(
            0xFD.toByte(),
            0xBA.toByte(),
            0xDC.toByte(),
            0x01.toByte(),
            0x50.toByte(),
            0xB4.toByte(),
            0x11.toByte(),
            0xFF.toByte()
        )

        fun packetToRaw(packet: Packet): ByteArray {
            val result = LinkedList<Byte>()
            val size = packet.payload.size
            val crc = CRC8()
            result.addAll(SFD)
            result.add(packet.type.ordinal.toByte())
            result.add((size and 0xFF).toByte())
            result.add(((size shr 8) and 0xFF).toByte())
            result.addAll(packet.payload)
            crc.add(result)
            result.add(crc.calculate())
            return result.toByteArray()
        }
    }
}