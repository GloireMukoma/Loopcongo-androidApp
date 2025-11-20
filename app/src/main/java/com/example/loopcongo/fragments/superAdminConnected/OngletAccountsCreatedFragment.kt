package com.example.loopcongo.fragments.superAdminConnected

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.loopcongo.R
import com.example.loopcongo.adapters.superAdminConnected.AccountCreatedArrayAdapter
import com.example.loopcongo.models.User
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OngletAccountsCreatedFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var txtEmpty: TextView
    private lateinit var adapter: AccountCreatedArrayAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.onglet_accounts_superadmin_connected, container, false)

        listView = view.findViewById(R.id.listViewAccounts)
        progressBar = view.findViewById(R.id.progressBarAccounts)
        txtEmpty = view.findViewById(R.id.txtEmptyAccounts)

        // Adapter vide au début
        adapter = AccountCreatedArrayAdapter(requireContext(), emptyList())
        listView.adapter = adapter

        fetchAccounts()

        return view
    }

    private fun fetchAccounts() {
        progressBar.visibility = View.VISIBLE
        txtEmpty.visibility = View.GONE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val users = ApiClient.instance.getAllUsers() // directement la liste

                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE

                    if (users.isNotEmpty()) {
                        adapter = AccountCreatedArrayAdapter(requireContext(), users)
                        listView.adapter = adapter
                    } else {
                        txtEmpty.visibility = View.VISIBLE
                        txtEmpty.text = "Aucun compte disponible"
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    txtEmpty.visibility = View.VISIBLE
                    txtEmpty.text = "Erreur : ${e.message}"
                }
            }
        }
    }

}
