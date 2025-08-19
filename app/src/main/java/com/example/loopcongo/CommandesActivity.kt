package com.example.loopcongo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import com.example.loopcongo.adapters.commande.CommandesAdapter
import com.example.loopcongo.models.Commande
import com.example.loopcongo.models.CommandeResponse
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Cette activité est vue comme une activité de notification
// Quand on appuie sur l'icon de notification, on redirige sur cette activité
// Notifier l'utilisateur les commandes sur ses produits
class CommandesActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: CommandesAdapter
    private val commandes = mutableListOf<Commande>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commandes)
        supportActionBar?.title = "Notifications"

        listView = findViewById(R.id.commandesListView)
        adapter = CommandesAdapter(this, commandes)
        listView.adapter = adapter

        val vendeurId = 1 // Récupérer depuis SharedPreferences ou session
        loadCommandes(vendeurId)
    }

    private fun loadCommandes(vendeurId: Int) {
        ApiClient.instance.getCommandesVendeur(vendeurId)
            .enqueue(object : Callback<CommandeResponse> {
                override fun onResponse(
                    call: Call<CommandeResponse>,
                    response: Response<CommandeResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()?.commandes ?: emptyList()
                        commandes.clear()
                        commandes.addAll(result)
                        adapter.notifyDataSetChanged()
                    }
                }
                override fun onFailure(call: Call<CommandeResponse>, t: Throwable) {
                    Toast.makeText(this@CommandesActivity, "Erreur de chargement", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
