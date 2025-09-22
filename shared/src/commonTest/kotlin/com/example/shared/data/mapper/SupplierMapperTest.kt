package com.example.shared.data.mapper

import com.example.shared.data.model.SupplierEntity
import com.example.shared.data.model.SupplierOptionEntity
import com.example.shared.data.source.network.model.response.supplier.GetSupplierByIdResponse
import com.example.shared.data.source.network.model.response.supplier.GetSupplierOptionResponse
import com.example.shared.data.source.network.model.response.supplier.GetSupplierResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class SupplierMapperTest {
    private val mapper = SupplierMapper()

    // --- mapSuppliers Test ---

    @Test
    fun `when supplier list data is empty then return empty list`() {
        // Arrange
        val data = emptyList<GetSupplierResponse.Data.Data>()

        // Act
        val result = mapper.mapSupplier(data)

        // Assert
        assertEquals(0, result.size)
    }

    @Test
    fun `when supplier list data has one item then return mapped value`() {
        // Arrange
        val data = listOf(
            GetSupplierResponse.Data.Data(
                id = "id",
                companyName = "companyName",
                item = listOf(
                    GetSupplierResponse.Data.Data.Item(
                        id = "itemId",
                        supplierId = "supplierId",
                        itemName = "itemName",
                        sku = listOf("sku")
                    )
                ),
                country = "country",
                state = "state",
                city = "city",
                picName = "picName",
                status = true,
                modifiedBy = "modifiedBy",
                updatedAt = "updatedAt",
                createdAt = "createdAt"
            )
        )

        val expected = listOf(
            SupplierEntity(
                id = "id",
                companyName = "companyName",
                item = listOf(
                    SupplierEntity.Item(
                        id = "itemId",
                        supplierId = "supplierId",
                        itemName = "itemName",
                        sku = listOf("sku")
                    )
                ),
                country = "country",
                state = "state",
                city = "city",
                picName = "picName",
                status = true,
                modifiedBy = "modifiedBy",
                updatedAt = "updatedAt",
                createdAt = "createdAt"
            )
        )

        // Act
        val result = mapper.mapSupplier(data)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun `when supplier list data has multiple item then return mapped value`() {
        // Arrange
        val data = listOf(
            GetSupplierResponse.Data.Data(
                id = "id",
                companyName = "companyName",
                item = listOf(
                    GetSupplierResponse.Data.Data.Item(
                        id = "itemId",
                        supplierId = "supplierId",
                        itemName = "itemName",
                        sku = listOf("sku")
                    )
                ),
                country = "country",
                state = "state",
                city = "city",
                picName = "picName",
                status = true,
                modifiedBy = "modifiedBy",
                updatedAt = "updatedAt",
                createdAt = "createdAt"
            ),
            GetSupplierResponse.Data.Data(
                id = "id",
                companyName = "companyName",
                item = listOf(
                    GetSupplierResponse.Data.Data.Item(
                        id = "itemId",
                        supplierId = "supplierId",
                        itemName = "itemName",
                        sku = listOf("sku")
                    )
                ),
                country = "country",
                state = "state",
                city = "city",
                picName = "picName",
                status = true,
                modifiedBy = "modifiedBy",
                updatedAt = "updatedAt",
                createdAt = "createdAt"
            )
        )

        val expected = listOf(
            SupplierEntity(
                id = "id",
                companyName = "companyName",
                item = listOf(
                    SupplierEntity.Item(
                        id = "itemId",
                        supplierId = "supplierId",
                        itemName = "itemName",
                        sku = listOf("sku")
                    )
                ),
                country = "country",
                state = "state",
                city = "city",
                picName = "picName",
                status = true,
                modifiedBy = "modifiedBy",
                updatedAt = "updatedAt",
                createdAt = "createdAt"
            ),
            SupplierEntity(
                id = "id",
                companyName = "companyName",
                item = listOf(
                    SupplierEntity.Item(
                        id = "itemId",
                        supplierId = "supplierId",
                        itemName = "itemName",
                        sku = listOf("sku")
                    )
                ),
                country = "country",
                state = "state",
                city = "city",
                picName = "picName",
                status = true,
                modifiedBy = "modifiedBy",
                updatedAt = "updatedAt",
                createdAt = "createdAt"
            )
        )

        // Act
        val result = mapper.mapSupplier(data)

        // Assert
        assertEquals(expected, result)
    }

    // --- mapSupplyById Test ---

    @Test
    fun `mapSupplyById should correctly map GetSupplierByIdResponse Data to SupplierEntity Data`() {
        // Arrange
        val data =
            GetSupplierByIdResponse.Data(
                id = "id",
                companyName = "companyName",
                item = listOf(
                    GetSupplierByIdResponse.Data.Item(
                        id = "itemId",
                        supplierId = "supplierId",
                        itemName = "itemName",
                        sku = listOf("sku")
                    )
                ),
                country = "country",
                state = "state",
                city = "city",
                picName = "picName",
                status = true,
                modifiedBy = "modifiedBy",
                updatedAt = "updatedAt",
                createdAt = "createdAt"
            )

        val expected =
            SupplierEntity(
                id = "id",
                companyName = "companyName",
                item = listOf(
                    SupplierEntity.Item(
                        id = "itemId",
                        supplierId = "supplierId",
                        itemName = "itemName",
                        sku = listOf("sku")
                    )
                ),
                country = "country",
                state = "state",
                city = "city",
                picName = "picName",
                status = true,
                modifiedBy = "modifiedBy",
                updatedAt = "updatedAt",
                createdAt = "createdAt"
            )

        // Act
        val result = mapper.mapSupplierById(data)

        // Assert
        assertEquals(expected, result)
    }

    // --- mapSupplierDetailItems Test ---

    @Test
    fun `mapSupplierDetailItems should correctly map the list of GetSupplierByIdResponse Data item to SupplierEntity Item`() {
        // Arrange
        val data = listOf(
            GetSupplierByIdResponse.Data.Item(
                id = "1",
                supplierId = "Supplier 1",
                itemName = "Name 1",
                sku = listOf(
                    "Sku 1",
                    "Sku 2"
                )
            ),
            GetSupplierByIdResponse.Data.Item(
                id = "2",
                supplierId = "Supplier 2",
                itemName = "Name 2",
                sku = listOf(
                    "Sku 1",
                    "Sku 2"
                )
            )
        )

        val expected = listOf(
            SupplierEntity.Item(
                id = "1",
                supplierId = "Supplier 1",
                itemName = "Name 1",
                sku = listOf(
                    "Sku 1",
                    "Sku 2"
                )
            ),
            SupplierEntity.Item(
                id = "2",
                supplierId = "Supplier 2",
                itemName = "Name 2",
                sku = listOf(
                    "Sku 1",
                    "Sku 2"
                )
            )
        )

        // Act
        val result = mapper.mapSupplierDetailItems(data)

        // Assert
        assertEquals(expected, result)
    }

    // --- mapSupplierListItems Test ---

    @Test
    fun `mapSupplierListItems should correctly map the list of GetSupplierResponse Data Item to SupplierEntity Item`() {
        // Arrange
        val data = listOf(
            GetSupplierResponse.Data.Data.Item(
                id = "1",
                supplierId = "Supplier 1",
                itemName = "Name 1",
                sku = listOf(
                    "Sku 1",
                    "Sku 2"
                )
            ),
            GetSupplierResponse.Data.Data.Item(
                id = "2",
                supplierId = "Supplier 2",
                itemName = "Name 2",
                sku = listOf(
                    "Sku 1",
                    "Sku 2"
                )
            )
        )

        val expected = listOf(
            SupplierEntity.Item(
                id = "1",
                supplierId = "Supplier 1",
                itemName = "Name 1",
                sku = listOf(
                    "Sku 1",
                    "Sku 2"
                )
            ),
            SupplierEntity.Item(
                id = "2",
                supplierId = "Supplier 2",
                itemName = "Name 2",
                sku = listOf(
                    "Sku 1",
                    "Sku 2"
                )
            )
        )

        // Act
        val result = mapper.mapSupplierListItems(data)

        // Assert
        assertEquals(expected, result)
    }

    // --- mapSupplyListOption Test ---

    @Test
    fun `mapSupplyListOption should correctly map GetSupplierOptionResponse Data to SupplierOptionEntity`() {
        // Arrange
        val data = GetSupplierOptionResponse.Data(
            itemNameOption = listOf(
                GetSupplierOptionResponse.Data.OptionData(
                    label = "Item Label 1",
                    value = "Item Value 1"
                ),
                GetSupplierOptionResponse.Data.OptionData(
                    label = "Item Label 2",
                    value = "Item Value 2"
                )
            ),
            supplierOption = listOf(
                GetSupplierOptionResponse.Data.OptionData(
                    label = "Supplier Label 1",
                    value = "Supplier Value 1"
                ),
                GetSupplierOptionResponse.Data.OptionData(
                    label = "Supplier Label 2",
                    value = "Supplier Value 2"
                )
            ),
            cityOption = listOf(
                GetSupplierOptionResponse.Data.OptionData(
                    label = "City Label 1",
                    value = "City Value 1"
                ),
                GetSupplierOptionResponse.Data.OptionData(
                    label = "City Label 2",
                    value = "City Value 2"
                )
            ),
            modifiedByOption = listOf(
                GetSupplierOptionResponse.Data.OptionData(
                    label = "Modifier Label 1",
                    value = "Modifier Value 1"
                ),
                GetSupplierOptionResponse.Data.OptionData(
                    label = "Modifier Label 2",
                    value = "Modifier Value 2"
                )
            )
        )

        val expected = SupplierOptionEntity(
            itemNameOption = listOf(
                SupplierOptionEntity.OptionData(
                    label = "Item Label 1",
                    value = "Item Value 1"
                ),
                SupplierOptionEntity.OptionData(
                    label = "Item Label 2",
                    value = "Item Value 2"
                )
            ),
            supplierOption = listOf(
                SupplierOptionEntity.OptionData(
                    label = "Supplier Label 1",
                    value = "Supplier Value 1"
                ),
                SupplierOptionEntity.OptionData(
                    label = "Supplier Label 2",
                    value = "Supplier Value 2"
                )
            ),
            cityOption = listOf(
                SupplierOptionEntity.OptionData(
                    label = "City Label 1",
                    value = "City Value 1"
                ),
                SupplierOptionEntity.OptionData(
                    label = "City Label 2",
                    value = "City Value 2"
                )
            ),
            modifiedByOption = listOf(
                SupplierOptionEntity.OptionData(
                    label = "Modifier Label 1",
                    value = "Modifier Value 1"
                ),
                SupplierOptionEntity.OptionData(
                    label = "Modifier Label 2",
                    value = "Modifier Value 2"
                )
            )
        )

        // Act
        val result = mapper.mapSupplierOption(data)

        // Assert
        assertEquals(expected, result)
    }

    // --- mapOptions Test ---
    @Test
    fun `mapOptions should correctly map GetSupplierOptionResponse Data OptionData to SupplierOptionEntity OptionData`() {
        // Arrange
        val data = listOf(
            GetSupplierOptionResponse.Data.OptionData(
                label = "Label 1",
                value = "Value 1"
            ),
            GetSupplierOptionResponse.Data.OptionData(
                label = "Label 2",
                value = "Value 2"
            )
        )

        val expected = listOf(
            SupplierOptionEntity.OptionData(
                label = "Label 1",
                value = "Value 1"
            ),
            SupplierOptionEntity.OptionData(
                label = "Label 2",
                value = "Value 2"
            )
        )

        // Act
        val result = mapper.mapOptions(data)

        // Assert
        assertEquals(expected, result)
    }
}