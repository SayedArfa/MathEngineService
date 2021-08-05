package com.example.downloadfiles.base.di

import android.app.Application
import com.sarfa.mathengineservice.core.app.MyApp
import com.sarfa.mathengineservice.core.di.ActivityBuilder
import com.sarfa.mathengineservice.core.di.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, ActivityBuilder::class])
interface AppComponent {

    fun inject(app: MyApp)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

}