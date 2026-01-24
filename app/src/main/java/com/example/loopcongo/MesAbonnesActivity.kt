package com.example.loopcongo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.adapters.ImmoUserDemandeAdapter
import com.example.loopcongo.adapters.MesAbonnesAdapter
import com.example.loopcongo.database.AppDatabase
import com.example.loopcongo.models.ApiResponseDemande
import com.example.loopcongo.models.ApiResponseMesAbonnes
import com.example.loopcongo.models.ImmoUserDemande
import com.example.loopcongo.models.User
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MesAbonnesActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: MesAbonnesAdapter
    private val abonnes = mutableListOf<User>()
    private lateinit var progressBar: ProgressBar
    private lateinit var tvNoAbonnes: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mes_abonnes)
        supportActionBar?.hide()

        listView = findViewById(R.id.mesAbonnesListView)
        progressBar = findViewById(R.id.progressBarMesAbonnes)
        tvNoAbonnes = findViewById(R.id.tvNoAbonnes)

        adapter = MesAbonnesAdapter(this, abonnes)
        listView.adapter = adapter

        // 🔹 Récupération du user connecté depuis Room
        lifecycleScope.launch {

            val db = AppDatabase.getDatabase(this@MesAbonnesActivity)
            val userDao = db.userDao()

            // 1 Cas vendeur connecté
            val vendeur = userDao.getUser()

            if (vendeur != null) {
                loadAbonnes(vendeur.id)
                return@launch
            }

            // 3️⃣ Aucun utilisateur connecté
            tvNoAbonnes.visibility = View.VISIBLE
            listView.visibility = View.GONE
        }
    }

    private fun loadAbonnes(userId: Int) {
        progressBar.visibility = View.VISIBLE

        ApiClient.instance.getMesAbonnes(userId)
            .enqueue(object : Callback<ApiResponseMesAbonnes> {

                override fun onResponse(
                    call: Call<ApiResponseMesAbonnes>,
                    response: Response<ApiResponseMesAbonnes>
                ) {
                    progressBar.visibility = View.GONE

                    if (response.isSuccessful && response.body()?.status == true) {

                        abonnes.clear()
                        abonnes.addAll(response.body()!!.abonnes)
                        adapter.notifyDataSetChanged()

                        tvNoAbonnes.visibility =
                            if (abonnes.isEmpty()) View.VISIBLE else View.GONE
                        listView.visibility =
                            if (abonnes.isEmpty()) View.GONE else View.VISIBLE

                    } else {
                        tvNoAbonnes.visibility = View.VISIBLE
                        listView.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<ApiResponseMesAbonnes>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@MesAbonnesActivity,
                        "Erreur réseau",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
