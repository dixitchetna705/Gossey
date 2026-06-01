package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.model.*
import com.example.ui.theme.BackgroundDark
import com.example.ui.viewmodel.CallState
import com.example.ui.viewmodel.SocialViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import android.widget.Toast

val translations = mapOf(
    "English" to mapOf(
        "Feed" to "Feed", "Chat" to "Chat", "Peers" to "Peers", "Alerts" to "Alerts", "Me" to "Me",
        "ManageAccounts" to "Manage Signed-In Accounts (max 3)", "AddAccount" to "Add Account",
        "MyActivity" to "My Activity Logs", "NoActivity" to "You haven't posted anything yet.",
        "EditProfile" to "Edit profile details", "Settings" to "Settings", "Theme" to "Theme",
        "Language" to "Language", "PrivacyPolicy" to "Privacy Policy", "Close" to "Close",
        "CreatePost" to "Create Post", "WhatsOnMind" to "What's on your mind?", "PostFeed" to "Post Feed",
        "DeletePost" to "Delete Post", "WriteComment" to "Write a comment...", "AddFriend" to "Add Friend",
        "Friends" to "Friends", "Sent" to "Sent", "Received" to "Received", "Accept" to "Accept",
        "Share" to "Share", "SharePostTitle" to "Share Post", "CopyLink" to "Copy Link",
        "SharedToWhatsApp" to "Sharing to WhatsApp...", "SharedToFacebook" to "Sharing to Facebook...",
        "SharedToInstagram" to "Sharing to Instagram...", "CopiedToClipboard" to "Post link copied to clipboard!",
        "ShareToUsers" to "Direct message to classmates", "ExternalPlatforms" to "Share on External Networks",
        "Send" to "Send", "SentSuccessfully" to "Sent", "ShareToGosssey" to "Sharing to Gosssey..."
    ),
    "Chinese" to mapOf(
        "Feed" to "动态", "Chat" to "聊天", "Peers" to "发现", "Alerts" to "通知", "Me" to "我",
        "ManageAccounts" to "管理已登录帐户 (最多3个)", "AddAccount" to "添加帐户",
        "MyActivity" to "我的活动日志", "NoActivity" to "您还没有发布任何内容。",
        "EditProfile" to "编辑个人资料", "Settings" to "设置", "Theme" to "主题",
        "Language" to "语言", "PrivacyPolicy" to "隐私政策", "Close" to "关闭",
        "CreatePost" to "创建帖子", "WhatsOnMind" to "在想些什么？", "PostFeed" to "发布动态",
        "DeletePost" to "删除帖子", "WriteComment" to "写评论...", "AddFriend" to "加好友",
        "Friends" to "好友", "Sent" to "已发送", "Received" to "已收到", "Accept" to "接受"
    ),
    "French" to mapOf(
        "Feed" to "Fil", "Chat" to "Chat", "Peers" to "Pairs", "Alerts" to "Alertes", "Me" to "Moi",
        "ManageAccounts" to "Gérer les comptes (max 3)", "AddAccount" to "Ajouter un compte",
        "MyActivity" to "Mon historique", "NoActivity" to "Vous n'avez encore rien publié.",
        "EditProfile" to "Modifier le profil", "Settings" to "Paramètres", "Theme" to "Thème",
        "Language" to "Langue", "PrivacyPolicy" to "Politique de confidentialité", "Close" to "Fermer",
        "CreatePost" to "Créer un message", "WhatsOnMind" to "Qu'avez-vous en tête ?", "PostFeed" to "Publier",
        "DeletePost" to "Supprimer", "WriteComment" to "Écrire un commentaire...", "AddFriend" to "Ajouter",
        "Friends" to "Amis", "Sent" to "Envoyé", "Received" to "Reçu", "Accept" to "Accepter"
    ),
    "Hindi" to mapOf(
        "Feed" to "फ़ीड", "Chat" to "चैट", "Peers" to "सहकर्मी", "Alerts" to "अलर्ट", "Me" to "प्रोफ़ाइल",
        "ManageAccounts" to "खाते प्रबंधित करें (अधिकतम 3)", "AddAccount" to "खाता जोड़ें",
        "MyActivity" to "मेरी गतिविधि", "NoActivity" to "आपने अभी तक कुछ भी पोस्ट नहीं किया है।",
        "EditProfile" to "विवरण संपादित करें", "Settings" to "सेटिंग्स", "Theme" to "थीम",
        "Language" to "भाषा", "PrivacyPolicy" to "गोपनीयता नीति", "Close" to "बंद करें",
        "CreatePost" to "पोस्ट बनाएं", "WhatsOnMind" to "आपके मन में क्या है?", "PostFeed" to "पोस्ट साझा करें",
        "DeletePost" to "पोस्ट हटाएं", "WriteComment" to "टिप्पणी लिखें...", "AddFriend" to "मित्र जोड़ें",
        "Friends" to "मित्र", "Sent" to "भेजा गया", "Received" to "प्राप्त", "Accept" to "स्वीकार करें",
        "Share" to "साझा करें", "SharePostTitle" to "पोस्ट साझा करें", "CopyLink" to "लिंक कॉपी करें",
        "SharedToWhatsApp" to "व्हाट्सएप पर साझा कर रहे हैं...", "SharedToFacebook" to "फेसबुक पर साझा कर रहे हैं...",
        "SharedToInstagram" to "इंस्टाग्राम पर साझा कर रहे हैं...", "CopiedToClipboard" to "पोस्ट लिंक क्लिपबोर्ड पर कॉपी किया गया!",
        "ShareToUsers" to "सहपाठियों को सीधा संदेश भेजें", "ExternalPlatforms" to "बाहरी नेटवर्क पर साझा करें",
        "Send" to "भेजें", "SentSuccessfully" to "सफलतापूर्वक भेजा गया", "ShareToGosssey" to "गोस्से पर साझा कर रहे हैं..."
    ),
    "Urdu" to mapOf(
        "Feed" to "فیڈ", "Chat" to "چیট", "Peers" to "ساتھی", "Alerts" to "الرٹس", "Me" to "मेरी प्रोफाइल",
        "ManageAccounts" to "کھاتوں کا انتظام کریں (زیادہ سے زیادہ 3)", "AddAccount" to "کھاتہ شامل کریں",
        "MyActivity" to "میری سرگرمی", "NoActivity" to "آپ نے ابھی تک کچھ پوسٹ نہیں کیا۔",
        "EditProfile" to "پروفائل ترمیم کریں", "Settings" to "ترتیبات", "Theme" to "تھیم",
        "Language" to "زبان", "PrivacyPolicy" to "رازداری کی پالیسی", "Close" to "بند کریں",
        "CreatePost" to "پوسٹ بنائیں", "WhatsOnMind" to "آپ کے دماغ میں کیا ہے؟", "PostFeed" to "پوسٹ شیئر کریں",
        "DeletePost" to "پوسٹ حذف کریں", "WriteComment" to "تبصرہ لکھیں...", "AddFriend" to "دوست بنائیں",
        "Friends" to "دوست", "Sent" to "بھیجا گیا", "Received" to "موصول", "Accept" to "قبول کریں"
    ),
    "Telugu" to mapOf(
        "Feed" to "ఫీడ్", "Chat" to "చాట్", "Peers" to "స్నేహితులు", "Alerts" to "అలర్ట్స్", "Me" to "నా ప్రొఫైల్",
        "ManageAccounts" to "ఖాతాల నిర్వహణ (గరిష్టంగా 3)", "AddAccount" to "ఖాతాను జోడించు",
        "MyActivity" to "నా కార్యాచరణ", "NoActivity" to "మీరు ఇంకా ఏమీ పోస్ట్ చేయలేదు.",
        "EditProfile" to "ప్రొఫైల్ సవరించు", "Settings" to "సెట్టింగ్స్", "Theme" to "థీమ్",
        "Language" to "భాష", "PrivacyPolicy" to "గోప్యతా విధానం", "Close" to "మూసివేయి",
        "CreatePost" to "పోస్ట్ సృష్టించు", "WhatsOnMind" to "మీ మనసులో ఏముంది?", "PostFeed" to "పోస్ట్ చేయి",
        "DeletePost" to "పోస్ట్ తొலగించు", "WriteComment" to "వ్యాఖ్య రాయండి...", "AddFriend" to "మిత్రులని జోడించు",
        "Friends" to "స్నేహితులు", "Sent" to "పంపబడింది", "Received" to "అందుకున్నారు", "Accept" to "ఆమోదించు"
    ),
    "Tamil" to mapOf(
        "Feed" to "செய்தி ஓடை", "Chat" to "அரட்டை", "Peers" to "கூட்டாளிகள்", "Alerts" to "அறிவிப்புகள்", "Me" to "என்னுடையது",
        "ManageAccounts" to "கணக்குகளை நிர்வகி (அதிகபட்சம் 3)", "AddAccount" to "கணக்கைச் சேர்",
        "MyActivity" to "என்னுடைய பதிவு", "NoActivity" to "நீங்கள் இன்னும் எதுவும் பதிவிடவில்லை.",
        "EditProfile" to "சுயவிவரத்தை திருத்து", "Settings" to "அமைப்புகள்", "Theme" to "தீம்",
        "Language" to "மொழி", "PrivacyPolicy" to "தனியுரிமைக் கொள்கை", "Close" to "மூடு",
        "CreatePost" to "பதிவை உருவாக்கு", "WhatsOnMind" to "உங்கள் மனதில் என்ன?", "PostFeed" to "பதிவிடு",
        "DeletePost" to "பதிவை நீக்கு", "WriteComment" to "கருத்து எழுது...", "AddFriend" to "நண்பரைச் சேர்",
        "Friends" to "நண்பர்கள்", "Sent" to "அனுப்பப்பட்டது", "Received" to "பெறப்பட்டது", "Accept" to "ஏற்றுக்கொள்"
    ),
    "Espanol" to mapOf(
        "Feed" to "Inicio", "Chat" to "Mensajes", "Peers" to "Amigos", "Alerts" to "Notificaciones", "Me" to "Perfil",
        "ManageAccounts" to "Cuentas conectadas (máx 3)", "AddAccount" to "Añadir cuenta",
        "MyActivity" to "Mi actividad reciente", "NoActivity" to "Aún no has publicado nada.",
        "EditProfile" to "Editar perfil", "Settings" to "Ajustes", "Theme" to "Tema",
        "Language" to "Idioma", "PrivacyPolicy" to "Política de privacidad", "Close" to "Cerrar",
        "CreatePost" to "Crear publicación", "WhatsOnMind" to "¿Qué estás pensando?", "PostFeed" to "Publicar",
        "DeletePost" to "Eliminar publicación", "WriteComment" to "Escribir comentario...", "AddFriend" to "Añadir amigo",
        "Friends" to "Amigos", "Sent" to "Enviado", "Received" to "Recibido", "Accept" to "Aceptar"
    ),
    "Arabic" to mapOf(
        "Feed" to "الرئيسية", "Chat" to "المحادثات", "Peers" to "الأقران", "Alerts" to "التنبيهات", "Me" to "الملف الشخصي",
        "ManageAccounts" to "إدارة الحسابات (الأقصى 3)", "AddAccount" to "إضافة حساب",
        "MyActivity" to "نشاطاتي", "NoActivity" to "لم تنشر أي شيء بعد.",
        "EditProfile" to "تعديل الحساب", "Settings" to "الإعدادات", "Theme" to "المظهر",
        "Language" to "اللغة", "PrivacyPolicy" to "سياسة الخصوصية", "Close" to "إغلاق",
        "CreatePost" to "إنشاء منشور", "WhatsOnMind" to "ماذا يخطر في بالك؟", "PostFeed" to "نشر المنشور",
        "DeletePost" to "حذف المنشور", "WriteComment" to "اكتب تعليقاً...", "AddFriend" to "إضافة صديق",
        "Friends" to "أصدقاء", "Sent" to "مرسلة", "Received" to "الواردة", "Accept" to "قبول"
    ),
    "Bengali" to mapOf(
        "Feed" to "ফিড", "Chat" to "চ্যাট", "Peers" to "সহকর্মী", "Alerts" to "বিজ্ঞপ্তি", "Me" to "আমার প্রোফাইল",
        "ManageAccounts" to "অ্যাকাউন্ট পরিচালনা (সর্বোচ্চ ৩)", "AddAccount" to "অ্যাকাউন্ট যোগ করুন",
        "MyActivity" to "আমার ক্রিয়াকলাপ", "NoActivity" to "আপনি এখনও কিছু পোস্ট করেননি।",
        "EditProfile" to "প্রোফাইল সম্পাদন", "Settings" to "সেটিংস", "Theme" to "থিম",
        "Language" to "ভাষা", "PrivacyPolicy" to "গোপনীয়তা নীতি", "Close" to "বন্ধ করুন",
        "CreatePost" to "পোস্ট তৈরি করুন", "WhatsOnMind" to "আপনার মনে কী চলছে?", "PostFeed" to "পোস্ট করুন",
        "DeletePost" to "পোস্ট মুছুন", "WriteComment" to "মন্তব্য লিখুন...", "AddFriend" to "বন্ধু যোগ করুন",
        "Friends" to "বন্ধু", "Sent" to "পাঠানো", "Received" to "গৃহীত", "Accept" to "গ্রহণ করুন"
    ),
    "Russian" to mapOf(
        "Feed" to "Лента", "Chat" to "Чат", "Peers" to "Контакты", "Alerts" to "Оповещения", "Me" to "Профиль",
        "ManageAccounts" to "Управление аккаунтами (макс 3)", "AddAccount" to "Добавить аккаунт",
        "MyActivity" to "Моя активность", "NoActivity" to "Вы еще ничего не опубликовали.",
        "EditProfile" to "Редактировать профиль", "Settings" to "Настройки", "Theme" to "Тема",
        "Language" to "Язык", "PrivacyPolicy" to "Конфиденциальность", "Close" to "Закрыть",
        "CreatePost" to "Создать публикацию", "WhatsOnMind" to "Что у вас на уме?", "PostFeed" to "Опубликовать",
        "DeletePost" to "Удалить", "WriteComment" to "Написать комментарий...", "AddFriend" to "Добавить друга",
        "Friends" to "Друзья", "Sent" to "Отправлено", "Received" to "Получено", "Accept" to "Принять"
    ),
    "Portuguese" to mapOf(
        "Feed" to "Feed", "Chat" to "Conversas", "Peers" to "Contatos", "Alerts" to "Alertas", "Me" to "Perfil",
        "ManageAccounts" to "Gerenciar contas (máx 3)", "AddAccount" to "Adicionar conta",
        "MyActivity" to "Minhas publicações", "NoActivity" to "Você ainda não publicou nada.",
        "EditProfile" to "Editar perfil", "Settings" to "Configurações", "Theme" to "Tema",
        "Language" to "Idioma", "PrivacyPolicy" to "Política de privacidade", "Close" to "Fechar",
        "CreatePost" to "Criar publicação", "WhatsOnMind" to "O que você está pensando?", "PostFeed" to "Publicar",
        "DeletePost" to "Deletar publicação", "WriteComment" to "Escrever comentário...", "AddFriend" to "Adicionar amigo",
        "Friends" to "Amigos", "Sent" to "Enviado", "Received" to "Recebido", "Accept" to "Aceitar"
    )
)

