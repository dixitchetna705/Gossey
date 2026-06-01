package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val fullName: String,
    val username: String,
    val avatarIndex: Int,
    val avatarUri: String? = null, // Real photo URI from image upload/selection
    val bio: String,
    val friendStatus: String = "NONE", // NONE, SENT, RECEIVED, FRIENDS
    val isCurrentUser: Boolean = false
)

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val authorId: String,
    val authorName: String,
    val authorAvatarIndex: Int,
    val authorAvatarUri: String? = null, // Real uploaded photo URI for the author
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val isLikedByMe: Boolean = false,
    val gradientIndex: Int = 0, // 0 for normal, 1-3 for beautiful Blue-Green gradient backgrounds
    val mediaUri: String? = null, // Real uploaded photo/video data URI
    val isVideo: Boolean = false  // True if mediaUri represents video
)

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val postId: Int,
    val authorId: String,
    val authorName: String,
    val authorAvatarIndex: Int,
    val authorAvatarUri: String? = null,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val parentCommentId: Int = 0
)

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val type: String, // LIKE, COMMENT, FRIEND_REQUEST, SYSTEM
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "local_accounts")
data class LocalAccount(
    @PrimaryKey val id: String, // e.g. custom generated ID or username
    val username: String,
    val fullName: String,
    val email: String,
    val phoneNo: String,
    val passwordField: String,
    val avatarIndex: Int = 0,
    val avatarUri: String? = null,
    val bio: String = "No bio added yet.",
    val isActive: Boolean = false
)
