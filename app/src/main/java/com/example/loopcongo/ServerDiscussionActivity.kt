package com.example.loopcongo

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.servers.ServerMessagesAdapter
import com.example.loopcongo.models.ServerMessage
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServerDiscussionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_discussion)
        supportActionBar?.hide()

        val serverId = intent.getIntExtra("server_id", 0)
        val serverName = intent.getStringExtra("serverName") ?: ""

        val serverNameTopBarTxt = findViewById<TextView>(R.id.serverNameTopBarTxt)
        serverNameTopBarTxt.text = serverName

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
}