fun getLocalizedText(key: String, selectedLanguage: String): String {
    val langMap = translations[selectedLanguage] ?: translations["English"]!!
    return langMap[key] ?: key
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: SocialViewModel) {
    val activeAccount by viewModel.activeAccount.collectAsStateWithLifecycle()
    val allAccounts by viewModel.allAccounts.collectAsStateWithLifecycle()
    val signupError by viewModel.signupError.collectAsStateWithLifecycle()
    val loginError by viewModel.loginError.collectAsStateWithLifecycle()

    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val allPosts by viewModel.allPosts.collectAsStateWithLifecycle()
    val friends by viewModel.friends.collectAsStateWithLifecycle()
    val discoverUsers by viewModel.discoverUsers.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val activeCall by viewModel.activeCall.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) }
    var showCreatePostSheet by remember { mutableStateOf(false) }
    var activePostForComments by remember { mutableStateOf<Post?>(null) }
    var activeStoryView by remember { mutableStateOf<Story?>(null) }
    var activePostForSharing by remember { mutableStateOf<Post?>(null) }

    val primaryGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF00EF60), Color(0xFF0575E6))
    )

    if (activeAccount == null) {
        // Gates of Authentications panel
        AuthGateScreen(
            allAccounts = allAccounts,
            signupError = signupError,
            loginError = loginError,
            viewModel = viewModel
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize().testTag("main_scaffold"),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "gosssey",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 28.sp,
                            fontFamily = FontFamily.SansSerif,
                            style = LocalTextStyle.current.copy(
                                brush = primaryGradient
                            )
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    modifier = Modifier.testTag("main_navigation_bar"),
                    containerColor = MaterialTheme.colorScheme.background,
                    windowInsets = WindowInsets.navigationBars
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == 0) Icons.Default.Home else Icons.Outlined.Home,
                                contentDescription = "Feed"
                            )
                        },
                        label = { Text(getLocalizedText("Feed", selectedLanguage), fontSize = 11.sp) },
                        modifier = Modifier.testTag("tab_feed")
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == 1) Icons.Default.ChatBubble else Icons.Outlined.ChatBubbleOutline,
                                contentDescription = "Messenger"
                            )
                        },
                        label = { Text(getLocalizedText("Chat", selectedLanguage), fontSize = 11.sp) },
                        modifier = Modifier.testTag("tab_messenger")
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == 2) Icons.Default.People else Icons.Outlined.People,
                                contentDescription = "Relations"
                            )
                        },
                        label = { Text(getLocalizedText("Peers", selectedLanguage), fontSize = 11.sp) },
                        modifier = Modifier.testTag("tab_relations")
                    )
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == 3) Icons.Default.Notifications else Icons.Outlined.NotificationsNone,
                                contentDescription = "Alerts"
                            )
                        },
                        label = { Text(getLocalizedText("Alerts", selectedLanguage), fontSize = 11.sp) },
                        modifier = Modifier.testTag("tab_alerts")
                    )
                    NavigationBarItem(
                        selected = selectedTab == 4,
                        onClick = { selectedTab = 4 },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == 4) Icons.Default.Person else Icons.Outlined.Person,
                                contentDescription = "Profile"
                            )
                        },
                        label = { Text(getLocalizedText("Me", selectedLanguage), fontSize = 11.sp) },
                        modifier = Modifier.testTag("tab_profile")
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        fadeIn(animationSpec = spring()) togetherWith fadeOut(animationSpec = spring())
                    },
                    label = "SwitchNavigationTabs"
                ) { targetTab ->
                    when (targetTab) {
                        0 -> FeedTab(
                            allPosts = allPosts,
                            currentUser = currentUser,
                            onLikeClicked = { viewModel.toggleLikePost(it) },
                            onCommentClicked = { activePostForComments = it },
                            onDeleteClicked = { viewModel.deletePost(it) },
                            onCreatePostRequested = { showCreatePostSheet = true },
                            onStoryClicked = { activeStoryView = it },
                            onShareClicked = { activePostForSharing = it }
                        )
                        1 -> MessengerTab(viewModel = viewModel)
                        2 -> RelationshipsTab(
                            friends = friends,
                            discoverUsers = discoverUsers,
                            onAddFriend = { viewModel.sendFriendRequest(it) },
                            onAcceptFriend = { viewModel.acceptFriendRequest(it) }
                        )
                        3 -> NotificationsTab(
                            notifications = notifications,
                            onMarkAsRead = { viewModel.markNotificationsAsRead() }
                        )
                        4 -> ProfileTab(
                            currentUser = currentUser,
                            allPosts = allPosts,
                            viewModel = viewModel,
                            allAccounts = allAccounts,
                            onShareClicked = { activePostForSharing = it }
                        )
                    }
                }

                // Global Create Post Sheet
                if (showCreatePostSheet) {
                    CreatePostSheet(
                        currentUser = currentUser,
                        onDismiss = { showCreatePostSheet = false },
                        onPostCreated = { content, gradient, mediaUri, isVideo ->
                            viewModel.createPost(content, gradient, mediaUri, isVideo)
                            showCreatePostSheet = false
                        }
                    )
                }

                // Comments Bottom Drawer Sheet
                activePostForComments?.let { post ->
                    CommentsSheet(
                        post = post,
                        viewModel = viewModel,
                        onDismiss = { activePostForComments = null }
                    )
                }

                // Real Instagram style Story Viewer popup overlay
                activeStoryView?.let { story ->
                    StoryViewerDialog(
                        story = story,
                        onDismiss = { activeStoryView = null }
                    )
                }

                // Share Post modal overlay
                activePostForSharing?.let { post ->
                    SharePostDialog(
                        post = post,
                        viewModel = viewModel,
                        friendsList = friends,
                        onDismiss = { activePostForSharing = null }
                    )
                }

                // Real-time voice/video simulated call overlay
                activeCall?.let { call ->
                    SimulatedCallScreen(
                        callState = call,
                        onHangUp = { viewModel.hangUpCall() }
                    )
                }
            }
        }
    }
}

