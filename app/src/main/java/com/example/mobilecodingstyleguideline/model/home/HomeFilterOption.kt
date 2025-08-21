package com.example.mobilecodingstyleguideline.model.home

import com.tagsamurai.common.model.OptionData

data class HomeFilterOption(
    val activeOption: List<OptionData<Boolean>> = emptyList(),
    val supplierOption: List<OptionData<String>> = emptyList(),
    val cityOption: List<OptionData<String>> = emptyList(),
    val itemOption: List<OptionData<String>> = emptyList(),
    val picOption: List<OptionData<String>> = emptyList()
)
