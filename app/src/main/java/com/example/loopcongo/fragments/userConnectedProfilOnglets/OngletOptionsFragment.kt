package com.example.loopcongo.fragments.userConnectedProfilOnglets

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.loopcongo.BoostActivity
import com.example.loopcongo.PlanAbonnementActivity
import com.example.loopcongo.PrestataireActivity
import com.example.loopcongo.R
import com.example.loopcongo.adapters.vendeurs.OngletOptionsVendeurAdapter
import com.example.loopcongo.adapters.vendeurs.SettingItem

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
            SettingItem(R.drawable.ic_delete_, "Abonnement"),
            SettingItem(R.drawable.ic_person, "Booster un article"),
            SettingItem(R.drawable.ic_encontinu, "Certifier mon compte")
        )

        // ✅ Configuration de l’adaptateur
        val adapter = OngletOptionsVendeurAdapter(requireContext(), items)
        listSettings.adapter = adapter

        // ✅ Gestion du clic sur les éléments
        listSettings.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val item = items[position]
            when (item.title) {
                "Publier un article" -> {
                    val intent = Intent(requireContext(), BoostActivity::class.java)
                    startActivity(intent)
                }
                "Abonnement" -> {
                    val intent = Intent(requireContext(), PlanAbonnementActivity::class.java)
                    startActivity(intent)
                }
                "Booster un article" -> {
                    Toast.makeText(requireContext(), "Ouvrir la liste d’articles à booster", Toast.LENGTH_SHORT).show()
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
