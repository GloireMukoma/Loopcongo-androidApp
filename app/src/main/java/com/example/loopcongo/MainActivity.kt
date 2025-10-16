package com.example.loopcongo

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.fragments.ArticleFragment
import com.example.loopcongo.fragments.HomeFragment
import com.example.loopcongo.fragments.ImmobilierFragment
import com.example.loopcongo.fragments.article.onglets.ArticleForAllCategoriesFragment
import com.example.loopcongo.fragments.vendeurs.VendeurMainFragment
import com.example.loopcongo.models.Product
import com.example.loopcongo.restApi.ApiClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    //private lateinit var adapter: ProductAdapter
    private lateinit var products: List<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        //forcer pour que la barre de notification et d'en bas prenne un couleur
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Couleur de la status bar (en haut)
            window.statusBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)
            // Couleur de la navigation bar (en bas)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)
        }

        /*supportActionBar?.title = "Acceuil"
        val textView = findViewById<TextView>(R.id.toolbarTitle) // si tu utilises custom layout
        textView.setTextColor(Color.RED)*/

        loadFragment(HomeFragment())
        // import la barre de navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bNavHome -> loadFragment(HomeFragment())
                R.id.bNavArticle -> loadFragment(ArticleFragment())
                R.id.bNavImmo -> loadFragment(ImmobilierFragment())
                R.id.bNavUsers -> loadFragment(VendeurMainFragment())
                /*R.id.bNavCitizens -> {
                     val intent = Intent(this, PrestataireActivity::class.java)
                     startActivity(intent)
                     true
                 }*/
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        // Injecter le fragment sur l'activité principale de l'application
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    // Fonction pour le menu d'en haut
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Affiche la popup des users premiums et leurs articles
    fun showUserProfilePopup(context: Context, userId: Int) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.popup_premium_user_and_articles, null)

        val recyclerView: RecyclerView = view.findViewById(R.id.popupPremiumUserRecyclerView)
        val viewProfileBtn: Button = view.findViewById(R.id.popupPremiumUserViewProfilBtn)

        val userName: TextView = view.findViewById(R.id.popupPremiumUsername)
        val numero: TextView = view.findViewById(R.id.popupPremiumUserNumero) // ou autre id pour le nom
        val userImage: ImageView = view.findViewById(R.id.popupPremiumUserAvatarImg)

        val adapter = ProductAdapter(context, listOf())
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        val dialog = AlertDialog.Builder(context)
            .setView(view)
            .create()

        dialog.show()

        // Charger les données
        ApiClient.instance.getUserProfile(userId).enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.status && body.user.subscription_type == "premium") {
                        userName.text = body.user.nom
                        Glide.with(context)
                            .load("https://loopcongo.com/${body.user.file_url}")
                            .placeholder(R.drawable.loading)
                            .into(userImage)

                        adapter.updateData(body.products)
                    } else {
                        Toast.makeText(context, "L'utilisateur n'est pas premium", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Toast.makeText(context, "Erreur: ${t.message}", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        })

        viewProfileBtn.setOnClickListener {
            // Redirection vers la page complète du profil
            dialog.dismiss()
        }
    }


}