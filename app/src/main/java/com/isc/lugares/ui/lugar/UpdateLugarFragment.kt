package com.isc.lugares.ui.lugar

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.isc.lugares.R
import com.isc.lugares.databinding.FragmentUpdateLugarBinding
import com.isc.lugares.model.Lugar
import com.isc.lugares.viewmodel.LugarViewModel
import android.Manifest

class UpdateLugarFragment : Fragment() {

    private lateinit var lugarViewModel: LugarViewModel
    private var _binding: FragmentUpdateLugarBinding? = null
    private val binding get() = _binding!!

    //le define el objeto para leer el argumento pasado al fragmento
    private val args by navArgs<UpdateLugarFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel =
            ViewModelProvider(this).get(LugarViewModel::class.java)

        _binding = FragmentUpdateLugarBinding.inflate(inflater, container, false)

        //actualizo la información de los controles visuales...
        binding.etNombre.setText(args.lugar.nombre)
        binding.etCorreo.setText(args.lugar.correo)
        binding.etTelefono.setText(args.lugar.telefono)
        binding.etWeb.setText(args.lugar.web)
        binding.tvLatitud.text=args.lugar.latitud.toString()
        binding.tvLongitud.text=args.lugar.longitud.toString()
        binding.tvAltura.text=args.lugar.altura.toString()

        binding.btUpdateLugar.setOnClickListener {modificarLugar() }
        binding.btEmail.setOnClickListener { escribirCorreo() }
        binding.btPhone.setOnClickListener { llamarLugar() }
        binding.btWhatsapp.setOnClickListener { enviarWhatsApp() }
        binding.btWeb.setOnClickListener { verWebLugar() }
        binding.btLocation.setOnClickListener { verMapaLugar() }

        //El fragmento tiene un menú....
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun verMapaLugar() {
        //Extraemos las coordenadas desde los tv...
        val latitud = binding.tvLatitud.text.toString().toDouble()
        val longitud = binding.tvLongitud.text.toString().toDouble()

        if(latitud.isFinite() && longitud.isFinite()) {  //si son valores válidos
            //se crea un intento para hacer una vista del mapa
            val location = Uri.parse("geo:$latitud,$longitud?z18")
            val intent =Intent(Intent.ACTION_VIEW,location)
            startActivity(intent)
        } else {  //No tiene las coordenadas...
            Toast.makeText(requireContext(),
                getString(R.string.msg_datos),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun verWebLugar() {
        //Extraemos el sitio web del código
        val sitio = binding.etWeb.text.toString()
        if(sitio.isNotEmpty()) {  //si tiene algo... se ve el sitio web
            //se crea un intento para hacer una vista de un recurso
            val webpage = Uri.parse("http://$sitio")
            val intent =Intent(Intent.ACTION_VIEW,webpage)
            startActivity(intent)
        } else {  //No tiene un sitio web...
            Toast.makeText(requireContext(),
                getString(R.string.msg_datos),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun enviarWhatsApp() {
        //Extraemos el telefono del código
        val telefono = binding.etTelefono.text.toString()
        if(telefono.isNotEmpty()) {  //si tiene algo... se envia el mensaje
            //se crea un intento para hacer una vista de un recurso
            val intent =Intent(Intent.ACTION_VIEW)
            //Se define que se hace una llamada y se coloca el el url con la info
            intent.data = Uri.parse("whatsapp://send?phone=506" +
                    "$telefono&text="+getString(R.string.msg_saludos))
            intent.setPackage("com.whatsapp")
            startActivity(intent)
        } else {  //No tiene un telefono...
            Toast.makeText(requireContext(),
                getString(R.string.msg_datos),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun llamarLugar() {
        //Extraemos el telefono del código
        val telefono = binding.etTelefono.text.toString()
        if(telefono.isNotEmpty()) {  //si tiene algo... se llama
            //se crea un intento para hacer una llamada
            val intent =Intent(Intent.ACTION_CALL)
            //Se define que se hace una llamada y se coloca el número
            intent.data = Uri.parse("tel:$telefono")
            //Se piden los permisos
            if(requireActivity().checkSelfPermission(Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
                requireActivity().requestPermissions(arrayOf(Manifest.permission.CALL_PHONE),105)
            } else {
                requireActivity().startActivity(intent)
            }
        } else {  //No tiene un telefono...
            Toast.makeText(requireContext(),
                getString(R.string.msg_datos),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun escribirCorreo() {
        //Extraemos el correo del código
        val para = binding.etCorreo.text.toString()
        if(para.isNotEmpty()) {  //si tiene algo... se envia correo
            //Se crea un intento para "enviar" algo...
            val intent=Intent(Intent.ACTION_SEND)
            //se define el tipo de lo que se va a enviar correo electrónico
            intent.type="message/rfc822"
            //Se coloca el destinatario
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(para))
            //Se coloca el asunto
            intent.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.msg_saludos)+
                        " "+binding.etNombre.text)
            //Se coloca el mensaje del correo...
            intent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.msg_mensaje_correo))



            //Se invoca al recurso que envia correos...
            startActivity(intent)
        } else {  //No tiene un correo...
           Toast.makeText(requireContext(),
               getString(R.string.msg_datos),
               Toast.LENGTH_SHORT
           ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.menu_delete) {
            deleteLugar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun modificarLugar() {
        val nombre = binding.etNombre.text.toString()
        val correo = binding.etCorreo.text.toString()
        val telefono = binding.etTelefono.text.toString()
        val web = binding.etWeb.text.toString()
        if (validos(nombre,correo,telefono,web)) {
            val lugar= Lugar(args.lugar.id,
                nombre,
                correo,
                telefono,
                web,
                args.lugar.longitud,
                args.lugar.latitud,
                args.lugar.altura,
                args.lugar.rutaAudio,
                args.lugar.rutaImagen)
            lugarViewModel.updateLugar(lugar)
            Toast.makeText(
                requireContext(),
                getString(R.string.msgLugarModificado),
                Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateLugarFragment_to_nav_lugar)
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

    private fun deleteLugar() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.menu_delete)
        builder.setMessage(
            getString(R.string.seguroBorra)
                    + " ${args.lugar.nombre}?"
        )
        builder.setPositiveButton(getString(R.string.si)) { _, _ ->
            lugarViewModel.deleteLugar(args.lugar)
            findNavController().navigate(R.id.action_updateLugarFragment_to_nav_lugar)
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ -> }
        builder.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}