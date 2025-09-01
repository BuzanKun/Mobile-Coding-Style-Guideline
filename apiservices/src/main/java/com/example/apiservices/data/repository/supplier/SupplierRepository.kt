package com.example.apiservices.data.repository.supplier

import com.example.apiservices.base.Result
import com.example.apiservices.data.model.SupplierEntity
import com.example.apiservices.data.model.SupplierOptionEntity
import com.example.apiservices.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierQueryParams
import kotlinx.coroutines.flow.Flow

interface SupplierRepository {
    fun getSupplierList(query: GetSupplierQueryParams): Flow<Result<List<SupplierEntity>>>

    fun getSupplierById(path: String): Flow<Result<SupplierEntity>>

    fun getSupplierOption(query: GetSupplierOptionQueryParams): Flow<Result<SupplierOptionEntity>>

    fun createSupplier(body: CreateUpdateSupplierBody): Flow<Result<Unit>>

    fun deleteSupplier(body: DeleteSupplierBody): Flow<Result<Unit>>

    fun editSupplier(path: String, body: CreateUpdateSupplierBody): Flow<Result<Unit>>
}