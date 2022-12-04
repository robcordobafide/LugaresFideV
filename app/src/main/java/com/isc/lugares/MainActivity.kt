package com.isc.lugares

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.isc.lugares.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //Se crea un objeto binding para acceder directamente a los controles
    private lateinit var binding: ActivityMainBinding

    //Se crea un objeto auth para lograr el proceso de autenticación
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Se inicializa el binding para acceder a los controles visuales
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Se inicializa el objeto auth para acceder a Firebase
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        //Se programa el evento onclick en los controles...
        binding.btLogin.setOnClickListener { haceLogin() }
        binding.btRegister.setOnClickListener { haceRegistro() }
    }

    private fun haceRegistro() {
        if (validos()) {
            val email = binding.etEmail.text.toString()
            val password = binding.etClave.text.toString()
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {  //Si se logró el registro
                        val user = auth.currentUser
                        updateUI(user)   //SE procesa para pasar a la pantalla del sistema en si.
                    } else {  //Si ocurrió algún error en el registro
                        Toast.makeText(baseContext,
                        getText(R.string.msg_fallo),
                        Toast.LENGTH_LONG).show()
                        updateUI(null)
                    }
                }
        } else {
            Toast.makeText(baseContext,
                getText(R.string.msg_datos),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {  //Existe un usuario autenticado
            //Se crea un intento por crear un activity/pantalla
            val intent = Intent(this,Principal::class.java)
            startActivity(intent)   //se inicia un nuevo activity / pantalla
        }
    }

    private fun haceLogin() {
        if (validos()) {
            val email = binding.etEmail.text.toString()
            val password = binding.etClave.text.toString()
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {  //Si se logró el registro
                        val user = auth.currentUser
                        updateUI(user)   //SE procesa para pasar a la pantalla del sistema en si.
                    } else {  //Si ocurrió algún error en el registro
                        Toast.makeText(baseContext,
                            getText(R.string.msg_fallo),
                            Toast.LENGTH_LONG).show()
                        updateUI(null)
                    }
                }
        } else {
            Toast.makeText(baseContext,
                getText(R.string.msg_datos),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun validos(): Boolean {
        return !(binding.etEmail.text.isEmpty() ||
                binding.etClave.text.isEmpty())
    }

    //Esto se ejecuta siempre que se presente esta pantalla en el celular
    public override fun onStart() {
        super.onStart()
        //Valido el usuario que está autenticado...
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

}