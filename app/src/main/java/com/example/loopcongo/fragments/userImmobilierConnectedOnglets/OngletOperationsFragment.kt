package com.example.loopcongo.fragments.userImmobilierConnectedOnglets

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.SubscriptionActivity
import com.example.loopcongo.adapters.userImmobilierConnected.OngletOperationsUserImmoConnectedAdapter
import com.example.loopcongo.database.AppDatabase
import kotlinx.coroutines.launch
import java.net.URLEncoder

class OngletOperationsFragment : Fragment() {

    private lateinit var operationsUserImmoRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.onglet_operations_profil_user_immobilier_connected,
            container,
            false
        )

        operationsUserImmoRecyclerView = view.findViewById(R.id.ongletOperationsUserImmoConnectedRecyclerView)

        // Configuration du RecyclerView
        operationsUserImmoRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Ligne de séparation entre les éléments
        val divider = DividerItemDecoration(operationsUserImmoRecyclerView.context, DividerItemDecoration.VERTICAL)
        operationsUserImmoRecyclerView.addItemDecoration(divider)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val icons = listOf(
            R.drawable.ic_home,
            R.drawable.ic_arrow,
            R.drawable.ic_subscription,
            R.drawable.ic_encontinu
        )

        val operations = listOf(
            "Publier un immobilier",
            "Publier une annonce",
            "Abonnement",
            "Certifier mon compte"
        )

        val adapter = OngletOperationsUserImmoConnectedAdapter(requireContext(), icons, operations) { selectedOperation ->

            lifecycleScope.launch {
                val user = AppDatabase.getDatabase(requireContext()).userDao().getUser()

                if (user == null) {
                    Toast.makeText(requireContext(), "Utilisateur non trouvé", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                val encodedCity = URLEncoder.encode(user.city, "UTF-8")

                when (selectedOperation) {
                    "Publier un immobilier" -> {
                        val url = "https://loopcongo.com/from-android/immobilier/form/${user.id}/$encodedCity"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }

                    "Publier une annonce" -> {
                        val url = "https://loopcongo.com/user/annonce/publish/${user.id}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }

                    "Abonnement" -> {
                        val intent = Intent(requireContext(), SubscriptionActivity::class.java)
                        startActivity(intent)
                    }

                    "Certifier mon compte" -> {
                        Toast.makeText(requireContext(), "Ouvrir la page de certification", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        Toast.makeText(requireContext(), "Action inconnue", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        operationsUserImmoRecyclerView.adapter = adapter
    }
}
