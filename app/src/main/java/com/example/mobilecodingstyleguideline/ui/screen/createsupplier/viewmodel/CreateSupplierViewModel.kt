package com.example.mobilecodingstyleguideline.ui.screen.createsupplier.viewmodel

import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiservices.base.Result
import com.example.apiservices.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.apiservices.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.apiservices.domain.supplier.CreateSupplierUseCase
import com.example.apiservices.domain.supplier.EditSupplierUseCase
import com.example.apiservices.domain.supplier.GetSupplierByIdUseCase
import com.example.apiservices.domain.supplier.GetSuppliersUseCase
import com.example.mobilecodingstyleguideline.model.createsupplier.CreateSupplierCallback
import com.example.mobilecodingstyleguideline.model.createsupplier.CreateSupplierFormData
import com.example.mobilecodingstyleguideline.model.createsupplier.CreateSupplierFormError
import com.example.mobilecodingstyleguideline.model.createsupplier.CreateSupplierFormOption
import com.example.mobilecodingstyleguideline.ui.screen.createsupplier.uistate.CreateSupplierUiState
import com.example.mobilecodingstyleguideline.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CreateSupplierViewModel @Inject constructor(
    private val getSuppliersUseCase: GetSuppliersUseCase,
    private val getSupplierByIdUseCase: GetSupplierByIdUseCase,
    private val createSupplierUseCase: CreateSupplierUseCase,
    private val editSupplierUseCase: EditSupplierUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateSupplierUiState())
    val uiState = _uiState.asStateFlow()

    fun init(supplierId: String? = null) {
        _uiState.update { currData ->
            currData.copy(
                assetId = "",
                formData = CreateSupplierFormData(),
                formError = CreateSupplierFormError(),
                submitState = null,
                isEditForm = false
            )
        }

        supplierId?.let { item ->
            getSupplyById(supplierId)
        }
        getFormOption()
    }

    fun getCallback(): CreateSupplierCallback {
        return CreateSupplierCallback(
            onClearField = ::clearField,
            onResetMessageState = ::resetMessageState,
            onUpdateFormData = ::updateFormData,
            onSubmitForm = ::submitForm,
            onUpdateStayOnForm = ::updateStayOnForm,
            onAddSupplierItem = ::addSupplierItem,
            onRemoveSupplierItem = ::removeSupplierItem
        )
    }

    fun getSupplyById(id: String) {
        _uiState.update { it.copy(isLoadingOverlay = true) }

        getSupplierByIdUseCase(id).onEach { result ->
            when (result) {
                is Result.Success -> {
                    val data = result.data
                    _uiState.update { currData ->
                        currData.copy(
                            isLoadingOverlay = false,
                            formData = CreateSupplierFormData(
                                companyName = data.companyName,
                                items = data.item.map {
                                    CreateSupplierFormData.Item(
                                        itemName = it.itemName,
                                        itemSku = it.itemSku
                                    )
                                },
                                country = data.country,
                                state = data.state,
                                city = data.city,
                                zipCode = data.zipCode,
                                companyLocation = data.companyLocation,
                                countryCode = data.countryCode,
                                companyPhoneNumber = data.companyPhoneNumber,
                                picName = data.picName,
                                picCountryCode = data.picCountryCode,
                                picPhoneNumber = data.picPhoneNumber,
                                picEmail = data.picEmail
                            ),
                            assetId = id,
                            isEditForm = true
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update { it.copy(isLoadingOverlay = false) }

                }
            }
        }.launchIn(viewModelScope)
    }

    fun getFormOption() {
        _uiState.update { it.copy(isLoadingFormOption = true) }

        getSuppliersUseCase(GetSupplierQueryParams()).onEach { result ->
            when (result) {
                is Result.Success -> {
                    val data = result.data

                    _uiState.update { currData ->
                        currData.copy(
                            isLoadingFormOption = false,
                            formOption = CreateSupplierFormOption(
                                itemNameList = Util.generateOptionsDataString(data.flatMap { supplier -> supplier.item.map { it.itemName } }
                                    .distinct()),
                                itemSkuList = Util.generateOptionsDataString(data.flatMap { supplier -> supplier.item.flatMap { item -> item.itemSku.map { it } } }
                                    .distinct()),
                                country = Util.generateOptionsDataString(data.map { it.country }
                                    .distinct()),
                                state = Util.generateOptionsDataString(data.map { it.state }
                                    .distinct()),
                                city = Util.generateOptionsDataString(data.map { it.city }
                                    .distinct()),
                            )
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(isLoadingFormOption = false)
                    }
                }
            }
        }.launchIn(viewModelScope)

    }

    private fun clearField() {
        _uiState.update {
            it.copy(
                formData = CreateSupplierFormData(),
                formError = CreateSupplierFormError(),
                submitState = null
            )
        }
    }

    private fun resetMessageState() {
        _uiState.update { currData ->
            currData.copy(
                submitState = null
            )
        }
    }

    fun updateFormData(formData: CreateSupplierFormData) {
        _uiState.update { currData ->
            currData.copy(
                formData = formData,
                formError = CreateSupplierFormError()
            )
        }
    }

    private fun submitForm() {
        val data = _uiState.value.formData

        val body = CreateUpdateSupplierBody(
            companyName = data.companyName,
            item = data.items.map {
                CreateUpdateSupplierBody.Item(
                    itemName = it.itemName,
                    sku = it.itemSku
                )
            },
            country = data.country,
            state = data.state,
            city = data.city,
            zipCode = data.zipCode,
            companyLocation = data.companyLocation,
            companyPhoneNumber = data.companyPhoneNumber,
            picName = data.picName,
            picPhoneNumber = data.picPhoneNumber,
            picEmail = data.picEmail
        )

        if (!formValidation(data)) return

        _uiState.update { it.copy(isLoadingOverlay = true) }

        val domain = if (_uiState.value.isEditForm) {
            editSupplierUseCase(id = _uiState.value.assetId, body = body)
        } else {
            createSupplierUseCase(body = body)
        }

        domain.onEach { result ->
            when (result) {
                is Result.Success -> {
                    _uiState.update { currData ->
                        currData.copy(
                            isLoadingOverlay = false,
                            submitState = true
                        )
                    }
                    if (_uiState.value.isStayOnForm) {
                        clearField()
                    }
                }

                is Result.Error -> {
                    _uiState.update { currData ->
                        currData.copy(
                            isLoadingOverlay = false,
                            submitState = false
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun formValidation(data: CreateSupplierFormData): Boolean {
        var formError = CreateSupplierFormError()

        if (data.companyName.isEmpty()) {
            formError = formError.copy(
                companyName = "Company name must not be empty"
            )
        } else if (data.companyName.length > 30) {
            formError = formError.copy(
                companyName = "Max. 30 characters"
            )
        }

        val isAtLeastOneNameSelected = data.items.any { it.itemName.isNotEmpty() }
        val itemNameErrors: List<String?> =
            if (!isAtLeastOneNameSelected) {
                data.items.map { "You must pick an item name" }
            } else {
                List(data.items.size) { null }
            }
        formError = formError.copy(itemName = itemNameErrors)

        val itemSkuErrors = data.items.map { orderItem ->
            if (orderItem.itemName.isNotEmpty() && orderItem.itemSku.isEmpty()) {
                "You must pick a SKU for this item"
            } else {
                null
            }
        }
        formError = formError.copy(itemSku = itemSkuErrors)

        if (data.zipCode.length > 15) {
            formError = formError.copy(
                zipCode = "Max. 15 characters"
            )
        }
        if (data.companyLocation.length > 120) {
            formError = formError.copy(
                address = "Max. 120 characters"
            )
        }

        if (data.companyPhoneNumber.length > 15) {
            formError = formError.copy(
                phoneNumber = "Max. 15 characters"
            )
        }

        if (data.picName.length > 30) {
            formError = formError.copy(
                picName = "Max. 30 characters"
            )
        }

        if (data.picPhoneNumber.length > 15) {
            formError = formError.copy(
                picPhoneNumber = "Max. 15 characters"
            )
        }

        if (data.picEmail.isNotEmpty() && !PatternsCompat.EMAIL_ADDRESS.matcher(data.picEmail)
                .matches()
        ) {
            formError = formError.copy(
                picEmail = "Email format is incorrect"
            )
        }

        _uiState.update { currData ->
            currData.copy(
                formError = formError
            )
        }

        return !formError.hasError()
    }

    private fun updateStayOnForm() {
        _uiState.update { currData ->
            currData.copy(isStayOnForm = !currData.isStayOnForm)
        }
    }

    private fun addSupplierItem() {
        _uiState.update { currentState ->
            val currentList = currentState.formData.items
            val newItem = CreateSupplierFormData.Item()
            val newList = currentList + newItem

            currentState.copy(
                formData = currentState.formData.copy(
                    items = newList
                )
            )
        }
    }

    private fun removeSupplierItem(item: CreateSupplierFormData.Item) {
        _uiState.update { currentState ->
            val newList = currentState.formData.items - item
            currentState.copy(
                formData = currentState.formData.copy(
                    items = newList
                )
            )
        }
    }
}