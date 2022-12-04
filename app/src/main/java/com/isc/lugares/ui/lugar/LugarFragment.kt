package com.isc.lugares.ui.lugar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.isc.lugares.R
import com.isc.lugares.adapter.LugarAdapter
import com.isc.lugares.databinding.FragmentLugarBinding
import com.isc.lugares.viewmodel.LugarViewModel

class LugarFragment : Fragment() {

    private lateinit var lugarViewModel: LugarViewModel
    private var _binding: FragmentLugarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel =
            ViewModelProvider(this).get(LugarViewModel::class.java)

        _binding = FragmentLugarBinding.inflate(inflater, container, false)

        binding.btAddLugarFab.setOnClickListener {
            findNavController().navigate(R.id.action_nav_lugar_to_addLugarFragment)
        }

        //Activamos el adapter para ver la info..
        val lugarAdapter = LugarAdapter()
        //Activamos el recyclerView (el reciclador)
        val reciclador = binding.reciclador

        //Se asocia el adapterLugar al reciclerView
        reciclador.adapter=lugarAdapter

        //usualmente SIEMPRE va esta línea
        reciclador.layoutManager = LinearLayoutManager(requireContext())

        lugarViewModel.getAllData.observe(
            viewLifecycleOwner,{
                lugares -> lugarAdapter.setData(lugares)
            }
        )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}