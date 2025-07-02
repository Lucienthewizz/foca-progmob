package com.example.foca.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foca.R
import com.example.foca.ui.theme.PoppinsFont

/**
 * Komponen pencarian dan filter yang dapat digunakan kembali
 * @param searchQuery Query pencarian saat ini
 * @param onSearchQueryChange Callback saat query pencarian berubah
 * @param selectedCategory Kategori yang dipilih saat ini
 * @param onCategorySelected Callback saat kategori dipilih
 * @param selectedRating Rating yang dipilih saat ini
 * @param onRatingSelected Callback saat rating dipilih
 * @param sortOption Opsi pengurutan yang dipilih saat ini
 * @param onSortOptionSelected Callback saat opsi pengurutan dipilih
 * @param categoryOptions Daftar opsi kategori yang tersedia
 * @param ratingOptions Daftar opsi rating yang tersedia
 * @param sortOptions Daftar opsi pengurutan yang tersedia
 * @param onResetFilters Callback saat tombol reset ditekan
 * @param onApplyFilters Callback saat tombol terapkan ditekan
 */
@Composable
fun SearchFilterComponent(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    selectedRating: Double,
    onRatingSelected: (Double) -> Unit,
    sortOption: String,
    onSortOptionSelected: (String) -> Unit,
    categoryOptions: List<String>,
    ratingOptions: List<Double>,
    sortOptions: List<String>,
    onResetFilters: () -> Unit,
    onApplyFilters: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Hanya menampilkan search bar yang lebih kecil dan rapi
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        placeholder = { 
            Text(
                "Cari menu...", 
                color = Color(0xFFBDBDBD), 
                fontFamily = PoppinsFont, 
                fontSize = 14.sp
            ) 
        },
        modifier = modifier
            .fillMaxWidth(0.85f) // Ukuran lebih kecil
            .heightIn(min = 50.dp, max = 55.dp), // Tinggi yang lebih besar agar teks tidak terpotong
        singleLine = true,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search Icon",
                modifier = Modifier.size(20.dp), // Ikon yang proporsional dengan tinggi search bar
                tint = Color(0xFFFCB507)
            )
        },
        shape = RoundedCornerShape(20.dp), // Bentuk lebih bulat
        textStyle = TextStyle(
            fontFamily = PoppinsFont,
            fontSize = 14.sp, // Font yang sesuai dengan placeholder
            color = MaterialTheme.colorScheme.onSurface
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFFDFBF7), // Warna background menyatu dengan tema
            unfocusedContainerColor = Color(0xFFFDFBF7), // Warna background menyatu dengan tema
            focusedIndicatorColor = Color(0xFFFCB507),
            unfocusedIndicatorColor = Color.LightGray,
            cursorColor = Color(0xFFFCB507)
        )
    )
    
    // Filter section dihilangkan sesuai permintaan
}

/**
 * Komponen dropdown filter yang dapat digunakan kembali
 */
@Composable
fun FilterDropdown(
    label: String,
    value: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    Column(modifier = modifier) {
        Text(
            text = label,
            fontFamily = PoppinsFont,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { expanded = true },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_down), // Pastikan ada icon dropdown yang sesuai
                        contentDescription = "Dropdown Icon",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Gray
                    )
                },
                shape = RoundedCornerShape(10.dp),
                textStyle = TextStyle(fontFamily = PoppinsFont, fontSize = 14.sp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFFFCB507),
                    unfocusedIndicatorColor = Color.LightGray
                )
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, fontFamily = PoppinsFont) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}