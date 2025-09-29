package com.example.shared.data.source.network.datasource

import com.example.shared.base.ApiResponse
import com.example.shared.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.shared.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.shared.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.shared.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.shared.data.source.network.model.request.supplier.PatchEditStatusSupplierBody
import com.example.shared.data.source.network.model.response.supplier.CreateSupplierResponse
import com.example.shared.data.source.network.model.response.supplier.DeleteSupplierResponse
import com.example.shared.data.source.network.model.response.supplier.GetSupplierByIdResponse
import com.example.shared.data.source.network.model.response.supplier.GetSupplierOptionResponse
import com.example.shared.data.source.network.model.response.supplier.GetSupplierResponse
import com.example.shared.data.source.network.model.response.supplier.PatchEditStatusSupplierResponse
import com.example.shared.data.source.network.model.response.supplier.PutEditSupplierResponse

interface SupplierApiDataSource {
    suspend fun getSuppliers(
        query: GetSupplierQueryParams
    ): ApiResponse<GetSupplierResponse>

    suspend fun getSupplierById(
        id: String
    ): ApiResponse<GetSupplierByIdResponse>

    suspend fun getSupplierOption(
        query: GetSupplierOptionQueryParams
    ): ApiResponse<GetSupplierOptionResponse>

    suspend fun createSupplier(
        body: CreateUpdateSupplierBody
    ): ApiResponse<CreateSupplierResponse>

    suspend fun deleteSupplier(
        body: DeleteSupplierBody
    ): ApiResponse<DeleteSupplierResponse>

    suspend fun editSupplier(
        id: String,
        body: CreateUpdateSupplierBody
    ): ApiResponse<PutEditSupplierResponse>

    suspend fun editStatusSupplier(
        body: PatchEditStatusSupplierBody
    ): ApiResponse<PatchEditStatusSupplierResponse>
}