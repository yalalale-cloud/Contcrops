package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.model.MarketPriceItem
import com.example.model.SubscriptionState
import com.example.ui.components.SparklineChart
import com.example.ui.theme.*

@Composable
fun PricesScreen(
    marketPrices: List<MarketPriceItem>,
    subscription: SubscriptionState,
    onOpenPaywall: () -> Unit
) {
    val markets = listOf("سوق العبور", "سوق البحيرة", "سوق الشرقية", "سوق 6 أكتوبر")
    var selectedMarket by remember { mutableStateOf("سوق العبور") }
    var selectedPriceItem by remember { mutableStateOf<MarketPriceItem?>(null) }

    val hasPriceAccess = subscription.active && (subscription.plan == "prices" || subscription.plan == "bundle")

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
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
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
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "الأسعار اليومية",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    color = ContTextPrimary
                                )
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = null,
                                    tint = ContGold,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "تحديث لحظي من البورصة وأسواق الجملة",
                                fontSize = 11.sp,
                                color = ContTextSecondary
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = ContGreenLight
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(modifier = Modifier.size(6.dp).background(ContGreen, CircleShape))
                            Text("مباشر", color = ContGreen, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        }
                    }
                }

                // Market selector tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    markets.forEach { market ->
                        val isSelected = selectedMarket == market
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) ContBlack else Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) ContBlack else ContBorder),
                            modifier = Modifier.clickable { selectedMarket = market }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Place,
                                    contentDescription = null,
                                    tint = if (isSelected) Color.White else ContTextSecondary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = market,
                                    color = if (isSelected) Color.White else ContTextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // List
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(marketPrices, key = { _, item -> item.id }) { index, item ->
                val isLocked = !hasPriceAccess && index >= 2
                val marketRange = item.markets[selectedMarket] ?: Pair(item.min, item.max)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isLocked) onOpenPaywall() else selectedPriceItem = item
                        },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                                .then(if (isLocked) Modifier.blur(4.dp) else Modifier),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AsyncImage(
                                model = item.image,
                                contentDescription = item.name,
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(14.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = item.name,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp,
                                        color = ContTextPrimary
                                    )

                                    val isUp = item.direction == "up"
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isUp) ContGreenLight else ContRedLight
                                    ) {
                                        Text(
                                            text = "${if (isUp) "+" else ""}${item.change}%",
                                            color = if (isUp) ContGreen else ContRed,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "${marketRange.first} - ${marketRange.second} ج / ${item.unit}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = ContBlack,
                                    modifier = Modifier.padding(top = 2.dp)
                                )

                                Text(
                                    text = "${item.lastUpdate} • $selectedMarket",
                                    fontSize = 10.sp,
                                    color = ContTextMuted,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                SparklineChart(
                                    data = item.sparkline,
                                    isUp = item.direction == "up",
                                    width = 64.dp,
                                    height = 26.dp
                                )
                                Text(
                                    text = "7 أيام",
                                    fontSize = 9.sp,
                                    color = ContTextMuted
                                )
                            }
                        }

                        // Premium Lock Overlay
                        if (isLocked) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color.White.copy(alpha = 0.7f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = ContBlack,
                                    shadowElevation = 4.dp
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = null,
                                            tint = ContGold,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = "محتوى مميز - اضغط للفتح",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Teaser Banner
            if (!hasPriceAccess) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A150A)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ContGold.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(listOf(ContGold, ContGoldDark))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                            }

                            Text(
                                text = "الأسعار التفصيلية مقفولة 🔒",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = Color.White,
                                modifier = Modifier.padding(top = 10.dp)
                            )

                            Text(
                                text = "شوف كل الأسعار اليومية، التوقعات، وأفضل سوق تبيع فيه - محدثة كل ساعة من 4 أسواق جملة رئيسية",
                                color = ContGold.copy(alpha = 0.75f),
                                fontSize = 11.sp,
                                lineHeight = 16.sp,
                                modifier = Modifier.padding(top = 6.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )

                            Button(
                                onClick = onOpenPaywall,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 14.dp)
                                    .height(46.dp),
                                shape = RoundedCornerShape(23.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ContGold)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = ContBlack)
                                    Text(
                                        text = "اشترك لعرض الأسعار - 99 ج/شهر",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp,
                                        color = ContBlack
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

    // Commodity Detail Dialog
    selectedPriceItem?.let { item ->
        Dialog(onDismissRequest = { selectedPriceItem = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ContCreamBg)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${item.name} - تفاصيل السعر",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = ContTextPrimary
                        )

                        IconButton(
                            onClick = { selectedPriceItem = null },
                            modifier = Modifier
                                .size(32.dp)
                                .background(ContBorder, CircleShape)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "إغلاق", modifier = Modifier.size(16.dp))
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 10.dp), color = ContBorderLight)

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Header card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = item.image,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(14.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Column {
                                    Text(item.name, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                    Text(
                                        text = "متوسط اليوم: ${item.avg} ج / ${item.unit}",
                                        fontSize = 12.sp,
                                        color = ContTextSecondary,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )

                                    val isUp = item.direction == "up"
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isUp) ContGreenLight else ContRedLight,
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        Text(
                                            text = "${if (isUp) "+" else ""}${item.change}% اليوم",
                                            color = if (isUp) ContGreen else ContRed,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Sparkline Detailed Chart
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("تذبذب الأسعار لـ 7 أيام ماضية", fontWeight = FontWeight.Bold, fontSize = 12.sp)

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .padding(top = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    SparklineChart(
                                        data = item.sparkline,
                                        isUp = item.direction == "up",
                                        width = 240.dp,
                                        height = 80.dp
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("قبل أسبوع: ${item.sparkline.first()} ج", fontSize = 10.sp, color = ContTextMuted)
                                    Text("اليوم: ${item.sparkline.last()} ج", fontSize = 10.sp, color = ContGreen, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Recommendation grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("أفضل سوق للبيع", fontSize = 10.sp, color = ContTextMuted)
                                    Text(item.bestMarket, fontWeight = FontWeight.Black, fontSize = 12.sp, color = ContGreen, modifier = Modifier.padding(top = 2.dp))
                                }
                            }

                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("توقع الأسعار القادمة", fontSize = 10.sp, color = ContTextMuted)
                                    val predText = when (item.prediction) {
                                        "up" -> "متوقع ارتفاع 📈"
                                        "down" -> "متوقع انخفاض 📉"
                                        else -> "مستقر ⚖️"
                                    }
                                    Text(predText, fontWeight = FontWeight.Black, fontSize = 12.sp, color = ContTextPrimary, modifier = Modifier.padding(top = 2.dp))
                                }
                            }
                        }

                        // Markets breakdown
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("الأسعار في أسواق الجملة الرئيسية", fontWeight = FontWeight.Bold, fontSize = 12.sp)

                                item.markets.forEach { (marketName, range) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(ContCreamBg, RoundedCornerShape(10.dp))
                                            .padding(horizontal = 10.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(Icons.Default.Storefront, contentDescription = null, tint = ContGreen, modifier = Modifier.size(14.dp))
                                            Text(marketName, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Text("${range.first} - ${range.second} ج", fontSize = 11.sp, fontWeight = FontWeight.Black, color = ContTextPrimary)
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
