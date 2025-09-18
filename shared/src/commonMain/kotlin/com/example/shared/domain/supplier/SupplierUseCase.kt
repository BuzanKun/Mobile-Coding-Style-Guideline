package com.example.shared.domain.supplier

import com.example.shared.data.repository.supplier.SupplierRepository
import com.example.shared.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.shared.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.shared.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.shared.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.shared.data.source.network.model.request.supplier.PatchEditStatusSupplierBody

class GetSuppliersUseCase(private val repository: SupplierRepository) {
    operator fun invoke(query: GetSupplierQueryParams) = repository.getSupplierList(query)
}

class GetSupplierByIdUseCase(private val repository: SupplierRepository) {
    operator fun invoke(id: String) = repository.getSupplierById(id)
}

class GetSupplierOptionUseCase(private val repository: SupplierRepository) {
    operator fun invoke(query: GetSupplierOptionQueryParams) =
        repository.getSupplierOption(query)
}

class CreateSupplierUseCase(private val repository: SupplierRepository) {
    operator fun invoke(body: CreateUpdateSupplierBody) = repository.createSupplier(body)
}

class DeleteSupplierUseCase(private val repository: SupplierRepository) {
    operator fun invoke(body: DeleteSupplierBody) = repository.deleteSupplier(body)
}

class EditSupplierUseCase(private val repository: SupplierRepository) {
    operator fun invoke(id: String, body: CreateUpdateSupplierBody) =
        repository.editSupplier(id, body)
}

class EditStatusSupplierUseCase(private val repository: SupplierRepository) {
    operator fun invoke(body: PatchEditStatusSupplierBody) = repository.editStatusSupplier(body)
}