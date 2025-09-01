package com.example.apiservices.data.model

data class SupplierOptionEntity(
    val itemNameOption: List<OptionData> = emptyList(),
    val supplierOption: List<OptionData> = emptyList(),
    val cityOption: List<OptionData> = emptyList(),
    val modifiedByOption: List<OptionData> = emptyList(),
    val statusOption: List<OptionData> = emptyList()
) {
    data class OptionData(
        val label: String = "",
        val value: String = ""
    )
}