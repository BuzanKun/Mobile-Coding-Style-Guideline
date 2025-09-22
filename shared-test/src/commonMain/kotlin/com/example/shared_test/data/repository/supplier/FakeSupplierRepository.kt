package com.example.shared_test.data.repository.supplier

import com.example.shared.base.Result
import com.example.shared.data.model.SupplierEntity
import com.example.shared.data.model.SupplierOptionEntity
import com.example.shared.data.repository.supplier.SupplierRepository
import com.example.shared.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.shared.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.shared.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.shared.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.shared.data.source.network.model.request.supplier.PatchEditStatusSupplierBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * A Fake implementation of the SupplierRepository for testing.
 * It simulates the behavior of the real repository using an in-memory list.
 * This is fully KMP-compatible and works on all targets.
 */
@OptIn(ExperimentalTime::class)
class FakeSupplierRepository : SupplierRepository {

    private val suppliers = mutableListOf<SupplierEntity>()
    private var shouldReturnError = false
    private var nextId = 1

    /**
     * Toggles the behavior of the fake to return a generic error for all operations.
     */
    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    /**
     * Pre-populates the in-memory list with data before a test runs.
     */
    fun addInitialSuppliers(initialSuppliers: List<SupplierEntity>) {
        suppliers.addAll(initialSuppliers)
        nextId = (initialSuppliers.maxOfOrNull { it.id.toIntOrNull() ?: 0 } ?: 0) + 1
    }

    /**
     * Resets the state of the fake, clearing all suppliers and error states.
     * Should be called in a `@BeforeTest` block.
     */
    fun clear() {
        suppliers.clear()
        shouldReturnError = false
        nextId = 1
    }

    /**
     * Provides direct, non-Flow access to the internal list for making assertions.
     */
    fun getSuppliersData(): List<SupplierEntity> = suppliers.toList()


    // --- Interface Implementation ---

    override fun getSupplierList(query: GetSupplierQueryParams): Flow<Result<List<SupplierEntity>>> {
        if (shouldReturnError) {
            return flowOf(Result.Error("Fake Repository: Network Error"))
        }
        val filteredList = suppliers.filter {
            query.search?.let { s -> it.companyName.contains(s, ignoreCase = true) } ?: true
        }
        return flowOf(Result.Success(filteredList))
    }

    override fun getSupplierById(path: String): Flow<Result<SupplierEntity>> {
        if (shouldReturnError) {
            return flowOf(Result.Error("Fake Repository: Network Error"))
        }

        val supplier = suppliers.find { it.id == path }
        return if (supplier != null) {
            flowOf(Result.Success(supplier))
        } else {
            flowOf(Result.Error("Supplier with id '$path' not found."))
        }
    }

    override fun getSupplierOption(query: GetSupplierOptionQueryParams): Flow<Result<SupplierOptionEntity>> {
        if (shouldReturnError) {
            return flowOf(Result.Error("Fake Repository: Network Error"))
        }

        // Return dummy/mocked options. The exact data is usually not important for tests.
        val options = SupplierOptionEntity(
            supplierOption = suppliers.map {
                SupplierOptionEntity.OptionData(
                    it.companyName,
                    it.companyName
                )
            },
            cityOption = suppliers.map { SupplierOptionEntity.OptionData(it.city, it.city) }
                .distinct(),
            itemNameOption = suppliers.flatMap { supplier ->
                supplier.item.map {
                    SupplierOptionEntity.OptionData(
                        it.itemName,
                        it.itemName
                    )
                }
            }.distinct(),
            modifiedByOption = suppliers.map {
                SupplierOptionEntity.OptionData(
                    it.modifiedBy,
                    it.modifiedBy
                )
            }
        )
        return flowOf(Result.Success(options))
    }

    override fun createSupplier(body: CreateUpdateSupplierBody): Flow<Result<Unit>> {
        if (shouldReturnError) {
            return flowOf(Result.Error("Fake Repository: Network Error"))
        }

        val newSupplier = SupplierEntity(
            id = (nextId++).toString(),
            companyName = body.companyName,
            status = true,
            item = body.item.map { SupplierEntity.Item(itemName = it.itemName, sku = it.sku) },
            country = body.country,
            state = body.state,
            city = body.city,
            zipCode = body.zipCode,
            companyLocation = body.companyLocation,
            companyPhoneNumber = body.companyPhoneNumber,
            picName = body.picName,
            picPhoneNumber = body.picPhoneNumber,
            picEmail = body.picEmail,
            modifiedBy = "Test User",
            createdAt = Clock.System.now().toString(),
            updatedAt = Clock.System.now().toString()
        )
        suppliers.add(newSupplier)
        return flowOf(Result.Success(Unit))
    }

    override fun deleteSupplier(body: DeleteSupplierBody): Flow<Result<Unit>> {
        if (shouldReturnError) {
            return flowOf(Result.Error("Fake Repository: Network Error"))
        }

        val initialSize = suppliers.size
        suppliers.removeAll { it.id in body.supplierID }

        return if (suppliers.size < initialSize) {
            flowOf(Result.Success(Unit))
        } else {
            flowOf(Result.Error("No suppliers found with the given IDs to delete."))
        }
    }

    override fun editSupplier(path: String, body: CreateUpdateSupplierBody): Flow<Result<Unit>> {
        if (shouldReturnError) {
            return flowOf(Result.Error("Fake Repository: Network Error"))
        }

        val index = suppliers.indexOfFirst { it.id == path }
        return if (index != -1) {
            val existingSupplier = suppliers[index]
            suppliers[index] = existingSupplier.copy(
                companyName = body.companyName,
                item = body.item.map { SupplierEntity.Item(itemName = it.itemName, sku = it.sku) },
                country = body.country,
                state = body.state,
                city = body.city,
                zipCode = body.zipCode,
                companyLocation = body.companyLocation,
                companyPhoneNumber = body.companyPhoneNumber,
                picName = body.picName,
                picPhoneNumber = body.picPhoneNumber,
                picEmail = body.picEmail,
                updatedAt = Clock.System.now().toString()
            )
            flowOf(Result.Success(Unit))
        } else {
            flowOf(Result.Error("Supplier with id '$path' not found."))
        }
    }

    override fun editStatusSupplier(body: PatchEditStatusSupplierBody): Flow<Result<Unit>> {
        if (shouldReturnError) {
            return flowOf(Result.Error("Fake Repository: Network Error"))
        }

        var updatedCount = 0
        suppliers.forEachIndexed { index, supplier ->
            if (supplier.id in body.supplierID) {
                suppliers[index] = supplier.copy(status = body.status)
                updatedCount++
            }
        }

        return if (updatedCount > 0) {
            flowOf(Result.Success(Unit))
        } else {
            flowOf(Result.Error("No suppliers found with the given IDs to update status."))
        }
    }
}
