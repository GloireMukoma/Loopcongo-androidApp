package com.example.loopcongo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.adapters.servers.ListServersAdapter
import com.example.loopcongo.models.Server
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetServersListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_servers_list)
        supportActionBar?.hide()

        val listView = findViewById<ListView>(R.id.getServersListView)

        ApiClient.instance.getServers()
            .enqueue(object : Callback<List<Server>> {

                override fun onResponse(
                    call: Call<List<Server>>,
                    response: Response<List<Server>>
                ) {
                    if (response.isSuccessful && response.body() != null) {

                        val adapter = ListServersAdapter(
                            this@GetServersListActivity,
                            response.body()!!
                        )

                        listView.adapter = adapter
                    }
                }

                override fun onFailure(call: Call<List<Server>>, t: Throwable) {
                    Toast.makeText(
                        this@GetServersListActivity,
                        "Erreur de chargement des serveurs",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
