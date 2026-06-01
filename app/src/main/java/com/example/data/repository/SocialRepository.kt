package com.example.data.repository

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.R
import com.example.data.local.*
import com.example.data.model.*
import com.example.widget.GossseyWidgetProvider
import com.example.widget.GossseyMessagesWidgetProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class SocialRepository(private val db: GossseyDatabase, private val context: Context) {
    private val userDao = db.userDao()
    private val postDao = db.postDao()
    private val commentDao = db.commentDao()
    private val messageDao = db.messageDao()
    private val notificationDao = db.notificationDao()
    private val localAccountDao = db.localAccountDao()

    val allPosts: Flow<List<Post>> = postDao.getAllPosts()
    val allUsers: Flow<List<User>> = userDao.getAllUsers()
    val currentUser: Flow<User?> = userDao.getCurrentUser()
    val friends: Flow<List<User>> = userDao.getFriends()
    val discoverUsers: Flow<List<User>> = userDao.getDiscoverUsers()
    val notifications: Flow<List<Notification>> = notificationDao.getAllNotifications()
    val allAccounts: Flow<List<LocalAccount>> = localAccountDao.getAllAccounts()
    val activeAccount: Flow<LocalAccount?> = localAccountDao.getActiveAccountFlow()
    val allMessages: Flow<List<Message>> = messageDao.getAllMessages()

    fun getCommentsForPost(postId: Int): Flow<List<Comment>> = commentDao.getCommentsForPost(postId)
    
    fun getChatHistory(myId: String, theirId: String): Flow<List<Message>> = messageDao.getChatHistory(myId, theirId)

    // Trigger physical push notification on device status bar
    fun sendLocalSystemNotification(title: String, content: String) {
        try {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = "gosssey_channels"
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Gosssey Notifications",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Updates from your connections."
                }
                notificationManager.createNotificationChannel(channel)
            }

            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun createPost(
        authorId: String,
        authorName: String,
        authorAvatarIndex: Int,
        authorAvatarUri: String?,
        content: String,
        gradientIndex: Int = 0,
        mediaUri: String? = null,
        isVideo: Boolean = false
    ) {
        val post = Post(
            authorId = authorId,
            authorName = authorName,
            authorAvatarIndex = authorAvatarIndex,
            authorAvatarUri = authorAvatarUri,
            content = content,
            gradientIndex = gradientIndex,
            timestamp = System.currentTimeMillis(),
            mediaUri = mediaUri,
            isVideo = isVideo
        )
        postDao.insertPost(post)
        GossseyWidgetProvider.triggerWidgetUpdate(context)
    }

    suspend fun deletePost(post: Post) {
        postDao.deletePost(post)
        GossseyWidgetProvider.triggerWidgetUpdate(context)
    }

    suspend fun toggleLikePost(postId: Int) {
        val post = postDao.getPostById(postId) ?: return
        val isLiked = !post.isLikedByMe
        val cleanLikeCount = if (isLiked) post.likeCount + 1 else maxOf(0, post.likeCount - 1)
        postDao.updateLikeStatus(postId, cleanLikeCount, isLiked)

        if (isLiked && post.authorId != "user_me") {
            // Trigger local database notification
            val notifContent = "You liked " + post.authorName + "'s post."
            notificationDao.insertNotification(
                Notification(
                    title = "Post Liked",
                    content = notifContent,
                    type = "LIKE"
                )
            )
            // Trigger native push notification
            sendLocalSystemNotification("New reaction on feed", notifContent)
        }
        GossseyWidgetProvider.triggerWidgetUpdate(context)
    }

    suspend fun addComment(postId: Int, authorId: String, authorName: String, authorAvatarIndex: Int, authorAvatarUri: String?, content: String, parentCommentId: Int = 0) {
        val comment = Comment(
            postId = postId,
            authorId = authorId,
            authorName = authorName,
            authorAvatarIndex = authorAvatarIndex,
            authorAvatarUri = authorAvatarUri,
            content = content,
            timestamp = System.currentTimeMillis(),
            parentCommentId = parentCommentId
        )
        commentDao.insertComment(comment)
        postDao.incrementCommentCount(postId)

        val post = postDao.getPostById(postId)
        if (post != null && post.authorId != "user_me") {
            val notifContent = "You commented on " + post.authorName + "'s post."
            notificationDao.insertNotification(
                Notification(
                    title = "New conversation activity",
                    content = notifContent,
                    type = "COMMENT"
                )
            )
            sendLocalSystemNotification("New comment posted", notifContent)
        }
    }

    suspend fun sendMessage(senderId: String, receiverId: String, content: String) {
        val message = Message(
            senderId = senderId,
            receiverId = receiverId,
            content = content,
            timestamp = System.currentTimeMillis()
        )
        messageDao.insertMessage(message)
        GossseyMessagesWidgetProvider.triggerWidgetUpdate(context)

        // Trigger bot simulator reply
        if (receiverId != "user_me" && senderId == "user_me") {
            triggerSimulationReply(receiverId, content)
        }
    }

    private suspend fun triggerSimulationReply(botId: String, userMessage: String) {
        kotlinx.coroutines.delay(1200)
        
        val replyText = when (botId) {
            "user_alex" -> "Outstanding points! I completely agree that spacing defines visual structure. Have you uploaded your custom vector avatar yet?"
            "user_sarah" -> "That sounds very interesting. Material Design 3 is fantastic to work with. Let me know if you want to try a face call!"
            "user_marcus" -> "Focus is essential. It is wise to focus on what is within our control and let go of trivial noise."
            "user_elena" -> "Thank you! I was just rehearsing some melodies earlier. Nature has always been a key source of inspiration."
            else -> "Thank you for your thoughtful input! It remains a pleasure connecting here on Gosssey."
        }

        val message = Message(
            senderId = botId,
            receiverId = "user_me",
            content = replyText,
            timestamp = System.currentTimeMillis()
        )
        messageDao.insertMessage(message)
        GossseyMessagesWidgetProvider.triggerWidgetUpdate(context)

        val botUser = userDao.getAllUsers().firstOrNull()?.find { it.id == botId }
        val botName = botUser?.fullName ?: "Peer Partner"

        notificationDao.insertNotification(
            Notification(
                title = "New Message from " + botName,
                content = replyText,
                type = "SYSTEM"
            )
        )

        // Real system push notification for chat message
        sendLocalSystemNotification("Message from " + botName, replyText)
    }

    suspend fun sendFriendRequest(userId: String) {
        userDao.updateFriendStatus(userId, "SENT")
        val contentText = "You sent a connection request on Gosssey."
        notificationDao.insertNotification(
            Notification(
                title = "Friend request initialized",
                content = contentText,
                type = "FRIEND_REQUEST"
            )
        )
        sendLocalSystemNotification("Connection request sent", contentText)
    }

    suspend fun acceptFriendRequest(userId: String) {
        userDao.updateFriendStatus(userId, "FRIENDS")
        val contentText = "You accepted the connection request. You are now friends."
        notificationDao.insertNotification(
            Notification(
                title = "Connection established",
                content = contentText,
                type = "FRIEND_REQUEST"
            )
        )
        sendLocalSystemNotification("Connection accepted", contentText)
    }

    suspend fun updateProfile(fullName: String, bio: String, avatarIndex: Int, avatarUri: String?) {
        userDao.updateProfile(fullName, bio, avatarIndex, avatarUri)
        // Also update corresponding LocalAccount if any is active
        val active = localAccountDao.getActiveAccount()
        if (active != null) {
            val updated = active.copy(
                fullName = fullName,
                bio = bio,
                avatarIndex = avatarIndex,
                avatarUri = avatarUri
            )
            localAccountDao.insertAccount(updated)
        }
    }

    suspend fun markNotificationAsRead(id: Int) {
        notificationDao.markAsRead(id)
    }

    suspend fun markAllNotificationsAsRead() {
        notificationDao.markAllAsRead()
    }

    // ---------------- MULTI-ACCOUNT MANAGEMENT ----------------

    suspend fun signup(
        username: String,
        fullName: String,
        email: String,
        phoneNo: String,
        passwordField: String,
        avatarIndex: Int,
        avatarUri: String?
    ): String? {
        val count = localAccountDao.getAccountCount()
        if (count >= 3) {
            return "Registration limit reached. You can make maximum 3 accounts on this device."
        }
        
        // Check uniqueness
        val existing = localAccountDao.findAccountByCredentials(username, email, phoneNo)
        if (existing != null) {
            return "An account with this username, email, or phone number already exists."
        }

        val accountId = UUID.randomUUID().toString()
        val newAccount = LocalAccount(
            id = accountId,
            username = username,
            fullName = fullName,
            email = email,
            phoneNo = phoneNo,
            passwordField = passwordField,
            avatarIndex = avatarIndex,
            avatarUri = avatarUri,
            isActive = false
        )

        localAccountDao.insertAccount(newAccount)
        // Auto sign in the new account
        switchAccount(newAccount)
        sendLocalSystemNotification("Registration successful", "Welcome to Gosssey, " + fullName + "!")
        return null
    }

    suspend fun login(credential: String, passwordField: String): String? {
        val account = localAccountDao.loginAccount(credential, passwordField)
            ?: return "Invalid username/email/phone or password."
        
        switchAccount(account)
        sendLocalSystemNotification("Logged in successfully", "Welcome back, " + account.fullName)
        return null
    }

    suspend fun logoutCurrent() {
        localAccountDao.deactivateAllAccounts()
        // Delete current user me entry to force login route
        val current = userDao.getCurrentUser().firstOrNull()
        if (current != null) {
            userDao.insertUser(current.copy(isCurrentUser = false))
        }
    }

    suspend fun switchAccountById(id: String) {
        val accounts = localAccountDao.getAllAccounts().firstOrNull()
        val target = accounts?.find { it.id == id } ?: return
        switchAccount(target)
    }

    private suspend fun switchAccount(account: LocalAccount) {
        localAccountDao.deactivateAllAccounts()
        localAccountDao.insertAccount(account.copy(isActive = true))

        // Create or update current user 'user_me'
        val userMe = User(
            id = "user_me",
            fullName = account.fullName,
            username = account.username,
            avatarIndex = account.avatarIndex,
            avatarUri = account.avatarUri,
            bio = account.bio,
            friendStatus = "FRIENDS",
            isCurrentUser = true
        )
        userDao.insertUser(userMe)
    }

    suspend fun deleteAccountById(id: String) {
        localAccountDao.deleteAccountById(id)
    }

    suspend fun seedMockDataIfEmpty() {
        // All simulated data has been completely removed as requested
    }
}
