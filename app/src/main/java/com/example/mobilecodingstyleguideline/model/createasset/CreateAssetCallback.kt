package com.example.mobilecodingstyleguideline.model.createasset

data class CreateAssetCallback(
    val onClearField: () -> Unit = {},
    val onResetMessageState: () -> Unit = {},
    val onUpdateFormData: (CreateAssetFormData) -> Unit = {},
    val onSubmitForm: () -> Unit = {},
    val onUpdateStayOnForm: () -> Unit = {}
)