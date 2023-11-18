package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.myapplication.component.CryptoDetailTv
import com.example.myapplication.component.FilterIcon
import com.example.myapplication.component.FilterOption
import com.example.myapplication.component.TextFieldSearch
import com.example.myapplication.component.TopBar
import com.example.myapplication.data.ApiResponse
import com.example.myapplication.models.DataX
import com.example.myapplication.models.ResponseData
import com.example.myapplication.screens.ViewModelMainScreen
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}


@Composable
fun MainScreen() {
    val viewModel: ViewModelMainScreen = viewModel()
    val resdata = viewModel.apiResponse.collectAsState()
    val list = remember {
        mutableStateOf(resdata.value.data?.data)
    }
    val searchText = remember { mutableStateOf("") }
    val filterItems = listOf(
        FilterOption.NONE,
        FilterOption.PRICE,
        FilterOption.VOLUME
    )

    var selectedFilter = remember { mutableStateOf(FilterOption.NONE) }

    LaunchedEffect(key1 = resdata.value, key2 = searchText.value, key3 = selectedFilter.value) {
        filterAndSorting(resdata, searchText, selectedFilter, filterItems, list)
    }
    Box(modifier = Modifier.fillMaxSize().padding(bottom = 18.dp), contentAlignment = Alignment.BottomCenter) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(26.dp)
        ) {
            TopBar()
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                TextFieldSearch {
                    searchText.value = it
                }
                FilterIcon(
                    onFilterSelected = { selectedFilter.value = it },
                    filterItems = filterItems,
                    selectedFilter = selectedFilter.value
                )
            }

            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = "Cryptocurrency",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF0B0B0B),
                )
            )

            Spacer(modifier = Modifier.height(15.dp))
            resdata.value.data?.data?.let { BtcCard(it) }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Top Cryptocurrencies",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF0B0B0B),
                )
            )
            Spacer(modifier = Modifier.height(20.dp))

            list.value?.let { TopCryptoList(it) }

        }
        BottomBar()
    }

}


@Composable
fun TopCryptoList(value: List<DataX>) {
    LazyColumn() {
        items(value) {
            CryptoDetailTv(
                it, Modifier, true
            )
            Spacer(modifier = Modifier.padding(12.dp))
        }
    }
}

@Composable
fun BtcCard(list: List<DataX>) {
    val btcDetails = list.filter {
        it.name == "Bitcoin"
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(131.dp)
            .background(color = Color(0x1A00CE08), shape = RoundedCornerShape(size = 20.dp)),
        color = Color.Transparent
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            CryptoDetailTv(
                modifier = Modifier
                    .padding(20.dp), dataX = btcDetails[0], isGraph = false
            )
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.graph),
                contentDescription = "graph",
                contentScale = ContentScale.FillWidth
            )
        }

    }
}


@Preview
@Composable
fun BottomBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(71.dp).padding(horizontal = 16.dp)
            .background(
                color = Color(0xFF0B0B0B),
                shape = RoundedCornerShape(size = 25.dp)
            ), color = Color.Transparent
    ) {
        val id = R.drawable.eshopicon
        val text = "€-Shop"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarItem(id, text)
            BottomBarItem(R.drawable.exchangeicon, "Exchange")
            Image(
                modifier = Modifier
                    .shadow(
                        elevation = 54.dp,
                        spotColor = Color(0x80F4B000),
                        ambientColor = Color(0x80F4B000)
                    )
                    .size(65.dp),
                painter = painterResource(id = R.drawable.homeicon),
                contentDescription = "Home Icon",
                contentScale = ContentScale.FillHeight
            )
            BottomBarItem(R.drawable.launchpadicon, "Launchpad")
            BottomBarItem(R.drawable.wallet, "Wallet")

        }

    }
}

@Composable
private fun BottomBarItem(id: Int, text: String) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = id),
            contentDescription = "eshopIcon"
        )
        Text(
            text = text,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight(600),
                color = Color(0x66FFFFFF),
                textAlign = TextAlign.Center,
            )
        )
    }
}













