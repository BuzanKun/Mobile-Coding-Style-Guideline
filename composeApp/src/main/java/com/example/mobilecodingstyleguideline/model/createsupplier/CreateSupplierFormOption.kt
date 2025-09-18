package com.example.mobilecodingstyleguideline.model.createsupplier

import com.tagsamurai.common.model.OptionData

data class CreateSupplierFormOption(
    val itemNameList: List<OptionData<String>> = emptyList(),
    val itemSkuList: List<OptionData<String>> = emptyList(),
    val country: List<OptionData<String>> = emptyList(),
    val state: List<OptionData<String>> = emptyList(),
    val city: List<OptionData<String>> = emptyList(),
)
