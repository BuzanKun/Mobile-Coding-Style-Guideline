package com.example.apiservices.data.source.network.services

import com.example.apiservices.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.apiservices.data.source.network.model.response.supplier.CreateSupplierResponse
import com.example.apiservices.data.source.network.model.response.supplier.DeleteSupplierResponse
import com.example.apiservices.data.source.network.model.response.supplier.GetSupplierByIdResponse
import com.example.apiservices.data.source.network.model.response.supplier.GetSupplierOptionResponse
import com.example.apiservices.data.source.network.model.response.supplier.GetSupplierResponse
import com.example.apiservices.data.source.network.model.response.supplier.PutEditSupplierResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface SupplierApi {
    @GET("/v2/supplier")
    suspend fun getSuppliers(
        @Header("Authorization") token: String,
        @QueryMap query: Map<String, String?> = mapOf()
    ): Response<GetSupplierResponse>

    @GET("/v2/supplier/{id}")
    suspend fun getSupplierById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<GetSupplierByIdResponse>

    @GET("/v2/supplier/option")
    suspend fun getSupplierOption(
        @Header("Authorization") token: String,
        @QueryMap query: Map<String, Boolean?> = mapOf()
    ): Response<GetSupplierOptionResponse>

    @POST("/v2/supplier")
    suspend fun createSupplier(
        @Header("Authorization") token: String,
        @Body body: CreateUpdateSupplierBody
    ): Response<CreateSupplierResponse>

    @HTTP(method = "DELETE", path = "/v2/supplier", hasBody = true)
    suspend fun deleteSupplier(
        @Header("Authorization") token: String,
        @Body body: DeleteSupplierBody
    ): Response<DeleteSupplierResponse>

    @PUT("/v2/supplier/{id}")
    suspend fun editSupplier(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body body: CreateUpdateSupplierBody
    ): Response<PutEditSupplierResponse>
}
