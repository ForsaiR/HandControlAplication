package com.handcontrol.api

import android.content.Context
import com.handcontrol.model.Action
import com.handcontrol.model.Gesture
import com.handcontrol.server.protobuf.HandleRequestGrpc
import com.handcontrol.server.protobuf.Request
import com.handcontrol.server.protobuf.Uuid
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
    private val prothesisId: String = Api.getProthesis().toString()

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
                .withInterceptors(WrongTokenInterceptor(context))
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
                val response = stub.login(loginRequest)
                Api.setToken(response.token)
            } catch (e: StatusRuntimeException) {
                if (e.status.code == Status.Code.ALREADY_EXISTS) {
                    val response = stub.login(loginRequest)
                    Api.setToken(response.token)
                } else throw e
            }
        }
    }

    override suspend fun getGestures(): MutableList<Gesture> {
        if (authorizedStub == null)
            throw IllegalStateException("Haven't been authorized")
        return withContext(Dispatchers.IO) {
            val getGestureRequest = Request.getGesturesRequest.newBuilder()
                .setId(prothesisId)
                .build()
            val response = authorizedStub.getGestures(getGestureRequest)
            response.gestures.gesturesList.map { Gesture(it) }.toMutableList()
        }
    }

    override suspend fun saveGesture(gesture: Gesture) {
        if (authorizedStub == null)
            throw IllegalStateException("Haven't been authorized")
        withContext(Dispatchers.IO) {
            val saveGestureRequest = Request.saveGestureRequest.newBuilder()
                .setId(prothesisId)
                .setGesture(gesture.getProtoModel())
                .setTimeSync(System.currentTimeMillis())
                .build()
            val response = authorizedStub.saveGesture(saveGestureRequest)
        }
    }

    override suspend fun performGesture(gesture: Gesture) {
        if (authorizedStub == null)
            throw IllegalStateException("Haven't been authorized")
        withContext(Dispatchers.IO) {
            val response = if (gesture.id == null) {
                val performGesture = Request.performGestureRawRequest.newBuilder()
                    .setId(prothesisId)
                    .setGesture(gesture.getProtoModel())
                    .build()
                authorizedStub.performGestureRaw(performGesture)
            } else {
                val performGesture = Request.performGestureIdRequest.newBuilder()
                    .setId(prothesisId)
                    .setGestureId(Uuid.UUID.newBuilder().setValue(gesture.id.toString()))
                    .build()
                authorizedStub.performGestureId(performGesture)
            }
        }
    }


    override suspend fun deleteGesture(gestureId: String) {
        if (authorizedStub == null)
            throw IllegalStateException("Haven't been authorized")
        withContext(Dispatchers.IO) {
            val deleteGestureRequest = Request.deleteGestureRequest.newBuilder()
                .setId(prothesisId)
                .setGestureId(Uuid.UUID.newBuilder().setValue(gestureId))
                .setTimeSync(System.currentTimeMillis())
                .build()
            val response = authorizedStub.deleteGesture(deleteGestureRequest)
        }
    }

    override suspend fun setPositions(action: Action) {
        if (authorizedStub == null)
            throw IllegalStateException("Haven't been authorized")
        withContext(Dispatchers.IO) {
            val setPositionsRequest = Request.setPositionsRequest.newBuilder()
                .setId(prothesisId)
                .setLittleFingerPosition(action.littleFinger)
                .setMiddleFingerPosition(action.middleFinger)
                .setPointerFingerPosition(action.pointerFinger)
                .setRingFingerPosition(action.ringFinger)
                .setThumbFingerPosition(action.thumbFinger)
                .build()
            val response = authorizedStub.setPositions(setPositionsRequest)
        }
    }

    suspend fun authorization(login: String, password: String) {
        withContext(Dispatchers.IO) {
            val loginRequest = Request.LoginRequest.newBuilder()
                .setLogin(login)
                .setPassword(password)
                .build()
            try {
                val response = stub.login(loginRequest)
                Api.setToken(response.token)
            } catch (e: StatusRuntimeException) {
                if (e.status.code == Status.Code.ALREADY_EXISTS) {
                    val response = stub.login(loginRequest)
                    Api.setToken(response.token)
                } else throw e
            }
        }
    }

    suspend fun registration(login: String, password: String) {
        withContext(Dispatchers.IO) {
            val registrationRequest = Request.LoginRequest.newBuilder()
                .setLogin(login)
                .setPassword(password)
                .build()
            try {
                val response = stub.registry(registrationRequest)
                Api.setToken(response.token)
            } catch (e: StatusRuntimeException) {
                if (e.status.code == Status.Code.ALREADY_EXISTS) {
                    val response = stub.registry(registrationRequest)
                    Api.setToken(response.token)
                } else throw e
            }
        }
    }

    suspend fun getProto(): String{
        if (authorizedStub == null)
            throw IllegalStateException("Haven't been authorized")
        return withContext(Dispatchers.IO) {
            val getOnlineRequest = Request.getOnlineRequest.newBuilder()
                .build()
            val response = authorizedStub.getOnline(getOnlineRequest)
            response.list
        }
    }

    companion object {
        private const val GRPC_ADDRESS = "paulrozhkin.ru"
        private const val GRPC_PORT = 6565
    }
}