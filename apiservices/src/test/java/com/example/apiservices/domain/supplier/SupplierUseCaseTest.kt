package com.example.apiservices.domain.supplier

import com.example.apiservices.base.Result
import com.example.apiservices.data.model.SupplierEntity
import com.example.apiservices.data.model.SupplierOptionEntity
import com.example.apiservices.data.repository.supplier.SupplierRepository
import com.example.apiservices.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.apiservices.data.source.network.model.request.supplier.PatchEditStatusSupplierBody
import com.example.apiservices.util.Constant
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SupplierUseCaseTest {
    private val supplierRepository: SupplierRepository = mockk()

    // GetSupplierUseCase Test
    private val getSupplierUseCase = GetSuppliersUseCase(supplierRepository)

    @Test
    fun `invoke returns list of supplier`() = runTest {
        // Arrange
        val query = GetSupplierQueryParams()
        val expected = listOf(
            SupplierEntity(
                id = "id",
                status = false,
                companyName = "companyName",
                item = listOf(
                    SupplierEntity.Item(
                        itemName = "itemName",
                        itemSku = listOf(
                            "Sku"
                        )
                    )
                ),
                country = "country",
                state = "state",
                city = "city",
                zipCode = "zipCode",
                companyLocation = "companyLocation",
                countryCode = "countryCode",
                companyPhoneNumber = "companyPhoneNumber",
                picName = "picName",
                picCountryCode = "picCountryCode",
                picPhoneNumber = "picPhoneNumber",
                picEmail = "picEmail",
                modifiedBy = "modifiedBy",
                updatedAt = "updatedAt",
                createdAt = "createdAt"
            )
        )

        coEvery {
            supplierRepository.getSupplierList(query)
        } returns flowOf(Result.Success(expected))

        // Act
        val result = getSupplierUseCase(query).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expected)
    }

    @Test
    fun `invoke returns empty list of supplier`() = runTest {
        // Arrange
        val query = GetSupplierQueryParams()
        val expected = emptyList<SupplierEntity>()

        coEvery {
            supplierRepository.getSupplierList(query)
        } returns flowOf(Result.Success(expected))

        // Act
        val result = getSupplierUseCase(query).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expected)
    }

    @Test
    fun `(getSupplierUseCase) when repository fails invoke returns error`() = runTest {
        // Arrange
        val query = GetSupplierQueryParams()
        val error = Constant.UNEXPECTED_ERROR

        coEvery {
            supplierRepository.getSupplierList(query)
        } returns flowOf(Result.Error(error))

        // Act
        val result = getSupplierUseCase(query).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(error)
    }

    // GetSupplierByIdUseCase Test
    private val getSupplierByIdUseCase = GetSupplierByIdUseCase(supplierRepository)

    @Test
    fun `invoke returns supplier by id`() = runTest {
        // Arrange
        val id = "id"
        val expected =
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

        coEvery {
            supplierRepository.getSupplierById(id)
        } returns flowOf(Result.Success(expected))

        // Act
        val result = getSupplierByIdUseCase.invoke(id).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expected)
    }

    @Test
    fun `(getSupplierByIdUseCase) when repository fails invoke returns error`() = runTest {
        // Arrange
        val id = "id"
        val error = Constant.UNEXPECTED_ERROR

        coEvery {
            supplierRepository.getSupplierById(id)
        } returns flowOf(Result.Error(error))

        // Act
        val result = getSupplierByIdUseCase.invoke(id).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(error)
    }

    // GetSupplierOptionUseCase
    private val getSupplierOptionUseCase = GetSupplierOptionUseCase(supplierRepository)

    @Test
    fun `invoke returns list of options`() = runTest {
        // Arrange
        val query = GetSupplierOptionQueryParams()
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

        val expected = SupplierOptionEntity(
            itemNameOption = itemNameOption,
            supplierOption = supplierOption,
            cityOption = cityOption,
            modifiedByOption = modifiedByOption,
            statusOption = statusOption
        )

        coEvery {
            supplierRepository.getSupplierOption(query)
        } returns flowOf(Result.Success(expected))

        // Act
        val result = getSupplierOptionUseCase.invoke(query).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expected)
    }

    @Test
    fun `invoke returns list of empty options`() = runTest {
        // Arrange
        val query = GetSupplierOptionQueryParams()
        val expected = SupplierOptionEntity(
            itemNameOption = emptyList(),
            cityOption = emptyList(),
            supplierOption = emptyList(),
            modifiedByOption = emptyList(),
            statusOption = emptyList()
        )

        coEvery {
            supplierRepository.getSupplierOption(query)
        } returns flowOf(Result.Success(expected))

        // Act
        val result = getSupplierOptionUseCase.invoke(query).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expected)
    }

    @Test
    fun `(getSupplierOptionUseCase) when repository fails invoke returns error`() = runTest {
        // Arrange
        val query = GetSupplierOptionQueryParams()
        val error = Constant.UNEXPECTED_ERROR

        coEvery {
            supplierRepository.getSupplierOption(query)
        } returns flowOf(Result.Error(error))

        // Act
        val result = getSupplierOptionUseCase.invoke(query).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(error)
    }

    // CreateSupplierUseCase Test
    private val createSupplierUseCase = CreateSupplierUseCase(supplierRepository)

    @Test
    fun `invoke creates supplier successfully`() = runTest {
        // Arrange
        val body =
            CreateUpdateSupplierBody(
                companyName = "companyName",
                item = listOf(
                    CreateUpdateSupplierBody.Item(
                        itemName = "itemName",
                        sku = listOf(
                            "Sku"
                        )
                    )
                ),
                country = "country",
                state = "state",
                city = "city",
                zipCode = "zipCode",
                companyLocation = "companyLocation",
                companyPhoneNumber = "companyPhoneNumber",
                picName = "picName",
                picPhoneNumber = "picPhoneNumber",
                picEmail = "picEmail"
            )

        coEvery {
            supplierRepository.createSupplier(body)
        } returns flowOf(Result.Success(Unit))

        // Act
        val result = createSupplierUseCase.invoke(body).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(Unit)
    }

    @Test
    fun `invoke returns error when creation fails`() = runTest {
        // Arrange
        val body = CreateUpdateSupplierBody()
        val error = Constant.UNEXPECTED_ERROR

        coEvery {
            supplierRepository.createSupplier(body)
        } returns flowOf(Result.Error(error))

        // Act
        val result = createSupplierUseCase.invoke(body).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(error)
    }

    // DeleteSupplierUseCase Test
    private val deleteSupplierUseCase = DeleteSupplierUseCase(supplierRepository)

    @Test
    fun `invoke deletes supplier successfully`() = runTest {
        // Arrange
        val body =
            DeleteSupplierBody(
                listOf(
                    "SupplierID 1",
                    "SupplierID 2"
                )
            )

        coEvery {
            supplierRepository.deleteSupplier(body)
        } returns flowOf(Result.Success(Unit))

        // Act
        val result = deleteSupplierUseCase.invoke(body).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(Unit)
    }

    @Test
    fun `invoke returns error when deletion fails`() = runTest {
        // Arrange
        val body = DeleteSupplierBody()
        val error = Constant.UNEXPECTED_ERROR

        coEvery {
            supplierRepository.deleteSupplier(body)
        } returns flowOf(Result.Error(error))

        // Act
        val result = deleteSupplierUseCase.invoke(body).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(error)
    }

    // EditSupplierUseCase Test
    private val editSupplierUseCase = EditSupplierUseCase(supplierRepository)

    @Test
    fun `invoke edits supplier successfully`() = runTest {
        // Arrange
        val id = "id"
        val body =
            CreateUpdateSupplierBody(
                companyName = "companyName",
                item = listOf(
                    CreateUpdateSupplierBody.Item(
                        itemName = "itemName",
                        sku = listOf(
                            "Sku"
                        )
                    )
                ),
                country = "country",
                state = "state",
                city = "city",
                zipCode = "zipCode",
                companyLocation = "companyLocation",
                companyPhoneNumber = "companyPhoneNumber",
                picName = "picName",
                picPhoneNumber = "picPhoneNumber",
                picEmail = "picEmail"
            )

        coEvery {
            supplierRepository.editSupplier(id, body)
        } returns flowOf(Result.Success(Unit))

        // Act
        val result = editSupplierUseCase.invoke(id, body).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(Unit)
    }

    @Test
    fun `invoke returns error when edition fails`() = runTest {
        // Arrange
        val id = "id"
        val body = CreateUpdateSupplierBody()
        val error = Constant.UNEXPECTED_ERROR

        coEvery {
            supplierRepository.editSupplier(id, body)
        } returns flowOf(Result.Error(error))

        // Act
        val result = editSupplierUseCase.invoke(id, body).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(error)
    }

    // DeleteSupplierUseCase Test
    private val editStatusSupplierUseCase = EditStatusSupplierUseCase(supplierRepository)

    @Test
    fun `invoke edits supplier's status successfully`() = runTest {
        // Arrange
        val body =
            PatchEditStatusSupplierBody(
                listOf(
                    "SupplierID 1",
                    "SupplierID 2"
                )
            )

        coEvery {
            supplierRepository.editStatusSupplier(body)
        } returns flowOf(Result.Success(Unit))

        // Act
        val result = editStatusSupplierUseCase.invoke(body).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(Unit)
    }

    @Test
    fun `invoke returns error when status edition fails`() = runTest {
        // Arrange
        val body = PatchEditStatusSupplierBody()
        val error = Constant.UNEXPECTED_ERROR

        coEvery {
            supplierRepository.editStatusSupplier(body)
        } returns flowOf(Result.Error(error))

        // Act
        val result = editStatusSupplierUseCase.invoke(body).first()

        // Assert
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo(error)
    }
}