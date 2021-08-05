package com.sarfa.mathengineservice.data

import java.io.Serializable

data class EquationRequest(
    val numbers: List<Double>,
    val operationType: OperationType,
    val delayTime: Long,
    var id: Int = 0
) : Serializable

enum class OperationType {
    ADD, SUB, MUL, DIV
}
