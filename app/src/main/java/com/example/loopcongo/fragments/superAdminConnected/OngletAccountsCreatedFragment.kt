package com.example.loopcongo.fragments.superAdminConnected

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.loopcongo.R
import com.example.loopcongo.adapters.superAdminConnected.AccountCreatedAdapter
import com.example.loopcongo.models.User
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OngletAccountsCreatedFragment :
    Fragment(),
    AccountCreatedAdapter.OnSubscriptionActionListener {

    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var txtEmpty: TextView
    private lateinit var inputSearch: EditText

    private lateinit var adapter: AccountCreatedAdapter
    private var fullList: List<User> = emptyList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.onglet_accounts_superadmin_connected, container, false)

        listView = view.findViewById(R.id.listViewAccounts)
        progressBar = view.findViewById(R.id.progressBarAccounts)
        txtEmpty = view.findViewById(R.id.txtEmptyAccounts)
        inputSearch = view.findViewById(R.id.inputSearch)

        adapter = AccountCreatedAdapter(requireContext(), mutableListOf(), this)
        listView.adapter = adapter

        fetchAccounts()

        inputSearch.addTextChangedListener { filterList(it.toString()) }

        return view
    }


    // 🔥 Charger comptes
    private fun fetchAccounts() {
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val users = ApiClient.instance.getAllUsers()

                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    fullList = users

                    if (users.isNotEmpty()) {
                        adapter.update(users)
                        txtEmpty.visibility = View.GONE
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


    // 🔎 Filtrer
    private fun filterList(query: String) {
        val filtered = fullList.filter {
            (it.username ?: "").contains(query, ignoreCase = true)
        }

        adapter.update(filtered)

        txtEmpty.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
        txtEmpty.text = if (filtered.isEmpty()) "Aucun résultat" else ""
    }



    // 🔥 Activer un abonnement
    override fun onValidate(userId: Int, type: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.instance.activateSubscription(userId, type)

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Abonnement $type activé", Toast.LENGTH_SHORT).show()
                    fetchAccounts()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Erreur : ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // ❌ Désactiver
    override fun onReject(userId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.instance.deactivateSubscription(userId)

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Abonnement désactivé", Toast.LENGTH_SHORT).show()
                    fetchAccounts()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Erreur : ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
