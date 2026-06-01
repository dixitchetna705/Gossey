package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.*

@Database(
    entities = [
        User::class,
        Post::class,
        Comment::class,
        Message::class,
        Notification::class,
        LocalAccount::class
    ],
    version = 4,
    exportSchema = false
)
abstract class GossseyDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    abstract fun messageDao(): MessageDao
    abstract fun notificationDao(): NotificationDao
    abstract fun localAccountDao(): LocalAccountDao

    companion object {
        @Volatile
        private var INSTANCE: GossseyDatabase? = null

        fun getDatabase(context: Context): GossseyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GossseyDatabase::class.java,
                    "gosssey_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
