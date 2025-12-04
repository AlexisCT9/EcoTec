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

class BasurerosFragment : Fragment() {

    private lateinit var recycler: RecyclerView

    private val api by lazy {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_basureros, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.findViewById(R.id.recyclerBasureros)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        cargarBasureros()
    }

    private fun cargarBasureros() {
        api.getBasureros().enqueue(object : Callback<ResponseBasureros> {

            override fun onResponse(
                call: Call<ResponseBasureros>,
                response: Response<ResponseBasureros>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(requireContext(), "Error de servidor", Toast.LENGTH_SHORT).show()
                    return
                }

                val lista = response.body()?.basureros ?: emptyList()

                recycler.adapter = AdapterBasureros(
                    lista,
                    onClick = { basurero ->
                        val intent = Intent(requireContext(), DetalleBasureroActivity::class.java)
                        intent.putExtra("id", basurero.bote_id)
                        startActivity(intent)
                    }
                )
            }

            override fun onFailure(call: Call<ResponseBasureros>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
