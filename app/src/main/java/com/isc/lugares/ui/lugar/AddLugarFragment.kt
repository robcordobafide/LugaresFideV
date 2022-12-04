package com.isc.lugares.ui.lugar

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.isc.lugares.R
import com.isc.lugares.databinding.FragmentAddLugarBinding
import com.isc.lugares.model.Lugar
import com.isc.lugares.viewmodel.LugarViewModel


class AddLugarFragment : Fragment() {

    private lateinit var lugarViewModel: LugarViewModel
    private var _binding: FragmentAddLugarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel =
            ViewModelProvider(this).get(LugarViewModel::class.java)

        _binding = FragmentAddLugarBinding.inflate(inflater, container, false)

        binding.btAddLugar.setOnClickListener {
            agregarLugar()
        }

        ubicaGPS()

        return binding.root
    }

    private fun ubicaGPS() {
        //se crea un localización
        val fusedLocationClient: FusedLocationProviderClient=
            LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            //Se solicitan los permisos...
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),105)
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            location: Location? ->
            if (location != null) {  //Si se pudo obtener la ubicación
                binding.tvLatitud.text = "${location.latitude}"
                binding.tvLongitud.text = "${location.longitude}"
                binding.tvAltura.text = "${location.altitude}"
            } else {
                binding.tvLatitud.text = "Error"
                binding.tvLongitud.text = "Error"
                binding.tvAltura.text = "Error"
            }
        }
    }

    private fun agregarLugar() {
        val nombre = binding.etNombre.text.toString()
        val correo = binding.etCorreo.text.toString()
        val telefono = binding.etTelefono.text.toString()
        val web = binding.etWeb.text.toString()
        val latitud = binding.tvLatitud.text.toString().toDouble()
        val longitud = binding.tvLongitud.text.toString().toDouble()
        val altura = binding.tvAltura.text.toString().toDouble().toInt()

        if (validos(nombre,correo,telefono,web)) {
            val lugar= Lugar("",nombre,correo,telefono,web,
                longitud,latitud,altura,"","")
            lugarViewModel.addLugar(lugar)
            Toast.makeText(
                requireContext(),
                getString(R.string.msgLugarAgregado),
                Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addLugarFragment_to_nav_lugar)
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.msgFaltanDatos),
                Toast.LENGTH_LONG).show()
        }
    }

    private fun validos(nombre: String, correo: String, telefono: String, web: String): Boolean {
        return !(nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty()
                || web.isEmpty())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}