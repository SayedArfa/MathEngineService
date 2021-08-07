package com.sarfa.mathengineservice.domain.usecase

import com.sarfa.mathengineservice.domain.model.EquationRequest
import com.sarfa.mathengineservice.domain.model.OperationType
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CalculateUseCase @Inject constructor() {
    fun calculate(equationRequest: EquationRequest): Observable<Double> {
        return scheduleRequest(equationRequest)
    }

    private fun scheduleRequest(equationRequest: EquationRequest): Observable<Double> {
        return Observable.timer(equationRequest.delayTime, TimeUnit.SECONDS).map {
            when (equationRequest.operationType) {
                OperationType.ADD -> {
                    equationRequest.numbers.reduce { acc, i ->
                        acc + i
                    }
                }
                OperationType.SUB -> {
                    equationRequest.numbers.reduce { acc, i ->
                        acc - i
                    }
                }
                OperationType.MUL -> {
                    equationRequest.numbers.reduce { acc, i ->
                        acc * i
                    }
                }
                OperationType.DIV -> {
                    equationRequest.numbers.reduce { acc, i ->
                        acc / i
                    }
                }
            }
        }


    }
}