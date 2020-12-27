package com.handcontrol.bluetooth

open class BluetoothServiceException : Exception()
class ConnectingFailedException : BluetoothServiceException()
class DisconnectedException : BluetoothServiceException()
class TimeoutException : BluetoothServiceException()
class HandlingException : BluetoothServiceException()