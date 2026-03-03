package com.example.loopcongo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.adapters.servers.ServerAdapter
import com.example.loopcongo.adapters.servers.ServerMessagesAdapter
import com.example.loopcongo.models.Server
import com.example.loopcongo.models.ServerMessage
import com.example.loopcongo.models.ServerResponse
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServeurActivity : AppCompatActivity() {

    private lateinit var serverNameText: TextView
    private lateinit var membersCountText: TextView

    private lateinit var serversRecyclerView: RecyclerView
    private lateinit var messagesRecyclerView: RecyclerView

    private lateinit var serverAdapter: ServerAdapter
    private lateinit var messagesAdapter: ServerMessagesAdapter

    private val serverList = mutableListOf<Server>()
    private val messageList = mutableListOf<ServerMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serveur)
        supportActionBar?.hide()

        serverNameText = findViewById(R.id.serverName)
        membersCountText = findViewById(R.id.nombreNombreServeur)

        // ===== RecyclerView serveurs =====
        serversRecyclerView = findViewById(R.id.serversRecyclerView)
        serversRecyclerView.layoutManager = LinearLayoutManager(this)

        /*serverAdapter = ServerAdapter(serverList) { server ->
            loadServerMessages(server.id)
        }*/


        serverAdapter = ServerAdapter(serverList) { server ->

            // 🔥 Mettre à jour le header
            serverNameText.text = server.name
            membersCountText.text = "${server.membres_count} membres · Communauté"

            // 🔥 Charger les messages
            loadServerMessages(server.id)
        }

        serversRecyclerView.adapter = serverAdapter

        // ===== RecyclerView messages =====
        messagesRecyclerView = findViewById(R.id.serverDiscussionMsgRecyclerView)
        messagesRecyclerView.layoutManager = LinearLayoutManager(this)

        messagesAdapter = ServerMessagesAdapter(messageList)
        messagesRecyclerView.adapter = messagesAdapter

        loadServers()
    }

    // ===============================
    // Charger serveurs utilisateur
    // ===============================
    private fun loadServers() {

        ApiClient.instance.getUserServers(28, "vendeur")
            .enqueue(object : Callback<ServerResponse> {

                override fun onResponse(
                    call: Call<ServerResponse>,
                    response: Response<ServerResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {

                        serverList.clear()
                        serverList.addAll(response.body()!!.data)
                        serverAdapter.notifyDataSetChanged()

                        // 🔥 Charger automatiquement le premier serveur
                        /*if (serverList.isNotEmpty()) {
                            loadServerMessages(serverList[0].id)
                        }*/

                        if (serverList.isNotEmpty()) {

                            val firstServer = serverList[0]

                            serverNameText.text = firstServer.name
                            membersCountText.text = "${firstServer.membres_count} membres · Communauté"

                            loadServerMessages(firstServer.id)
                        }
                    }
                }

                override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                    Toast.makeText(
                        this@ServeurActivity,
                        "Erreur: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    // ===============================
    // Charger messages d'un serveur
    // ===============================
    private fun loadServerMessages(serverId: Int) {

        ApiClient.instance.getServerMessages(serverId)
            .enqueue(object : Callback<List<ServerMessage>> {

                override fun onResponse(
                    call: Call<List<ServerMessage>>,
                    response: Response<List<ServerMessage>>
                ) {
                    if (response.isSuccessful && response.body() != null) {

                        messageList.clear()
                        messageList.addAll(response.body()!!)
                        messagesAdapter.notifyDataSetChanged()

                        // Scroll vers le bas (style Discord)
                        messagesRecyclerView.scrollToPosition(messageList.size - 1)
                    }
                }

                override fun onFailure(call: Call<List<ServerMessage>>, t: Throwable) {
                    Toast.makeText(
                        this@ServeurActivity,
                        "Impossible de charger les messages",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}