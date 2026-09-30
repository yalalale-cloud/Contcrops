package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.ProductItem
import com.example.model.User
import com.example.ui.components.RoleBadge
import com.example.ui.theme.*

@Composable
fun ProductDetailScreen(
    product: ProductItem,
    seller: User,
    isFollowing: Boolean,
    onBack: () -> Unit,
    onOpenSellerProfile: (String) -> Unit,
    onToggleFollow: (String) -> Unit,
    onOpenChat: (User) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ContCreamBg)
    ) {
        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Photo Hero
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                AsyncImage(
                    model = product.image,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Back and Share buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.White.copy(alpha = 0.9f), CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "رجوع", tint = ContBlack)
                    }

                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.White.copy(alpha = 0.9f), CircleShape)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "مشاركة", tint = ContBlack)
                    }
                }

                // Bottom badge overlays
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ContBlack.copy(alpha = 0.8f)
                    ) {
                        Text(
                            text = product.qty,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ContGreen
                    ) {
                        Text(
                            text = product.type,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    if (product.cold) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ContBlue
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.AcUnit, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                Text("مبرد", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Name & Price
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = product.name,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = ContTextPrimary
                        )

                        Text(
                            text = product.price,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = ContGreen,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Row(
                            modifier = Modifier.padding(top = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Place, contentDescription = null, tint = ContTextMuted, modifier = Modifier.size(13.dp))
                            Text(product.location, fontSize = 11.sp, color = ContTextSecondary)
                            Text("•", fontSize = 11.sp, color = ContTextMuted)
                            Text("نشر: ${product.time}", fontSize = 11.sp, color = ContTextMuted)
                        }
                    }
                }

                // Seller Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.clickable { onOpenSellerProfile(seller.id) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(seller.role.bgHex)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = seller.avatar,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(seller.role.colorHex)
                                )
                            }

                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(seller.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    if (seller.verified) {
                                        Icon(Icons.Default.Verified, contentDescription = null, tint = ContGreen, modifier = Modifier.size(14.dp))
                                    }
                                    RoleBadge(role = seller.role)
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.padding(top = 2.dp)
                                ) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = ContGold, modifier = Modifier.size(12.dp))
                                    Text("${seller.rating} (${seller.reviews} تقييم)", fontSize = 10.sp, color = ContTextSecondary)
                                }
                            }
                        }

                        Button(
                            onClick = { onToggleFollow(seller.id) },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isFollowing) ContCreamBg else ContGreen,
                                contentColor = if (isFollowing) ContTextPrimary else Color.White
                            ),
                            border = if (isFollowing) androidx.compose.foundation.BorderStroke(1.dp, ContBorder) else null,
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(if (isFollowing) "تتابع ✓" else "متابعة", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Specifications
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("مواصفات التوريد", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = ContCreamBg)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("تاريخ الحصاد", fontSize = 10.sp, color = ContTextMuted)
                                    Text(product.harvest, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
                                }
                            }

                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = ContCreamBg)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("الشحن والنقل", fontSize = 10.sp, color = ContTextMuted)
                                    Text(if (product.cold) "شاحنة تبريد -18" else "عادي", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
                                }
                            }
                        }

                        Text(
                            text = product.desc,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = ContTextPrimary,
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth()
                                .background(ContCreamBg, RoundedCornerShape(12.dp))
                                .border(1.dp, ContBorderLight, RoundedCornerShape(12.dp))
                                .padding(10.dp)
                        )
                    }
                }
            }
        }

        // Bottom CTA Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { onOpenChat(seller) },
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                ) {
                    Icon(Icons.Default.Handshake, contentDescription = null, tint = ContBlack)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ربط كسمسار", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = ContBlack)
                }

                Button(
                    onClick = { onOpenChat(seller) },
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier
                        .weight(1.3f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ContGreen)
                ) {
                    Text("تقديم عرض سعر", fontWeight = FontWeight.Black, fontSize = 13.sp)
                }
            }
        }
    }
}
