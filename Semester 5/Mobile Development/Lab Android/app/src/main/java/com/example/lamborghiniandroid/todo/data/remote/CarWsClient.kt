package com.example.lamborghiniandroid.todo.data.remote

import android.util.Log
import com.example.lamborghiniandroid.core.TAG
import com.example.lamborghiniandroid.core.data.remote.Api
import com.example.lamborghiniandroid.utils.LocalDateJsonAdapter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class CarWsClient(
    private val okHttpClient: OkHttpClient
) {

    @Volatile
    private var token: String? = null

    private var webSocket: WebSocket? = null

    fun authorize(token: String) {
        Log.d(TAG, "CarWsClient.authorize()")
        this.token = token
    }

    suspend fun openSocket(
        onEvent: (CarEvent?) -> Unit,
        onClosed: () -> Unit,
        onFailure: () -> Unit
    ) {
        withContext(Dispatchers.IO) {
            Log.d(TAG, "CarWsClient.openSocket, url=${Api.wsUrl}")
            val request = Request.Builder()
                .url(Api.wsUrl)
                .build()

            webSocket = okHttpClient.newWebSocket(
                request,
                CarWebSocketListener(
                    onEvent = onEvent,
                    onClosed = onClosed,
                    onFailure = onFailure
                )
            )
        }
    }

    fun closeSocket() {
        Log.d(TAG, "CarWsClient.closeSocket")
        webSocket?.close(1000, "client closed")
        webSocket = null
    }

    private inner class CarWebSocketListener(
        private val onEvent: (CarEvent?) -> Unit,
        private val onClosed: () -> Unit,
        private val onFailure: () -> Unit
    ) : WebSocketListener() {

        private val moshi: Moshi = Moshi.Builder()
            .add(LocalDateJsonAdapter())
            .build()

        private val eventAdapter: JsonAdapter<CarEvent> =
            moshi.adapter(CarEvent::class.java)

        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d(TAG, "CarWsClient.onOpen - sending STOMP CONNECT + SUBSCRIBE")
            val t = token

            val connectFrame = buildString {
                append("CONNECT\n")
                append("accept-version:1.2\n")
                append("host:localhost\n")
                if (t != null) {
                    append("Authorization:Bearer $t\n")
                }
                append("\n")
                append("\u0000")
            }
            webSocket.send(connectFrame)

            val subscribeFrame = buildString {
                append("SUBSCRIBE\n")
                append("id:sub-0\n")
                append("destination:/user/queue/notifications\n")
                append("\n")
                append("\u0000")
            }
            webSocket.send(subscribeFrame)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d(TAG, "CarWsClient.onMessage raw=$text")

            val body = extractBody(text) ?: return
            Log.d(TAG, "CarWsClient.onMessage body=$body")

            if (body.isBlank()) {
                return
            }

            try {
                val event = eventAdapter.fromJson(body)
                onEvent(event)
            } catch (e: Exception) {
                Log.w(TAG, "CarWsClient: failed to parse event", e)
            }
        }


        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.d(TAG, "CarWsClient.onMessage bytes (ignored) $bytes")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "CarWsClient.onClosed code=$code reason=$reason")
            onClosed()
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "CarWsClient.onClosing code=$code reason=$reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d(TAG, "CarWsClient.onFailure $t")
            onFailure()
        }

        /**
         * STOMP frame:
         * COMMAND\n
         * header:value\n
         * ...
         * \n
         * {json}\u0000
         */
        private fun extractBody(frame: String): String? {
            val index = frame.indexOf("\n\n")
            if (index == -1) return null
            val withNull = frame.substring(index + 2)
            return withNull.trimEnd('\u0000', '\n', '\r')
        }
    }
}
