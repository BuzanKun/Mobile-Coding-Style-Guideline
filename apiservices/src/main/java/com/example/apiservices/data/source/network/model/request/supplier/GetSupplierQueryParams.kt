package com.example.apiservices.data.source.network.model.request.supplier

data class GetSupplierQueryParams(
    val search: String? = null,
    val supplier: String? = null,
    val city: String? = null,
    val itemName: String? = null,
    val isActive: String? = null,
    val modifiedBy: String? = null,
    val updatedAt: String? = null,
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
            "supplier" to supplier,
            "city" to city,
            "itemName" to itemName,
            "isActive" to isActive,
            "modifiedBy" to modifiedBy,
            "lastUpdate" to updatedAt,
            "page" to page?.toString(),
            "limit" to limit?.toString(),
            "sortBy" to sortBy,
            "sortOrder" to sortOrder?.toString()
        ).filterValues { it != null }
    }
}