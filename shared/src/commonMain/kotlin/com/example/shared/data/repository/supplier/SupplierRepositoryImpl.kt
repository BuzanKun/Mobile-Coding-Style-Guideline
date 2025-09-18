package com.example.shared.data.repository.supplier

import com.example.shared.base.ApiResponse
import com.example.shared.base.Result
import com.example.shared.data.mapper.SupplierMapper
import com.example.shared.data.model.SupplierEntity
import com.example.shared.data.model.SupplierOptionEntity
import com.example.shared.data.source.network.datasource.SupplierApiDataSource
import com.example.shared.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.shared.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.shared.data.source.network.model.request.supplier.GetSupplierOptionQueryParams
import com.example.shared.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.shared.data.source.network.model.request.supplier.PatchEditStatusSupplierBody
import com.example.shared.util.Constant
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SupplierRepositoryImpl(
    private val supplierApiDataSource: SupplierApiDataSource,
    private val supplierMapper: SupplierMapper,
    private val ioDispatcher: CoroutineDispatcher,
    private var token: String = Constant.BEARER_TOKEN
) : SupplierRepository {
    override fun getSupplierList(query: GetSupplierQueryParams): Flow<Result<List<SupplierEntity>>> =
        flow {
            if (token.isNotBlank()) {
                when (val response = supplierApiDataSource.getSuppliers(token, query)) {
                    is ApiResponse.Success -> {
                        if (response.httpResponse.status.isSuccess()) {
                            val resultData = response.body.data?.data.orEmpty()
                            emit(Result.Success(supplierMapper.mapSupplier(resultData)))
                        } else {
                            emit(Result.Error(Constant.RESPONSE_ERROR))
                        }
                    }

                    is ApiResponse.Error -> {
                        emit(Result.Error(response.exception.message))
                    }
                }
            } else {
                emit(Result.Error(Constant.EMPTY_TOKEN_ERROR))
            }
        }.catch {
            emit(Result.Error(it.message))
        }.flowOn(ioDispatcher)

    override fun getSupplierById(path: String): Flow<Result<SupplierEntity>> = flow {
        if (token.isNotBlank()) {
            when (val response = supplierApiDataSource.getSupplierById(token, path)) {
                is ApiResponse.Success -> {
                    if (response.httpResponse.status.isSuccess()) {
                        val resultData = response.body.data
                        emit(Result.Success(supplierMapper.mapSupplierById(resultData)))
                    } else {
                        emit(Result.Error(Constant.RESPONSE_ERROR))
                    }
                }

                is ApiResponse.Error -> {
                    emit(Result.Error(response.exception.message))
                }
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
                when (val response = supplierApiDataSource.getSupplierOption(token, query)) {
                    is ApiResponse.Success -> {
                        if (response.httpResponse.status.isSuccess()) {
                            val resultData = response.body.data
                            emit(Result.Success(supplierMapper.mapSupplierOption(resultData)))
                        } else {
                            emit(Result.Error(Constant.RESPONSE_ERROR))
                        }
                    }

                    is ApiResponse.Error -> {
                        emit(Result.Error(response.exception.message))
                    }
                }
            } else {
                emit(Result.Error(Constant.EMPTY_TOKEN_ERROR))
            }
        }.catch {
            emit(Result.Error(it.message))
        }.flowOn(ioDispatcher)

    override fun createSupplier(body: CreateUpdateSupplierBody): Flow<Result<Unit>> = flow {
        if (token.isNotBlank()) {
            when (val response = supplierApiDataSource.createSupplier(token, body)) {
                is ApiResponse.Success -> {
                    if (response.httpResponse.status.isSuccess()) {
                        emit(Result.Success(Unit))
                    } else {
                        emit(Result.Error(Constant.RESPONSE_ERROR))
                    }
                }

                is ApiResponse.Error -> {
                    emit(Result.Error(response.exception.message))
                }
            }
        } else {
            emit(Result.Error(Constant.EMPTY_TOKEN_ERROR))
        }
    }.catch {
        emit(Result.Error(it.message))
    }.flowOn(ioDispatcher)

    override fun deleteSupplier(body: DeleteSupplierBody): Flow<Result<Unit>> = flow {
        if (token.isNotBlank()) {
            when (val response = supplierApiDataSource.deleteSupplier(token, body)) {
                is ApiResponse.Success -> {
                    if (response.httpResponse.status.isSuccess()) {
                        emit(Result.Success(Unit))
                    } else {
                        emit(Result.Error(Constant.RESPONSE_ERROR))
                    }
                }

                is ApiResponse.Error -> {
                    emit(Result.Error(response.exception.message))
                }
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
            when (val response = supplierApiDataSource.editSupplier(token, path, body)) {
                is ApiResponse.Success -> {
                    if (response.httpResponse.status.isSuccess()) {
                        emit(Result.Success(Unit))
                    } else {
                        emit(Result.Error(Constant.RESPONSE_ERROR))
                    }
                }

                is ApiResponse.Error -> {
                    emit(Result.Error(response.exception.message))
                }
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
            when (val response = supplierApiDataSource.editStatusSupplier(token, body)) {
                is ApiResponse.Success -> {
                    if (response.httpResponse.status.isSuccess()) {
                        emit(Result.Success(Unit))
                    } else {
                        emit(Result.Error(Constant.RESPONSE_ERROR))
                    }
                }

                is ApiResponse.Error -> {
                    emit(Result.Error(response.exception.message))
                }
            }
        } else {
            emit(Result.Error(Constant.EMPTY_TOKEN_ERROR))
        }
    }.catch {
        emit(Result.Error(it.message))
    }.flowOn(ioDispatcher)
}