package com.isc.lugares.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.isc.lugares.data.LugarDao
import com.isc.lugares.model.Lugar
import com.isc.lugares.repository.LugarRepository

class LugarViewModel(application: Application)
    : AndroidViewModel(application) {
        val getAllData: MutableLiveData<List<Lugar>>

        private val repository: LugarRepository = LugarRepository(LugarDao())

        init{
            getAllData = repository.getAllData
        }

    //Implementamos las funciones CRUD
    fun addLugar(lugar: Lugar) {
        repository.addLugar(lugar)
    }

    fun updateLugar(lugar: Lugar) {
        repository.updateLugar(lugar)
    }

    fun deleteLugar(lugar: Lugar) {
        repository.deleteLugar(lugar)
    }
}