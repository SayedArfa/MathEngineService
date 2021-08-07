package com.sarfa.mathengineservice.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sarfa.mathengineservice.domain.model.EquationRequest
import com.sarfa.mathengineservice.domain.usecase.CalculateUseCase
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MathEngineService : Service() {
    companion object {
        const val EQUATION_REQUEST_KEY = "EQUATION_REQUEST_KEY"
    }

    private var binder = LocalBinder()
    private val requestList = mutableListOf<Pair<EquationRequest, Double?>>()
    private val _requestsLiveData = MutableLiveData<List<Pair<EquationRequest, Double?>>>()
    val requestsLiveData: LiveData<List<Pair<EquationRequest, Double?>>> = _requestsLiveData
    private val compositeDisposable = CompositeDisposable()
    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        (intent?.getSerializableExtra(EQUATION_REQUEST_KEY) as? EquationRequest)?.let {
            handleEquationRequest(it)
        }
        return START_STICKY
    }

    private fun handleEquationRequest(equationRequest: EquationRequest) {
        equationRequest.id = getNewId()
        requestList.add(Pair(equationRequest, null))
        _requestsLiveData.postValue(requestList)
        scheduleRequest(equationRequest)
    }

    @Inject
    lateinit var calculateUseCase: CalculateUseCase

    private fun scheduleRequest(equationRequest: EquationRequest) {
        val d = calculateUseCase.calculate(equationRequest).subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ answer ->
                for (i in requestList.indices) {
                    if (requestList[i].first == equationRequest) {
                        requestList[i] = Pair(equationRequest, answer)
                        break
                    }
                }
                _requestsLiveData.postValue(requestList)
            }, {
                it.printStackTrace()
            })

        compositeDisposable.add(d)
    }

    private var newId = 0
    private fun getNewId(): Int {
        return newId++
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    inner class LocalBinder : Binder() {
        val service: MathEngineService
            get() = this@MathEngineService
    }
}