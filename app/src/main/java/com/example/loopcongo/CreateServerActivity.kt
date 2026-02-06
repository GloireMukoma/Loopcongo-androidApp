package com.example.loopcongo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.adapters.servers.ServerCategorieAdapter
import com.example.loopcongo.models.ServerCategorie
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateServerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_server)
        supportActionBar?.hide()

        window.navigationBarColor =
            ContextCompat.getColor(this, R.color.BleuClairPrimaryColor)

        val listView = findViewById<ListView>(R.id.serverCategoriesListView)

        ApiClient.instance.getServerCategories()
            .enqueue(object : Callback<List<ServerCategorie>> {

                override fun onResponse(
                    call: Call<List<ServerCategorie>>,
                    response: Response<List<ServerCategorie>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val adapter = ServerCategorieAdapter(
                            this@CreateServerActivity,
                            response.body()!!
                        )
                        listView.adapter = adapter
                    }
                }

                override fun onFailure(call: Call<List<ServerCategorie>>, t: Throwable) {
                    Toast.makeText(
                        this@CreateServerActivity,
                        "Erreur de chargement des catégories",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
