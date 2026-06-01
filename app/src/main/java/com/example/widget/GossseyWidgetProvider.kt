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

class GossseyWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val db = GossseyDatabase.getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            // Retrieve newest post (if any) from database
            val latestPosts = db.postDao().getAllPosts().firstOrNull()
            val newestPost = latestPosts?.firstOrNull()

            for (appWidgetId in appWidgetIds) {
                updateWidgetState(context, appWidgetManager, appWidgetId, newestPost?.authorName, newestPost?.content)
            }
        }
    }

    private fun updateWidgetState(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        authorName: String?,
        content: String?
    ) {
        val views = RemoteViews(context.packageName, R.layout.gosssey_widget_layout)

        if (authorName != null && content != null) {
            views.setTextViewText(R.id.widget_post_author, authorName)
            views.setTextViewText(R.id.widget_post_content, content)
        } else {
            views.setTextViewText(R.id.widget_post_author, "Gosssey Feed")
            views.setTextViewText(R.id.widget_post_content, "Explore real feeds & vector story designs. Start posting cards now!")
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
        views.setOnClickPendingIntent(R.id.header, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    companion object {
        // Trigger a refresh/update of all widgets when feed updates
        fun triggerWidgetUpdate(context: Context) {
            val intent = Intent(context, GossseyWidgetProvider::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            }
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val ids = appWidgetManager.getAppWidgetIds(
                ComponentName(context, GossseyWidgetProvider::class.java)
            )
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            context.sendBroadcast(intent)
        }
    }
}
