package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY fullName ASC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE isCurrentUser = 1 LIMIT 1")
    fun getCurrentUser(): Flow<User?>

    @Query("SELECT * FROM users WHERE friendStatus = 'FRIENDS' AND isCurrentUser = 0")
    fun getFriends(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE (friendStatus = 'RECEIVED' OR friendStatus = 'SENT' OR friendStatus = 'NONE') AND isCurrentUser = 0")
    fun getDiscoverUsers(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)

    @Update
    suspend fun updateUser(user: User)

    @Query("UPDATE users SET friendStatus = :status WHERE id = :userId")
    suspend fun updateFriendStatus(userId: String, status: String)

    @Query("UPDATE users SET bio = :bio, fullName = :name, avatarIndex = :avatarIndex, avatarUri = :avatarUri WHERE isCurrentUser = 1")
    suspend fun updateProfile(name: String, bio: String, avatarIndex: Int, avatarUri: String?)
}

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun getAllPosts(): Flow<List<Post>>

    @Query("SELECT * FROM posts WHERE authorId = :authorId ORDER BY timestamp DESC")
    fun getPostsByAuthor(authorId: String): Flow<List<Post>>

    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: Int): Post?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    @Update
    suspend fun updatePost(post: Post)

    @Delete
    suspend fun deletePost(post: Post)

    @Query("UPDATE posts SET likeCount = :likeCount, isLikedByMe = :isLiked WHERE id = :postId")
    suspend fun updateLikeStatus(postId: Int, likeCount: Int, isLiked: Boolean)

    @Query("UPDATE posts SET commentCount = commentCount + 1 WHERE id = :postId")
    suspend fun incrementCommentCount(postId: Int)
}

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY timestamp ASC")
    fun getCommentsForPost(postId: Int): Flow<List<Comment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: Comment)
}

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE (senderId = :myId AND receiverId = :theirId) OR (senderId = :theirId AND receiverId = :myId) ORDER BY timestamp ASC")
    fun getChatHistory(myId: String, theirId: String): Flow<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)
    
    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    fun getAllMessages(): Flow<List<Message>>
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<Notification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: Notification)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :notificationId")
    suspend fun markAsRead(notificationId: Int)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()
}

@Dao
interface LocalAccountDao {
    @Query("SELECT * FROM local_accounts")
    fun getAllAccounts(): Flow<List<LocalAccount>>

    @Query("SELECT * FROM local_accounts WHERE isActive = 1 LIMIT 1")
    fun getActiveAccountFlow(): Flow<LocalAccount?>

    @Query("SELECT * FROM local_accounts WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveAccount(): LocalAccount?

    @Query("SELECT * FROM local_accounts WHERE username = :username OR email = :email OR phoneNo = :phone LIMIT 1")
    suspend fun findAccountByCredentials(username: String, email: String, phone: String): LocalAccount?

    @Query("SELECT * FROM local_accounts WHERE (email = :credential OR username = :credential OR phoneNo = :credential) AND passwordField = :password LIMIT 1")
    suspend fun loginAccount(credential: String, password: String): LocalAccount?

    @Query("SELECT COUNT(*) FROM local_accounts")
    suspend fun getAccountCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: LocalAccount)

    @Update
    suspend fun updateAccount(account: LocalAccount)

    @Query("UPDATE local_accounts SET isActive = 0")
    suspend fun deactivateAllAccounts()

    @Query("UPDATE local_accounts SET isActive = 1 WHERE id = :id")
    suspend fun activateAccount(id: String)

    @Query("DELETE FROM local_accounts WHERE id = :id")
    suspend fun deleteAccountById(id: String)
}
