package com.example.myapplication.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.myapplication.R
import com.example.myapplication.models.DataX

@Composable
fun FilterIcon(
    onFilterSelected: (FilterOption) -> Unit,
    filterItems: List<FilterOption>,
    selectedFilter: FilterOption
) {
    var expanded = remember { mutableStateOf(false) }

    var iconModifier = Modifier.padding()

    iconModifier = if (expanded.value) {
        iconModifier.then(Modifier.rotate(180f))
    } else {
        iconModifier.then(Modifier.rotate(0f))
    }

    Column {
        Icon(
            imageVector = Icons.Default.FilterList,
            contentDescription = null,
            modifier = iconModifier
                .clickable { expanded.value = !expanded.value }
        )

        // Dropdown menu
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            filterItems.forEach { filter ->
                DropdownMenuItem(
                    onClick = {
                        onFilterSelected(filter)
                        expanded.value = false
                    }, text = { Text(text = filter.name) }, trailingIcon = {
                        // Add a checkmark if the item is selected
                        if (filter == selectedFilter) {
                            Spacer(modifier = Modifier.width(16.dp))
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TextFieldSearch(searchText: (String) -> Unit) {
    val text = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = text.value,
        onValueChange = {
            if (it.trim().isEmpty()) {
                searchText.invoke(it)
            }
            text.value = it
        },
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0x0D0B0B0B)),
        placeholder = {
            Text(
                text = "Search",
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Gray,
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        ), keyboardActions = KeyboardActions(
            onSearch = {
                if (text.value.trim().isNotEmpty()) {
                    searchText.invoke(text.value)
                }
                keyboardController?.hide()
            }
        )

    )
}


@Composable
 fun TopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Exchanges",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFF0B0B0B),
            )
        )
        Row() {
            Image(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "notification icon"
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "notification icon"
            )
        }

    }
}


@Composable
 fun CryptoDetailTv(dataX: DataX, modifier: Modifier, isGraph: Boolean) {
    val change = dataX.quote.USD.percent_change_24h
    val changeString = String.format("%.1f", change)

    val price = String.format("%.1f", dataX.quote.USD.price)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
    ) {
        Image(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth(),
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://s2.coinmarketcap.com/static/img/coins/64x64/${dataX.id}.png")
                    .size(46).build()
            ),
            contentDescription = null, contentScale = ContentScale.FillHeight
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .padding(start = 20.dp), contentAlignment = Alignment.TopStart
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = dataX.symbol,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight(700),
                            color = Color(0xFF000000),
                            textAlign = TextAlign.Center,
                        )
                    )
                    Text(
                        text = "$$price USD",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight(700),
                            color = Color(0xFF000000),
                            textAlign = TextAlign.Right,
                        )
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = dataX.name,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight(500),
                            color = Color(0xFF000000),
                            textAlign = TextAlign.Center,
                        )
                    )
                    Text(
                        text = "$changeString%",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight(700),
                            color = if (change >= 0) Color(0xFF00CE08) else Color(0xFFFF3D00),
                            textAlign = TextAlign.Right,
                        )
                    )
                }
            }
            if (isGraph) {
                Image(
                    modifier = Modifier
                        .height(20.dp)
                        .width(120.dp)
                        .align(Alignment.TopStart)
                        .padding(start = 60.dp),
                    painter = painterResource(id = if (change >= 0) R.drawable.pos_graph else R.drawable.neg_graph),
                    contentDescription = "graph", contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}

enum class FilterOption {
    PRICE, VOLUME, NONE
}