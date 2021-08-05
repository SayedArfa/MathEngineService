package com.sarfa.mathengineservice.presentation.main

import android.content.Context
import com.sarfa.mathengineservice.core.viewmodel.BaseViewModel
import com.sarfa.mathengineservice.data.OperationType
import javax.inject.Inject

class MainViewModel @Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var context: Context
    var selectedOperationPos = 0
    val allOperationTypes = OperationType.values()
    var selectedNumbers = mutableListOf("1", "2")
    var selectedTime = "10"
}