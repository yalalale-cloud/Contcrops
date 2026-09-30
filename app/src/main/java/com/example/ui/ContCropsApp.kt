package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ContCropsRepository
import com.example.model.FilterState
import com.example.model.ProductItem
import com.example.model.User
import com.example.ui.components.FilterBottomSheet
import com.example.ui.components.PaywallModal
import com.example.ui.components.UserSwitchDialog
import com.example.ui.screens.*
import com.example.ui.theme.*

@Composable
fun ContCropsApp(
    repository: ContCropsRepository = remember { ContCropsRepository() }
) {
    // Force RTL layout direction for pristine Arabic UX
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        val currentUserId by repository.currentUserId.collectAsState()
        val users by repository.users.collectAsState()
        val followingIds by repository.followingIds.collectAsState()
        val products by repository.products.collectAsState()
        val marketPrices by repository.marketPrices.collectAsState()
        val posts by repository.posts.collectAsState()
        val requests by repository.requests.collectAsState()
        val subscription by repository.subscription.collectAsState()
        val filterState by repository.filterState.collectAsState()
        val savedFilters by repository.savedFilters.collectAsState()
        val chatMessages by repository.chatMessages.collectAsState()

        val currentUser = remember(currentUserId, users) {
            users.find { it.id == currentUserId } ?: users.first()
        }

        // Navigation state
        var currentTab by remember { mutableStateOf("home") } // "home", "prices", "mojaz", "requests", "profile"
        var feedTab by remember { mutableStateOf("all") } // "all", "following"
        var selectedType by remember { mutableStateOf("all") }
        var searchQuery by remember { mutableStateOf("") }

        // Overlays & Sub-screens
        var selectedProductDetail by remember { mutableStateOf<ProductItem?>(null) }
        var activeChatPartner by remember { mutableStateOf<User?>(null) }
        var showAddProductScreen by remember { mutableStateOf(false) }
        var viewingProfileUserId by remember { mutableStateOf<String?>(null) }

        // Modals
        var showFilterSheet by remember { mutableStateOf(false) }
        var showPaywallModal by remember { mutableStateOf(false) }
        var showUserSwitchModal by remember { mutableStateOf(false) }

        // Filter evaluation
        val filteredProducts = remember(products, filterState, searchQuery, feedTab, selectedType, followingIds) {
            products.filter { p ->
                val seller = users.find { it.id == p.userId } ?: currentUser

                // Following tab check
                if (feedTab == "following" && !followingIds.contains(seller.id)) {
                    return@filter false
                }

                // Type check from horizontal chips
                if (selectedType != "all" && p.type != selectedType) {
                    return@filter false
                }

                // Search query
                if (searchQuery.isNotBlank()) {
                    val q = searchQuery.trim().lowercase()
                    val matchName = p.name.lowercase().contains(q)
                    val matchLoc = p.location.lowercase().contains(q)
                    val matchSeller = seller.name.lowercase().contains(q)
                    if (!matchName && !matchLoc && !matchSeller) return@filter false
                }

                // Region filter
                if (filterState.regions.isNotEmpty()) {
                    val fullLoc = "${p.location} ${seller.location} ${seller.gov}".lowercase()
                    val matchesRegion = filterState.regions.any { reg ->
                        if (reg == "الكل") true
                        else if (reg == "بدر الصناعية") fullLoc.contains("بدر") || fullLoc.contains("صناعية")
                        else fullLoc.contains(reg.lowercase())
                    }
                    if (!matchesRegion) return@filter false
                }

                // Quantity filter
                val qtyInt = p.qty.filter { it.isDigit() }.toIntOrNull() ?: 0
                if (qtyInt !in filterState.qtyMin..filterState.qtyMax) {
                    return@filter false
                }

                // Price filter
                val priceKg = p.priceNum / 1000
                if (priceKg !in filterState.priceMin..filterState.priceMax) {
                    return@filter false
                }

                // Rating filter
                if (filterState.rating > 0 && seller.rating < filterState.rating) {
                    return@filter false
                }

                // Product types from sheet
                if (filterState.productTypes.isNotEmpty() && !filterState.productTypes.contains(p.type)) {
                    return@filter false
                }

                // Role filter
                if (filterState.roles.isNotEmpty() && !filterState.roles.contains(seller.role)) {
                    return@filter false
                }

                // Delivery
                if (filterState.delivery.isNotEmpty()) {
                    val matchesDelivery = filterState.delivery.any { d ->
                        when (d) {
                            "ready" -> true
                            "cold" -> p.cold
                            "farm" -> !p.cold
                            else -> true
                        }
                    }
                    if (!matchesDelivery) return@filter false
                }

                true
            }.sortedWith { a, b ->
                val sellerA = users.find { it.id == a.userId } ?: currentUser
                val sellerB = users.find { it.id == b.userId } ?: currentUser
                when (filterState.sort) {
                    "cheapest" -> a.priceNum.compareTo(b.priceNum)
                    "largestQty" -> {
                        val qtyB = b.qty.filter { it.isDigit() }.toIntOrNull() ?: 0
                        val qtyA = a.qty.filter { it.isDigit() }.toIntOrNull() ?: 0
                        qtyB.compareTo(qtyA)
                    }
                    "highestRated" -> sellerB.rating.compareTo(sellerA.rating)
                    "nearest" -> {
                        val nearA = if (sellerA.gov == currentUser.gov) 0 else 1
                        val nearB = if (sellerB.gov == currentUser.gov) 0 else 1
                        nearA.compareTo(nearB)
                    }
                    else -> b.id.compareTo(a.id)
                }
            }
        }

        // Active filter badge count
        val activeFilterCount = remember(filterState) {
            var count = 0
            if (filterState.regions.isNotEmpty()) count++
            if (filterState.qtyQuick != "all" || filterState.qtyMin != 1 || filterState.qtyMax != 100) count++
            if (filterState.priceQuick != "all" || filterState.priceMin != 0 || filterState.priceMax != 100) count++
            if (filterState.rating > 0) count++
            if (filterState.productTypes.isNotEmpty()) count++
            if (filterState.roles.isNotEmpty()) count++
            if (filterState.delivery.isNotEmpty()) count++
            if (filterState.sort != "latest") count++
            count
        }

        // BackHandler logic
        BackHandler(
            enabled = selectedProductDetail != null || activeChatPartner != null || showAddProductScreen || viewingProfileUserId != null || currentTab != "home"
        ) {
            when {
                selectedProductDetail != null -> selectedProductDetail = null
                activeChatPartner != null -> activeChatPartner = null
                showAddProductScreen -> showAddProductScreen = false
                viewingProfileUserId != null -> viewingProfileUserId = null
                currentTab != "home" -> currentTab = "home"
            }
        }

        Scaffold(
            bottomBar = {
                // Bottom navigation bar is visible unless on full sub-screens
                if (selectedProductDetail == null && activeChatPartner == null && !showAddProductScreen) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White.copy(alpha = 0.96f),
                        shadowElevation = 10.dp
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .navigationBarsPadding()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 1. Home
                                NavigationItem(
                                    icon = Icons.Default.Home,
                                    label = "الرئيسية",
                                    isSelected = currentTab == "home" && viewingProfileUserId == null,
                                    onClick = {
                                        currentTab = "home"
                                        viewingProfileUserId = null
                                    }
                                )

                                // 2. Prices
                                NavigationItem(
                                    icon = Icons.Default.TrendingUp,
                                    label = "الأسعار",
                                    isSelected = currentTab == "prices",
                                    badge = "جديد",
                                    onClick = {
                                        currentTab = "prices"
                                        viewingProfileUserId = null
                                    }
                                )

                                // Center Spacer for FAB
                                Spacer(modifier = Modifier.width(48.dp))

                                // 3. Mojaz
                                NavigationItem(
                                    icon = Icons.Default.Newspaper,
                                    label = "الموجز",
                                    isSelected = currentTab == "mojaz",
                                    onClick = {
                                        currentTab = "mojaz"
                                        viewingProfileUserId = null
                                    }
                                )

                                // 4. Requests or Profile
                                NavigationItem(
                                    icon = Icons.Default.Person,
                                    label = "حسابي",
                                    isSelected = currentTab == "profile" || (viewingProfileUserId == currentUser.id),
                                    onClick = {
                                        currentTab = "profile"
                                        viewingProfileUserId = null
                                    }
                                )
                            }

                            // Center Floating Action Button
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .offset(y = (-18).dp)
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(ContGreen)
                                    .border(3.dp, Color.White, CircleShape)
                                    .clickable { showAddProductScreen = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "إضافة محصول",
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Main Switch Screen
                when (currentTab) {
                    "home" -> HomeScreen(
                        currentUser = currentUser,
                        users = users,
                        products = filteredProducts,
                        followingIds = followingIds,
                        subscription = subscription,
                        feedTab = feedTab,
                        selectedType = selectedType,
                        searchQuery = searchQuery,
                        activeFilterCount = activeFilterCount,
                        onFeedTabChange = { feedTab = it },
                        onTypeSelect = { selectedType = it },
                        onSearchChange = { searchQuery = it },
                        onOpenFilter = { showFilterSheet = true },
                        onOpenMessages = {
                            val partner = users.firstOrNull { it.id != currentUser.id } ?: currentUser
                            activeChatPartner = partner
                        },
                        onOpenUserSwitch = { showUserSwitchModal = true },
                        onOpenProfile = { userId -> viewingProfileUserId = userId },
                        onSelectProduct = { prod -> selectedProductDetail = prod },
                        onToggleFollow = { targetId -> repository.toggleFollow(targetId) },
                        onOpenPaywall = { showPaywallModal = true }
                    )

                    "prices" -> PricesScreen(
                        marketPrices = marketPrices,
                        subscription = subscription,
                        onOpenPaywall = { showPaywallModal = true }
                    )

                    "mojaz" -> MojazFeedScreen(
                        currentUser = currentUser,
                        users = users,
                        posts = posts,
                        subscription = subscription,
                        onAddPost = { text -> repository.addPost(text) },
                        onToggleLike = { id -> repository.toggleLikePost(id) },
                        onOpenProfile = { userId -> viewingProfileUserId = userId },
                        onOpenPaywall = { showPaywallModal = true }
                    )

                    "requests" -> RequestsScreen(
                        requests = requests,
                        users = users,
                        onOpenMessages = { partner -> activeChatPartner = partner },
                        onSubmitOffer = { req ->
                            val partner = users.find { it.id == req.userId } ?: currentUser
                            activeChatPartner = partner
                        }
                    )

                    "profile" -> ProfileScreen(
                        user = currentUser,
                        isSelf = true,
                        isFollowing = false,
                        isSubscribed = subscription.active,
                        userProducts = products.filter { it.userId == currentUser.id },
                        userRequests = requests.filter { it.userId == currentUser.id },
                        onToggleFollow = {},
                        onOpenMessages = {},
                        onSelectProduct = { prod -> selectedProductDetail = prod },
                        onSwitchUser = { showUserSwitchModal = true },
                        onOpenPaywall = { showPaywallModal = true }
                    )
                }

                // Sub-Screen 1: Other User's Profile
                viewingProfileUserId?.let { userId ->
                    val profileUser = users.find { it.id == userId } ?: currentUser
                    ProfileScreen(
                        user = profileUser,
                        isSelf = profileUser.id == currentUser.id,
                        isFollowing = followingIds.contains(profileUser.id),
                        isSubscribed = profileUser.id == currentUser.id && subscription.active,
                        userProducts = products.filter { it.userId == profileUser.id },
                        userRequests = requests.filter { it.userId == profileUser.id },
                        onToggleFollow = { repository.toggleFollow(it) },
                        onOpenMessages = { activeChatPartner = it },
                        onSelectProduct = { selectedProductDetail = it },
                        onSwitchUser = { showUserSwitchModal = true },
                        onOpenPaywall = { showPaywallModal = true }
                    )
                }

                // Sub-Screen 2: Product Detail
                selectedProductDetail?.let { product ->
                    val seller = users.find { it.id == product.userId } ?: currentUser
                    ProductDetailScreen(
                        product = product,
                        seller = seller,
                        isFollowing = followingIds.contains(seller.id),
                        onBack = { selectedProductDetail = null },
                        onOpenSellerProfile = { viewingProfileUserId = it },
                        onToggleFollow = { repository.toggleFollow(it) },
                        onOpenChat = { activeChatPartner = it }
                    )
                }

                // Sub-Screen 3: Chat Room
                activeChatPartner?.let { partner ->
                    ChatScreen(
                        partner = partner,
                        messages = chatMessages,
                        onSendMessage = { txt -> repository.sendChatMessage(txt) },
                        onRespondOffer = { msgId, accept -> repository.respondToOffer(msgId, accept) },
                        onBack = { activeChatPartner = null }
                    )
                }

                // Sub-Screen 4: Add Product
                if (showAddProductScreen) {
                    AddProductScreen(
                        currentUser = currentUser,
                        onProductAdded = { name, type, qty, price, desc, cold ->
                            repository.addProduct(name, type, qty, price, desc, cold)
                            showAddProductScreen = false
                        },
                        onDismiss = { showAddProductScreen = false }
                    )
                }

                // Modals
                if (showFilterSheet) {
                    FilterBottomSheet(
                        currentFilter = filterState,
                        savedFilters = savedFilters,
                        matchCount = filteredProducts.size,
                        onDismiss = { showFilterSheet = false },
                        onApply = { newFilter ->
                            repository.updateFilter(newFilter)
                            showFilterSheet = false
                        },
                        onSaveCurrent = { name ->
                            repository.saveCurrentFilter(name)
                        }
                    )
                }

                if (showPaywallModal) {
                    PaywallModal(
                        initialPlan = "bundle",
                        onDismiss = { showPaywallModal = false },
                        onSubscribe = { plan ->
                            repository.setSubscription(true, plan)
                            showPaywallModal = false
                        }
                    )
                }

                if (showUserSwitchModal) {
                    UserSwitchDialog(
                        users = users,
                        currentUserId = currentUserId,
                        onDismiss = { showUserSwitchModal = false },
                        onSelectUser = { uid -> repository.setCurrentUser(uid) }
                    )
                }
            }
        }
    }
}

@Composable
private fun NavigationItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    badge: String? = null,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) ContGreen else ContTextMuted,
                modifier = Modifier.size(22.dp)
            )

            if (badge != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-4).dp)
                        .background(ContGold, CircleShape)
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(text = badge, fontSize = 7.sp, color = Color.White, fontWeight = FontWeight.Black)
                }
            }
        }

        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal,
            color = if (isSelected) ContGreen else ContTextMuted,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
