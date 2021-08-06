package com.sarfa.mathengineservice.domain.model

import java.io.Serializable

data class EquationRequest(
    val numbers: List<Double>,
    val operationType: OperationType,
    val delayTime: Long,
    var id: Int = 0
) : Serializable

enum class OperationType(val label: String) {
    ADD("+"), SUB("-"), MUL("*"), DIV("/")
}
