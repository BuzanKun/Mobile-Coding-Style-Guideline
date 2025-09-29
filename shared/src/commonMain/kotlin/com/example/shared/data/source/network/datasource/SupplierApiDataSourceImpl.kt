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
import com.example.shared.data.source.network.services.SupplierApiService
import com.example.shared.util.ApiUtil
import io.ktor.client.call.body

class SupplierApiDataSourceImpl(
    private val supplierApi: SupplierApiService
) : SupplierApiDataSource {
    override suspend fun getSuppliers(
        query: GetSupplierQueryParams
    ): ApiResponse<GetSupplierResponse> {
        return try {
            val response = supplierApi.getSuppliers(query.toQueryMap())
            ApiResponse.Success(response, response.body())
        } catch (e: Exception) {
            ApiResponse.Error(ApiUtil.handleApiError(e))
        }
    }

    override suspend fun getSupplierById(
        id: String
    ): ApiResponse<GetSupplierByIdResponse> {
        return try {
            val response = supplierApi.getSupplierById(id)
            ApiResponse.Success(response, response.body())
        } catch (e: Exception) {
            ApiResponse.Error(ApiUtil.handleApiError(e))
        }
    }

    override suspend fun getSupplierOption(
        query: GetSupplierOptionQueryParams
    ): ApiResponse<GetSupplierOptionResponse> {
        return try {
            val response = supplierApi.getSupplierOption(query.toQueryMap())
            ApiResponse.Success(response, response.body())
        } catch (e: Exception) {
            ApiResponse.Error(ApiUtil.handleApiError(e))
        }
    }

    override suspend fun createSupplier(
        body: CreateUpdateSupplierBody
    ): ApiResponse<CreateSupplierResponse> {
        return try {
            val response = supplierApi.createSupplier(body)
            ApiResponse.Success(response, response.body())
        } catch (e: Exception) {
            ApiResponse.Error(ApiUtil.handleApiError(e))
        }
    }

    override suspend fun deleteSupplier(
        body: DeleteSupplierBody
    ): ApiResponse<DeleteSupplierResponse> {
        return try {
            val response = supplierApi.deleteSupplier(body)
            ApiResponse.Success(response, response.body())
        } catch (e: Exception) {
            ApiResponse.Error(ApiUtil.handleApiError(e))
        }
    }

    override suspend fun editSupplier(
        id: String,
        body: CreateUpdateSupplierBody
    ): ApiResponse<PutEditSupplierResponse> {
        return try {
            val response = supplierApi.editSupplier(id, body)
            ApiResponse.Success(response, response.body())
        } catch (e: Exception) {
            ApiResponse.Error(ApiUtil.handleApiError(e))
        }
    }

    override suspend fun editStatusSupplier(
        body: PatchEditStatusSupplierBody
    ): ApiResponse<PatchEditStatusSupplierResponse> {
        return try {
            val response = supplierApi.editStatusSupplier(body)
            ApiResponse.Success(response, response.body())
        } catch (e: Exception) {
            ApiResponse.Error(ApiUtil.handleApiError(e))
        }
    }
}