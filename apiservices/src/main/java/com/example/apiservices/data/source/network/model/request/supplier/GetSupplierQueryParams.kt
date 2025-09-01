package com.example.apiservices.data.source.network.model.request.supplier

data class GetSupplierQueryParams(
    val search: String? = null,
    val supplier: List<String>? = null,
    val city: List<String>? = null,
    val itemName: List<String>? = null,
    val modifiedBy: String? = null,
    val page: Int? = null,
    val limit: Int? = null,
    val sortBy: String? = null,
    val sortOrder: Int? = null
) {
    companion object {
        const val ASC = 1
        const val DESC = -1
    }

    fun toQueryMap(): Map<String, String?> {
        return mapOf(
            "search" to search,
            "supplier" to supplier?.joinToString(","),
            "city" to city?.joinToString(","),
            "itemName" to itemName?.joinToString(","),
            "modifiedBy" to modifiedBy,
            "page" to page?.toString(),
            "limit" to limit?.toString(),
            "sortBy" to sortBy,
            "sordOrder" to sortOrder?.toString()
        ).filterValues { it != null }
    }
}