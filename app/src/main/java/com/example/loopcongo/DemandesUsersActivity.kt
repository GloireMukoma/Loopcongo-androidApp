package com.example.loopcongo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.adapters.ImmoUserDemandeAdapter
import com.example.loopcongo.models.ApiResponseDemande
import com.example.loopcongo.models.ImmoUserDemande
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DemandesUsersActivity : AppCompatActivity() {

    private lateinit var demandeRecycler: RecyclerView
    private lateinit var demandeAdapter: ImmoUserDemandeAdapter
    private val demandes = mutableListOf<ImmoUserDemande>()
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demandes_users)

        // Couleur de la status bar (en haut)
        window.statusBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)
        // Couleur de la navigation bar (en bas)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)

        demandeRecycler = findViewById(R.id.demandesRecyclerView)
        progressBar = findViewById(R.id.progressBarDemandes)

        // 🔹 LinearLayoutManager vertical pour que les dividers fonctionnent
        demandeRecycler.layoutManager = LinearLayoutManager(this)

        // 🔹 Adapter
        demandeAdapter = ImmoUserDemandeAdapter(demandes, R.layout.item_demande2)
        demandeRecycler.adapter = demandeAdapter

        // 🔹 Ajouter le divider par défaut
        val dividerItemDecoration = DividerItemDecoration(
            demandeRecycler.context,
            (demandeRecycler.layoutManager as LinearLayoutManager).orientation
        )
        demandeRecycler.addItemDecoration(dividerItemDecoration)

        // 🔹 Charger les demandes
        loadDemandes()
    }

    private fun loadDemandes() {
        progressBar.visibility = View.VISIBLE

        ApiClient.instance.getUserImmoDemandes().enqueue(object : Callback<ApiResponseDemande> {
            override fun onResponse(
                call: Call<ApiResponseDemande>,
                response: Response<ApiResponseDemande>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()!!.demandes
                    demandes.clear()
                    demandes.addAll(result)
                    demandeAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@DemandesUsersActivity, "Aucune demande trouvée", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponseDemande>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@DemandesUsersActivity, "Erreur de chargement des demandes", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
