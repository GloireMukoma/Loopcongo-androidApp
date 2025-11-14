package com.example.loopcongo.fragments.userImmobilierConnectedOnglets

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.loopcongo.R
import com.example.loopcongo.SubscriptionActivity
import com.example.loopcongo.adapters.userImmobilierConnected.OngletOperationsAdapter
import com.example.loopcongo.adapters.vendeurs.SettingItem
import com.example.loopcongo.database.AppDatabase
import kotlinx.coroutines.launch
import java.net.URLEncoder

class OngletOperationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.onglet_operations_profil_user_immobilier_connected,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<ListView>(R.id.ongletOperationsUserImmoConnectedListView)

        // ✅ Liste des éléments
        val items = listOf(
            SettingItem(R.drawable.ic_publish_product, "Publier un bien"),
            SettingItem(R.drawable.ic_annonce_, "Publier une annonce"),
            SettingItem(R.drawable.ic_stars_, "Abonnement"),
            SettingItem(R.drawable.ic_person_, "A propos")
        )

        val adapter = OngletOperationsAdapter(requireContext(), items)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = items[position]

            lifecycleScope.launch {
                val user = AppDatabase.getDatabase(requireContext()).userDao().getUser()
                if (user == null) {
                    Toast.makeText(requireContext(), "Utilisateur non trouvé", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val encodedCity = URLEncoder.encode(user.city, "UTF-8")

                when (selectedItem.title) {
                    "Publier un bien" -> {
                        val url = "https://loopcongo.com/from-android/immobilier/form/${user.id}/$encodedCity"
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    }

                    "Publier une annonce" -> {
                        val url = "https://loopcongo.com/user/annonce/publish/${user.id}"
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    }

                    "Abonnement" -> {
                        startActivity(Intent(requireContext(), SubscriptionActivity::class.java))
                    }

                    "A propos" -> {
                        Toast.makeText(requireContext(), "Page à propos en cours...", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        Toast.makeText(requireContext(), "Action inconnue", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
