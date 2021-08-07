package com.sarfa.mathengineservice.domain.usecase

import com.sarfa.mathengineservice.domain.model.EquationValidationResult
import com.sarfa.mathengineservice.domain.model.OperationType
import junit.framework.TestCase

class ValidateEquationUseCaseTest : TestCase() {
    private val validateEquationUseCase = ValidateEquationUseCase()
    fun testValidateValidEquation() {
        validateEquationUseCase.validate("10", listOf("1", "2"), OperationType.ADD).test()
            .assertValue {
                it is EquationValidationResult.Valid
            }
    }

    fun testValidateInvalidDelayTime() {
        validateEquationUseCase.validate("", listOf("1", "2"), OperationType.ADD).test()
            .assertValue {
                it is EquationValidationResult.InValid
            }
    }

    fun testValidateInvalidNumber() {
        validateEquationUseCase.validate("10", listOf("1", ""), OperationType.ADD).test()
            .assertValue {
                it is EquationValidationResult.InValid
            }
    }
}