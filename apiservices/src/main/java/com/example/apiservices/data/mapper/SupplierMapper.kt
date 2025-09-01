package com.example.apiservices.data.mapper

import com.example.apiservices.data.model.SupplierEntity
import com.example.apiservices.data.model.SupplierOptionEntity
import com.example.apiservices.data.source.network.model.response.supplier.GetSupplierByIdResponse
import com.example.apiservices.data.source.network.model.response.supplier.GetSupplierOptionResponse
import com.example.apiservices.data.source.network.model.response.supplier.GetSupplierResponse
import javax.inject.Inject

class SupplierMapper @Inject constructor() {
    fun mapSupplier(data: List<GetSupplierResponse.Data.Data>): List<SupplierEntity> {
        return data.map {
            SupplierEntity(
                id = it.id,
                status = it.status,
                companyName = it.companyName,
                item = mapSupplierListItems(it.item),
                country = it.country,
                state = it.state,
                city = it.city,
                picName = it.picName,
                modifiedBy = it.modifiedBy,
                updatedAt = it.updatedAt,
                createdAt = it.createdAt
            )
        }
    }

    fun mapSupplierById(data: GetSupplierByIdResponse.Data): SupplierEntity {
        return SupplierEntity(
            id = data.id,
            status = data.status,
            companyName = data.companyName,
            item = mapSupplierDetailItems(data.item),
            country = data.country,
            state = data.state,
            city = data.city,
            zipCode = data.zipCode,
            companyLocation = data.companyLocation,
            companyPhoneNumber = data.companyPhoneNumber,
            picName = data.picName,
            picPhoneNumber = data.picPhoneNumber,
            picEmail = data.picEmail,
            modifiedBy = data.modifiedBy,
            updatedAt = data.updatedAt,
            createdAt = data.createdAt
        )
    }

    fun mapSupplierDetailItems(data: List<GetSupplierByIdResponse.Data.Item>): List<SupplierEntity.Item> {
        return data.map {
            SupplierEntity.Item(
                id = it.id,
                supplierId = it.supplierId,
                itemName = it.itemName,
                itemSku = it.sku
            )
        }
    }

    fun mapSupplierListItems(data: List<GetSupplierResponse.Data.Data.Item>): List<SupplierEntity.Item> {
        return data.map {
            SupplierEntity.Item(
                id = it.id,
                supplierId = it.supplierId,
                itemName = it.itemName,
                itemSku = it.sku
            )
        }
    }

    fun mapSupplierOption(data: GetSupplierOptionResponse.Data): SupplierOptionEntity {
        return SupplierOptionEntity(
            itemNameOption = mapOptions(data.itemNameOption),
            supplierOption = mapOptions(data.supplierOption),
            cityOption = mapOptions(data.cityOption),
            modifiedByOption = mapOptions(data.modifiedByOption)
        )
    }

    fun mapOptions(data: List<GetSupplierOptionResponse.Data.OptionData>): List<SupplierOptionEntity.OptionData> {
        return data.map {
            SupplierOptionEntity.OptionData(
                label = it.label,
                value = it.value
            )
        }
    }
}