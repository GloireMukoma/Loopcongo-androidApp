package com.example.loopcongo.fragments.superAdminConnected

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
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

class OngletAccountsCreatedFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var txtEmpty: TextView
    private lateinit var inputSearch: EditText

    private lateinit var adapter: AccountCreatedAdapter
    private var fullList: List<User> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.onglet_accounts_superadmin_connected, container, false)

        listView = view.findViewById(R.id.listViewAccounts)
        progressBar = view.findViewById(R.id.progressBarAccounts)
        txtEmpty = view.findViewById(R.id.txtEmptyAccounts)
        inputSearch = view.findViewById(R.id.inputSearch)

        adapter = AccountCreatedAdapter(requireContext(), mutableListOf())
        listView.adapter = adapter

        fetchAccounts()

        // 🔍 Recherche instantanée
        inputSearch.addTextChangedListener {
            filterList(it.toString())
        }

        return view
    }

    private fun fetchAccounts() {
        progressBar.visibility = View.VISIBLE
        txtEmpty.visibility = View.GONE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val users = ApiClient.instance.getAllUsers()

                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE

                    fullList = users

                    if (users.isNotEmpty()) {
                        adapter.update(users)
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

    private fun filterList(query: String) {
        val filtered = fullList.filter {
            it.username?.contains(query, ignoreCase = true) ?: false
        }
        adapter.update(filtered)
        txtEmpty.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
        txtEmpty.text = if (filtered.isEmpty()) "Aucun résultat" else ""
    }

}
