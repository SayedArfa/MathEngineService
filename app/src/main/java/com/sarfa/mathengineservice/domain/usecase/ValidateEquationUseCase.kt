package com.sarfa.mathengineservice.domain.usecase

import com.sarfa.mathengineservice.domain.model.EquationRequest
import com.sarfa.mathengineservice.domain.model.EquationValidationResult
import com.sarfa.mathengineservice.domain.model.OperationType
import com.sarfa.mathengineservice.domain.util.NumberValidator
import io.reactivex.Observable
import javax.inject.Inject

class ValidateEquationUseCase @Inject constructor() {
    fun validate(
        time: String,
        numbers: List<String>,
        operationType: OperationType
    ): Observable<EquationValidationResult> {
        return Observable.create {
            val timeValidation = NumberValidator.validate(time)
            val numbersValidation = mutableListOf<Pair<Int, String>>()
            for (i in numbers.indices) {
                val result = NumberValidator.validate(numbers[i])
                if (result != null) {
                    numbersValidation.add(Pair(i, result))
                }
            }

            if (timeValidation == null && numbersValidation.isEmpty()) {
                it.onNext(
                    EquationValidationResult.Valid(
                        EquationRequest(
                            numbers.map { it.toDouble() },
                            operationType,
                            time.toLong()
                        )
                    )
                )
            } else {
                it.onNext(EquationValidationResult.InValid(timeValidation, numbersValidation))
            }
            it.onComplete()
        }
    }
}