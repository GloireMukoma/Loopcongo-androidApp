package com.example.loopcongo.fragments.userConnectedProfilOnglets

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.loopcongo.SubscriptionActivity
import com.example.loopcongo.R
import com.example.loopcongo.adapters.vendeurs.OngletOptionsVendeurAdapter
import com.example.loopcongo.adapters.vendeurs.SettingItem
import com.example.loopcongo.database.AppDatabase
import kotlinx.coroutines.launch
import java.net.URLEncoder

class OngletOptionsFragment : Fragment() {

    private lateinit var listSettings: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.onglet_options_profil_user_connected, container, false)
        listSettings = view.findViewById(R.id.listSettings)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ Liste des éléments du menu de l'onglet "Options"
        val items = listOf(
            SettingItem(R.drawable.ic_citizen, "Publier un article"),
            SettingItem(R.drawable.ic_citizen, "Publier une annonce"),
            SettingItem(R.drawable.ic_delete_, "Abonnement"),
            SettingItem(R.drawable.ic_person, "A propos"),
            //SettingItem(R.drawable.ic_encontinu, "Certifier mon compte")
        )

        // ✅ Configuration de l’adaptateur
        val adapter = OngletOptionsVendeurAdapter(requireContext(), items)
        listSettings.adapter = adapter

        // ✅ Gestion du clic sur les éléments
        listSettings.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val item = items[position]

            // Lancer une coroutine avec lifecycleScope pour gérer correctement le cycle de vie
            lifecycleScope.launch {
                // Récupérer l'utilisateur depuis la DB (suspend function)
                val user = AppDatabase.getDatabase(requireContext()).userDao().getUser()

                if (user == null) {
                    Toast.makeText(requireContext(), "Utilisateur non trouvé", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // Encoder le nom si nécessaire
                val encodedName = URLEncoder.encode(user.username, "UTF-8")

                // Construire l'URL ou exécuter l'action selon l'item
                when (item.title) {
                    "Publier un article" -> {
                        val url = "https://loopcongo.com/from-android/product/form/${user.id}"
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

    }
}
