package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatMessage
import com.example.model.OfferStatus
import com.example.model.User
import com.example.ui.components.RoleBadge
import com.example.ui.theme.*

@Composable
fun ChatScreen(
    partner: User,
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    onRespondOffer: (String, Boolean) -> Unit,
    onBack: () -> Unit
) {
    var text by remember { mutableStateOf("") }

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
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(34.dp)
                            .background(ContBorder, CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع", modifier = Modifier.size(18.dp))
                    }

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(partner.role.bgHex)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = partner.avatar,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(partner.role.colorHex)
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(partner.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            RoleBadge(role = partner.role)
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(modifier = Modifier.size(6.dp).background(ContGreen, CircleShape))
                            Text("متصل الآن • ${partner.location}", fontSize = 10.sp, color = ContGreen)
                        }
                    }
                }

                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(34.dp)
                        .background(ContCreamBg, CircleShape)
                ) {
                    Icon(Icons.Default.Call, contentDescription = "اتصال", tint = ContGreen, modifier = Modifier.size(16.dp))
                }
            }
        }

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, ContBorderLight)
                    ) {
                        Text(
                            text = "اليوم - محادثة آمنة موثقة عبر ContCrops",
                            fontSize = 10.sp,
                            color = ContTextMuted,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            items(messages, key = { it.id }) { msg ->
                val isMe = msg.isSender

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                ) {
                    Card(
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isMe) 16.dp else 4.dp,
                            bottomEnd = if (isMe) 4.dp else 16.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isMe) ContGreen else Color.White
                        ),
                        border = if (isMe) null else androidx.compose.foundation.BorderStroke(1.dp, ContBorder),
                        modifier = Modifier.widthIn(max = 280.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = msg.text,
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                color = if (isMe) Color.White else ContTextPrimary
                            )

                            // Interactive Offer Card
                            msg.offer?.let { offer ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isMe) ContGreenDark else ContCreamBg
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, ContGold.copy(alpha = 0.5f))
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Handshake,
                                                contentDescription = null,
                                                tint = ContGold,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Text(
                                                text = "عرض سعر ContCrops",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = ContGold
                                            )
                                        }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 6.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Text("المحصول", fontSize = 9.sp, color = ContTextMuted)
                                                Text(offer.cropName, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = if (isMe) Color.White else ContTextPrimary)
                                            }
                                            Column {
                                                Text("الكمية", fontSize = 9.sp, color = ContTextMuted)
                                                Text(offer.qty, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = if (isMe) Color.White else ContTextPrimary)
                                            }
                                            Column {
                                                Text("السعر", fontSize = 9.sp, color = ContTextMuted)
                                                Text(offer.pricePerTon, fontWeight = FontWeight.Black, fontSize = 11.sp, color = ContGreen)
                                            }
                                        }

                                        when (offer.status) {
                                            OfferStatus.PENDING -> {
                                                if (!isMe) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(top = 8.dp),
                                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                    ) {
                                                        OutlinedButton(
                                                            onClick = { onRespondOffer(msg.id, false) },
                                                            shape = RoundedCornerShape(14.dp),
                                                            modifier = Modifier
                                                                .weight(1f)
                                                                .height(32.dp),
                                                            contentPadding = PaddingValues(0.dp)
                                                        ) {
                                                            Text("رفض", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ContRed)
                                                        }

                                                        Button(
                                                            onClick = { onRespondOffer(msg.id, true) },
                                                            shape = RoundedCornerShape(14.dp),
                                                            colors = ButtonDefaults.buttonColors(containerColor = ContGreen),
                                                            modifier = Modifier
                                                                .weight(1f)
                                                                .height(32.dp),
                                                            contentPadding = PaddingValues(0.dp)
                                                        ) {
                                                            Text("قبول العرض", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                        }
                                                    }
                                                } else {
                                                    Surface(
                                                        shape = RoundedCornerShape(8.dp),
                                                        color = Color.White.copy(alpha = 0.15f),
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(top = 6.dp)
                                                    ) {
                                                        Text(
                                                            text = "بانتظار رد المشتري...",
                                                            color = Color.White,
                                                            fontSize = 10.sp,
                                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                                            modifier = Modifier.padding(4.dp)
                                                        )
                                                    }
                                                }
                                            }

                                            OfferStatus.ACCEPTED -> {
                                                Surface(
                                                    shape = RoundedCornerShape(8.dp),
                                                    color = ContGreenLight,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(top = 6.dp)
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(4.dp),
                                                        horizontalArrangement = Arrangement.Center,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ContGreen, modifier = Modifier.size(12.dp))
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text("تم قبول العرض بنجاح ✓", color = ContGreenDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                    }
                                                }
                                            }

                                            OfferStatus.REJECTED -> {
                                                Surface(
                                                    shape = RoundedCornerShape(8.dp),
                                                    color = ContRedLight,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(top = 6.dp)
                                                ) {
                                                    Text(
                                                        text = "تم رفض العرض",
                                                        color = ContRed,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                                        modifier = Modifier.padding(4.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Text(
                                text = msg.time,
                                fontSize = 9.sp,
                                color = if (isMe) Color.White.copy(alpha = 0.7f) else ContTextMuted,
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Input Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(38.dp)
                        .background(ContCreamBg, CircleShape)
                ) {
                    Icon(Icons.Default.AttachFile, contentDescription = "مرفق", tint = ContTextSecondary)
                }

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("اكتب رسالة أو تفاصيل عرض...", fontSize = 12.sp, color = ContTextMuted) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF7F3ED),
                        focusedContainerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = ContGreen.copy(alpha = 0.5f)
                    ),
                    singleLine = true
                )

                IconButton(
                    onClick = {
                        if (text.isNotBlank()) {
                            onSendMessage(text)
                            text = ""
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(ContGreen, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "إرسال",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
