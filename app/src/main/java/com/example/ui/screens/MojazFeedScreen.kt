package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.FeedPost
import com.example.model.SubscriptionState
import com.example.model.User
import com.example.ui.components.RoleBadge
import com.example.ui.theme.*

@Composable
fun MojazFeedScreen(
    currentUser: User,
    users: List<User>,
    posts: List<FeedPost>,
    subscription: SubscriptionState,
    onAddPost: (String) -> Unit,
    onToggleLike: (String) -> Unit,
    onOpenProfile: (String) -> Unit,
    onOpenPaywall: () -> Unit
) {
    var newPostText by remember { mutableStateOf("") }
    val hasMojazAccess = subscription.active && (subscription.plan == "mojaz" || subscription.plan == "bundle")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ContCreamBg)
    ) {
        // Header
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(ContBlack, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Newspaper, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "الموجز الزراعي",
                                fontWeight = FontWeight.Black,
                                fontSize = 17.sp,
                                color = ContTextPrimary
                            )
                            Surface(
                                shape = CircleShape,
                                color = ContGold
                            ) {
                                Text(
                                    text = "PREMIUM",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "أخبار التجار والفرص التصديرية أولاً بأول",
                            fontSize = 11.sp,
                            color = ContTextSecondary
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Post Creator Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(ContBlack),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = currentUser.avatar,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            OutlinedTextField(
                                value = newPostText,
                                onValueChange = { newPostText = it },
                                placeholder = {
                                    Text("اكتب خبراً أو معلومة تفيد مجتمع التجار...", fontSize = 12.sp, color = ContTextMuted)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 60.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = Color(0xFFF7F3ED),
                                    focusedContainerColor = Color.White,
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedBorderColor = ContGreen.copy(alpha = 0.4f)
                                )
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                IconButton(
                                    onClick = {},
                                    modifier = Modifier
                                        .size(34.dp)
                                        .background(ContCreamBg, CircleShape)
                                ) {
                                    Icon(Icons.Default.Image, contentDescription = "صورة", tint = ContGreen, modifier = Modifier.size(18.dp))
                                }
                                IconButton(
                                    onClick = {},
                                    modifier = Modifier
                                        .size(34.dp)
                                        .background(ContCreamBg, CircleShape)
                                ) {
                                    Icon(Icons.Default.Videocam, contentDescription = "فيديو", tint = ContGoldDark, modifier = Modifier.size(18.dp))
                                }
                            }

                            Button(
                                onClick = {
                                    if (newPostText.isNotBlank()) {
                                        onAddPost(newPostText)
                                        newPostText = ""
                                    }
                                },
                                enabled = newPostText.isNotBlank(),
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ContGreen,
                                    disabledContainerColor = ContBorder
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("نشر في الموجز", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Posts Stream
            itemsIndexed(posts, key = { _, post -> post.id }) { index, post ->
                val author = users.find { it.id == post.userId } ?: currentUser
                val isLocked = !hasMojazAccess && index >= 3

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                                .then(if (isLocked) Modifier.blur(5.dp) else Modifier)
                        ) {
                            // Author Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.clickable { onOpenProfile(author.id) }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Color(author.role.bgHex)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = author.avatar,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 13.sp,
                                            color = Color(author.role.colorHex)
                                        )
                                    }

                                    Column {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = author.name,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = ContTextPrimary
                                            )
                                            if (author.verified) {
                                                Icon(
                                                    imageVector = Icons.Default.Verified,
                                                    contentDescription = null,
                                                    tint = ContGreen,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                            }
                                            RoleBadge(role = author.role)
                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            modifier = Modifier.padding(top = 2.dp)
                                        ) {
                                            Text(text = post.time, fontSize = 10.sp, color = ContTextMuted)
                                            Text("•", fontSize = 10.sp, color = ContTextMuted)
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                                            ) {
                                                Icon(Icons.Default.Visibility, contentDescription = null, tint = ContTextMuted, modifier = Modifier.size(10.dp))
                                                Text(post.views.toString(), fontSize = 10.sp, color = ContTextMuted)
                                            }
                                        }
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = ContCreamBg
                                ) {
                                    Text(
                                        text = post.tag,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ContTextSecondary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            // Post text
                            Text(
                                text = post.text,
                                fontSize = 13.sp,
                                lineHeight = 20.sp,
                                color = ContTextPrimary,
                                modifier = Modifier.padding(top = 10.dp)
                            )

                            // Optional photo
                            if (post.image != null) {
                                AsyncImage(
                                    model = post.image,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .padding(top = 10.dp)
                                        .clip(RoundedCornerShape(14.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Divider(modifier = Modifier.padding(top = 12.dp, bottom = 8.dp), color = ContBorderLight)

                            // Action Bar (Likes, comments, shares)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    // Like button
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier.clickable { onToggleLike(post.id) }
                                    ) {
                                        Icon(
                                            imageVector = if (post.liked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                            contentDescription = "إعجاب",
                                            tint = if (post.liked) ContRed else ContTextSecondary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = post.likes.toString(),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (post.liked) ContRed else ContTextSecondary
                                        )
                                    }

                                    // Comments
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.ChatBubbleOutline,
                                            contentDescription = "تعليق",
                                            tint = ContTextSecondary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = post.comments.toString(),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ContTextSecondary
                                        )
                                    }

                                    // Shares
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Share,
                                            contentDescription = "مشاركة",
                                            tint = ContTextSecondary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = post.shares.toString(),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ContTextSecondary
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = {},
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.BookmarkBorder,
                                        contentDescription = "حفظ",
                                        tint = ContTextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        // Premium Lock Overlay
                        if (isLocked) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color.White.copy(alpha = 0.82f))
                                    .clickable { onOpenPaywall() }
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(ContBlack),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Lock, contentDescription = null, tint = ContGold, modifier = Modifier.size(20.dp))
                                    }

                                    Text(
                                        text = "الموجز الكامل حصري لمشتركي بريميوم",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp,
                                        color = ContTextPrimary
                                    )

                                    Button(
                                        onClick = onOpenPaywall,
                                        colors = ButtonDefaults.buttonColors(containerColor = ContBlack),
                                        shape = RoundedCornerShape(20.dp),
                                        modifier = Modifier.height(36.dp)
                                    ) {
                                        Text("اشترك الآن لفتح الموجز - 99ج", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
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
