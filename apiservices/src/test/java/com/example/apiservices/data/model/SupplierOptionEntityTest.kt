package com.example.apiservices.data.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SupplierOptionEntityTest {
    @Test
    fun `constructor should handle empty lists`() {
        // Arrange
        val supplierOptionEntity = SupplierOptionEntity(
            itemNameOption = emptyList(),
            supplierOption = emptyList(),
            cityOption = emptyList(),
            modifiedByOption = emptyList(),
            statusOption = emptyList()
        )

        // Assert
        assertThat(supplierOptionEntity.itemNameOption).isEmpty()
        assertThat(supplierOptionEntity.supplierOption).isEmpty()
        assertThat(supplierOptionEntity.cityOption).isEmpty()
        assertThat(supplierOptionEntity.modifiedByOption).isEmpty()
        assertThat(supplierOptionEntity.statusOption).isEmpty()
    }

    @Test
    fun `constructor should handle empty values with default ones`() {
        // Arrange
        val supplierOptionEntity = SupplierOptionEntity()

        // Assert
        assertThat(supplierOptionEntity.itemNameOption).isEmpty()
        assertThat(supplierOptionEntity.supplierOption).isEmpty()
        assertThat(supplierOptionEntity.cityOption).isEmpty()
        assertThat(supplierOptionEntity.modifiedByOption).isEmpty()
        assertThat(supplierOptionEntity.statusOption).isEmpty()
    }

    @Test
    fun `constructor should handle given values`() {
        // Arrange
        val itemNameOption = listOf(
            SupplierOptionEntity.OptionData(
                label = "Item Label 1",
                value = "Item Value 1"
            ),
            SupplierOptionEntity.OptionData(
                label = "Item Label 2",
                value = "Item Value 2"
            )
        )

        val supplierOption = listOf(
            SupplierOptionEntity.OptionData(
                label = "Supplier Label 1",
                value = "Supplier Value 1"
            ),
            SupplierOptionEntity.OptionData(
                label = "Supplier Label 2",
                value = "Supplier Value 2"
            )
        )

        val cityOption = listOf(
            SupplierOptionEntity.OptionData(
                label = "City Label 1",
                value = "City Value 1"
            ),
            SupplierOptionEntity.OptionData(
                label = "City Label 2",
                value = "City Value 2"
            )
        )

        val modifiedByOption = listOf(
            SupplierOptionEntity.OptionData(
                label = "Modifier Label 1",
                value = "Modifier Value 1"
            ),
            SupplierOptionEntity.OptionData(
                label = "Modifier Label 2",
                value = "Modifier Value 2"
            )
        )

        val statusOption = listOf(
            SupplierOptionEntity.OptionData(
                label = "Status Label 1",
                value = "Status Value 1"
            ),
            SupplierOptionEntity.OptionData(
                label = "Status Label 2",
                value = "Status Value 2"
            )
        )

        // Act
        val supplierOptionEntity = SupplierOptionEntity(
            itemNameOption = itemNameOption,
            supplierOption = supplierOption,
            cityOption = cityOption,
            modifiedByOption = modifiedByOption,
            statusOption = statusOption
        )

        // Assert
        assertThat(supplierOptionEntity.itemNameOption).hasSize(2)
        assertThat(supplierOptionEntity.itemNameOption[0].label).isEqualTo("Item Label 1")
        assertThat(supplierOptionEntity.itemNameOption[0].value).isEqualTo("Item Value 1")
        assertThat(supplierOptionEntity.itemNameOption[1].label).isEqualTo("Item Label 2")
        assertThat(supplierOptionEntity.itemNameOption[1].value).isEqualTo("Item Value 2")

        assertThat(supplierOptionEntity.supplierOption).hasSize(2)
        assertThat(supplierOptionEntity.supplierOption[0].label).isEqualTo("Supplier Label 1")
        assertThat(supplierOptionEntity.supplierOption[0].value).isEqualTo("Supplier Value 1")
        assertThat(supplierOptionEntity.supplierOption[1].label).isEqualTo("Supplier Label 2")
        assertThat(supplierOptionEntity.supplierOption[1].value).isEqualTo("Supplier Value 2")

        assertThat(supplierOptionEntity.cityOption).hasSize(2)
        assertThat(supplierOptionEntity.cityOption[0].label).isEqualTo("City Label 1")
        assertThat(supplierOptionEntity.cityOption[0].value).isEqualTo("City Value 1")
        assertThat(supplierOptionEntity.cityOption[1].label).isEqualTo("City Label 2")
        assertThat(supplierOptionEntity.cityOption[1].value).isEqualTo("City Value 2")

        assertThat(supplierOptionEntity.modifiedByOption).hasSize(2)
        assertThat(supplierOptionEntity.modifiedByOption[0].label).isEqualTo("Modifier Label 1")
        assertThat(supplierOptionEntity.modifiedByOption[0].value).isEqualTo("Modifier Value 1")
        assertThat(supplierOptionEntity.modifiedByOption[1].label).isEqualTo("Modifier Label 2")
        assertThat(supplierOptionEntity.modifiedByOption[1].value).isEqualTo("Modifier Value 2")

        assertThat(supplierOptionEntity.statusOption).hasSize(2)
        assertThat(supplierOptionEntity.statusOption[0].label).isEqualTo("Status Label 1")
        assertThat(supplierOptionEntity.statusOption[0].value).isEqualTo("Status Value 1")
        assertThat(supplierOptionEntity.statusOption[1].label).isEqualTo("Status Label 2")
        assertThat(supplierOptionEntity.statusOption[1].value).isEqualTo("Status Value 2")
    }
}