package com.example.mobilecodingstyleguideline.model.createsupplier

data class CreateSupplierCallback(
    val onClearField: () -> Unit = {},
    val onResetMessageState: () -> Unit = {},
    val onUpdateFormData: (CreateSupplierFormData) -> Unit = {},
    val onSubmitForm: () -> Unit = {},
    val onUpdateStayOnForm: () -> Unit = {}
)