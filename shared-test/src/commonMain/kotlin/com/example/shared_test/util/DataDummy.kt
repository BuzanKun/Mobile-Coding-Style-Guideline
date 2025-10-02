package com.example.shared_test.util

import com.example.shared.data.model.SupplierEntity
import com.example.shared.data.source.network.model.response.supplier.GetSupplierResponse

object DataDummy {
    val INITIAL_SUPPLIER_DTO = listOf(
        GetSupplierResponse.Data.Data(
            id = "1",
            status = false,
            companyName = "Company A",
            item = listOf(
                GetSupplierResponse.Data.Data.Item(
                    itemName = "Item A",
                    sku = listOf(
                        "Sku A"
                    )
                )
            ),
            country = "Country A",
            state = "State A",
            city = "City A",
            picName = "PIC A",
            modifiedBy = "Modifier A",
            updatedAt = "2025-09-30T10:00:00Z",
            createdAt = "2025-09-30T10:00:00Z"
        ),
        GetSupplierResponse.Data.Data(
            id = "2",
            status = true,
            companyName = "Company B",
            item = listOf(
                GetSupplierResponse.Data.Data.Item(
                    itemName = "Item B",
                    sku = listOf(
                        "Sku B"
                    )
                )
            ),
            country = "Country B",
            state = "State B",
            city = "City B",
            picName = "PIC B",
            modifiedBy = "Modifier B",
            updatedAt = "2025-09-30T10:00:00Z",
            createdAt = "2025-09-30T10:00:00Z"
        )
    )

    // The API response structure for the list endpoint
    val INITIAL_SUPPLIER_RESPONSE = GetSupplierResponse(
        data = GetSupplierResponse.Data(
            totalRecords = 2,
            data = INITIAL_SUPPLIER_DTO
        ),
        message = "Success",
        status = 200
    )

    val INITIAL_SUPPLIER_ENTITY = listOf(
        SupplierEntity(
            id = "1",
            companyName = "Initial Test Corp",
            status = true,
            city = "Testville",
            country = "Testland",
            picName = "John Doe",
            updatedAt = "2025-09-30T10:00:00Z"
        )
    )
}