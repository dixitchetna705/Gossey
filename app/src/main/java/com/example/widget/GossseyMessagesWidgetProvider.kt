package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.MainActivity
import com.example.R
import com.example.data.local.GossseyDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class GossseyMessagesWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val db = GossseyDatabase.getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            val latestMessages = db.messageDao().getAllMessages().firstOrNull()
            val newestMsg = latestMessages?.firstOrNull()
            var senderName: String? = null
            
            if (newestMsg != null) {
                val users = db.userDao().getAllUsers().firstOrNull()
                val sender = users?.find { it.id == newestMsg.senderId }
                senderName = sender?.fullName ?: if (newestMsg.senderId == "user_me") "Me" else newestMsg.senderId
            }

            for (appWidgetId in appWidgetIds) {
                updateWidgetState(
                    context, 
                    appWidgetManager, 
                    appWidgetId, 
                    senderName, 
                    newestMsg?.content
                )
            }
        }
    }

    private fun updateWidgetState(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        senderName: String?,
        content: String?
    ) {
        val views = RemoteViews(context.packageName, R.layout.gosssey_messages_widget_layout)

        if (senderName != null && content != null) {
            views.setTextViewText(R.id.widget_msg_sender, senderName)
            views.setTextViewText(R.id.widget_msg_content, content)
        } else {
            views.setTextViewText(R.id.widget_msg_sender, "No Recent Chats")
            views.setTextViewText(R.id.widget_msg_content, "Create peer connections to exchange real-time status card messages!")
        }

        // PendingIntent to launch MainActivity when widget card is clicked
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.msg_header, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    companion object {
        // Trigger a refresh/update of all message widgets when messages update
        fun triggerWidgetUpdate(context: Context) {
            val intent = Intent(context, GossseyMessagesWidgetProvider::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            }
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val ids = appWidgetManager.getAppWidgetIds(
                ComponentName(context, GossseyMessagesWidgetProvider::class.java)
            )
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            context.sendBroadcast(intent)
        }
    }
}
