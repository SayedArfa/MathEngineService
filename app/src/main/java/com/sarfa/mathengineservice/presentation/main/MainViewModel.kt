package com.sarfa.mathengineservice.presentation.main

import com.sarfa.mathengineservice.core.viewmodel.BaseViewModel
import com.sarfa.mathengineservice.data.OperationType
import javax.inject.Inject

class MainViewModel @Inject constructor() : BaseViewModel() {
    var selectedOperationPos = 0
    val allOperationTypes = OperationType.values()
    var selectedNumbers = mutableListOf("", "")
    var selectedTime = "10"
}