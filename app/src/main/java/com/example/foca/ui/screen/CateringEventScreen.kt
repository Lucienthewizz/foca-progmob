package com.example.foca.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.foca.data.viewmodel.CateringViewModel
import com.example.foca.ui.components.FoodCard
import com.example.foca.ui.components.SearchFilterComponent
import com.example.foca.ui.theme.PoppinsFont
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun CateringEventScreen(navController: NavController) {
    val viewModel: CateringViewModel = viewModel()
    val allItems by viewModel.allItems.collectAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.collectAsState()
    val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }
    val (selectedSubcategory, setSelectedSubcategory) = remember { mutableStateOf("Wedding") }
    val subcategories = listOf("Wedding", "Birthday", "Meeting", "Other")

    LaunchedEffect(Unit) {
        viewModel.loadAllCateringItems()
    }

    val filteredItems = allItems.filter {
        it.category.lowercase() == "event-${selectedSubcategory.lowercase()}" &&
        (searchQuery.isBlank() || it.title.contains(searchQuery, ignoreCase = true) || it.description.contains(searchQuery, ignoreCase = true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFBF7))
            .padding(start = 20.dp, end = 20.dp, top = 75.dp, bottom = 24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack("home", false) }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Catering Event", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            subcategories.forEach { subcat ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedSubcategory == subcat) Color(0xFFFCB507) else Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                        .height(60.dp)
                        .clickable { setSelectedSubcategory(subcat) }
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(subcat, fontFamily = PoppinsFont, fontWeight = FontWeight.Medium, color = if (selectedSubcategory == subcat) Color.White else Color.Black)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        SearchFilterComponent(
            searchQuery = searchQuery,
            onSearchQueryChange = setSearchQuery,
            selectedCategory = "",
            onCategorySelected = {},
            selectedRating = 0.0,
            onRatingSelected = {},
            sortOption = "",
            onSortOptionSelected = {},
            categoryOptions = emptyList(),
            ratingOptions = emptyList(),
            sortOptions = emptyList(),
            onResetFilters = {},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFFCB507))
            }
        } else if (filteredItems.isEmpty()) {
            Text("Tidak ada menu event.", color = Color.Gray)
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(filteredItems) { item ->
                    FoodCard(
                        item = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        onClick = {
                            val itemJson = Gson().toJson(item)
                            val encodedItemJson = URLEncoder.encode(itemJson, StandardCharsets.UTF_8.toString())
                            navController.navigate("detail/$encodedItemJson")
                        }
                    )
                }
            }
        }
    }
}
