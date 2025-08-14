package com.example.loopcongo

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.loopcongo.adapters.UserProfileViewPagerAdapter
import com.example.loopcongo.adapters.prestataire.ProfilePrestataireViewPagerAdapter
import com.example.loopcongo.models.Prestataire
import com.example.loopcongo.models.PrestataireResponse
import com.example.loopcongo.restApi.ApiClient
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilePrestataireActivity : AppCompatActivity() {

    private lateinit var tvUsername: TextView
    private lateinit var tvTelephone: TextView
    private lateinit var tvProfession: TextView
    private lateinit var tvDescription: TextView
    private lateinit var avatar: ImageView

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private lateinit var prestataire: Prestataire

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_prestataire)
        //forcer pour que la barre de notification et d'en bas prenne un couleur
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Couleur de la status bar (en haut)
            window.statusBarColor = ContextCompat.getColor(this, R.color.primaryColor)
        }

        tvUsername = findViewById(R.id.profilPrestataireUsername)
        tvTelephone = findViewById(R.id.profilPrestataireTelephone)
        tvProfession = findViewById(R.id.profilPrestataireProfession)
        tvDescription = findViewById(R.id.profilPrestataireDescription)
        avatar = findViewById(R.id.profilPrestataireAvatarImage)

        viewPager = findViewById(R.id.profilePrestataireViewPager)
        tabLayout = findViewById(R.id.profilePrestataireTabLayout)

        val prestataireId = intent.getIntExtra("prestataire_id", -1)
        if (prestataireId == -1) {
            Toast.makeText(this, "ID prestataire invalide", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadPrestataire(prestataireId)
    }

    private fun loadPrestataire(id: Int) {
        ApiClient.instance.getPrestataireById(id).enqueue(object : Callback<PrestataireResponse> {
            override fun onResponse(call: Call<PrestataireResponse>, response: Response<PrestataireResponse>) {
                if (response.isSuccessful && response.body()?.status == true) {
                    prestataire = response.body()!!.data
                    afficherInfosPrestataire(prestataire)
                    setupViewPagerAndTabs(prestataire)
                } else {
                    Toast.makeText(this@ProfilePrestataireActivity, "Erreur récupération prestataire", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<PrestataireResponse>, t: Throwable) {
                Toast.makeText(this@ProfilePrestataireActivity, "Erreur réseau: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun afficherInfosPrestataire(prestataire: Prestataire) {
        tvUsername.text = prestataire.username
        tvTelephone.text = prestataire.telephone ?: "Téléphone non renseigné"
        tvProfession.text = prestataire.profession
        tvDescription.text = prestataire.description ?: "Pas de description"

        Glide.with(this)
            .load("https://loopcongo.com/" + prestataire.photo_profil)
            .placeholder(R.drawable.avatar)
            .into(avatar)
    }

    private fun setupViewPagerAndTabs(prestataire: Prestataire) {
        val adapter = ProfilePrestataireViewPagerAdapter(this, prestataire)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Prestations"
                else -> "Autres"
            }
        }.attach()
    }
}
