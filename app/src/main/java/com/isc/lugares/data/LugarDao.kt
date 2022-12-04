package com.isc.lugares.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.isc.lugares.model.Lugar


class LugarDao {
    //Variable para registrar el correo del usuario autenticado
    private var codigoUsuario: String
    //Enlace hacia Firestore...
    private var firestore: FirebaseFirestore

    init {
        val usuario = Firebase.auth.currentUser?.email
        codigoUsuario="$usuario"
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings =
            FirebaseFirestoreSettings.Builder().build()
    }

    fun saveLugar(lugar: Lugar) {
        val documento: DocumentReference
        if (lugar.id.isEmpty()) {  //Es un documento nuevo... se debe crear...
            documento = firestore
                .collection("lugaresApp")
                .document(codigoUsuario)
                .collection("misLugares")
                .document()
            lugar.id = documento.id
        } else {  //El documento ya existe...
            documento = firestore
                .collection("lugaresApp")
                .document(codigoUsuario)
                .collection("misLugares")
                .document(lugar.id)
        }
        documento.set(lugar)
            .addOnSuccessListener {
                Log.d("Firestores","Documento almacenado")
            }
            .addOnCanceledListener {
                Log.d("Firestores","Documento NO almacenado")
            }
    }

    fun deleteLugar(lugar: Lugar) {
        if (lugar.id.isNotEmpty()) {  //Si el documento/lugar existe... lo podemos borrar
            firestore
                .collection("lugaresApp")
                .document(codigoUsuario)
                .collection("misLugares")
                .document(lugar.id)
                .delete()
                .addOnSuccessListener {
                    Log.d("Firestore","Documento eliminado")
                }
                .addOnCanceledListener {
                    Log.d("Firestore","Documento NO eliminado")
                }
        }
    }

    fun getLugares() : MutableLiveData<List<Lugar>> {
        val listaFinal = MutableLiveData<List<Lugar>>()
        firestore
            .collection("lugaresApp")
            .document(codigoUsuario)
            .collection("misLugares")
            .addSnapshotListener { instantanea, e ->  //Le toma una foto/recupera los lugares del usuario
                if (e != null) {
                    Log.d("Firestore","Error recuperando lugares")
                    return@addSnapshotListener
                }
                if (instantanea!=null) {  //Hay datos en la recuperación
                    val lista = ArrayList<Lugar>()
                    val lugares= instantanea.documents
                    lugares.forEach {
                        val lugar = it.toObject(Lugar::class.java)
                        if (lugar!= null) {  //Si se pudo convertir a un lugar
                            lista.add(lugar)
                        }
                    }
                    listaFinal.value = lista
                }
            }
        return listaFinal
    }

}