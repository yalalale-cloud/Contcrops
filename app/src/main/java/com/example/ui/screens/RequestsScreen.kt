package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Place
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
import com.example.model.SupplyRequest
import com.example.model.User
import com.example.ui.theme.*

@Composable
fun RequestsScreen(
    requests: List<SupplyRequest>,
    users: List<User>,
    onOpenMessages: (User) -> Unit,
    onSubmitOffer: (SupplyRequest) -> Unit
) {
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
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(ContBlack, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Description, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Column {
                        Text(
                            text = "طلبات التوريد والعقود",
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = ContTextPrimary
                        )
                        Text(
                            text = "شركات ومصانع تطلب كميات كبيرة للتصدير والتصنيع",
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
            items(requests, key = { it.id }) { req ->
                val requester = users.find { it.id == req.userId }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
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
                                        .clip(CircleShape)
                                        .background(ContBlack),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = req.company.firstOrNull()?.toString() ?: "ش",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }

                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(req.company, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        if (req.verified) {
                                            Icon(
                                                imageVector = Icons.Default.Verified,
                                                contentDescription = null,
                                                tint = ContGreen,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                    Text(req.time, fontSize = 10.sp, color = ContTextMuted)
                                }
                            }

                            Surface(
                                shape = CircleShape,
                                color = ContCreamBg
                            ) {
                                Text(
                                    text = "${req.offers} عروض مقدمة",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ContTextSecondary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Text(
                            text = req.title,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            color = ContTextPrimary,
                            modifier = Modifier.padding(top = 10.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(shape = RoundedCornerShape(12.dp), color = ContBlack) {
                                Text(
                                    text = req.qty,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            Surface(shape = RoundedCornerShape(12.dp), color = ContGreenLight) {
                                Text(
                                    text = req.budget,
                                    color = ContGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White,
                                border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(Icons.Default.Place, contentDescription = null, tint = ContTextSecondary, modifier = Modifier.size(11.dp))
                                    Text(req.location, fontSize = 10.sp, color = ContTextSecondary)
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 14.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { requester?.let { onOpenMessages(it) } },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp),
                                shape = RoundedCornerShape(20.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                            ) {
                                Text("مراسلة", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = ContTextPrimary)
                            }

                            Button(
                                onClick = { onSubmitOffer(req) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp),
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ContGreen)
                            ) {
                                Text("تقديم عرض توريد", fontWeight = FontWeight.Bold, fontSize = 12.sp)
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
