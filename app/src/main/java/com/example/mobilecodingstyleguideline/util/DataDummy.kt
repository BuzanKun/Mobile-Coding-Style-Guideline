package com.example.mobilecodingstyleguideline.util

import com.tagsamurai.common.model.OptionData

data class Asset(
    val id: String = "",
    val active: Boolean = true,
    val name: String = "",
    val orderList: List<OrderItem> = emptyList(),
    val country: String = "",
    val state: String = "",
    val city: String = "",
    val location: String = "",
    val zipCode: String = "",
    val address: String = "",
    val countryCode: String = "",
    val phoneNumber: String = "",
    val picName: String = "",
    val picCountryCode: String = "",
    val picPhoneNumber: String = "",
    val picEmail: String = "",
    val lastModified: Long = 0L
)

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
    fun getAssets(): List<Asset> {
        return listOf(
            Asset(
                id = "1",
                active = true,
                name = "PT. ABC Indonesia",
                country = "Indonesia",
                state = "DKI Jakarta",
                city = "Jakarta Utara",
                location = "Jakarta Utara, Indonesia",
                orderList = listOf(
                    OrderItem(
                        id = 1,
                        item = itemTv,
                        orderedSku = listOf(
                            itemTv.avalaibleSKUs[0],
                            itemTv.avalaibleSKUs[1]
                        )
                    )
                ),
                picName = "Nakamoto Y",
                lastModified = 1724212800L
            ),
            Asset(
                id = "2",
                active = false,
                name = "PT. Sinar Mas Dunia",
                country = "Indonesia",
                state = "DKI Jakarta",
                city = "Jakarta Pusat",
                location = "Jakarta Pusat, Indonesia",
                orderList = listOf(
                    OrderItem(
                        id = 1,
                        item = itemKulkas,
                        orderedSku = listOf(
                            itemKulkas.avalaibleSKUs[0],
                            itemKulkas.avalaibleSKUs[1]
                        )
                    ),
                    OrderItem(
                        id = 2,
                        item = itemAc,
                        orderedSku = listOf(
                            itemAc.avalaibleSKUs[0],
                            itemAc.avalaibleSKUs[1]
                        )
                    ),
                ),
                picName = "Mark L",
                lastModified = 1724385600L
            ),
            Asset(
                id = "3",
                active = true,
                name = "PT. GHI Indonesia",
                country = "Indonesia",
                state = "DKI Jakarta",
                city = "Jakarta Timur",
                location = "Jakarta Timur, Indonesia",
                orderList = listOf(
                    OrderItem(
                        id = 1,
                        item = itemLaptop,
                        orderedSku = listOf(
                            itemLaptop.avalaibleSKUs[0],
                            itemLaptop.avalaibleSKUs[1]
                        )
                    ),
                    OrderItem(
                        id = 2,
                        item = itemKulkas,
                        orderedSku = listOf(
                            itemKulkas.avalaibleSKUs[0],
                            itemKulkas.avalaibleSKUs[1]
                        )
                    ),
                    OrderItem(
                        id = 3,
                        item = itemAc,
                        orderedSku = listOf(
                            itemAc.avalaibleSKUs[0],
                            itemAc.avalaibleSKUs[1]
                        )
                    )
                ),
                picName = "Karina Y",
                lastModified = 1724558400L
            ),
            Asset(
                id = "4",
                active = false,
                name = "PT. Ichitan Indonesia",
                country = "Indonesia",
                state = "DKI Jakarta",
                city = "Jakarta Selatan",
                location = "Jakarta Selatan, Indonesia",
                orderList = listOf(
                    OrderItem(
                        id = 1,
                        item = itemAc,
                        orderedSku = listOf(
                            itemAc.avalaibleSKUs[0],
                            itemAc.avalaibleSKUs[1]
                        )
                    ),
                    OrderItem(
                        id = 2,
                        item = itemKulkas,
                        orderedSku = listOf(
                            itemKulkas.avalaibleSKUs[0],
                            itemKulkas.avalaibleSKUs[1]
                        )
                    ),
                    OrderItem(
                        id = 3,
                        item = itemLaptop,
                        orderedSku = listOf(
                            itemLaptop.avalaibleSKUs[0],
                            itemLaptop.avalaibleSKUs[1]
                        )
                    ),
                    OrderItem(
                        id = 4,
                        item = itemTv,
                        orderedSku = listOf(
                            itemTv.avalaibleSKUs[0],
                            itemTv.avalaibleSKUs[1]
                        )
                    )
                ),
                picName = "Hong E",
                lastModified = 1724731200L
            ),
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
        return getAssets().map { it.name }.distinct()
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
        return getAssets().flatMap { asset -> asset.orderList.map { orderItem -> orderItem.item.name } }
            .distinct()
    }

    fun getItemSku(): List<String> {
        return getAssets()
            .flatMap { it.orderList }
            .flatMap { it.orderedSku }
            .map { it.id }
            .distinct()
    }

    fun getModifiedBy(): List<String> {
        return getAssets().map { it.picName }.distinct()
    }

}