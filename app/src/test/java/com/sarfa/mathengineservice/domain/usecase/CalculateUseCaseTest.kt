package com.sarfa.mathengineservice.domain.usecase

import com.sarfa.mathengineservice.domain.model.EquationRequest
import com.sarfa.mathengineservice.domain.model.OperationType
import junit.framework.TestCase

class CalculateUseCaseTest : TestCase() {

    private val calculateUseCase = CalculateUseCase()
    fun testCalculate() {
        calculateUseCase.calculate(EquationRequest(listOf(1.0, 2.0), OperationType.ADD, 10))
            .test().assertValue(3.0)
        calculateUseCase.calculate(EquationRequest(listOf(1.0, 2.0), OperationType.SUB, 10))
            .test().assertValue(-1.0)
        calculateUseCase.calculate(EquationRequest(listOf(1.0, 2.0), OperationType.MUL, 10))
            .test().assertValue(2.0)
        calculateUseCase.calculate(EquationRequest(listOf(1.0, 2.0), OperationType.DIV, 10))
            .test().assertValue(0.5)
    }
}