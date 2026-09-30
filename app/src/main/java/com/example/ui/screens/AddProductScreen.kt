package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.model.User
import com.example.ui.theme.*

@Composable
fun AddProductScreen(
    currentUser: User,
    onProductAdded: (String, String, String, String, String, Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("فريش") }
    var qty by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var cold by remember { mutableStateOf(true) }

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
                            .size(36.dp)
                            .background(ContGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Text(
                        text = "إضافة محصول جديد",
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = ContTextPrimary
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
        }

        // Form
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Photo upload placeholder
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, ContBorderLight)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(ContCreamBg, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = ContGreen, modifier = Modifier.size(22.dp))
                    }
                    Text("صورة المحصول المفرز", fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.padding(top = 6.dp))
                    Text("يتم تعيين صورة عالية الجودة تلقائياً", fontSize = 11.sp, color = ContTextMuted)
                }
            }

            // Crop Processing Type
            Column {
                Text("نوع التجهيز", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = ContTextSecondary)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("فريش", "مجمد", "مجفف").forEach { t ->
                        val isSelected = type == t
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { type = t },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) ContGreen else Color.White
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) ContGreen else ContBorder)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = t,
                                    color = if (isSelected) Color.White else ContTextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }

            // Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("اسم المحصول") },
                placeholder = { Text("مثال: طماطم بلدي فرز أول") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                singleLine = true
            )

            // Quantity & Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = qty,
                    onValueChange = { qty = it },
                    label = { Text("الكمية") },
                    placeholder = { Text("مثال: 15 طن") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("السعر للطن (ج)") },
                    placeholder = { Text("مثال: 8500") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    singleLine = true
                )
            }

            // Cold chain toggle
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
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
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.AcUnit, contentDescription = null, tint = ContBlue)
                        Column {
                            Text("نقل مبرد متوفر", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("يدعم الشحن بسيارات مبردة للتصدير", fontSize = 10.sp, color = ContTextMuted)
                        }
                    }

                    Switch(
                        checked = cold,
                        onCheckedChange = { cold = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = ContGreen, checkedTrackColor = ContGreenLight)
                    )
                }
            }

            // Description
            OutlinedTextField(
                value = desc,
                onValueChange = { desc = it },
                label = { Text("مواصفات الفرز والتعبئة") },
                placeholder = { Text("مثال: طماطم عالية الصلابة، خالية من العيوب، معبأة في برانيك بلاستيك جديدة...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            // Submit Button
            val isValid = name.isNotBlank() && qty.isNotBlank() && price.isNotBlank()

            Button(
                onClick = {
                    if (isValid) {
                        onProductAdded(name, type, qty, price, desc, cold)
                    }
                },
                enabled = isValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ContGreen,
                    disabledContainerColor = ContBorder
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Publish, contentDescription = null)
                    Text("نشر المحصول على ContCrops", fontWeight = FontWeight.Black, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}
