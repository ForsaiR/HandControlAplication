package com.handcontrol.api

import android.content.Context
import com.handcontrol.server.protobuf.Gestures
import com.handcontrol.server.protobuf.HandleRequestGrpc
import com.handcontrol.server.protobuf.Request
import io.grpc.Metadata
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.android.AndroidChannelBuilder
import io.grpc.stub.MetadataUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GrpcHandler(
    context: Context?,
    token: String?
) : IApiHandler {
    private val stub: HandleRequestGrpc.HandleRequestBlockingStub
    private val authorizedStub: HandleRequestGrpc.HandleRequestBlockingStub?

    init {
        val channel = AndroidChannelBuilder.forAddress(GRPC_ADDRESS, GRPC_PORT)
            .usePlaintext()
            .context(context)
            .build()
        stub = HandleRequestGrpc.newBlockingStub(channel)
        authorizedStub = token?.let {
            val header = Metadata()
            val key = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER)
            header.put(key, it)
            MetadataUtils.attachHeaders(stub, header)
        }
    }

    suspend fun setTestToken() {
        withContext(Dispatchers.IO) {
            val testLogin = "testLogin"
            val testPassword = "testPassword"
            val loginRequest = Request.LoginRequest.newBuilder()
                .setLogin(testLogin)
                .setPassword(testPassword)
                .build()
            try {
                val response = stub.registry(loginRequest)
                Api.setToken(response.token)
            } catch (e: StatusRuntimeException) {
                if (e.status.code == Status.Code.ALREADY_EXISTS) {
                    val response = stub.login(loginRequest)
                    Api.setToken(response.token)
                } else throw e
            }
        }
    }

    override suspend fun getGestures(): MutableList<Gestures.Gesture> {
        if (authorizedStub == null)
            throw IllegalStateException("Haven't been authorized")
        return withContext(Dispatchers.IO) {
            val getGestureRequest = Request.getGesturesRequest.newBuilder()
                .setId("1")
                .build()
            val response = authorizedStub.getGestures(getGestureRequest)
            response.gestures.gesturesList
        }
    }

    companion object {
        private const val GRPC_ADDRESS = "paulrozhkin.ru"
        private const val GRPC_PORT = 6565
    }
}