package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.User
import com.example.ui.theme.*

@Composable
fun UserSwitchDialog(
    users: List<User>,
    currentUserId: String,
    onDismiss: () -> Unit,
    onSelectUser: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = ContCreamBg)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "تبديل الحساب التجريبي",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = ContTextPrimary
                        )
                        Text(
                            text = "اختر أحد الحسابات لاستعراض المنصة بدوره",
                            fontSize = 11.sp,
                            color = ContTextSecondary
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .background(ContBorder, CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", modifier = Modifier.size(16.dp))
                    }
                }

                Divider(color = ContBorderLight)

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(users, key = { it.id }) { u ->
                        val isCurrent = u.id == currentUserId
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectUser(u.id)
                                    onDismiss()
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isCurrent) ContGreenLight else Color.White
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (isCurrent) 1.5.dp else 1.dp,
                                color = if (isCurrent) ContGreen else ContBorder
                            )
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
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(Color(u.role.bgHex)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = u.avatar,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(u.role.colorHex)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = u.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = ContTextPrimary
                                        )
                                        if (u.verified) {
                                            Icon(
                                                imageVector = Icons.Default.Verified,
                                                contentDescription = null,
                                                tint = ContGreen,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                        RoleBadge(role = u.role)
                                    }

                                    Text(
                                        text = "${u.location} • ${u.followers} متابع",
                                        fontSize = 11.sp,
                                        color = ContTextSecondary,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }

                                if (isCurrent) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .background(ContGreen, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
