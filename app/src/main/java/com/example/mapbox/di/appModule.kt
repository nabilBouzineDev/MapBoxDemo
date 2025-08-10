package com.example.mapbox.di

import com.example.mapbox.presentation.MapBoxViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MapBoxViewModel)
}