// ---------------- USER AVATAR HELPER with Custom Photo Picker & Vectors ----------------
@Composable
fun UserAvatar(
    avatarIndex: Int,
    avatarUri: String? = null,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .testTag("user_avatar_slot"),
        contentAlignment = Alignment.Center
    ) {
        if (!avatarUri.isNullOrEmpty()) {
            AsyncImage(
                model = avatarUri,
                contentDescription = "User upload photo avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().clip(CircleShape)
            )
        } else {
            val gradient = when (avatarIndex) {
                0 -> Brush.linearGradient(colors = listOf(Color(0xFF00C6FF), Color(0xFF0072FF)))
                1 -> Brush.linearGradient(colors = listOf(Color(0xFF00F260), Color(0xFF0575E6)))
                2 -> Brush.linearGradient(colors = listOf(Color(0xFF11998E), Color(0xFF38EF7D)))
                3 -> Brush.linearGradient(colors = listOf(Color(0xFF1A1A24), Color(0xFF02AABD)))
                else -> Brush.linearGradient(colors = listOf(Color(0xFFFF5F6D), Color(0xFFFFC371)))
            }
            val iconVector = when (avatarIndex) {
                0 -> Icons.Default.Person
                1 -> Icons.Default.Brush
                2 -> Icons.Default.DeveloperMode
                3 -> Icons.Default.AutoAwesome
                else -> Icons.Default.Piano
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradient)
                    .padding(size / 6),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = "Fallback design avatar",
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

// ---------------- STORY DATA & VIEWERS (Instagram Style) ----------------
data class Story(
    val id: String,
    val authorName: String,
    val avatarIndex: Int,
    val avatarUri: String? = null,
    val capTitle: String,
    val contentQuote: String,
    val bgGradient: Brush
)

@Composable
fun StoryViewerDialog(story: Story, onDismiss: () -> Unit) {
    var progress by remember { mutableFloatStateOf(0f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(story) {
        val step = 0.02f
        while (progress < 1.0f) {
            delay(100)
            progress += step
        }
        onDismiss()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(story.bgGradient)
                .clickable { onDismiss() }
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Progress timeline bar
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = Color.White,
                    trackColor = Color.White.copy(alpha = 0.3f),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Author Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    UserAvatar(
                        avatarIndex = story.avatarIndex,
                        avatarUri = story.avatarUri,
                        size = 40.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = story.authorName,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Quotes",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = story.capTitle,
                            fontWeight = FontWeight.Black,
                            fontSize = 24.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 30.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = story.contentQuote,
                            fontWeight = FontWeight.Light,
                            fontSize = 15.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                }

                Text(
                    text = "Tap anywhere to skip story card",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )
            }
        }
    }
}

// ---------------- GLOBAL AUTH GATES (LOGIN/SIGN UP) ----------------
@Composable
fun AuthGateScreen(
    allAccounts: List<LocalAccount>,
    signupError: String?,
    loginError: String?,
    viewModel: SocialViewModel
) {
    var isLoginTab by remember { mutableStateOf(allAccounts.isNotEmpty()) }
    
    // Sign Up Fields
    var signupName by remember { mutableStateOf("") }
    var signupPhone by remember { mutableStateOf("") }
    var signupEmail by remember { mutableStateOf("") }
    var signupUsername by remember { mutableStateOf("") }
    var signupPassword by remember { mutableStateOf("") }
    var selectedAvatarIdx by remember { mutableIntStateOf(0) }
    var customAvatarUriStr by remember { mutableStateOf<String?>(null) }

    // Login Fields
    var loginCredential by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    val pickPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            customAvatarUriStr = uri.toString()
            selectedAvatarIdx = -1 // custom upload active
            viewModel.dismissSignupError()
        }
    }

    Scaffold(
        containerColor = BackgroundDark,
        modifier = Modifier.fillMaxSize().testTag("auth_scaffold")
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "gosssey",
                    fontWeight = FontWeight.Black,
                    fontSize = 42.sp,
                    fontFamily = FontFamily.SansSerif,
                    style = LocalTextStyle.current.copy(
                        brush = Brush.linearGradient(colors = listOf(Color(0xFF00EF60), Color(0xFF0072FF)))
                    )
                )
                Text(
                    text = "Pure connection loop. Maximum 3 accounts device capacity.",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            // Quick multi-profile switch list if there are actual registered accounts
            if (allAccounts.isNotEmpty()) {
                item {
                    Text(
                        text = "Switch active device profile",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
                items(allAccounts) { acc ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.switchAccount(acc.id) }
                            .testTag("auth_account_switch_${acc.username}"),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(14.dp),
                        border = if (acc.isActive) BorderStroke(1.5.dp, Color(0xFF00EF60)) else null
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp).fillMaxWidth()
                        ) {
                            UserAvatar(avatarIndex = acc.avatarIndex, avatarUri = acc.avatarUri, size = 38.dp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(acc.fullName, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("@" + acc.username, fontSize = 12.sp, color = Color.Gray)
                            }
                            if (acc.isActive) {
                                Text("Active", color = Color(0xFF00EF60), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            } else {
                                Icon(Icons.Default.ArrowForwardIos, contentDescription = "Login", modifier = Modifier.size(12.dp), tint = Color.Gray)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                // Selection Tab Row
                TabRow(
                    selectedTabIndex = if (isLoginTab) 0 else 1,
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        val activeTab = if (isLoginTab) 0 else 1
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                            color = Color(0xFF00EF60)
                        )
                    }
                ) {
                    Tab(
                        selected = isLoginTab,
                        onClick = { isLoginTab = true },
                        text = { Text("Login Profile", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = !isLoginTab,
                        onClick = {
                            if (allAccounts.size >= 3) {
                                // Block signup with inline notification error
                                viewModel.loginAccount("", "") // clear flows
                            } else {
                                isLoginTab = false
                            }
                        },
                        text = {
                            Text(
                                "Create Account" + if (allAccounts.size >= 3) " (Full)" else "",
                                fontWeight = FontWeight.Bold,
                                color = if (allAccounts.size >= 3) Color.Gray else Color.White
                            )
                        }
                    )
                }
            }

            if (isLoginTab) {
                // ---------------- LOGIN FORM ----------------
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = loginCredential,
                            onValueChange = { loginCredential = it },
                            label = { Text("Username, Email or Phone") },
                            modifier = Modifier.fillMaxWidth().testTag("login_cred_input"),
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.AlternateEmail, contentDescription = "Credentials") }
                        )
                        OutlinedTextField(
                            value = loginPassword,
                            onValueChange = { loginPassword = it },
                            label = { Text("Password credentials") },
                            modifier = Modifier.fillMaxWidth().testTag("login_pass_input"),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock") }
                        )

                        if (!loginError.isNullOrEmpty()) {
                            Text(
                                text = loginError,
                                color = Color.Red,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                            )
                        }

                        Button(
                            onClick = {
                                if (loginCredential.isNotBlank() && loginPassword.isNotBlank()) {
                                    viewModel.loginAccount(loginCredential, loginPassword)
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("login_submit_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00EF60), contentColor = Color.Black)
                        ) {
                            Text("Sign In", fontWeight = FontWeight.Black)
                        }
                    }
                }
            } else {
                // ---------------- SIGN UP FORM ----------------
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // User Avatar selection row with Photo selector
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
                        ) {
                            UserAvatar(
                                avatarIndex = selectedAvatarIdx,
                                avatarUri = customAvatarUriStr,
                                size = 70.dp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                (0..3).forEach { index ->
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .border(
                                                width = 2.dp,
                                                color = if (selectedAvatarIdx == index) Color(0xFF00EF60) else Color.Transparent,
                                                shape = CircleShape
                                            )
                                            .clickable {
                                                selectedAvatarIdx = index
                                                customAvatarUriStr = null
                                            }
                                            .padding(2.dp)
                                    ) {
                                        UserAvatar(avatarIndex = index, modifier = Modifier.fillMaxSize())
                                    }
                                }
                                // Gallery Image picker trigger button
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(Color.Gray.copy(alpha = 0.3f))
                                        .clickable { pickPhotoLauncher.launch("image/*") }
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AddPhotoAlternate,
                                        contentDescription = "Upload personal photo for avatar",
                                        tint = Color.White,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                            Text(
                                text = "Select standard preset grid or tap camera to upload photo",
                                color = Color.Gray,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }

                        OutlinedTextField(
                            value = signupName,
                            onValueChange = { signupName = it },
                            label = { Text("Full Name") },
                            modifier = Modifier.fillMaxWidth().testTag("signup_name"),
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") }
                        )
                        OutlinedTextField(
                            value = signupUsername,
                            onValueChange = { signupUsername = it },
                            label = { Text("Desired Username") },
                            modifier = Modifier.fillMaxWidth().testTag("signup_username"),
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.AlternateEmail, contentDescription = "Username") }
                        )
                        OutlinedTextField(
                            value = signupEmail,
                            onValueChange = { signupEmail = it },
                            label = { Text("Email Address") },
                            modifier = Modifier.fillMaxWidth().testTag("signup_email"),
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") }
                        )
                        OutlinedTextField(
                            value = signupPhone,
                            onValueChange = { signupPhone = it },
                            label = { Text("Phone Number") },
                            modifier = Modifier.fillMaxWidth().testTag("signup_phone"),
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone") }
                        )
                        OutlinedTextField(
                            value = signupPassword,
                            onValueChange = { signupPassword = it },
                            label = { Text("Password credential") },
                            modifier = Modifier.fillMaxWidth().testTag("signup_password"),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") }
                        )

                        if (!signupError.isNullOrEmpty()) {
                            Text(
                                text = signupError,
                                color = Color.Red,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                            )
                        }

                        Button(
                            onClick = {
                                if (signupName.isNotBlank() &&
                                    signupUsername.isNotBlank() &&
                                    signupEmail.isNotBlank() &&
                                    signupPhone.isNotBlank() &&
                                    signupPassword.isNotBlank()
                                ) {
                                    viewModel.registerNewAccount(
                                        username = signupUsername,
                                        fullName = signupName,
                                        email = signupEmail,
                                        phoneNo = signupPhone,
                                        passwordField = signupPassword,
                                        avatarIndex = selectedAvatarIdx,
                                        avatarUri = customAvatarUriStr
                                    )
                                }
                            },
                            enabled = signupName.isNotBlank() && signupUsername.isNotBlank() && signupEmail.isNotBlank() && signupPhone.isNotBlank() && signupPassword.isNotBlank(),
                            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("signup_submit_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00EF60), contentColor = Color.Black)
                        ) {
                            Text("Complete Sign Up", fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }
}

// ---------------- NEWS FEED TAB WITH STORIES BAR ----------------
@Composable
fun FeedTab(
    allPosts: List<Post>,
    currentUser: User?,
    onLikeClicked: (Int) -> Unit,
    onCommentClicked: (Post) -> Unit,
    onDeleteClicked: (Post) -> Unit,
    onCreatePostRequested: () -> Unit,
    onStoryClicked: (Story) -> Unit,
    onShareClicked: (Post) -> Unit
) {
    // Standard mock Stories data
    val storyList = remember(currentUser) {
        listOf(
            Story(
                id = "story_alex",
                authorName = "Alex Rivers",
                avatarIndex = 1,
                capTitle = "Spacing is Geometry",
                contentQuote = "Good design feels natural and invisible. Let typography breathe elegantly.",
                bgGradient = Brush.linearGradient(colors = listOf(Color(0xFF8A2387), Color(0xFFE94057), Color(0xFFF27121)))
            ),
            Story(
                id = "story_sarah",
                authorName = "Sarah Chen",
                avatarIndex = 2,
                capTitle = "Dynamic Brushes preset",
                contentQuote = "Integrating gradient vector arrays inside Compose today. Totally gorgeous dynamic rendering!",
                bgGradient = Brush.linearGradient(colors = listOf(Color(0xFF00C6FF), Color(0xFF0072FF)))
            ),
            Story(
                id = "story_marcus",
                authorName = "Marcus Aurelius",
                avatarIndex = 3,
                capTitle = "The Power of Moderation",
                contentQuote = "Restraint in interactive features creates deep, mindful user focus.",
                bgGradient = Brush.linearGradient(colors = listOf(Color(0xFF1D2671), Color(0xFFC33764)))
            ),
            Story(
                id = "story_elena",
                authorName = "Elena Rostova",
                avatarIndex = 4,
                capTitle = "Guitar Echoes",
                contentQuote = "Recorded some raw acoustic rhythm cycles today. Feels incredibly meditative.",
                bgGradient = Brush.linearGradient(colors = listOf(Color(0xFF11998E), Color(0xFF38EF7D)))
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("feed_lazy_column"),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // IG Style Stories Bar Row
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)) {
                Text(
                    text = "Stories",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Own profile story button
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { onCreatePostRequested() }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray.copy(alpha = 0.2f))
                                    .padding(3.dp)
                            ) {
                                UserAvatar(
                                    avatarIndex = currentUser?.avatarIndex ?: 0,
                                    avatarUri = currentUser?.avatarUri,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF00EF60))
                                        .align(Alignment.BottomEnd),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Add Story", modifier = Modifier.size(12.dp), tint = Color.Black)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Your Story", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Friends circles list
                    items(storyList) { story ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { onStoryClicked(story) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(62.dp)
                                    .clip(CircleShape)
                                    .background(story.bgGradient)
                                    .padding(3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.background)
                                        .padding(2.dp)
                                ) {
                                    UserAvatar(
                                        avatarIndex = story.avatarIndex,
                                        avatarUri = story.avatarUri,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = story.authorName.split(" ").firstOrNull() ?: "",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Compose post card prompt (Resembling Facebook's status publisher)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCreatePostRequested() }
                    .testTag("composer_trigger_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(14.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UserAvatar(
                        avatarIndex = currentUser?.avatarIndex ?: 0,
                        avatarUri = currentUser?.avatarUri,
                        size = 42.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "What is on your mind, " + (currentUser?.fullName?.split(" ")?.firstOrNull() ?: "") + "?",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onCreatePostRequested) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Standard composer post",
                            tint = Color(0xFF00EF60)
                        )
                    }
                }
            }
        }

        // List feed posts with IG style parameters
        if (allPosts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Dashboard,
                            contentDescription = "Empty feed",
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No updates in the loop.",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        Text(
                            text = "Tap above to post your very first update card.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        } else {
            items(allPosts, key = { it.id }) { post ->
                PostItemCard(
                    post = post,
                    onLikeClicked = onLikeClicked,
                    onCommentClicked = onCommentClicked,
                    onDeleteClicked = onDeleteClicked,
                    onShareClicked = onShareClicked
                )
            }
        }
    }
}

// ---------------- POST CARD WITH INSTAGRAM DOUBLE-TAP ANIMATION ----------------
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostItemCard(
    post: Post,
    onLikeClicked: (Int) -> Unit,
    onCommentClicked: (Post) -> Unit,
    onDeleteClicked: (Post) -> Unit,
    onShareClicked: (Post) -> Unit
) {
    val dateString = remember(post.timestamp) {
        val sdf = SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault())
        sdf.format(Date(post.timestamp))
    }

    // Double tap heart indicator animation state
    var showHeartPopup by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val scaleHeart by animateFloatAsState(
        targetValue = if (showHeartPopup) 1.5f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "HeartScale"
    )

    val postGradient = when (post.gradientIndex) {
        1 -> Brush.linearGradient(colors = listOf(Color(0xFF0575E6), Color(0xFF00F260)))
        2 -> Brush.linearGradient(colors = listOf(Color(0xFF11998E), Color(0xFF38EF7D)))
        3 -> Brush.linearGradient(colors = listOf(Color(0xFF00C6FF), Color(0xFF0072FF)))
        else -> null
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("post_card_${post.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header author info
            Row(
                modifier = Modifier
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserAvatar(
                    avatarIndex = post.authorAvatarIndex,
                    avatarUri = post.authorAvatarUri,
                    size = 40.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.authorName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = dateString,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }

                if (post.authorId == "user_me") {
                    IconButton(
                        onClick = { onDeleteClicked(post) },
                        modifier = Modifier.testTag("delete_post_${post.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete post",
                            tint = Color.Red.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // Interactive Post Area (Double Tap to Like supported)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onDoubleClick = {
                            if (!post.isLikedByMe) {
                                onLikeClicked(post.id)
                            }
                            scope.launch {
                                showHeartPopup = true
                                delay(600)
                                showHeartPopup = false
                            }
                        },
                        onClick = {}
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    if (postGradient != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .background(postGradient)
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = post.content,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    } else {
                        Text(
                            text = post.content,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .fillMaxWidth(),
                            lineHeight = 19.sp
                        )
                    }

                    if (!post.mediaUri.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        if (post.isVideo) {
                            var isPlayingSimulated by remember { mutableStateOf(false) }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .background(Color.Black)
                                    .clickable { isPlayingSimulated = !isPlayingSimulated },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isPlayingSimulated) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        CircularProgressIndicator(color = Color(0xFF00EF60), modifier = Modifier.size(36.dp))
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Streaming Video Clip...", color = Color.White, fontSize = 12.sp)
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .background(Color.Black.copy(alpha = 0.6f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "Simulated video play button",
                                            tint = Color(0xFF00EF60),
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                    Text(
                                        text = "Video Clip Attached (Tap to stream)",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 11.sp,
                                        modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp)
                                    )
                                }
                            }
                        } else {
                            AsyncImage(
                                model = post.mediaUri,
                                contentDescription = "Attached post image upload",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                            )
                        }
                    }
                }

                // Smooth Instagram Heart Overlay Animation
                if (showHeartPopup) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Love reaction animation",
                        tint = Color.Red,
                        modifier = Modifier
                            .size(72.dp)
                            .scale(scaleHeart)
                    )
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                thickness = 1.dp,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Feed Reaction panel
            Row(
                modifier = Modifier
                    .padding(horizontal = 6.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onLikeClicked(post.id) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = if (post.isLikedByMe) Color.Red else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = if (post.isLikedByMe) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Love button",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (post.likeCount > 0) post.likeCount.toString() else "Like",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }

                Button(
                    onClick = { onCommentClicked(post) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Comment,
                        contentDescription = "Comment drawer",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (post.commentCount > 0) post.commentCount.toString() else "Comment",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }

                Button(
                    onClick = { onShareClicked(post) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("share_button_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Share button",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Share",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

// ---------------- CREATE LOCAL FEED POST SHEET ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostSheet(
    currentUser: User?,
    onDismiss: () -> Unit,
    onPostCreated: (String, Int, String?, Boolean) -> Unit
) {
    var textInput by remember { mutableStateOf("") }
    var selectedGradientIndex by remember { mutableIntStateOf(0) }
    var attachedMediaUri by remember { mutableStateOf<String?>(null) }
    var attachedMediaIsVideo by remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            attachedMediaUri = uri.toString()
            attachedMediaIsVideo = false
            selectedGradientIndex = 0 // Clear gradient index on attachment
        }
    }

    val pickVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            attachedMediaUri = uri.toString()
            attachedMediaIsVideo = true
            selectedGradientIndex = 0 // Clear gradient index on attachment
        }
    }

    val activeGradBrush = when (selectedGradientIndex) {
        1 -> Brush.linearGradient(colors = listOf(Color(0xFF0575E6), Color(0xFF00F260)))
        2 -> Brush.linearGradient(colors = listOf(Color(0xFF11998E), Color(0xFF38EF7D)))
        3 -> Brush.linearGradient(colors = listOf(Color(0xFF00C6FF), Color(0xFF0072FF)))
        else -> null
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier.fillMaxHeight(0.85f).testTag("create_post_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Create Post",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Button(
                    onClick = {
                        if (textInput.isNotBlank() || attachedMediaUri != null) {
                            onPostCreated(textInput, selectedGradientIndex, attachedMediaUri, attachedMediaIsVideo)
                        }
                    },
                    enabled = textInput.isNotBlank() || attachedMediaUri != null,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00EF60), contentColor = Color.Black),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("submit_post_button")
                ) {
                    Text("Post Feed", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserAvatar(
                    avatarIndex = currentUser?.avatarIndex ?: 0,
                    avatarUri = currentUser?.avatarUri,
                    size = 40.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = currentUser?.fullName ?: "Chetna Dixit",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = "Public post indicator",
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Public",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .then(
                        if (activeGradBrush != null) {
                            Modifier.background(activeGradBrush).padding(16.dp)
                        } else {
                            Modifier.background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f))
                        }
                    )
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = {
                        Text(
                            text = "What is on your mind?",
                            color = if (activeGradBrush != null) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("post_text_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = if (activeGradBrush != null) Color.White else MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = if (activeGradBrush != null) Color.White else MaterialTheme.colorScheme.onSurface
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = if (activeGradBrush != null) 20.sp else 16.sp,
                        fontWeight = if (activeGradBrush != null) FontWeight.Bold else FontWeight.Normal,
                        textAlign = if (activeGradBrush != null) TextAlign.Center else TextAlign.Start
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Background style selection",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (selectedGradientIndex == 0) Color(0xFF00EF60) else Color.Gray.copy(alpha = 0.3f))
                        .clickable { selectedGradientIndex = 0 }
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(colors = listOf(Color(0xFF0575E6), Color(0xFF00F260))))
                        .clickable { selectedGradientIndex = 1 }
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(colors = listOf(Color(0xFF11998E), Color(0xFF38EF7D))))
                        .clickable { selectedGradientIndex = 2 }
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(colors = listOf(Color(0xFF00C6FF), Color(0xFF0072FF))))
                        .clickable { selectedGradientIndex = 3 }
                )
            }

            if (attachedMediaUri != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    if (attachedMediaIsVideo) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.PlayCircle, contentDescription = "Video preview", tint = Color(0xFF00EF60), modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Video clip attached ready to post", color = Color.White, fontSize = 11.sp)
                        }
                    } else {
                        AsyncImage(
                            model = attachedMediaUri,
                            contentDescription = "Post image preview",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    IconButton(
                        onClick = { attachedMediaUri = null },
                        modifier = Modifier.align(Alignment.TopEnd).padding(6.dp).background(Color.Black.copy(alpha = 0.6f), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Remove attachment", tint = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { pickImageLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurface),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Attach image", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add Image", fontSize = 12.sp)
                }
                Button(
                    onClick = { pickVideoLauncher.launch("video/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurface),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.VideoCall, contentDescription = "Attach video", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add Video", fontSize = 12.sp)
                }
            }
        }
    }
}

// ---------------- FEED COMMENT MODAL BOTTOM DRAWERS ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsSheet(
    post: Post,
    viewModel: SocialViewModel,
    onDismiss: () -> Unit
) {
    val comments by viewModel.getCommentsForPost(post.id).collectAsStateWithLifecycle(emptyList())
    var commentText by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier.fillMaxHeight(0.85f).testTag("comments_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Comments Area",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (comments.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Start a discussion.",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    items(comments, key = { it.id }) { comment ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            UserAvatar(avatarIndex = comment.authorAvatarIndex, avatarUri = comment.authorAvatarUri, size = 32.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = comment.authorName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = comment.content,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = { Text("Write a comment...") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("comment_bar_input"),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.03f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.03f)
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            viewModel.addComment(post.id, commentText)
                            commentText = ""
                            focusManager.clearFocus()
                        }
                    },
                    modifier = Modifier
                        .testTag("submit_comment_btn")
                        .clip(CircleShape)
                        .background(Color(0xFF00EF60))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Submit comment",
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// ---------------- MESSENGER TAB (Direct Chat window list) ----------------
@Composable
fun MessengerTab(viewModel: SocialViewModel) {
    val friends by viewModel.friends.collectAsStateWithLifecycle(emptyList())
    val activeChatPartnerWrapper by viewModel.activeChatPartner.collectAsStateWithLifecycle()
    val rawChatMessages by viewModel.activeChatMessages.collectAsStateWithLifecycle()

    if (activeChatPartnerWrapper != null) {
        val partner = activeChatPartnerWrapper!!.user
        ChatWindow(
            partner = partner,
            messages = rawChatMessages,
            onSendMessage = { viewModel.sendMessage(it) },
            onBack = { viewModel.endChat() },
            viewModel = viewModel
        )
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Gosssey Messenger",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .testTag("messenger_friend_list"),
                contentPadding = PaddingValues(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (friends.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Establish peer connections to start chatting.",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    items(friends, key = { it.id }) { friend ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.startChat(friend) }
                                .testTag("messenger_friend_row_${friend.id}"),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                UserAvatar(avatarIndex = friend.avatarIndex, avatarUri = friend.avatarUri, size = 46.dp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = friend.fullName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = friend.bio,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.Chat,
                                    contentDescription = "Start Messenger conversation",
                                    tint = Color(0xFF00EF60),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------- INDIVIDUAL CHAT WINDOW PANELS with CALL TRIGGER TRIGGERS ----------------
@Composable
fun ChatWindow(
    partner: User,
    messages: List<Message>,
    onSendMessage: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: SocialViewModel
) {
    var chatInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("chat_window_pane")
    ) {
        // Chat Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 8.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("back_to_messenger")) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back back"
                )
            }
            UserAvatar(avatarIndex = partner.avatarIndex, avatarUri = partner.avatarUri, size = 36.dp)
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = partner.fullName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = "Active Peer Connection",
                    fontSize = 11.sp,
                    color = Color(0xFF00EF60),
                    fontWeight = FontWeight.Medium
                )
            }

            // Real simulated Call buttons (Voice and Video Calling)
            IconButton(
                onClick = { viewModel.initiateCall(partner, isVideo = false) },
                modifier = Modifier.testTag("phone_call_btn")
            ) {
                Icon(Icons.Default.Phone, contentDescription = "Simulated Voice call icon", tint = Color(0xFF00EF60))
            }

            IconButton(
                onClick = { viewModel.initiateCall(partner, isVideo = true) },
                modifier = Modifier.testTag("video_call_btn")
            ) {
                Icon(Icons.Default.VideoCall, contentDescription = "Simulated Video call icon", tint = Color(0xFF00EF60))
            }
        }

        // Chat conversation bubble space
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                val isMe = msg.senderId == "user_me"
                val isSharedPost = msg.content.startsWith("[Shared Post from ")
                var displayText = msg.content
                var shareAuthor = ""
                var sharedPostId = -1
                var sharedMediaUri: String? = null
                var sharedIsVideo = false

                if (isSharedPost) {
                    val header = msg.content.substringBefore("]: ").removePrefix("[Shared Post from ")
                    val parts = header.split("|")
                    shareAuthor = parts.getOrNull(0) ?: ""
                    parts.forEach { part ->
                        if (part.startsWith("postId=")) {
                            sharedPostId = part.substringAfter("postId=").toIntOrNull() ?: -1
                        } else if (part.startsWith("mediaUri=")) {
                            val uri = part.substringAfter("mediaUri=")
                            if (uri.isNotEmpty()) {
                                sharedMediaUri = uri
                            }
                        } else if (part.startsWith("isVideo=")) {
                            sharedIsVideo = part.substringAfter("isVideo=").toBoolean()
                        }
                    }
                    displayText = msg.content.substringAfter("]: ").trim()
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isMe) 16.dp else 4.dp,
                            bottomEnd = if (isMe) 4.dp else 16.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isMe) Color(0xFF00EF60) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                            contentColor = if (isMe) Color.Black else MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.widthIn(max = 280.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            if (isSharedPost) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Shared Post",
                                        modifier = Modifier.size(14.dp),
                                        tint = if (isMe) Color.Black.copy(alpha = 0.6f) else MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Shared Post from $shareAuthor",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isMe) Color.Black.copy(alpha = 0.7f) else MaterialTheme.colorScheme.primary
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isMe) Color.Black.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(8.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text(
                                            text = displayText,
                                            fontSize = 13.sp,
                                            lineHeight = 17.sp,
                                            fontStyle = FontStyle.Italic,
                                            color = if (isMe) Color.Black else MaterialTheme.colorScheme.onSurface
                                        )
                                        
                                        if (sharedMediaUri != null) {
                                            if (sharedIsVideo) {
                                                var isPlayingByTap by remember { mutableStateOf(false) }
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(130.dp)
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(Color.Black)
                                                        .clickable { isPlayingByTap = !isPlayingByTap },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    if (isPlayingByTap) {
                                                        Column(
                                                            modifier = Modifier.fillMaxSize(),
                                                            verticalArrangement = Arrangement.Center,
                                                            horizontalAlignment = Alignment.CenterHorizontally
                                                        ) {
                                                            CircularProgressIndicator(color = Color(0xFF00EF60), modifier = Modifier.size(24.dp))
                                                            Spacer(modifier = Modifier.height(4.dp))
                                                            Text("Streaming...", color = Color.White, fontSize = 10.sp)
                                                        }
                                                    } else {
                                                        Box(
                                                            modifier = Modifier
                                                                .size(36.dp)
                                                                .background(Color.Black.copy(alpha = 0.6f), CircleShape),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.PlayArrow,
                                                                contentDescription = "Play shared video",
                                                                tint = Color(0xFF00EF60),
                                                                modifier = Modifier.size(24.dp)
                                                            )
                                                        }
                                                    }
                                                    Text(
                                                        text = "Video Attached",
                                                        color = Color.White.copy(alpha = 0.6f),
                                                        fontSize = 9.sp,
                                                        modifier = Modifier.align(Alignment.BottomCenter).padding(4.dp)
                                                    )
                                                }
                                            } else {
                                                AsyncImage(
                                                    model = sharedMediaUri,
                                                    contentDescription = "Attached shared post image",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(130.dp)
                                                        .clip(RoundedCornerShape(6.dp))
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                Text(
                                    text = msg.content,
                                    fontSize = 14.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Message text input bar at bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = chatInput,
                onValueChange = { chatInput = it },
                placeholder = { Text("Type message...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_text_input_bar"),
                shape = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (chatInput.isNotBlank()) {
                            onSendMessage(chatInput)
                            chatInput = ""
                        }
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (chatInput.isNotBlank()) {
                        onSendMessage(chatInput)
                        chatInput = ""
                        focusManager.clearFocus()
                    }
                },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF00EF60))
                    .testTag("chat_send_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send Direct message chat",
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// ---------------- RELATIONSHIPS (PEERS) TAB ----------------
@Composable
fun RelationshipsTab(
    friends: List<User>,
    discoverUsers: List<User>,
    onAddFriend: (String) -> Unit,
    onAcceptFriend: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("relationships_tab"),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val pendingRequests = discoverUsers.filter { it.friendStatus == "RECEIVED" }
        if (pendingRequests.isNotEmpty()) {
            item {
                Text(
                    text = "Pending connections requests",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            items(pendingRequests, key = { it.id }) { requester ->
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("pending_card_${requester.id}"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        UserAvatar(avatarIndex = requester.avatarIndex, avatarUri = requester.avatarUri, size = 46.dp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = requester.fullName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = requester.bio,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                        Button(
                            onClick = { onAcceptFriend(requester.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00EF60), contentColor = Color.Black),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("accept_${requester.id}")
                        ) {
                            Text("Confirm", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        val discoverables = discoverUsers.filter { it.friendStatus != "RECEIVED" }
        item {
            Text(
                text = "Discover people on Gosssey",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (discoverables.isEmpty()) {
            item {
                Text(
                    "You are connected to all available peers.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                )
            }
        } else {
            items(discoverables, key = { it.id }) { peer ->
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("discover_card_${peer.id}"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        UserAvatar(avatarIndex = peer.avatarIndex, avatarUri = peer.avatarUri, size = 46.dp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = peer.fullName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = peer.bio,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }

                        val isSent = peer.friendStatus == "SENT"
                        Button(
                            onClick = { if (!isSent) onAddFriend(peer.id) },
                            enabled = !isSent,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSent) Color.Gray.copy(alpha = 0.3f) else MaterialTheme.colorScheme.secondary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("add_friend_${peer.id}")
                        ) {
                            Text(
                                text = if (isSent) "Pending" else "Add Peer",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------- NOTIFICATIONS (ALERTS) TAB ----------------
@Composable
fun NotificationsTab(
    notifications: List<Notification>,
    onMarkAsRead: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Notifications Area",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Button(
                onClick = onMarkAsRead,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Mark all read", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .testTag("notifications_list"),
            contentPadding = PaddingValues(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (notifications.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No notifications as of now.",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                items(notifications, key = { it.id }) { notif ->
                    val timestampStr = remember(notif.timestamp) {
                        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
                        sdf.format(Date(notif.timestamp))
                    }

                    val colorTint = if (notif.isRead) Color.Gray.copy(alpha = 0.4f) else Color(0xFF00EF60)

                    val vectorIcon = when (notif.type) {
                        "LIKE" -> Icons.Default.Favorite
                        "COMMENT" -> Icons.Default.Comment
                        "FRIEND_REQUEST" -> Icons.Default.People
                        else -> Icons.Default.Info
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (notif.isRead) MaterialTheme.colorScheme.surface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(colorTint.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = vectorIcon,
                                    contentDescription = "Notification type category icon",
                                    tint = colorTint,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = notif.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (notif.isRead) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = notif.content,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                            Text(
                                text = timestampStr,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------- PERSONAL PROFILE TAB with Account Switching ----------------
@Composable
fun ProfileTab(
    currentUser: User?,
    allPosts: List<Post>,
    viewModel: SocialViewModel,
    allAccounts: List<LocalAccount>,
    onShareClicked: (Post) -> Unit
) {
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    var showSettingsDialog by remember { mutableStateOf(false) }

    var editMode by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf(currentUser?.fullName ?: "Chetna Dixit") }
    var editBio by remember { mutableStateOf(currentUser?.bio ?: "") }
    var selectedAvatarIndex by remember { mutableIntStateOf(currentUser?.avatarIndex ?: 0) }
    var customAvatarUriStr by remember { mutableStateOf<String?>(currentUser?.avatarUri) }

    val myPosts = remember(allPosts, currentUser) {
        allPosts.filter { it.authorId == "user_me" }
    }

    val pickPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            customAvatarUriStr = uri.toString()
            selectedAvatarIndex = -1 // Use custom uploaded photo
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("profile_tab_column"),
        contentPadding = PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Switch/Manage Accounts Horizontal Row
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        getLocalizedText("ManageAccounts", selectedLanguage),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        allAccounts.forEach { acc ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable { viewModel.switchAccount(acc.id) }
                                    .border(
                                        width = if (acc.isActive) 2.dp else 0.dp,
                                        color = if (acc.isActive) Color(0xFF00EF60) else Color.Transparent,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .padding(4.dp)
                            ) {
                                UserAvatar(avatarIndex = acc.avatarIndex, avatarUri = acc.avatarUri, size = 38.dp)
                                Text(
                                    text = acc.fullName.split(" ").firstOrNull() ?: "",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (acc.isActive) Color(0xFF00EF60) else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        
                        if (allAccounts.size < 3) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable { viewModel.logout() } // Logouts so user gets back to sign in screen to add a and switch to a 2nd or 3rd account!
                                    .padding(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color.Gray.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Add another account link", tint = Color.White)
                                }
                                Text(getLocalizedText("AddAccount", selectedLanguage), fontSize = 10.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }

        // Top Banner profile card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.linearGradient(colors = listOf(Color(0xFF01C6FF), Color(0xFF00F260))))
                    )

                    Box(
                        modifier = Modifier
                            .offset(y = (-40).dp)
                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                            .padding(4.dp)
                    ) {
                        UserAvatar(
                            avatarIndex = if (editMode) selectedAvatarIndex else (currentUser?.avatarIndex ?: 0),
                            avatarUri = if (editMode) customAvatarUriStr else currentUser?.avatarUri,
                            size = 80.dp
                        )
                    }

                    if (editMode) {
                        Text(
                            "Choose your avatar profile theme",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            (0..3).forEach { idx ->
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (selectedAvatarIndex == idx) Color(0xFF00EF60) else Color.Gray.copy(alpha = 0.2f)
                                        )
                                        .clickable {
                                            selectedAvatarIndex = idx
                                            customAvatarUriStr = null
                                        }
                                        .padding(2.dp)
                                ) {
                                    UserAvatar(avatarIndex = idx, modifier = Modifier.fillMaxSize())
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray.copy(alpha = 0.3f))
                                    .clickable { pickPhotoLauncher.launch("image/*") }
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Edit photo profile upload", tint = Color.White)
                            }
                        }

                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text("Display Name") },
                            modifier = Modifier.fillMaxWidth().testTag("edit_profile_name"),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = editBio,
                            onValueChange = { editBio = it },
                            label = { Text("Profile Bio") },
                            modifier = Modifier.fillMaxWidth().testTag("edit_profile_bio"),
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    editMode = false
                                    editName = currentUser?.fullName ?: "Chetna Dixit"
                                    editBio = currentUser?.bio ?: ""
                                    selectedAvatarIndex = currentUser?.avatarIndex ?: 0
                                    customAvatarUriStr = currentUser?.avatarUri
                                }
                            ) {
                                Text("Discard")
                            }
                            Button(
                                onClick = {
                                    viewModel.updateProfile(editName, editBio, selectedAvatarIndex, customAvatarUriStr)
                                    editMode = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00EF60), contentColor = Color.Black),
                                modifier = Modifier.testTag("save_profile_button")
                            ) {
                                Text("Save Credentials", fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        Text(
                            text = currentUser?.fullName ?: "Chetna Dixit",
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp,
                            modifier = Modifier.offset(y = (-20).dp).testTag("profile_full_name")
                        )
                        Text(
                            text = "@" + (currentUser?.username ?: "chetna_dixit"),
                            fontSize = 13.sp,
                            color = Color(0xFF00EF60),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.offset(y = (-18).dp)
                        )
                        Text(
                            text = currentUser?.bio ?: "No bio defined yet.",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .offset(y = (-10).dp)
                                .padding(horizontal = 14.dp)
                                .testTag("profile_bio_text")
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    editMode = true
                                    editName = currentUser?.fullName ?: "Chetna Dixit"
                                    editBio = currentUser?.bio ?: ""
                                    selectedAvatarIndex = currentUser?.avatarIndex ?: 0
                                    customAvatarUriStr = currentUser?.avatarUri
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier.testTag("edit_profile_trigger")
                            ) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit profiles", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(getLocalizedText("EditProfile", selectedLanguage), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            // Settings Dialog Trigger button
                            IconButton(
                                onClick = { showSettingsDialog = true },
                                modifier = Modifier.testTag("settings_button")
                            ) {
                                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.primary)
                            }

                            // Sign Out overall
                            IconButton(onClick = { viewModel.logout() }) {
                                Icon(Icons.Default.ExitToApp, contentDescription = "Sign out active profile", tint = Color.Red)
                            }
                        }

                        // Settings Dialog Overlay Setup
                        if (showSettingsDialog) {
                            Dialog(
                                onDismissRequest = { showSettingsDialog = false }
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .testTag("settings_dialog_box"),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Text(
                                            text = getLocalizedText("Settings", selectedLanguage),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

                                        // 1. Theme Configuration section
                                        Text(
                                            text = getLocalizedText("Theme", selectedLanguage),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.secondary
                                        )

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            listOf("Light", "Dark", "Auto").forEach { mode ->
                                                val isSelected = themeMode == mode
                                                val bg = if (isSelected) {
                                                    Brush.linearGradient(colors = listOf(Color(0xFF00EF60), Color(0xFF0575E6)))
                                                } else {
                                                    null
                                                }
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .run {
                                                            if (bg != null) background(bg) else background(MaterialTheme.colorScheme.surfaceVariant)
                                                        }
                                                        .clickable { viewModel.setThemeMode(mode) }
                                                        .padding(vertical = 8.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = mode,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }
                                        }

                                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

                                        // 2. Language Selection scroll row
                                        Text(
                                            text = getLocalizedText("Language", selectedLanguage),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.secondary
                                        )

                                        val languagePairs = listOf(
                                            "English" to "English",
                                            "Chinese" to "中文",
                                            "French" to "Français",
                                            "Hindi" to "हिन्दी",
                                            "Urdu" to "اردو",
                                            "Telugu" to "తెలుగు",
                                            "Tamil" to "தமிழ்",
                                            "Espanol" to "Español",
                                            "Arabic" to "العربية",
                                            "Bengali" to "বাংলা",
                                            "Russian" to "Русский",
                                            "Portuguese" to "Português"
                                        )

                                        LazyRow(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            items(languagePairs) { (id, label) ->
                                                val isSelected = selectedLanguage == id
                                                FilterChip(
                                                    selected = isSelected,
                                                    onClick = { viewModel.setSelectedLanguage(id) },
                                                    label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                                    colors = FilterChipDefaults.filterChipColors(
                                                        selectedContainerColor = Color(0xFF00EF60).copy(alpha = 0.2f),
                                                        selectedLabelColor = Color(0xFF00EF60)
                                                    )
                                                )
                                            }
                                        }

                                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

                                        // 3. Privacy Policy read-only section
                                        Text(
                                            text = getLocalizedText("PrivacyPolicy", selectedLanguage),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.secondary
                                        )

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(80.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                                .padding(8.dp)
                                        ) {
                                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                                item {
                                                    Text(
                                                        text = "At Gosssey, your privacy is our primary engineering milestone. All profile details, uploaded assets like images/video frames, relationships, and chat histories are kept secure under local sandbox sandboxing in device databases. Chat encryption hashes assure total privacy. Absolutely no remote visual telemetry or profiling trackers are maintained.",
                                                        fontSize = 11.sp,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        lineHeight = 15.sp
                                                    )
                                                }
                                            }
                                        }

                                        Button(
                                            onClick = { showSettingsDialog = false },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00EF60), contentColor = Color.Black),
                                            modifier = Modifier.align(Alignment.End),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text(getLocalizedText("Close", selectedLanguage), fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Personal feeds
        item {
            Text(
                text = "My Activity Logs",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (myPosts.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "You haven't posted anything yet.",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        } else {
            items(myPosts, key = { it.id }) { post ->
                PostItemCard(
                    post = post,
                    onLikeClicked = { viewModel.toggleLikePost(it) },
                    onCommentClicked = { /* Handled parent sheet comments directly */ },
                    onDeleteClicked = { viewModel.deletePost(it) },
                    onShareClicked = onShareClicked
                )
            }
        }
    }
}

// ---------------- SHARE POST MODAL OVERLAY ----------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatformSvgIcon(platform: String, modifier: Modifier = Modifier) {
    androidx.compose.foundation.Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val center = androidx.compose.ui.geometry.Offset(w / 2f, h / 2f)
        
        when (platform) {
            "WhatsApp" -> {
                drawCircle(color = Color(0xFF25D366), radius = w / 2f)
                val speechPath = androidx.compose.ui.graphics.Path().apply {
                    addOval(androidx.compose.ui.geometry.Rect(w * 0.25f, h * 0.25f, w * 0.75f, h * 0.75f))
                    moveTo(w * 0.32f, h * 0.68f)
                    lineTo(w * 0.20f, h * 0.80f)
                    lineTo(w * 0.40f, h * 0.72f)
                    close()
                }
                drawPath(path = speechPath, color = Color.White)
                
                val handsetPath = androidx.compose.ui.graphics.Path().apply {
                    moveTo(w * 0.41f, h * 0.42f)
                    quadraticTo(w * 0.45f, h * 0.38f, w * 0.50f, h * 0.43f)
                    quadraticTo(w * 0.58f, h * 0.51f, w * 0.50f, h * 0.59f)
                    quadraticTo(w * 0.45f, h * 0.64f, w * 0.41f, h * 0.58f)
                    quadraticTo(w * 0.38f, h * 0.54f, w * 0.41f, h * 0.42f)
                }
                drawPath(path = handsetPath, color = Color(0xFF25D366))
                drawCircle(color = Color(0xFF25D366), radius = w * 0.08f, center = androidx.compose.ui.geometry.Offset(w * 0.50f, h * 0.50f))
            }
            "Facebook" -> {
                drawCircle(color = Color(0xFF1877F2), radius = w / 2f)
                val fPath = androidx.compose.ui.graphics.Path().apply {
                    moveTo(w * 0.58f, h * 0.85f)
                    lineTo(w * 0.58f, h * 0.55f)
                    lineTo(w * 0.68f, h * 0.55f)
                    lineTo(w * 0.70f, h * 0.45f)
                    lineTo(w * 0.58f, h * 0.45f)
                    lineTo(w * 0.58f, h * 0.38f)
                    quadraticTo(w * 0.58f, h * 0.28f, w * 0.66f, h * 0.28f)
                    lineTo(w * 0.72f, h * 0.28f)
                    lineTo(w * 0.72f, h * 0.18f)
                    quadraticTo(w * 0.52f, h * 0.18f, w * 0.48f, h * 0.34f)
                    lineTo(w * 0.48f, h * 0.45f)
                    lineTo(w * 0.40f, h * 0.45f)
                    lineTo(w * 0.40f, h * 0.55f)
                    lineTo(w * 0.48f, h * 0.55f)
                    lineTo(w * 0.48f, h * 0.85f)
                    close()
                }
                drawPath(path = fPath, color = Color.White)
            }
            "Instagram" -> {
                val gradientBrush = androidx.compose.ui.graphics.Brush.linearGradient(
                    colors = listOf(Color(0xFFF58529), Color(0xFFDD2A7B), Color(0xFF8134AF)),
                    start = androidx.compose.ui.geometry.Offset(0f, h),
                    end = androidx.compose.ui.geometry.Offset(w, 0f)
                )
                drawCircle(brush = gradientBrush, radius = w / 2f)
                
                val inset = w * 0.26f
                val sizeVal = w * 0.48f
                val cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.13f, h * 0.13f)
                
                drawRoundRect(
                    color = Color.White,
                    topLeft = androidx.compose.ui.geometry.Offset(inset, inset),
                    size = androidx.compose.ui.geometry.Size(sizeVal, sizeVal),
                    cornerRadius = cornerRadius,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = w * 0.045f)
                )
                
                drawCircle(
                    color = Color.White,
                    radius = w * 0.11f,
                    center = center,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = w * 0.045f)
                )
                
                drawCircle(
                    color = Color.White,
                    radius = w * 0.035f,
                    center = androidx.compose.ui.geometry.Offset(w * 0.62f, h * 0.38f)
                )
            }
            "Gosssey" -> {
                val brandBrush = androidx.compose.ui.graphics.Brush.linearGradient(
                    colors = listOf(Color(0xFF00EF60), Color(0xFF0575E6))
                )
                drawCircle(brush = brandBrush, radius = w / 2f)
                
                val gPath = androidx.compose.ui.graphics.Path().apply {
                    val rect = androidx.compose.ui.geometry.Rect(w * 0.26f, h * 0.26f, w * 0.74f, h * 0.74f)
                    arcTo(rect, startAngleDegrees = 0f, sweepAngleDegrees = -280f, forceMoveTo = true)
                    lineTo(w * 0.52f, h * 0.52f)
                    lineTo(w * 0.68f, h * 0.52f)
                }
                drawPath(
                    path = gPath,
                    color = Color.White,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = w * 0.08f, 
                        cap = androidx.compose.ui.graphics.StrokeCap.Round, 
                        join = androidx.compose.ui.graphics.StrokeJoin.Round
                    )
                )
            }
        }
    }
}

@Composable
fun SharePostDialog(
    post: Post,
    viewModel: SocialViewModel,
    friendsList: List<User>,
    onDismiss: () -> Unit
) {
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    // Track shared peers
    val sharedUserIds = remember { mutableStateListOf<String>() }

    // Collect message flow to build dynamic list of recently chatted classmates
    val allMessages by viewModel.allMessages.collectAsStateWithLifecycle()
    val recentlyChattedUsers = remember(allMessages, friendsList) {
        val recentIds = allMessages
            .filter { it.senderId != "user_me" || it.receiverId != "user_me" }
            .map { if (it.senderId == "user_me") it.receiverId else it.senderId }
            .distinct()
        val friendsMap = friendsList.associateBy { it.id }
        recentIds.mapNotNull { id -> friendsMap[id] }
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("share_post_dialog_box"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Outer dialog header block
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = getLocalizedText("SharePostTitle", selectedLanguage),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close sharing dialog")
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

                // Post Content Preview Frame
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        UserAvatar(avatarIndex = post.authorAvatarIndex, avatarUri = post.authorAvatarUri, size = 30.dp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Post by ${post.authorName}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = post.content,
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }

                // Section 1: Recently Chatted Peers (horizontal sheet row)
                if (recentlyChattedUsers.isNotEmpty()) {
                    Text(
                        text = "Recent Chats",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(horizontal = 2.dp)
                    ) {
                        items(recentlyChattedUsers, key = { "recent_" + it.id }) { friend ->
                            val isAlreadyShared = sharedUserIds.contains(friend.id)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .width(60.dp)
                                    .clickable {
                                        if (!isAlreadyShared) {
                                            viewModel.sharePostToUser(post.id, friend.id)
                                            sharedUserIds.add(friend.id)
                                            Toast.makeText(context, "${getLocalizedText("SentSuccessfully", selectedLanguage)} to ${friend.fullName}!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            ) {
                                Box(contentAlignment = Alignment.BottomEnd) {
                                    UserAvatar(avatarIndex = friend.avatarIndex, avatarUri = friend.avatarUri, size = 42.dp)
                                    if (isAlreadyShared) {
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF00EF60)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Shared check badge",
                                                tint = Color.Black,
                                                modifier = Modifier.size(10.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = friend.fullName.substringBefore(" "),
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                // Section 2: All Classmates / Peers Direct Message (vertical row list)
                Text(
                    text = getLocalizedText("ShareToUsers", selectedLanguage),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.secondary
                )

                if (friendsList.isEmpty()) {
                    Text(
                        text = "You don't have classmates or peers added yet.",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 110.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(friendsList, key = { it.id }) { friend ->
                            val isAlreadyShared = sharedUserIds.contains(friend.id)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                UserAvatar(avatarIndex = friend.avatarIndex, avatarUri = friend.avatarUri, size = 28.dp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = friend.fullName,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                Button(
                                    onClick = {
                                        if (!isAlreadyShared) {
                                            viewModel.sharePostToUser(post.id, friend.id)
                                            sharedUserIds.add(friend.id)
                                            Toast.makeText(context, "${getLocalizedText("SentSuccessfully", selectedLanguage)} to ${friend.fullName}!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isAlreadyShared) Color.Gray else Color(0xFF00EF60),
                                        contentColor = if (isAlreadyShared) Color.White else Color.Black
                                    ),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .height(30.dp)
                                        .testTag("send_to_${friend.id}")
                                ) {
                                    Text(
                                        text = if (isAlreadyShared) getLocalizedText("SentSuccessfully", selectedLanguage) else getLocalizedText("Send", selectedLanguage),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

                // Section 3: External sharing options using custom drawing vector SVG layers, including Gosssey internal reposting, and removing copy link
                Text(
                    text = getLocalizedText("ExternalPlatforms", selectedLanguage),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.secondary
                )

                // Share text formatting including post media indicators if photo/video are attached
                val mediaIndicator = if (post.mediaUri != null) {
                    if (post.isVideo) "\n[Video Clip Attached]" else "\n[Photo Attached]"
                } else ""
                val postShareText = "Check out ${post.authorName}'s post on Gosssey: \"${post.content}\"$mediaIndicator\n\nhttps://gosssey.app/posts/${post.id}"

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // WhatsApp column with vector SVG icon
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                Toast.makeText(context, getLocalizedText("SharedToWhatsApp", selectedLanguage), Toast.LENGTH_SHORT).show()
                                shareTextToPlatform(context, "WhatsApp", postShareText)
                            }
                    ) {
                        PlatformSvgIcon(platform = "WhatsApp", modifier = Modifier.size(42.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("WhatsApp", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }

                    // Facebook column with vector SVG icon
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                Toast.makeText(context, getLocalizedText("SharedToFacebook", selectedLanguage), Toast.LENGTH_SHORT).show()
                                shareTextToPlatform(context, "Facebook", postShareText)
                            }
                    ) {
                        PlatformSvgIcon(platform = "Facebook", modifier = Modifier.size(42.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Facebook", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }

                    // Instagram column with sunset gradient vector icon
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                Toast.makeText(context, getLocalizedText("SharedToInstagram", selectedLanguage), Toast.LENGTH_SHORT).show()
                                shareTextToPlatform(context, "Instagram", postShareText)
                            }
                    ) {
                        PlatformSvgIcon(platform = "Instagram", modifier = Modifier.size(42.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Instagram", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }

                    // Gosssey option - creates a fully integrated clone repost on user's feed
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                Toast.makeText(context, getLocalizedText("ShareToGosssey", selectedLanguage), Toast.LENGTH_SHORT).show()
                                viewModel.createPost(
                                    content = "Reposted from @${post.authorName}: ${post.content}",
                                    gradientIndex = post.gradientIndex,
                                    mediaUri = post.mediaUri,
                                    isVideo = post.isVideo
                                )
                                Toast.makeText(context, "Successfully reposted on Gosssey Feed!", Toast.LENGTH_SHORT).show()
                            }
                    ) {
                        PlatformSvgIcon(platform = "Gosssey", modifier = Modifier.size(42.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Gosssey", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00EF60))
                    }
                }
            }
        }
    }
}

fun shareTextToPlatform(context: android.content.Context, platform: String, text: String) {
    val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(android.content.Intent.EXTRA_TEXT, text)
        when (platform) {
            "WhatsApp" -> setPackage("com.whatsapp")
            "Facebook" -> setPackage("com.facebook.katana")
            "Instagram" -> setPackage("com.instagram.android")
        }
    }
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        val genericIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_TEXT, text)
        }
        val chooser = android.content.Intent.createChooser(genericIntent, "Share post via $platform")
        context.startActivity(chooser)
    }
}

// ---------------- SUB-COMPONENTS: VOICE & VIDEO SIMULATED CALL SCREENS ----------------
@Composable
fun SimulatedCallScreen(
    callState: CallState,
    onHangUp: () -> Unit
) {
    val durationText = remember(callState.durationSeconds) {
        val minutes = callState.durationSeconds / 60
        val seconds = callState.durationSeconds % 60
        String.format("%02d:%02d", minutes, seconds)
    }

    // Concentric pulsing scale animation for calling state
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    Dialog(
        onDismissRequest = onHangUp,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F0F16))
                .padding(24.dp)
        ) {
            if (callState.isVideo && callState.isConnected) {
                // simulated camera preview loop block
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Gray.copy(alpha = 0.1f))
                ) {
                    // Full-screen video peer avatar or generic beautiful camera streams
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        UserAvatar(
                            avatarIndex = callState.partner.avatarIndex,
                            avatarUri = callState.partner.avatarUri,
                            size = 140.dp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Connected in high fidelity video stream",
                            color = Color(0xFF00EF60),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Floating own camera self-preview box in top corner
                    Box(
                        modifier = Modifier
                            .size(width = 90.dp, height = 140.dp)
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black.copy(alpha = 0.6f))
                            .border(1.5.dp, Color(0xFF00EF60), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.CameraFront, contentDescription = "Self preview front lens", tint = Color.White, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Self Preview", fontSize = 8.sp, color = Color.White)
                        }
                    }
                }
            } else {
                // Voice Call Layout with concentric pulsing waves
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(200.dp)
                    ) {
                        // Pulse circle 1
                        if (!callState.isConnected) {
                            Box(
                                modifier = Modifier
                                    .size(160.dp)
                                    .scale(pulseScale)
                                    .clip(CircleShape)
                                    .background(Color(0xFF00EF60).copy(alpha = pulseAlpha))
                            )
                        }
                        
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E1E2C))
                                .border(2.dp, Color(0xFF00EF60), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            UserAvatar(
                                avatarIndex = callState.partner.avatarIndex,
                                avatarUri = callState.partner.avatarUri,
                                size = 100.dp
                            )
                        }
                    }

                    Text(
                        text = callState.partner.fullName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White
                    )

                    Text(
                        text = if (callState.isConnected) "Call Time Elapsed: $durationText" else "Gosssey ring status • Ringing...",
                        color = if (callState.isConnected) Color(0xFF00EF60) else Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Action calling buttons at bottom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 30.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Simulated Mute mic
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.MicNone, contentDescription = "Mute audio icon preview", tint = Color.White)
                }

                // HANG UP Red circle trigger!
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                        .clickable { onHangUp() }
                        .testTag("call_hang_up_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CallEnd, contentDescription = "Hang up call session", tint = Color.White, modifier = Modifier.size(30.dp))
                }

                // Speaker phone toggle
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.VolumeUp, contentDescription = "Speakerphone toggle", tint = Color.White)
                }
            }
        }
    }
}
