package com.lugares_v

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lugares_v.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //Definimos un objeto para acceder a la auth de Firebase
    private lateinit var auth : FirebaseAuth

    //Definimos un objeto para acceder a los elementos de la pantalla xml
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //inicializar la authentication
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        //Definir evento onlcick de boton register
        binding.btRegister.setOnClickListener{ haceRegistro() }

        //Definir evento onlcick de boton login
        binding.btRegister.setOnClickListener{ haceLogin() }
    }

    private fun haceRegistro() {
        //Recupero la info que el usuario ingreso en el login del app
        var email = binding.etCorreo.text.toString()
        var clave = binding.etClave.text.toString()

        //Utilizo el objeto auth para hacer el registro...
        auth.createUserWithEmailAndPassword(email, clave)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful){ //Se creo el ususario
                    val user = auth.currentUser
                    refresca(user)
                }else{ //si no se logro hubo un error...
                    Toast.makeText(baseContext,"Falló",Toast.LENGTH_LONG).show()
                    refresca(null)
                }
            }
    }

    private fun refresca(user: FirebaseUser?) {
        if (user != null){ //si hay un user se pasa a la pantalla principal
            val intent = Intent(this, Principal::class.java)
            startActivity(intent)

        }

    }

    private fun haceLogin() {
        TODO("Not yet implemented")
    }
}