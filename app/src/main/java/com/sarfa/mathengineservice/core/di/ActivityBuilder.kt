package com.sarfa.mathengineservice.core.di


import com.sarfa.mathengineservice.presentation.main.MainActivity
import com.sarfa.mathengineservice.services.MathEngineService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun bindMainActivity(): MainActivity
    @ContributesAndroidInjector(modules = [ServiceModule::class])
    abstract fun bindService(): MathEngineService
}