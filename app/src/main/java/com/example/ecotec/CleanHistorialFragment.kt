package com.example.ecotec

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecotec.adapters.CleanHistorialAdapter
import com.example.ecotec.api.ApiService
import com.example.ecotec.api.RetrofitClient
import com.example.ecotec.models.ResponseHistorial
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CleanHistorialFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var progress: ProgressBar

    private val api by lazy {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clean_historial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.findViewById(R.id.recyclerHistorial)
        progress = view.findViewById(R.id.progressHistorial)

        recycler.layoutManager = LinearLayoutManager(requireContext())

        cargarHistorial()
    }

    private fun cargarHistorial() {
        progress.visibility = View.VISIBLE

        api.getHistorialLimpieza().enqueue(object : Callback<ResponseHistorial> {
            override fun onResponse(
                call: Call<ResponseHistorial>,
                resp: Response<ResponseHistorial>
            ) {
                progress.visibility = View.GONE

                if (!resp.isSuccessful || resp.body() == null) {
                    Toast.makeText(requireContext(), "Error del servidor", Toast.LENGTH_SHORT).show()
                    return
                }

                recycler.adapter = CleanHistorialAdapter(resp.body()!!.historial)
            }

            override fun onFailure(call: Call<ResponseHistorial>, t: Throwable) {
                progress.visibility = View.GONE
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
