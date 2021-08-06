package com.sarfa.mathengineservice.domain.model

sealed class EquationValidationResult() {
    class Valid(val equationRequest: EquationRequest) : EquationValidationResult()
    class InValid(
        val delayTimeError: String?,
        var numbersError: List<Pair<Int, String>>
    ) : EquationValidationResult()
}
