package com.example.apiservices.data.source.network.model.response.supplier

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PutEditSupplierResponseTest {

    @Test
    fun `constructor() should handle empty item list`() {
        val emptyItemList = PutEditSupplierResponse(
            data = PutEditSupplierResponse.Data(
                set = PutEditSupplierResponse.Data.Set(
                    item = emptyList()
                )
            )
        )

        // Assert
        assertThat(emptyItemList.data.set.item).isEmpty()
    }

    @Test
    fun `constructor() should handle given values`() {
        val item = PutEditSupplierResponse.Data.Set.Item(
            itemName = "Test Item",
            sku = listOf("SKU123", "SKU456")
        )

        val set = PutEditSupplierResponse.Data.Set(
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

        val data = PutEditSupplierResponse.Data(
            set = set
        )

        val response = PutEditSupplierResponse(
            status = 200,
            message = "Success",
            data = data
        )

        // Assert Response Values
        assertThat(response.status).isEqualTo(200)
        assertThat(response.message).isEqualTo("Success")

        // Assert Data
        assertThat(response.data).isEqualTo(data)
        val responseData = response.data

        // Assert Set
        assertThat(responseData.set).isEqualTo(set)
        val responseSet = responseData.set

        assertThat(responseSet.status).isTrue()
        assertThat(responseSet.companyName).isEqualTo("Test Company")
        assertThat(responseSet.country).isEqualTo("Test Country")
        assertThat(responseSet.state).isEqualTo("Test State")
        assertThat(responseSet.companyLocation).isEqualTo("123 Test St")
        assertThat(responseSet.companyPhoneNumber).isEqualTo(1234567890L)
        assertThat(responseSet.zipCode).isEqualTo(90210)
        assertThat(responseSet.picName).isEqualTo("Test PIC")
        assertThat(responseSet.picPhoneNumber).isEqualTo(9876543210L)
        assertThat(responseSet.picEmail).isEqualTo("test@example.com")
        assertThat(responseSet.updatedAt).isEqualTo("2023-10-27T10:00:00Z")

        // Assert Item
        assertThat(responseSet.item).hasSize(1)
        assertThat(responseSet.item[0]).isEqualTo(item)
        val responseItem = responseSet.item[0]
        assertThat(responseItem.itemName).isEqualTo("Test Item")
        assertThat(responseItem.sku).containsExactly("SKU123", "SKU456")
    }
}