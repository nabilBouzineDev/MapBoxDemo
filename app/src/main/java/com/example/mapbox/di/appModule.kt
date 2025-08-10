package com.example.mapbox.di

import com.example.mapbox.data.PolygonAreaServiceImpl
import com.example.mapbox.data.PolygonDao
import com.example.mapbox.data.PolygonDatabase
import com.example.mapbox.presentation.PolygonAreaViewModel
import com.example.mapbox.service.PolygonAreaService
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<PolygonDatabase> { PolygonDatabase.getInstance(androidApplication()) }
    single<PolygonDao> { get<PolygonDatabase>().polygonDao() }

    single<PolygonAreaService> { PolygonAreaServiceImpl(get()) }

    viewModelOf(::PolygonAreaViewModel)
}