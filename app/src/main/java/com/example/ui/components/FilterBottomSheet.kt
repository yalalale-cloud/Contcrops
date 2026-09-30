package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FilterState
import com.example.model.SavedFilter
import com.example.model.UserRole
import com.example.ui.theme.*

val availableRegions = listOf(
    "الكل", "البحيرة", "الشرقية", "القاهرة", "الإسكندرية", "الجيزة",
    "كفر الشيخ", "المنوفية", "الدقهلية", "بدر الصناعية"
)

val sortOptions = listOf(
    Pair("latest", "الأحدث"),
    Pair("cheapest", "الأرخص"),
    Pair("highestRated", "الأعلى تقييماً"),
    Pair("nearest", "الأقرب لي"),
    Pair("largestQty", "الأكبر كمية")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    currentFilter: FilterState,
    savedFilters: List<SavedFilter>,
    matchCount: Int,
    onDismiss: () -> Unit,
    onApply: (FilterState) -> Unit,
    onSaveCurrent: (String) -> Unit
) {
    var state by remember { mutableStateOf(currentFilter) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var presetName by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = ContCreamBg,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                        Icon(Icons.Default.Tune, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Column {
                        Text(
                            text = "فلترة النتائج",
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = ContTextPrimary
                        )
                        Text(
                            text = "$matchCount منتج متطابق",
                            fontSize = 11.sp,
                            color = ContTextSecondary
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = { state = FilterState() }) {
                        Text("إعادة ضبط", color = ContTextSecondary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
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
            }

            Divider(color = ContBorderLight)

            // Saved Filters bar if present
            if (savedFilters.isNotEmpty()) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("الفلاتر المحفوظة", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ContTextSecondary)
                        Text(
                            text = "حفظ الحالي +",
                            color = ContGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { showSaveDialog = true }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(top = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        savedFilters.forEach { sf ->
                            FilterChip(
                                selected = state == sf.filters,
                                onClick = { state = sf.filters },
                                label = { Text(sf.name, fontSize = 11.sp) },
                                leadingIcon = {
                                    Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(12.dp))
                                }
                            )
                        }
                    }
                }
                Divider(color = ContBorderLight)
            }

            // Scrollable Filters content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 1. Sort Order
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.SwapVert, contentDescription = null, tint = ContBlack, modifier = Modifier.size(16.dp))
                        Text("ترتيب النتائج", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        sortOptions.forEach { (key, label) ->
                            val isSelected = state.sort == key
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) ContBlack else Color.White,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) ContBlack else ContBorder),
                                modifier = Modifier.clickable { state = state.copy(sort = key) }
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) Color.White else ContTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                // 2. Regions
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Place, contentDescription = null, tint = ContBlack, modifier = Modifier.size(16.dp))
                        Text("المحافظة / المنطقة", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        availableRegions.forEach { region ->
                            val isSelected = if (region == "الكل") state.regions.isEmpty() else state.regions.contains(region)
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) ContGreen else Color.White,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) ContGreen else ContBorder),
                                modifier = Modifier.clickable {
                                    state = if (region == "الكل") {
                                        state.copy(regions = emptyList())
                                    } else {
                                        val newReg = if (state.regions.contains(region)) {
                                            state.regions - region
                                        } else {
                                            state.regions + region
                                        }
                                        state.copy(regions = newReg)
                                    }
                                }
                            ) {
                                Text(
                                    text = region,
                                    color = if (isSelected) Color.White else ContTextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                // 3. Quantity (Tons)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.Inventory2, contentDescription = null, tint = ContBlack, modifier = Modifier.size(16.dp))
                                Text("الكمية المطلوبة", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Surface(color = ContCreamBg, shape = RoundedCornerShape(8.dp)) {
                                Text(
                                    text = "${state.qtyMin} - ${state.qtyMax} طن",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ContGreen,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        // Range Inputs
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = state.qtyMin.toString(),
                                onValueChange = { str ->
                                    val num = str.filter { it.isDigit() }.toIntOrNull() ?: 1
                                    state = state.copy(qtyMin = num.coerceIn(1, state.qtyMax))
                                },
                                label = { Text("من (طن)", fontSize = 10.sp) },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = state.qtyMax.toString(),
                                onValueChange = { str ->
                                    val num = str.filter { it.isDigit() }.toIntOrNull() ?: 100
                                    state = state.copy(qtyMax = num.coerceIn(state.qtyMin, 500))
                                },
                                label = { Text("إلى (طن)", fontSize = 10.sp) },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                        }

                        // Quick Chips
                        Row(
                            modifier = Modifier
                                .horizontalScroll(rememberScrollState())
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(
                                Triple("all", "الكل", Pair(1, 100)),
                                Triple("lt5", "أقل من 5 طن", Pair(1, 4)),
                                Triple("5-20", "5 - 20 طن", Pair(5, 20)),
                                Triple("gt20", "أكثر من 20 طن", Pair(21, 150))
                            ).forEach { (id, label, range) ->
                                val isSelected = state.qtyQuick == id
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isSelected) ContBlack else ContCreamBg,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) ContBlack else ContBorder),
                                    modifier = Modifier.clickable {
                                        state = state.copy(qtyMin = range.first, qtyMax = range.second, qtyQuick = id)
                                    }
                                ) {
                                    Text(
                                        text = label,
                                        color = if (isSelected) Color.White else ContTextPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // 4. Product Type
                Column {
                    Text("نوع تجهيز المحصول", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            Triple("فريش", Icons.Default.Eco, ContGreenLight),
                            Triple("مجمد", Icons.Default.AcUnit, ContBlueLight),
                            Triple("مجفف", Icons.Default.WbSunny, ContGoldBg)
                        ).forEach { (type, icon, bg) ->
                            val isSelected = state.productTypes.contains(type)
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        state = if (isSelected) {
                                            state.copy(productTypes = state.productTypes - type)
                                        } else {
                                            state.copy(productTypes = state.productTypes + type)
                                        }
                                    },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) ContGreen else bg
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = if (isSelected) Color.White else ContBlack,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = type,
                                        color = if (isSelected) Color.White else ContBlack,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // 5. Logistics / Delivery
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("الخدمات اللوجستية والتسليم", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                        Column(
                            modifier = Modifier.padding(top = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                Triple("ready", "جاهز للشحن الفوري", Icons.Default.LocalShipping),
                                Triple("cold", "توصيل مبرد تجميد -18", Icons.Default.AcUnit),
                                Triple("farm", "استلام من المزرعة / الفرز", Icons.Default.Agriculture)
                            ).forEach { (key, label, icon) ->
                                val isSelected = state.delivery.contains(key)
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            state = if (isSelected) {
                                                state.copy(delivery = state.delivery - key)
                                            } else {
                                                state.copy(delivery = state.delivery + key)
                                            }
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) ContGreenLight else ContCreamBg,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) ContGreen else ContBorder)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = icon,
                                                contentDescription = null,
                                                tint = if (isSelected) ContGreen else ContTextSecondary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = label,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) ContGreenDark else ContTextPrimary
                                            )
                                        }

                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = ContGreen,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Actions
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
                        onClick = { showSaveDialog = true },
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier.height(48.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ContBorder)
                    ) {
                        Icon(Icons.Default.BookmarkAdd, contentDescription = null, tint = ContTextSecondary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("حفظ", color = ContTextSecondary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Button(
                        onClick = { onApply(state) },
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ContGreen)
                    ) {
                        Text(
                            text = "عرض $matchCount نتيجة",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("حفظ الفلتر كنموذج مخصص") },
            text = {
                OutlinedTextField(
                    value = presetName,
                    onValueChange = { presetName = it },
                    placeholder = { Text("مثال: طماطم البحيرة 10 طن") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (presetName.isNotBlank()) {
                            onSaveCurrent(presetName)
                            showSaveDialog = false
                            presetName = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ContGreen)
                ) {
                    Text("حفظ")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("إلغاء")
                }
            }
        )
    }
}
