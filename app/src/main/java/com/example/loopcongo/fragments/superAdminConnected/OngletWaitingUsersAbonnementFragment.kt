package com.example.loopcongo.fragments.superAdminConnected

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

import com.example.loopcongo.R
import com.example.loopcongo.adapters.superAdminConnected.WaitingUsersAbonnementAdapter
import com.example.loopcongo.models.User
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.launch

class OngletWaitingUsersAbonnementFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var txtEmpty: TextView
    private lateinit var adapter: WaitingUsersAbonnementAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.onglet_waiting_users_abonnement, container, false)

        listView = view.findViewById(R.id.waitingUserDemandesAbonnementRecyclerView)
        txtEmpty = view.findViewById(R.id.txtEmptyWaiting)

        loadPendingUsers()

        return view
    }

    private fun loadPendingUsers() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.instance.getWaitingUsersAbonnements()

                if (response.isEmpty()) {
                    // Afficher message vide
                    txtEmpty.visibility = View.VISIBLE
                    listView.visibility = View.GONE
                    return@launch
                }

                // Sinon, afficher la liste
                txtEmpty.visibility = View.GONE
                listView.visibility = View.VISIBLE

                adapter = WaitingUsersAbonnementAdapter(
                    requireActivity(),
                    response,
                    object : WaitingUsersAbonnementAdapter.OnPendingActionListener {

                        override fun onValidate(userId: Int) {
                            validateAbonnement(userId)
                        }

                        override fun onReject(userId: Int) {
                            rejectAbonnement(userId)
                        }
                    }
                )

                listView.adapter = adapter

            } catch (e: Exception) {
                e.printStackTrace()
                txtEmpty.visibility = View.VISIBLE
                txtEmpty.text = "Erreur de chargement"
            }
        }
    }

    private fun validateAbonnement(userId: Int) {
        lifecycleScope.launch {
            ApiClient.instance.validateAbonnement(userId)
            loadPendingUsers()
            Toast.makeText(requireContext(), "Abonnement validé avec succès!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun rejectAbonnement(userId: Int) {
        lifecycleScope.launch {
            ApiClient.instance.rejectAbonnement(userId)
            loadPendingUsers()
            Toast.makeText(requireContext(), "Abonnement réjeté!", Toast.LENGTH_SHORT).show()
        }
    }
}
