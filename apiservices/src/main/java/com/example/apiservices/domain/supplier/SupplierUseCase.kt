package com.example.apiservices.domain.supplier

import com.example.apiservices.data.repository.supplier.SupplierRepository
import com.example.apiservices.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierQueryParams
import javax.inject.Inject

class GetSuppliersUseCase @Inject constructor(private val repository: SupplierRepository) {
    operator fun invoke(query: GetSupplierQueryParams) = repository.getSupplierList(query)
}

class GetSupplierByIdUseCase @Inject constructor(private val repository: SupplierRepository) {
    operator fun invoke(id: String) = repository.getSupplierById(id)
}

class GetSupplierOptionUseCase @Inject constructor(private val repository: SupplierRepository) {
    operator fun invoke(query: GetSupplierOptionQueryParams) =
        repository.getSupplierOption(query)
}

class CreateSupplierUseCase @Inject constructor(private val repository: SupplierRepository) {
    operator fun invoke(body: CreateUpdateSupplierBody) = repository.createSupplier(body)
}

class DeleteSupplierUseCase @Inject constructor(private val repository: SupplierRepository) {
    operator fun invoke(body: DeleteSupplierBody) = repository.deleteSupplier(body)
}

class EditSupplierUseCase @Inject constructor(private val repository: SupplierRepository) {
    operator fun invoke(id: String, body: CreateUpdateSupplierBody) =
        repository.editSupplier(id, body)
}