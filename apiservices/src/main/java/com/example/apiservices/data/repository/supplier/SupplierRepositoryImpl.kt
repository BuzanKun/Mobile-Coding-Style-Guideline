package com.example.apiservices.data.repository.supplier

import com.example.apiservices.base.Result
import com.example.apiservices.data.mapper.SupplierMapper
import com.example.apiservices.data.model.SupplierEntity
import com.example.apiservices.data.model.SupplierOptionEntity
import com.example.apiservices.data.source.network.datasource.SupplierApiDataSource
import com.example.apiservices.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.apiservices.data.source.network.model.request.supplier.PatchEditStatusSupplierBody
import com.example.apiservices.di.IODispatcher
import com.example.apiservices.util.Constant
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SupplierRepositoryImpl @Inject constructor(
    private val supplierApiDataSource: SupplierApiDataSource,
    private val supplierMapper: SupplierMapper,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private var token: String = Constant.BEARER_TOKEN
) : SupplierRepository {
    override fun getSupplierList(query: GetSupplierQueryParams): Flow<Result<List<SupplierEntity>>> =
        flow {
            if (token.isNotBlank()) {
                val response = supplierApiDataSource.getSuppliers(token, query)
                val resultData = response.body()?.data?.data.orEmpty()

                if (response.isSuccessful && response.code() == 200) {
                    emit(Result.Success(supplierMapper.mapSupplier(resultData)))
                } else {
                    emit(Result.Error(Constant.RESPONSE_ERROR))
                }
            } else {
                emit(Result.Error(Constant.EMPTY_TOKEN_ERROR))
            }
        }.catch {
            emit(Result.Error(it.message))
        }.flowOn(ioDispatcher)

    override fun getSupplierById(path: String): Flow<Result<SupplierEntity>> = flow {
        if (token.isNotBlank()) {
            val response = supplierApiDataSource.getSupplierById(token, path)
            val resultData = response.body()?.data
            if (response.isSuccessful && response.code() == 200 && resultData != null) {
                emit(Result.Success(supplierMapper.mapSupplierById(resultData)))
            } else {
                emit(Result.Error(Constant.RESPONSE_ERROR))
            }
        } else {
            emit(Result.Error(Constant.EMPTY_TOKEN_ERROR))
        }
    }.catch {
        emit(Result.Error(it.message))
    }.flowOn(ioDispatcher)

    override fun getSupplierOption(query: GetSupplierOptionQueryParams): Flow<Result<SupplierOptionEntity>> =
        flow {
            if (token.isNotBlank()) {
                val response = supplierApiDataSource.getSupplierOption(token, query)
                val resultData = response.body()?.data

                if (response.isSuccessful && response.code() == 200 && resultData != null) {
                    emit(Result.Success(supplierMapper.mapSupplierOption(resultData)))
                } else {
                    emit(Result.Error(Constant.RESPONSE_ERROR))
                }
            } else {
                emit(Result.Error(Constant.EMPTY_TOKEN_ERROR))
            }
        }.catch {
            emit(Result.Error(it.message))
        }.flowOn(ioDispatcher)

    override fun createSupplier(body: CreateUpdateSupplierBody): Flow<Result<Unit>> = flow {
        if (token.isNotBlank()) {
            val response = supplierApiDataSource.createSupplier(token, body)
            if (response.isSuccessful && response.code() == 201) {
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error(Constant.RESPONSE_ERROR))
            }
        } else {
            emit(Result.Error(Constant.EMPTY_TOKEN_ERROR))
        }
    }.catch {
        emit(Result.Error(it.message))
    }.flowOn(ioDispatcher)

    override fun deleteSupplier(body: DeleteSupplierBody): Flow<Result<Unit>> = flow {
        if (token.isNotBlank()) {
            val response = supplierApiDataSource.deleteSupplier(token, body)
            if (response.isSuccessful && response.code() == 200) {
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error(Constant.RESPONSE_ERROR))
            }
        } else {
            emit(Result.Error(Constant.EMPTY_TOKEN_ERROR))
        }
    }.catch {
        emit(Result.Error(it.message))
    }.flowOn(ioDispatcher)

    override fun editSupplier(
        path: String,
        body: CreateUpdateSupplierBody
    ): Flow<Result<Unit>> = flow {
        if (token.isNotBlank()) {
            val response = supplierApiDataSource.editSupplier(token, path, body)
            if (response.isSuccessful && response.code() == 200) {
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error(Constant.RESPONSE_ERROR))
            }
        } else {
            emit(Result.Error(Constant.EMPTY_TOKEN_ERROR))
        }
    }.catch {
        emit(Result.Error(it.message))
    }.flowOn(ioDispatcher)

    override fun editStatusSupplier(
        body: PatchEditStatusSupplierBody
    ): Flow<Result<Unit>> = flow {
        if (token.isNotBlank()) {
            val response = supplierApiDataSource.editStatusSupplier(token, body)
            if (response.isSuccessful && response.code() == 200) {
                emit(Result.Success(Unit))
            } else {
                emit(Result.Error(Constant.RESPONSE_ERROR))
            }
        } else {
            emit(Result.Error(Constant.EMPTY_TOKEN_ERROR))
        }
    }.catch {
        emit(Result.Error(it.message))
    }.flowOn(ioDispatcher)
}