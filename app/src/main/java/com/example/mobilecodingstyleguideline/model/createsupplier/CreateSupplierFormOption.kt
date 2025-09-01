package com.example.mobilecodingstyleguideline.model.createsupplier

import com.example.apiservices.data.model.SupplierEntity
import com.tagsamurai.common.model.OptionData

data class CreateSupplierFormOption(
    val name: List<OptionData<String>> = emptyList(),
    val itemMasterList: List<SupplierEntity.Item> = emptyList(),
    val itemNameList: List<OptionData<String>> = emptyList(),
    val itemSkuList: List<OptionData<String>> = emptyList(),
    val country: List<OptionData<String>> = emptyList(),
    val state: List<OptionData<String>> = emptyList(),
    val city: List<OptionData<String>> = emptyList(),
    val zipCode: List<OptionData<String>> = emptyList(),
    val address: List<OptionData<String>> = emptyList(),
    val countryCode: List<OptionData<String>> = emptyList(),
    val phoneNumber: List<OptionData<String>> = emptyList(),
    val picName: List<OptionData<String>> = emptyList(),
    val picCountryCode: List<OptionData<String>> = emptyList(),
    val picPhoneNumber: List<OptionData<String>> = emptyList(),
    val picEmail: List<OptionData<String>> = emptyList()
)
