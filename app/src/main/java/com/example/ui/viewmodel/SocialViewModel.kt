package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.GossseyDatabase
import com.example.data.model.*
import com.example.data.repository.SocialRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CallState(
    val partner: User,
    val isVideo: Boolean,
    val isConnected: Boolean = false,
    val durationSeconds: Int = 0
)

class SocialViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SocialRepository

    val allPosts: StateFlow<List<Post>>
    val currentUser: StateFlow<User?>
    val friends: StateFlow<List<User>>
    val discoverUsers: StateFlow<List<User>>
    val notifications: StateFlow<List<Notification>>
    val allMessages: StateFlow<List<Message>>
    
    // Multi-account states
    val allAccounts: StateFlow<List<LocalAccount>>
    val activeAccount: StateFlow<LocalAccount?>

    // Theme & Language settings (Persisted custom SharedPreferences entries)
    private val prefs = application.getSharedPreferences("gosssey_settings", android.content.Context.MODE_PRIVATE)

    private val _themeMode = MutableStateFlow(prefs.getString("theme_mode", "Auto") ?: "Auto")
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    private val _selectedLanguage = MutableStateFlow(prefs.getString("selected_language", "English") ?: "English")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    fun setThemeMode(mode: String) {
        _themeMode.value = mode
        prefs.edit().putString("theme_mode", mode).apply()
    }

    fun setSelectedLanguage(lang: String) {
        _selectedLanguage.value = lang
        prefs.edit().putString("selected_language", lang).apply()
    }

    // Auth flows
    private val _signupError = MutableStateFlow<String?>(null)
    val signupError: StateFlow<String?> = _signupError.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    // Chat target
    private val _activeChatPartner = MutableStateFlow<User?>(null)
    val activeChatPartner: StateFlow<StateFlowPartnerWrapper?> = _activeChatPartner
        .map { if (it == null) null else StateFlowPartnerWrapper(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Switchable flow for chat history
    val activeChatMessages: StateFlow<List<Message>> = _activeChatPartner
        .flatMapLatest { partner ->
            if (partner == null) {
                flowOf(emptyList())
            } else {
                repository.getChatHistory("user_me", partner.id)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Active Simulated Voice/Video Call State
    private val _activeCall = MutableStateFlow<CallState?>(null)
    val activeCall: StateFlow<CallState?> = _activeCall.asStateFlow()

    private var callTimerJob: Job? = null

    init {
        val database = GossseyDatabase.getDatabase(application)
        repository = SocialRepository(database, application)

        // Run baseline seeding
        viewModelScope.launch {
            repository.seedMockDataIfEmpty()
        }

        allPosts = repository.allPosts
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        currentUser = repository.currentUser
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

        friends = repository.friends
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        discoverUsers = repository.discoverUsers
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        notifications = repository.notifications
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allMessages = repository.allMessages
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allAccounts = repository.allAccounts
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        activeAccount = repository.activeAccount
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    }

    // Set active chat person
    fun startChat(user: User) {
        _activeChatPartner.value = user
    }

    fun endChat() {
        _activeChatPartner.value = null
    }

    fun createPost(content: String, gradientIndex: Int = 0, mediaUri: String? = null, isVideo: Boolean = false) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            repository.createPost(
                authorId = user.id,
                authorName = user.fullName,
                authorAvatarIndex = user.avatarIndex,
                authorAvatarUri = user.avatarUri,
                content = content,
                gradientIndex = gradientIndex,
                mediaUri = mediaUri,
                isVideo = isVideo
            )
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch {
            repository.deletePost(post)
        }
    }

    fun toggleLikePost(postId: Int) {
        viewModelScope.launch {
            repository.toggleLikePost(postId)
        }
    }

    fun addComment(postId: Int, content: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            repository.addComment(
                postId = postId,
                authorId = user.id,
                authorName = user.fullName,
                authorAvatarIndex = user.avatarIndex,
                authorAvatarUri = user.avatarUri,
                content = content
            )
        }
    }

    fun getCommentsForPost(postId: Int): Flow<List<Comment>> {
        return repository.getCommentsForPost(postId)
    }

    fun sendMessage(content: String) {
        val partner = _activeChatPartner.value ?: return
        val user = currentUser.value ?: return
        viewModelScope.launch {
            repository.sendMessage(
                senderId = user.id,
                receiverId = partner.id,
                content = content
            )
        }
    }

    fun sharePostToUser(postId: Int, receiverId: String) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val post = allPosts.value.find { it.id == postId } ?: return@launch
            // Format a post preview link/message content encoding media information
            val shareContent = "[Shared Post from ${post.authorName}|postId=${post.id}|mediaUri=${post.mediaUri ?: ""}|isVideo=${post.isVideo}]: ${post.content}"
            repository.sendMessage(
                senderId = user.id,
                receiverId = receiverId,
                content = shareContent
            )
        }
    }

    fun sendFriendRequest(userId: String) {
        viewModelScope.launch {
            repository.sendFriendRequest(userId)
        }
    }

    fun acceptFriendRequest(userId: String) {
        viewModelScope.launch {
            repository.acceptFriendRequest(userId)
        }
    }

    fun updateProfile(fullName: String, bio: String, avatarIndex: Int, avatarUri: String?) {
        viewModelScope.launch {
            repository.updateProfile(fullName, bio, avatarIndex, avatarUri)
        }
    }

    fun markNotificationsAsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }

    // Authentication Actions
    fun dismissSignupError() {
        _signupError.value = null
    }

    fun dismissLoginError() {
        _loginError.value = null
    }

    fun registerNewAccount(
        username: String,
        fullName: String,
        email: String,
        phoneNo: String,
        passwordField: String,
        avatarIndex: Int,
        avatarUri: String?
    ) {
        viewModelScope.launch {
            val error = repository.signup(
                username = username.trim().lowercase(),
                fullName = fullName.trim(),
                email = email.trim().lowercase(),
                phoneNo = phoneNo.trim(),
                passwordField = passwordField,
                avatarIndex = avatarIndex,
                avatarUri = avatarUri
            )
            _signupError.value = error
        }
    }

    fun loginAccount(credential: String, passwordField: String) {
        viewModelScope.launch {
            val error = repository.login(credential.trim(), passwordField)
            _loginError.value = error
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logoutCurrent()
        }
    }

    fun switchAccount(accountId: String) {
        viewModelScope.launch {
            repository.switchAccountById(accountId)
        }
    }

    fun deleteAccount(accountId: String) {
        viewModelScope.launch {
            repository.deleteAccountById(accountId)
        }
    }

    // ---------------- CALL ENGINE ----------------
    fun initiateCall(partner: User, isVideo: Boolean) {
        _activeCall.value = CallState(partner, isVideo)
        
        // Start call connection transition
        viewModelScope.launch {
            delay(2000) // Simulate calling ringing tone
            _activeCall.value = _activeCall.value?.copy(isConnected = true)
            startCallTimer()
            repository.sendLocalSystemNotification(
                "Call established",
                "Voice call session active with " + partner.fullName
            )
        }
    }

    private fun startCallTimer() {
        callTimerJob?.cancel()
        callTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _activeCall.value = _activeCall.value?.let {
                    it.copy(durationSeconds = it.durationSeconds + 1)
                } ?: break
            }
        }
    }

    fun hangUpCall() {
        callTimerJob?.cancel()
        callTimerJob = null
        val lastState = _activeCall.value
        _activeCall.value = null

        if (lastState != null) {
            val minutes = lastState.durationSeconds / 60
            val seconds = lastState.durationSeconds % 60
            val durString = String.format("%02d:%02d", minutes, seconds)
            
            viewModelScope.launch {
                repository.sendMessage(
                    senderId = "user_me",
                    receiverId = lastState.partner.id,
                    content = if (lastState.isVideo) "Missed video call duration ($durString)" else "Voice call complete ($durString)"
                )
            }
        }
    }
}

// Custom wrapper class to keep Flow serialization/StateFlow mappings safe in compose transition states
data class StateFlowPartnerWrapper(val user: User)
