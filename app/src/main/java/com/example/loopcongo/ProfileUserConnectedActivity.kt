package com.example.loopcongo


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.adapters.UserProfileViewPagerAdapter
import com.example.loopcongo.database.AppDatabase
import com.example.loopcongo.database.User
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class ProfileUserConnectedActivity : AppCompatActivity() {

    private lateinit var profileImage: ShapeableImageView
    private lateinit var nameUserConnected: TextView
    private lateinit var telephoneUserConnected: TextView
    private lateinit var descriptionUserConnected: TextView


    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_profile_user_connected)

        window.statusBarColor = ContextCompat.getColor(this, R.color.secondprimaryColor)

        profileImage = findViewById(R.id.profileImageUserConnected)
        nameUserConnected = findViewById(R.id.nameUserConnected)
        telephoneUserConnected = findViewById(R.id.telephoneUserConnected)
        descriptionUserConnected = findViewById(R.id.descriptionUserConnected)


        tabLayout = findViewById(R.id.userConnectedProfiletabLayout)
        viewPager = findViewById(R.id.userConnectedProfileviewPager)

        // Récupérer la DB et le DAO
        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()

        // Charger l'utilisateur depuis Room
        lifecycleScope.launch {
            val user: User? = userDao.getUser()
            user?.let {
                // Afficher les données dans la vue
                nameUserConnected.text = it.nom ?: "Utilisateur"
                telephoneUserConnected.text = it.contact ?: "N/A"
                descriptionUserConnected.text = it.about ?: ""

                // Passer l'id de l'utilisateur afin d'afficher les articles dans le tablayout
                setupViewPager(it.id)

                // Charger l'image de profil avec Glide
                if (!it.file_url.isNullOrEmpty()) {
                    Glide.with(this@ProfileUserConnectedActivity)
                        .load(it.file_url)
                        .placeholder(R.drawable.ic_person) // ton image par défaut
                        .into(profileImage)
                } else {
                    profileImage.setImageResource(R.drawable.ic_person)
                }
            }
        }
    }
    private fun setupViewPager(userId: Int) {
        val adapter = UserProfileViewPagerAdapter(this, userId)
        viewPager.adapter = adapter

        val tabTitles = arrayOf("Article", "Commande", "Operations")
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}
