package com.example.loopcongo

import android.os.Bundle
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.servers.ServerMessagesAdapter
import com.example.loopcongo.database.AppDatabase
import com.example.loopcongo.models.BasicResponse
import com.example.loopcongo.models.ServerMessage
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServerDiscussionActivity : AppCompatActivity() {

    private var isMember = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_discussion)
        supportActionBar?.hide()

        val serverId = intent.getIntExtra("server_id", 0)
        val serverName = intent.getStringExtra("serverName") ?: ""

        val serverNameTopBarTxt = findViewById<TextView>(R.id.serverNameTopBarTxt)
        serverNameTopBarTxt.text = serverName

        val btnMenu = findViewById<ImageView>(R.id.btnMenu)
        btnMenu.setOnClickListener {

            val popup = PopupMenu(this, btnMenu)
            popup.menuInflater.inflate(R.menu.server_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_rejoindre -> {

                        joinServeur(serverId)
                        //Toast.makeText(this, "Rejoindre serveur", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.menu_quitter -> {
                        Toast.makeText(this, "Quitter serveur", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.serverDiscussionMsgRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        ApiClient.instance.getServerMessages(serverId)
            .enqueue(object : Callback<List<ServerMessage>> {

                override fun onResponse(
                    call: Call<List<ServerMessage>>,
                    response: Response<List<ServerMessage>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        recyclerView.adapter =
                            ServerMessagesAdapter(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<List<ServerMessage>>, t: Throwable) {
                    Toast.makeText(
                        this@ServerDiscussionActivity,
                        "Impossible de charger les messages",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun joinServeur(serverId: Int) {

        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()
        val customerDao = db.customerDao()

        lifecycleScope.launch {

            val vendeur = userDao.getUser()
            val client = customerDao.getCustomer()

            var userId = 0
            var userType = ""

            if (vendeur != null) {
                userId = vendeur.id
                userType = "vendeur"
            } else if (client != null) {
                userId = client.id
                userType = "client"
            }

            ApiClient.instance.joinServer(serverId, userId, userType)
                .enqueue(object : Callback<BasicResponse> {

                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {

                        if (response.isSuccessful && response.body() != null) {

                            val apiResponse = response.body()!!

                            if (apiResponse.status) {

                                Toast.makeText(
                                    this@ServerDiscussionActivity,
                                    "Serveur rejoint avec succès",
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else {

                                Toast.makeText(
                                    this@ServerDiscussionActivity,
                                    apiResponse.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                        Toast.makeText(
                            this@ServerDiscussionActivity,
                            "Erreur connexion",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}
