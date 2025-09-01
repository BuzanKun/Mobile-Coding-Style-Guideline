package com.example.apiservices.data.source.network.datasource

import com.example.apiservices.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.apiservices.data.source.network.model.response.supplier.CreateSupplierResponse
import com.example.apiservices.data.source.network.model.response.supplier.DeleteSupplierResponse
import com.example.apiservices.data.source.network.model.response.supplier.GetSupplierByIdResponse
import com.example.apiservices.data.source.network.model.response.supplier.GetSupplierOptionResponse
import com.example.apiservices.data.source.network.model.response.supplier.GetSupplierResponse
import com.example.apiservices.data.source.network.model.response.supplier.PutEditSupplierResponse
import com.example.apiservices.data.source.network.services.SupplierApi
import com.example.apiservices.util.ApiUtil
import retrofit2.Response
import javax.inject.Inject

class SupplierApiDataSourceImpl @Inject constructor(
    private val supplierApi: SupplierApi
) : SupplierApiDataSource {
    override suspend fun getSuppliers(
        token: String,
        query: GetSupplierQueryParams
    ): Response<GetSupplierResponse> {
        return try {
            supplierApi.getSuppliers(token, query.toQueryMap())
        } catch (e: Exception) {
            ApiUtil.handleApiError(e)
        }
    }

    override suspend fun getSupplierById(
        token: String,
        id: String
    ): Response<GetSupplierByIdResponse> {
        return try {
            supplierApi.getSupplierById(token, id)
        } catch (e: Exception) {
            ApiUtil.handleApiError(e)
        }
    }

    override suspend fun getSupplierOption(
        token: String,
        query: GetSupplierOptionQueryParams
    ): Response<GetSupplierOptionResponse> {
        return try {
            supplierApi.getSupplierOption(token, query.toQueryMap())
        } catch (e: Exception) {
            ApiUtil.handleApiError(e)
        }
    }

    override suspend fun createSupplier(
        token: String,
        body: CreateUpdateSupplierBody
    ): Response<CreateSupplierResponse> {
        return try {
            supplierApi.createSupplier(token, body)
        } catch (e: Exception) {
            ApiUtil.handleApiError(e)
        }
    }

    override suspend fun deleteSupplier(
        token: String,
        body: DeleteSupplierBody
    ): Response<DeleteSupplierResponse> {
        return try {
            supplierApi.deleteSupplier(token, body)
        } catch (e: Exception) {
            ApiUtil.handleApiError(e)
        }
    }

    override suspend fun editSupplier(
        token: String,
        id: String,
        body: CreateUpdateSupplierBody
    ): Response<PutEditSupplierResponse> {
        return try {
            supplierApi.editSupplier(token, id, body)
        } catch (e: Exception) {
            ApiUtil.handleApiError(e)
        }
    }
}