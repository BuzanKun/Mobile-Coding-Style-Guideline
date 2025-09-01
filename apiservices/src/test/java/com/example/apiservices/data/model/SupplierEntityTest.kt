package com.example.apiservices.data.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SupplierEntityTest {

    @Test
    fun `constructor should handle empty list`() {
        val supplierEntity = SupplierEntity(
            item = emptyList()
        )

        assertThat(supplierEntity.item).isEmpty()
    }

    @Test
    fun `constructor should handle empty values with default ones`() {
        val supplierEntity = SupplierEntity()

        assertThat(supplierEntity.id).isEmpty()
        assertThat(supplierEntity.status).isFalse()
        assertThat(supplierEntity.companyName).isEmpty()
        assertThat(supplierEntity.item).isEmpty()
        assertThat(supplierEntity.country).isEmpty()
        assertThat(supplierEntity.state).isEmpty()
        assertThat(supplierEntity.city).isEmpty()
        assertThat(supplierEntity.zipCode).isEmpty()
        assertThat(supplierEntity.companyLocation).isEmpty()
        assertThat(supplierEntity.companyPhoneNumber).isEmpty()
        assertThat(supplierEntity.picName).isEmpty()
        assertThat(supplierEntity.picPhoneNumber).isEmpty()
        assertThat(supplierEntity.picEmail).isEmpty()
        assertThat(supplierEntity.modifiedBy).isEmpty()
        assertThat(supplierEntity.createdAt).isEmpty()
        assertThat(supplierEntity.updatedAt).isEmpty()
    }

    @Test
    fun `constructor should handle given values`() {
        val supplierEntity =
            SupplierEntity(
                id = "id",
                companyName = "companyName",
                item = listOf(
                    SupplierEntity.Item(
                        id = "itemId",
                        supplierId = "supplierId",
                        itemName = "itemName",
                        itemSku = listOf("sku")
                    )
                ),
                country = "country",
                state = "state",
                city = "city",
                companyPhoneNumber = "123",
                companyLocation = "companyLocation",
                zipCode = "zipCode",
                picName = "picName",
                picPhoneNumber = "123",
                picEmail = "email@gmail.com",
                status = true,
                modifiedBy = "modifiedBy",
                updatedAt = "updatedAt",
                createdAt = "createdAt"
            )

        assertThat(supplierEntity.id).isEqualTo("id")
        assertThat(supplierEntity.companyName).isEqualTo("companyName")
        assertThat(supplierEntity.item[0].id).isEqualTo("itemId")
        assertThat(supplierEntity.item[0].supplierId).isEqualTo("supplierId")
        assertThat(supplierEntity.item[0].itemName).isEqualTo("itemName")
        assertThat(supplierEntity.item[0].itemSku[0]).isEqualTo("sku")
        assertThat(supplierEntity.country).isEqualTo("country")
        assertThat(supplierEntity.state).isEqualTo("state")
        assertThat(supplierEntity.city).isEqualTo("city")
        assertThat(supplierEntity.companyPhoneNumber).isEqualTo("123")
        assertThat(supplierEntity.companyLocation).isEqualTo("companyLocation")
        assertThat(supplierEntity.zipCode).isEqualTo("zipCode")
        assertThat(supplierEntity.picName).isEqualTo("picName")
        assertThat(supplierEntity.picPhoneNumber).isEqualTo("123")
        assertThat(supplierEntity.picEmail).isEqualTo("email@gmail.com")
        assertThat(supplierEntity.status).isTrue()
        assertThat(supplierEntity.modifiedBy).isEqualTo("modifiedBy")
        assertThat(supplierEntity.updatedAt).isEqualTo("updatedAt")
        assertThat(supplierEntity.createdAt).isEqualTo("createdAt")
    }
}