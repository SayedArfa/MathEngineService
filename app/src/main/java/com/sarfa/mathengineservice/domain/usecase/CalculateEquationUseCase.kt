package com.sarfa.mathengineservice.domain.usecase

import com.sarfa.mathengineservice.core.extensions.calculate
import com.sarfa.mathengineservice.domain.model.EquationRequest
import io.reactivex.Observable
import javax.inject.Inject

class CalculateEquationUseCase @Inject constructor() {
    fun calculate(equationRequest: EquationRequest): Observable<Double> {
        return Observable.create {
            it.onNext(equationRequest.calculate())
            it.onComplete()
        }
    }
}