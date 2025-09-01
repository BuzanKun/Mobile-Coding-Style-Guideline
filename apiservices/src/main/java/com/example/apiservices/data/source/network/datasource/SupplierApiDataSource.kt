package com.example.apiservices.data.source.network.datasource

import com.example.apiservices.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.apiservices.data.source.network.model.request.supplier.PatchEditStatusSupplierBody
import com.example.apiservices.data.source.network.model.response.supplier.CreateSupplierResponse
import com.example.apiservices.data.source.network.model.response.supplier.DeleteSupplierResponse
import com.example.apiservices.data.source.network.model.response.supplier.GetSupplierByIdResponse
import com.example.apiservices.data.source.network.model.response.supplier.GetSupplierOptionResponse
import com.example.apiservices.data.source.network.model.response.supplier.GetSupplierResponse
import com.example.apiservices.data.source.network.model.response.supplier.PatchEditStatusSupplierResponse
import com.example.apiservices.data.source.network.model.response.supplier.PutEditSupplierResponse
import retrofit2.Response

interface SupplierApiDataSource {
    suspend fun getSuppliers(
        token: String,
        query: GetSupplierQueryParams
    ): Response<GetSupplierResponse>

    suspend fun getSupplierById(
        token: String,
        id: String
    ): Response<GetSupplierByIdResponse>

    suspend fun getSupplierOption(
        token: String,
        query: GetSupplierOptionQueryParams
    ): Response<GetSupplierOptionResponse>

    suspend fun createSupplier(
        token: String,
        body: CreateUpdateSupplierBody
    ): Response<CreateSupplierResponse>

    suspend fun deleteSupplier(
        token: String,
        body: DeleteSupplierBody
    ): Response<DeleteSupplierResponse>

    suspend fun editSupplier(
        token: String,
        id: String,
        body: CreateUpdateSupplierBody
    ): Response<PutEditSupplierResponse>

    suspend fun editStatusSupplier(
        token: String,
        body: PatchEditStatusSupplierBody
    ): Response<PatchEditStatusSupplierResponse>
}