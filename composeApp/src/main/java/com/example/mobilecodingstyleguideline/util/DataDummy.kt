package com.example.mobilecodingstyleguideline.util

import com.example.shared.data.model.SupplierEntity
import com.tagsamurai.common.model.OptionData

data class Sku(
    val id: String
)

data class Item(
    val name: String,
    val avalaibleSKUs: List<Sku>
)

data class OrderItem(
    val id: Int? = null,
    val item: Item,
    val orderedSku: List<Sku>
)

val itemLaptop = Item(
    name = "Laptop",
    avalaibleSKUs = listOf(
        Sku(id = "L01-A-1"),
        Sku(id = "L01-A-2"),
        Sku(id = "L01-A-3")
    )
)

val itemKulkas = Item(
    name = "Kulkas",
    avalaibleSKUs = listOf(
        Sku(id = "K01-B-1"),
        Sku(id = "K01-B-2"),
        Sku(id = "K01-B-3")
    )
)

val itemTv = Item(
    name = "TV",
    avalaibleSKUs = listOf(
        Sku(id = "T01-C-1"),
        Sku(id = "T01-C-2"),
        Sku(id = "T01-C-3")
    )
)

val itemAc = Item(
    name = "AC",
    avalaibleSKUs = listOf(
        Sku(id = "A01-D-1"),
        Sku(id = "A01-D-2"),
        Sku(id = "A01-D-3")
    )
)

object DataDummy {
    fun getAssets(): List<SupplierEntity> {
        return listOf(
            SupplierEntity()
        )
    }

    fun getItemMasterList(): List<Item> {
        val itemMasterList = listOf(itemKulkas, itemTv, itemAc, itemLaptop)
        return itemMasterList
    }

    fun generateOptionsDataString(list: List<String>): List<OptionData<String>> {
        return list.map { OptionData(it, it) }
    }

    fun getActive(): List<OptionData<Boolean>> {
        return listOf(
            OptionData("Active", true),
            OptionData("Inactive", false)
        )
    }

    fun getSupplier(): List<String> {
        return getAssets().map { it.companyName }.distinct()
    }

    fun getCountry(): List<String> {
        return getAssets().map { it.country }.distinct()
    }

    fun getState(): List<String> {
        return getAssets().map { it.state }.distinct()
    }

    fun getCity(): List<String> {
        return getAssets().map { it.city }.distinct()
    }

    fun getItemName(): List<String> {
        return getAssets().flatMap { asset -> asset.item.map { orderItem -> orderItem.itemName } }
            .distinct()
    }

    fun getItemSku(): List<String> {
        return getAssets()
            .flatMap { it.item }
            .flatMap { it.sku }
            .distinct()
    }

    fun getModifiedBy(): List<String> {
        return getAssets().map { it.picName }.distinct()
    }

}