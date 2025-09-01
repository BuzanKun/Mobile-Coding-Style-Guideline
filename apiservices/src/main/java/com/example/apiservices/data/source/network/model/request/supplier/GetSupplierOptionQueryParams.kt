package com.example.apiservices.data.source.network.model.request.supplier

data class GetSupplierOptionQueryParams(
    val supplierOption: Boolean? = true,
    val cityOption: Boolean? = true,
    val itemNameOption: Boolean? = true,
    val modifiedByOption: Boolean? = true
) {
    fun toQueryMap(): Map<String, Boolean?> {
        return mapOf(
            "supplierOption" to supplierOption,
            "cityOption" to cityOption,
            "itemNameOption" to itemNameOption,
            "modifiedByOption" to modifiedByOption
        )
    }
}