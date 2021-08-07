package com.sarfa.mathengineservice.presentation.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.sarfa.mathengineservice.core.util.Event
import com.sarfa.mathengineservice.core.viewmodel.BaseViewModel
import com.sarfa.mathengineservice.domain.model.EquationRequest
import com.sarfa.mathengineservice.domain.model.EquationValidationResult
import com.sarfa.mathengineservice.domain.model.OperationType
import com.sarfa.mathengineservice.domain.usecase.ValidateEquationUseCase
import com.sarfa.mathengineservice.presentation.NumberItem
import com.sarfa.mathengineservice.services.MathEngineService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(private val validateEquationUseCase: ValidateEquationUseCase) :
    BaseViewModel() {
    var selectedOperationPos = 0
    val allOperationTypes = OperationType.values()
    var selectedNumbers = mutableListOf(NumberItem(""), NumberItem(""))
    var selectedTime = "10"

    @Inject
    lateinit var context: Context
    private val _validationErrorLiveData =
        MutableLiveData<Event<EquationValidationResult.InValid>>()
    val validationErrorLiveData:
            LiveData<Event<EquationValidationResult.InValid>> = _validationErrorLiveData

    fun calculateEquation(
    ) {
        val d = validateEquationUseCase.validate(
            selectedTime,
            selectedNumbers.map { it.number },
            allOperationTypes[selectedOperationPos]
        )
            .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is EquationValidationResult.Valid -> {
                        sendRequest(it.equationRequest)
                    }
                    is EquationValidationResult.InValid -> {
                        if (it.numbersError.isNotEmpty()) {
                            selectedNumbers.forEach {
                                it.validationError = null
                            }
                            for (item in it.numbersError) {
                                selectedNumbers[item.first].validationError = item.second
                            }
                        }
                        _validationErrorLiveData.value = Event(it)
                    }
                }
            }, {
                it.printStackTrace()
            })
        compositeDisposable.add(d)
    }

    private fun sendRequest(equationRequest: EquationRequest) {
        Intent(context, MathEngineService::class.java).also { intent ->
            intent.putExtra(
                MathEngineService.EQUATION_REQUEST_KEY,
                equationRequest
            )
            context.startService(intent)
        }
    }

    private val _pendingRequestsLiveData = MutableLiveData<List<Pair<EquationRequest, Double?>>>()
    val pendingRequestsLiveData: LiveData<List<Pair<EquationRequest, Double?>>> =
        _pendingRequestsLiveData

    private val _completedRequestsLiveData = MutableLiveData<List<Pair<EquationRequest, Double?>>>()
    val completedRequestsLiveData: LiveData<List<Pair<EquationRequest, Double?>>> =
        _completedRequestsLiveData

    fun startBindService() {
        val intent = Intent(context, MathEngineService::class.java)
        context.startService(intent)
        context.bindService(intent, boundServiceConnection, AppCompatActivity.BIND_AUTO_CREATE)
    }

    fun stopBindService() {
        if (isBound)
            context.unbindService(boundServiceConnection)
    }

    private val observer =
        Observer<List<Pair<EquationRequest, Double?>>> { t ->
            t?.let {
                val pendingRequests = it.filter {
                    it.second == null
                }
                val completedRequests = it.filter {
                    it.second != null
                }

                _pendingRequestsLiveData.value = pendingRequests
                _completedRequestsLiveData.value = completedRequests

            }
        }

    private var isBound = false
    private val boundServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            isBound = true
            (binder as? MathEngineService.LocalBinder)?.let {
                it.service.requestsLiveData.observeForever(observer)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }


}