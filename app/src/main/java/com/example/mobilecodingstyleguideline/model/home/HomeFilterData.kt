package com.example.mobilecodingstyleguideline.model.home

data class HomeFilterData(
    val activeSelected: List<Boolean> = emptyList(),
    val supplierSelected: List<String> = emptyList(),
    val citySelected: List<String> = emptyList(),
    val itemSelected: List<String> = emptyList(),
    val picSelected: List<String> = emptyList(),
    val dateSelected: List<Long> = emptyList()
)
