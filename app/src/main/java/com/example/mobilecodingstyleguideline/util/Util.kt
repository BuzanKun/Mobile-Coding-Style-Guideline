package com.example.mobilecodingstyleguideline.util

import com.example.apiservices.data.model.SupplierOptionEntity
import com.tagsamurai.common.model.OptionData

object Util {
    fun generateOptionsDataString(list: List<String>): List<OptionData<String>> {
        return list.map { OptionData(it, it) }
    }

    fun convertOptionsData(list: List<SupplierOptionEntity.OptionData>): List<OptionData<String>> {
        return list.map { OptionData(label = it.label, value = it.value) }
    }
}