package com.sarfa.mathengineservice.core.extensions

import com.sarfa.mathengineservice.domain.model.EquationRequest
import com.sarfa.mathengineservice.domain.model.OperationType

fun EquationRequest.calculate(): Double {
    return when (operationType) {
        OperationType.ADD -> {
            numbers.reduce { acc, i ->
                acc + i
            }
        }
        OperationType.SUB -> {
            numbers.reduce { acc, i ->
                acc - i
            }
        }
        OperationType.MUL -> {
            numbers.reduce { acc, i ->
                acc * i
            }
        }
        OperationType.DIV -> {
            numbers.reduce { acc, i ->
                acc / i
            }
        }
    }
}