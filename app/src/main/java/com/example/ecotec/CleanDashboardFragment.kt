package com.example.ecotec.clean

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecotec.CleanDetalleBasureroActivity
import com.example.ecotec.R
import com.example.ecotec.adapters.AdapterBasurerosClean
import com.example.ecotec.api.ApiService
import com.example.ecotec.api.RetrofitClient
import com.example.ecotec.models.ResponseBasureros
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CleanDashboardFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_clean_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.findViewById(R.id.recyclerClean)
        progress = view.findViewById(R.id.progressClean)

        recycler.layoutManager = LinearLayoutManager(requireContext())

        cargarBasureros()
    }

    private fun cargarBasureros() {
        progress.visibility = View.VISIBLE

        api.getBasureros().enqueue(object : Callback<ResponseBasureros> {

            override fun onResponse(
                call: Call<ResponseBasureros>,
                response: Response<ResponseBasureros>
            ) {
                progress.visibility = View.GONE

                if (!response.isSuccessful) {
                    Toast.makeText(requireContext(), "Error del servidor", Toast.LENGTH_SHORT).show()
                    return
                }

                val lista = response.body()?.basureros ?: emptyList()

                recycler.adapter = AdapterBasurerosClean(
                    lista,
                    onClick = { basurero ->
                        val intent = Intent(requireContext(), CleanDetalleBasureroActivity::class.java)
                        intent.putExtra("id", basurero.bote_id)
                        startActivity(intent)
                    }
                )
            }

            override fun onFailure(call: Call<ResponseBasureros>, t: Throwable) {
                progress.visibility = View.GONE
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
