package com.example.loopcongo.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.*
import com.example.loopcongo.adapters.vendeurs.VendeurAdapter
import com.example.loopcongo.database.AppDatabase
import com.example.loopcongo.database.Customer
import com.example.loopcongo.database.CustomerDao
import com.example.loopcongo.database.UserDao
import com.example.loopcongo.models.User
import com.example.loopcongo.models.UserResponse
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendeurListFragment : Fragment() {

    private lateinit var userDao: UserDao
    private lateinit var customerDao: CustomerDao

    private lateinit var recyclerView: RecyclerView
    private lateinit var vendeurAdapter: VendeurAdapter
    private val vendeurs = mutableListOf<User>()
    private var type: String? = null

    companion object {
        fun newInstance(type: String): VendeurListFragment {
            val fragment = VendeurListFragment()
            val args = Bundle()
            args.putString("type", type)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getString("type")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vendeur_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        vendeurAdapter = VendeurAdapter(vendeurs)
        recyclerView.adapter = vendeurAdapter

        loadVendeurs()

        return view
    }

    private fun loadVendeurs() {
        ApiClient.instance.getVendeurs().enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val allVendeurs = response.body()?.data ?: emptyList()

                    // Filtrer les vendeurs selon leur type_account
                    val filtered = allVendeurs.filter { it.type_account == type }

                    vendeurs.clear()
                    vendeurs.addAll(filtered)
                    vendeurAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Erreur lors du chargement.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Échec: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
