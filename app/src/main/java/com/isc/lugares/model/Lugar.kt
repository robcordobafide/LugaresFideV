package com.isc.lugares.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lugar(
    var id: String,   //Antes era Int por ser de SQLite auto-increment
    val nombre: String,
    val correo: String?,
    val telefono: String?,
    val web: String?,
    val longitud: Double?,
    val latitud: Double?,
    val altura: Int?,
    val rutaAudio: String?,
    val rutaImagen: String?
) : Parcelable {
    constructor():
            this("","","","","",0.0,0.0,0,"","")
}
