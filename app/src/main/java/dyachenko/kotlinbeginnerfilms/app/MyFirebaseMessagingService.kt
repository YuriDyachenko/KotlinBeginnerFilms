package dyachenko.kotlinbeginnerfilms.app

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(NEW_TOKEN, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(HANDLE, "From: ${remoteMessage.from}")
    }

    companion object {
        const val CURRENT_TOKEN = "CURRENT_TOKEN"
        private const val NEW_TOKEN = "NEW_TOKEN"
        private const val HANDLE = "HANDLE"
    }
}