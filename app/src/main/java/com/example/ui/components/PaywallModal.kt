package com.example.ui.components

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*

@Composable
fun PaywallModal(
    initialPlan: String = "bundle",
    onDismiss: () -> Unit,
    onSubscribe: (String) -> Unit
) {
    var selectedPlan by remember { mutableStateOf(initialPlan) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.75f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.92f),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = ContCreamBg)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Header with dark gradient
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF1A150A), Color(0xFF2A1F0A))
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(ContGold, ContGoldDark)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color.White.copy(alpha = 0.15f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "إغلاق",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "ContCrops ",
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "بريميوم",
                                    color = ContGold,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            Text(
                                text = "انضم لـ 2,400+ تاجر ومزارع بياخدوا قرارات أذكى كل يوم وتداول واثق بأحدث أسعار البورصة الزراعية",
                                color = Color.White.copy(alpha = 0.75f),
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                modifier = Modifier.padding(top = 6.dp)
                            )

                            Row(
                                modifier = Modifier.padding(top = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color.White.copy(alpha = 0.12f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(Icons.Default.Group, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                        Text("2.4k مشترك", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Surface(
                                    shape = CircleShape,
                                    color = ContGold.copy(alpha = 0.2f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = ContGold, modifier = Modifier.size(12.dp))
                                        Text("4.9 تقييم", color = ContGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // Content Scroll
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Plan cards
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val plans = listOf(
                                Triple("prices", "الأسعار فقط", "99ج"),
                                Triple("bundle", "الباقة الكاملة", "149ج"),
                                Triple("mojaz", "الموجز فقط", "99ج")
                            )

                            plans.forEach { (id, title, price) ->
                                val isSelected = selectedPlan == id
                                val isBundle = id == "bundle"

                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedPlan = id },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) ContGoldBg else Color.White
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) ContGold else ContBorder
                                    )
                                ) {
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        if (isBundle) {
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.TopCenter)
                                                    .background(ContGold, RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                                            ) {
                                                Text(
                                                    text = "الأكثر طلباً 🔥",
                                                    color = Color.White,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Black
                                                )
                                            }
                                        }

                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = if (isBundle) 14.dp else 10.dp, bottom = 10.dp, start = 8.dp, end = 8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = title,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = ContTextPrimary,
                                                textAlign = TextAlign.Center
                                            )

                                            Row(
                                                verticalAlignment = Alignment.Bottom,
                                                modifier = Modifier.padding(top = 4.dp)
                                            ) {
                                                Text(
                                                    text = price,
                                                    fontWeight = FontWeight.Black,
                                                    fontSize = 15.sp,
                                                    color = ContBlack
                                                )
                                                Text(
                                                    text = "/شهر",
                                                    fontSize = 9.sp,
                                                    color = ContTextMuted
                                                )
                                            }

                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = ContGold,
                                                    modifier = Modifier
                                                        .size(16.dp)
                                                        .padding(top = 4.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Feature checklist
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = ContGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "المميزات المشمولة في الاشتراك",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = ContBlack
                                    )
                                }

                                val features = listOf(
                                    "تحديث أسعار لحظي من 4 أسواق جملة رئيسية",
                                    "توقعات أسعار ذكية لـ 7 أيام قادمة",
                                    "الموجز الإخباري وتحليلات التجار بدون حجب",
                                    "فرص وعقود تصدير حصرية لحظة نشرها",
                                    "تنبيهات فورية عند تحرك سعر محصولك المفضل",
                                    "دعم فني استشاري مباشر عبر واتساب"
                                )

                                Column(
                                    modifier = Modifier.padding(top = 10.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    features.forEach { feature ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .background(ContGreenLight, CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = ContGreen,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                            }
                                            Text(
                                                text = feature,
                                                fontSize = 12.sp,
                                                color = ContTextPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Payment Methods
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F1EB))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "طرق الدفع المدعومة في مصر",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ContTextSecondary
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf(
                                        Pair("فودافون كاش", Color(0xFFE30613)),
                                        Pair("فوري", Color(0xFFF59E0B)),
                                        Pair("باي موب", Color(0xFF12110F))
                                    ).forEach { (name, color) ->
                                        Surface(
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color.White,
                                            border = androidx.compose.foundation.BorderStroke(1.dp, ContBorderLight)
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(vertical = 8.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(14.dp)
                                                        .background(color, CircleShape)
                                                )
                                                Text(
                                                    text = name,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(top = 4.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Action buttons footer
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        shadowElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val priceLabel = if (selectedPlan == "bundle") "149" else "99"

                            Button(
                                onClick = { onSubscribe(selectedPlan) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(25.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ContGold
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = Color.White)
                                    Text(
                                        text = "اشترك الآن - $priceLabel جنيه / شهر",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp
                                    )
                                }
                            }

                            OutlinedButton(
                                onClick = { onSubscribe(selectedPlan) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp),
                                shape = RoundedCornerShape(22.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ContTextPrimary),
                                border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = ContGreen, modifier = Modifier.size(16.dp))
                                    Text(
                                        text = "جرب مجاناً 3 أيام (إلغاء في أي وقت)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Text(
                                text = "دفع آمن ومحمي • تجديد تلقائي مرن • فاتورة رسمية",
                                fontSize = 10.sp,
                                color = ContTextMuted,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
