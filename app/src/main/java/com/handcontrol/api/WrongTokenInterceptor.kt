package com.handcontrol.api

import android.content.Context
import android.content.Intent
import com.handcontrol.ui.start.MainActivity
import io.grpc.*

class WrongTokenInterceptor(
    private val context: Context?
) : ClientInterceptor {
    override fun <ReqT : Any?, RespT : Any?> interceptCall(
        method: MethodDescriptor<ReqT, RespT>?,
        callOptions: CallOptions?,
        next: Channel?
    ): ClientCall<ReqT, RespT> {
        val call = next!!.newCall(method, callOptions)
        return object : ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(call) {
            override fun start(responseListener: Listener<RespT>?, headers: Metadata?) {
                delegate().start(
                    object : ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                        override fun onClose(status: Status?, trailers: Metadata?) {
                            super.onClose(status, trailers)
                            if (status?.code == Status.Code.UNAUTHENTICATED) {
                                Api.clearToken()
                                context?.run {
                                    startActivity(
                                        Intent(this, MainActivity::class.java)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    )
                                }
                            }
                        }
                    },
                    headers
                )
            }
        }
    }
}