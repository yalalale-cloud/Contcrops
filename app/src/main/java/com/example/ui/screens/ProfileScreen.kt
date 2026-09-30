package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.ProductItem
import com.example.model.SupplyRequest
import com.example.model.User
import com.example.ui.components.RoleBadge
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    user: User,
    isSelf: Boolean,
    isFollowing: Boolean,
    isSubscribed: Boolean,
    userProducts: List<ProductItem>,
    userRequests: List<SupplyRequest>,
    onToggleFollow: (String) -> Unit,
    onOpenMessages: (User) -> Unit,
    onSelectProduct: (ProductItem) -> Unit,
    onSwitchUser: () -> Unit,
    onOpenPaywall: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("products") } // "products", "requests", "reviews"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ContCreamBg)
    ) {
        // Top Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(user.name, fontWeight = FontWeight.Black, fontSize = 16.sp, color = ContTextPrimary)
                    if (user.verified) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = ContGreen, modifier = Modifier.size(16.dp))
                    }
                    if (isSelf && isSubscribed) {
                        Surface(
                            shape = CircleShape,
                            color = ContGold
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                                Text("مشترك مميز", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                if (isSelf) {
                    Button(
                        onClick = onSwitchUser,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ContCreamBg, contentColor = ContTextPrimary),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(Icons.Default.SwitchAccount, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("تبديل الحساب", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Profile Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(Color(user.role.bgHex)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.avatar,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(user.role.colorHex)
                        )
                    }

                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(userProducts.size.toString(), fontWeight = FontWeight.Black, fontSize = 16.sp)
                            Text("منتجات", fontSize = 11.sp, color = ContTextSecondary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(user.followers.toString(), fontWeight = FontWeight.Black, fontSize = 16.sp)
                            Text("متابعون", fontSize = 11.sp, color = ContTextSecondary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(user.following.toString(), fontWeight = FontWeight.Black, fontSize = 16.sp)
                            Text("يتابع", fontSize = 11.sp, color = ContTextSecondary)
                        }
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(user.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    RoleBadge(role = user.role)
                }

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.Place, contentDescription = null, tint = ContTextMuted, modifier = Modifier.size(12.dp))
                    Text(user.location, fontSize = 11.sp, color = ContTextSecondary)
                    Text("•", fontSize = 11.sp, color = ContTextMuted)
                    Icon(Icons.Default.Star, contentDescription = null, tint = ContGold, modifier = Modifier.size(12.dp))
                    Text("${user.rating} (${user.reviews} تقييم)", fontSize = 11.sp, color = ContTextSecondary)
                }

                Text(
                    text = user.bio,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = ContTextPrimary,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // Actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!isSelf) {
                        Button(
                            onClick = { onToggleFollow(user.id) },
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp),
                            shape = RoundedCornerShape(19.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isFollowing) ContCreamBg else ContBlack,
                                contentColor = if (isFollowing) ContTextPrimary else Color.White
                            ),
                            border = if (isFollowing) androidx.compose.foundation.BorderStroke(1.dp, ContBorder) else null
                        ) {
                            Text(if (isFollowing) "تتابع ✓" else "متابعة", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        Button(
                            onClick = { onOpenMessages(user) },
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp),
                            shape = RoundedCornerShape(19.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ContGreen)
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("رسالة", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    } else {
                        OutlinedButton(
                            onClick = onOpenPaywall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(38.dp),
                            shape = RoundedCornerShape(19.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSubscribed) ContGoldBg else ContCreamBg
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSubscribed) ContGold else ContBorder)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = ContGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isSubscribed) "إدارة اشتراك بريميوم (نشط)" else "ترقية إلى ContCrops بريميوم",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = ContBlack
                            )
                        }
                    }
                }
            }
        }

        // Tabs (Products, Requests, Reviews)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            TabRow(
                selectedTabIndex = when (selectedTab) {
                    "requests" -> 1
                    "reviews" -> 2
                    else -> 0
                },
                containerColor = Color.White,
                contentColor = ContBlack,
                divider = { Divider(color = ContBorderLight) }
            ) {
                Tab(
                    selected = selectedTab == "products",
                    onClick = { selectedTab = "products" },
                    text = { Text("المنتجات (${userProducts.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == "requests",
                    onClick = { selectedTab = "requests" },
                    text = { Text("الطلبات (${userRequests.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
                Tab(
                    selected = selectedTab == "reviews",
                    onClick = { selectedTab = "reviews" },
                    text = { Text("التقييمات", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
            }
        }

        // Tab Content
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                "products" -> {
                    if (userProducts.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("لا توجد منتجات منشورة حتى الآن", color = ContTextMuted, fontSize = 12.sp)
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            contentPadding = PaddingValues(4.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(userProducts, key = { it.id }) { product ->
                                Box(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .padding(2.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onSelectProduct(product) }
                                ) {
                                    AsyncImage(
                                        model = product.image,
                                        contentDescription = product.name,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )

                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Black.copy(alpha = 0.25f))
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color.Black.copy(alpha = 0.7f),
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .padding(4.dp)
                                    ) {
                                        Text(
                                            text = product.name,
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                "requests" -> {
                    if (userRequests.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("لا توجد طلبات توريد نشطة", color = ContTextMuted, fontSize = 12.sp)
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(userRequests) { req ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(req.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Row(
                                            modifier = Modifier.padding(top = 6.dp),
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Surface(shape = RoundedCornerShape(8.dp), color = ContBlack) {
                                                Text(req.qty, color = Color.White, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                            }
                                            Surface(shape = RoundedCornerShape(8.dp), color = ContGreenLight) {
                                                Text(req.budget, color = ContGreen, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "reviews" -> {
                    LazyColumn(
                        contentPadding = PaddingValues(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val sampleReviews = listOf(
                            Triple("أحمد - شركة تصدير", "تبريد ممتاز وفرز احترافي مطابق للمواصفات القياسية الأوروبية.", 5),
                            Triple("محمد تاجر جملة", "سعر عادل وتسليم في الموعد المتفق عليه بدون تأخير.", 5),
                            Triple("مصنع النور للأغذية", "تعامل راقٍ وشفافية في نسب الهالك والفرز.", 4)
                        )

                        items(sampleReviews) { (author, text, stars) ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(author, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Row {
                                            repeat(stars) {
                                                Icon(Icons.Default.Star, contentDescription = null, tint = ContGold, modifier = Modifier.size(13.dp))
                                            }
                                        }
                                    }
                                    Text(text, fontSize = 11.sp, color = ContTextSecondary, modifier = Modifier.padding(top = 4.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
