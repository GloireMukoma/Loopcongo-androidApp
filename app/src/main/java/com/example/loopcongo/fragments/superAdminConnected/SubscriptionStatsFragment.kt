package com.example.loopcongo.fragments.superAdminConnected

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.superAdminConnected.SubscribedUsersAdapter
import com.example.loopcongo.models.SubscriptionStatsResponse
import com.example.loopcongo.models.User
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubscriptionStatsFragment : Fragment() {

    private lateinit var txtTotal: TextView
    private lateinit var txtBasic: TextView
    private lateinit var txtPro: TextView
    private lateinit var txtPremium: TextView
    private lateinit var txtWaiting: TextView
    private lateinit var recycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.onglet_stats_super_admin_fragment, container, false)

        txtTotal = view.findViewById(R.id.txtTotalActive)
        txtBasic = view.findViewById(R.id.txtBasic)
        txtPro = view.findViewById(R.id.txtPro)
        txtPremium = view.findViewById(R.id.txtPremium)
        txtWaiting = view.findViewById(R.id.txtWaiting)
        recycler = view.findViewById(R.id.recyclerSubscribedUsers)

        recycler.layoutManager = LinearLayoutManager(requireContext())

        loadStats()
        loadSubscribedUsers()

        return view
    }

    private fun loadStats() {
        ApiClient.instance.getSubscriptionStats()
            .enqueue(object : Callback<SubscriptionStatsResponse> {
                override fun onResponse(
                    call: Call<SubscriptionStatsResponse>,
                    response: Response<SubscriptionStatsResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()!!.data

                        txtTotal.text = data.total_active.toString()
                        txtBasic.text = data.basic.toString()
                        txtPro.text = data.pro.toString()
                        txtPremium.text = data.premium.toString()
                        txtWaiting.text = "En attente : ${data.waiting}"
                    }
                }

                override fun onFailure(call: Call<SubscriptionStatsResponse>, t: Throwable) {}
            })
    }

    private fun loadSubscribedUsers() {
        ApiClient.instance.getSubscribedUsers()
            .enqueue(object : Callback<List<User>> {
                override fun onResponse(
                    call: Call<List<User>>,
                    response: Response<List<User>>
                ) {
                    if (response.isSuccessful) {
                        recycler.adapter = SubscribedUsersAdapter(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {}
            })
    }
}
