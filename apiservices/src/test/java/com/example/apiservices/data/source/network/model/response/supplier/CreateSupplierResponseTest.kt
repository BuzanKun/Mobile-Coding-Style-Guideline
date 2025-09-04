package com.example.apiservices.data.source.network.model.response.supplier

import com.google.common.truth.Truth
import org.junit.Test

class CreateSupplierResponseTest {
    @Test
    fun `constructor() should handle empty item list`() {
        val emptyItemList = CreateSupplierResponse(
            data = CreateSupplierResponse.Data(
                item = emptyList()
            )
        )

        // Assert
        Truth.assertThat(emptyItemList.data.item).isEmpty()
    }

    @Test
    fun `constructor() should handle given values`() {
        val item = CreateSupplierResponse.Data.Item(
            itemName = "Test Item",
            sku = listOf("SKU123", "SKU456")
        )

        val data = CreateSupplierResponse.Data(
            status = true,
            companyName = "Test Company",
            country = "Test Country",
            state = "Test State",
            companyLocation = "123 Test St",
            companyPhoneNumber = 1234567890L,
            item = listOf(item),
            zipCode = 90210,
            picName = "Test PIC",
            picPhoneNumber = 9876543210L,
            picEmail = "test@example.com",
            updatedAt = "2023-10-27T10:00:00Z"
        )

        val response = CreateSupplierResponse(
            status = 200,
            message = "Success",
            data = data
        )

        // Assert Response Values
        Truth.assertThat(response.status).isEqualTo(200)
        Truth.assertThat(response.message).isEqualTo("Success")

        // Assert Data
        Truth.assertThat(response.data).isEqualTo(data)

        val responseData = response.data
        Truth.assertThat(responseData.status).isTrue()
        Truth.assertThat(responseData.companyName).isEqualTo("Test Company")
        Truth.assertThat(responseData.country).isEqualTo("Test Country")
        Truth.assertThat(responseData.state).isEqualTo("Test State")
        Truth.assertThat(responseData.companyLocation).isEqualTo("123 Test St")
        Truth.assertThat(responseData.companyPhoneNumber).isEqualTo(1234567890L)
        Truth.assertThat(responseData.zipCode).isEqualTo(90210)
        Truth.assertThat(responseData.picName).isEqualTo("Test PIC")
        Truth.assertThat(responseData.picPhoneNumber).isEqualTo(9876543210L)
        Truth.assertThat(responseData.picEmail).isEqualTo("test@example.com")
        Truth.assertThat(responseData.updatedAt).isEqualTo("2023-10-27T10:00:00Z")

        // Assert Item
        Truth.assertThat(responseData.item).hasSize(1)
        Truth.assertThat(responseData.item[0]).isEqualTo(item)
        val responseItem = responseData.item[0]
        Truth.assertThat(responseItem.itemName).isEqualTo("Test Item")
        Truth.assertThat(responseItem.sku).containsExactly("SKU123", "SKU456")
    }
}