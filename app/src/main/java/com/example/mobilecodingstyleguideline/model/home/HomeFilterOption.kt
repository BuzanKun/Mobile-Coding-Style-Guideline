package com.example.mobilecodingstyleguideline.model.home

import com.tagsamurai.common.model.OptionData

data class HomeFilterOption(
    val statusOption: List<OptionData<Boolean>> = emptyList(),
    val supplierOption: List<OptionData<String>> = emptyList(),
    val cityOption: List<OptionData<String>> = emptyList(),
    val itemNameOption: List<OptionData<String>> = emptyList(),
    val modifiedByOption: List<OptionData<String>> = emptyList()
)
