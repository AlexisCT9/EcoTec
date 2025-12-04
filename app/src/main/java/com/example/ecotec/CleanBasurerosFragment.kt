package com.example.ecotec

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecotec.adapters.AdapterBasureros
import com.example.ecotec.api.ApiService
import com.example.ecotec.api.RetrofitClient
import com.example.ecotec.models.ResponseBasureros
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CleanBasurerosFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private var area = ""

    companion object {
        fun newInstance(area: String): CleanBasurerosFragment {
            val f = CleanBasurerosFragment()
            f.area = area
            return f
        }
    }

    private val api by lazy {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clean_basureros, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recycler = view.findViewById(R.id.recyclerBasurerosClean)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        cargar()
    }

    private fun cargar() {
        api.getBasureros().enqueue(object : Callback<ResponseBasureros> {

            override fun onResponse(
                call: Call<ResponseBasureros>,
                response: Response<ResponseBasureros>
            ) {
                val lista = response.body()?.basureros ?: return

                val filtrados = lista.filter { it.ubicacion.contains(area, ignoreCase = true) }

                recycler.adapter = AdapterBasureros(
                    filtrados,
                    onClick = { b ->
                        val i = Intent(requireContext(), CleanDetalleBasureroActivity::class.java)
                        i.putExtra("id", b.bote_id)
                        startActivity(i)
                    }
                )
            }

            override fun onFailure(call: Call<ResponseBasureros>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
