package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.ProductItem
import com.example.model.SubscriptionState
import com.example.model.User
import com.example.ui.components.ContCropsLogo
import com.example.ui.components.RoleBadge
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    currentUser: User,
    users: List<User>,
    products: List<ProductItem>,
    followingIds: Set<String>,
    subscription: SubscriptionState,
    feedTab: String, // "all" or "following"
    selectedType: String, // "all", "فريش", "مجمد", "مجفف"
    searchQuery: String,
    activeFilterCount: Int,
    onFeedTabChange: (String) -> Unit,
    onTypeSelect: (String) -> Unit,
    onSearchChange: (String) -> Unit,
    onOpenFilter: () -> Unit,
    onOpenMessages: () -> Unit,
    onOpenUserSwitch: () -> Unit,
    onOpenProfile: (String) -> Unit,
    onSelectProduct: (ProductItem) -> Unit,
    onToggleFollow: (String) -> Unit,
    onOpenPaywall: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ContCreamBg)
    ) {
        // Top Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ContCropsLogo(size = 36.dp, textSize = 19.sp)

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Messages button with badge
                        Box {
                            IconButton(
                                onClick = onOpenMessages,
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(ContBorder, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChatBubbleOutline,
                                    contentDescription = "الرسائل",
                                    tint = ContBlack,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-2).dp, y = 2.dp)
                                    .background(ContRed, CircleShape)
                            )
                        }

                        // Current User Avatar
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(ContBlack)
                                .clickable { onOpenUserSwitch() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentUser.avatar,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )

                            if (subscription.active) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .align(Alignment.BottomEnd)
                                        .background(ContGold, CircleShape)
                                        .border(1.5.dp, Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Search & Filter Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchChange,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        placeholder = { Text("ابحث: طماطم، البحيرة، تاجر...", fontSize = 12.sp, color = ContTextMuted) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = ContTextMuted, modifier = Modifier.size(18.dp))
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchChange("") }) {
                                    Icon(Icons.Default.Close, contentDescription = "مسح", modifier = Modifier.size(16.dp))
                                }
                            }
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF7F3ED),
                            focusedContainerColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = ContGreen.copy(alpha = 0.5f)
                        ),
                        singleLine = true
                    )

                    Button(
                        onClick = onOpenFilter,
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (activeFilterCount > 0) ContBlack else Color.White,
                            contentColor = if (activeFilterCount > 0) Color.White else ContTextPrimary
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (activeFilterCount > 0) ContBlack else ContBorder),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(16.dp))
                            Text("فلتر", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            if (activeFilterCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(ContGreen)
                                        .padding(horizontal = 6.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = activeFilterCount.toString(),
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Feed Mode: All vs Following
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFF6F1EB),
                        modifier = Modifier.width(220.dp)
                    ) {
                        Row(modifier = Modifier.padding(3.dp)) {
                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = if (feedTab == "all") ContBlack else Color.Transparent,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onFeedTabChange("all") }
                            ) {
                                Text(
                                    text = "الكل",
                                    color = if (feedTab == "all") Color.White else ContTextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = if (feedTab == "following") ContBlack else Color.Transparent,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onFeedTabChange("following") }
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Group,
                                        contentDescription = null,
                                        tint = if (feedTab == "following") Color.White else ContTextSecondary,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "المتابعون",
                                        color = if (feedTab == "following") Color.White else ContTextSecondary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Product Type Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        Pair("all", "الكل"),
                        Pair("فريش", "فريش 🌿"),
                        Pair("مجمد", "مجمد ❄️"),
                        Pair("مجفف", "مجفف ☀️")
                    ).forEach { (id, label) ->
                        val isSelected = selectedType == id
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = if (isSelected) ContGreen else Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) ContGreen else ContBorder),
                            modifier = Modifier.clickable { onTypeSelect(id) }
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else ContTextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Main List
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Stories Row
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { onOpenProfile(currentUser.id) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(listOf(ContGreen, Color(0xFFA3E635)))
                                    )
                                    .padding(2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = currentUser.avatar,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 16.sp,
                                        color = ContBlack
                                    )
                                }
                            }
                            Text(
                                text = "قصتي",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ContTextPrimary,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    val followedUsers = users.filter { followingIds.contains(it.id) }
                    items(followedUsers, key = { it.id }) { u ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { onOpenProfile(u.id) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(listOf(ContGold, ContPink))
                                    )
                                    .padding(2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = u.avatar,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color(u.role.colorHex)
                                    )
                                }
                            }
                            Text(
                                text = u.name.split(" ").firstOrNull() ?: u.name,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ContTextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .width(54.dp)
                                    .padding(top = 4.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Premium Teaser Banner if not active
            if (!subscription.active) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenPaywall() },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A150A)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ContGold.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(listOf(ContGold, ContGoldDark))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("ContCrops بريميوم ", color = Color.White, fontWeight = FontWeight.Black, fontSize = 12.sp)
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ContGold, modifier = Modifier.size(12.dp))
                                }
                                Text(
                                    text = "أسعار البورصة اللحظية + الموجز الكامل - 149ج/شهر",
                                    color = ContGold.copy(alpha = 0.85f),
                                    fontSize = 11.sp
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = ContGold
                            ) {
                                Text(
                                    text = "اشترك",
                                    color = ContBlack,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Sponsored Ad Banner (Trucking / Logistics)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEFCE8)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFFDE047),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFACC15))
                            ) {
                                Text(
                                    text = "إعلان ممول",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF854D0E),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text("Google AdMob Partner", fontSize = 9.sp, color = Color(0xFFA16207))
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            AsyncImage(
                                model = "https://images.unsplash.com/photo-1601584115197-04ecc0da31d7?w=200&h=200&fit=crop",
                                contentDescription = null,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "شركة الفجر للنقل المبرد",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 12.sp,
                                    color = Color(0xFF422006)
                                )
                                Text(
                                    text = "شاحنات تبريد 3 لـ 20 طن (-18°) من البحيرة للقاهرة",
                                    fontSize = 10.sp,
                                    color = Color(0xFF854D0E),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Button(
                                onClick = {},
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ContGreen),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("احجز", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Products Empty State
            if (products.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Inventory2, contentDescription = null, tint = ContTextMuted, modifier = Modifier.size(36.dp))
                            Text("لا توجد منتجات مطابقة للبحث", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
                            Text("جرب تغيير معايير الفلترة أو مسح كلمة البحث", fontSize = 11.sp, color = ContTextSecondary, modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                }
            }

            // Product Cards List
            items(products, key = { it.id }) { product ->
                val seller = users.find { it.id == product.userId } ?: currentUser
                val isFollowing = followingIds.contains(seller.id)
                val isMe = seller.id == currentUser.id

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                ) {
                    Column {
                        // Seller Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.clickable { onOpenProfile(seller.id) }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(seller.role.bgHex)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = seller.avatar,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 14.sp,
                                        color = Color(seller.role.colorHex)
                                    )
                                }

                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = seller.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = ContTextPrimary
                                        )
                                        if (seller.verified) {
                                            Icon(
                                                imageVector = Icons.Default.Verified,
                                                contentDescription = null,
                                                tint = ContGreen,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                        RoleBadge(role = seller.role)
                                    }

                                    Text(
                                        text = "${seller.gov} • ${product.time}",
                                        fontSize = 10.sp,
                                        color = ContTextMuted
                                    )
                                }
                            }

                            if (!isMe) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isFollowing) Color.White else ContGreen,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isFollowing) ContBorder else ContGreen),
                                    modifier = Modifier.clickable { onToggleFollow(seller.id) }
                                ) {
                                    Text(
                                        text = if (isFollowing) "تتابع ✓" else "متابعة",
                                        color = if (isFollowing) ContTextSecondary else Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        // Product Image with Overlays
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clickable { onSelectProduct(product) }
                        ) {
                            AsyncImage(
                                model = product.image,
                                contentDescription = product.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            // Type tag & cold indicator
                            Row(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                val typeColor = when (product.type) {
                                    "فريش" -> ContGreen
                                    "مجمد" -> ContBlue
                                    else -> ContOrange
                                }
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = typeColor
                                ) {
                                    Text(
                                        text = product.type,
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }

                                if (product.cold) {
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = Color.White.copy(alpha = 0.9f)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                                        ) {
                                            Icon(Icons.Default.AcUnit, contentDescription = null, tint = ContBlue, modifier = Modifier.size(11.dp))
                                            Text("مبرد", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ContBlack)
                                        }
                                    }
                                }
                            }

                            // Bottom bar: Quantity & Price
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color.Black.copy(alpha = 0.75f)
                                ) {
                                    Text(
                                        text = product.qty,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color.White,
                                    shadowElevation = 2.dp
                                ) {
                                    Text(
                                        text = product.price,
                                        color = ContBlack,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }

                        // Product Body & Action Buttons
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = product.name,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = ContTextPrimary
                            )

                            Text(
                                text = product.desc,
                                fontSize = 11.sp,
                                color = ContTextSecondary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 4.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { onSelectProduct(product) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(38.dp),
                                    shape = RoundedCornerShape(19.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = ContBlack)
                                ) {
                                    Text("عرض التفاصيل", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }

                                IconButton(
                                    onClick = onOpenMessages,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(ContCreamBg, CircleShape)
                                        .border(1.dp, ContBorder, CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ChatBubbleOutline,
                                        contentDescription = "محادثة",
                                        tint = ContBlack,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {},
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(ContCreamBg, CircleShape)
                                        .border(1.dp, ContBorder, CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = "اتصال",
                                        tint = ContGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }
}
