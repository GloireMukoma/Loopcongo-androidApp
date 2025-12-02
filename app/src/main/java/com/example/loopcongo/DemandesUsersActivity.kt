package com.example.loopcongo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
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

    private lateinit var demandeListView: ListView
    private lateinit var demandeAdapter: ImmoUserDemandeAdapter
    private val demandes = mutableListOf<ImmoUserDemande>()
    private lateinit var progressBar: ProgressBar

    private lateinit var tvNoDemande: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demandes_users)
        supportActionBar?.hide()

        window.statusBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)
        //window.navigationBarColor = ContextCompat.getColor(this, R.color.BleuClairPrimaryColor)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // ferme l'activité actuelle pour ne pas revenir avec le bouton "Retour"
        }

        demandeListView = findViewById(R.id.demandesListView)
        progressBar = findViewById(R.id.progressBarDemandes)

        tvNoDemande = findViewById(R.id.tvNoDemande)


        // 🔹 Adapter pour ListView
        demandeAdapter = ImmoUserDemandeAdapter(
            this,
            R.layout.item_demande2,
            demandes
        )
        demandeListView.adapter = demandeAdapter

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

                    // ✔Afficher / cacher le TextView selon la présence de données
                    if (demandes.isEmpty()) {
                        tvNoDemande.visibility = View.VISIBLE
                        demandeListView.visibility = View.GONE
                    } else {
                        tvNoDemande.visibility = View.GONE
                        demandeListView.visibility = View.VISIBLE
                    }

                } else {
                    // Pas de données reçues → message par défaut
                    tvNoDemande.visibility = View.VISIBLE
                    demandeListView.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<ApiResponseDemande>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@DemandesUsersActivity, "Erreur de chargement des demandes", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
