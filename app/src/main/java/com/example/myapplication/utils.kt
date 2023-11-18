package com.example.myapplication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import com.example.myapplication.component.FilterOption
import com.example.myapplication.data.ApiResponse
import com.example.myapplication.models.DataX
import com.example.myapplication.models.ResponseData

fun filterAndSorting(
    resdata: State<ApiResponse<ResponseData>>,
    searchText: MutableState<String>,
    selectedFilter: MutableState<FilterOption>,
    filterItems: List<FilterOption>,
    list: MutableState<List<DataX>?>
) {
    var templist = resdata.value.data?.data
    if (searchText.value.trim().isNotEmpty()) {
        val filteredList =
            templist?.filter {
                it.name.toLowerCase()
                    .contains(searchText.value.toLowerCase()) || it.symbol.toLowerCase()
                    .contains(searchText.value.toLowerCase())
            }
        templist = filteredList
    }
    if (selectedFilter.value.name != filterItems[0].name) {
        if (selectedFilter.value.name == filterItems[1].name) {
            templist = templist?.sortedByDescending {
                it.quote.USD.price
            }
        } else {
            templist = templist?.sortedByDescending {
                it.quote.USD.volume_24h
            }
        }
    }
    list.value = templist
}