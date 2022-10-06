package com.lugares_v.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lugares_v.data.LugarDao
import com.lugares_v.model.Lugar

class LugarRepository(private val lugarDao: LugarDao) {
    suspend fun saveLugar(lugar: Lugar){
        if (lugar.id==0) {//es un lug nuevo.. se inserta
            lugarDao.addLugar(lugar)
        } else {//el lugar ya existe.. se actualiza
            lugarDao.updateLugar(lugar)
        }
    }

    suspend fun deleteLugar(lugar: Lugar){
        lugarDao.deleteLugar(lugar)
    }

    val getLugares : LiveData<List<Lugar>> = lugarDao.getLugares()
}