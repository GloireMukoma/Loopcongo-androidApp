package com.example.loopcongo

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.adapters.articles.ArticleForPopupRandomUserPremiumAdapter
import com.example.loopcongo.database.AppDatabase
import com.example.loopcongo.fragments.ArticleFragment
import com.example.loopcongo.fragments.HomeFragment
import com.example.loopcongo.fragments.HomeImmobilierFragment
import com.example.loopcongo.fragments.ImmobilierFragment
import com.example.loopcongo.fragments.article.onglets.ArticleForAllCategoriesFragment
import com.example.loopcongo.fragments.vendeurs.VendeurMainFragment
import com.example.loopcongo.models.Product
import com.example.loopcongo.models.RandomPremiumUserResponse
import com.example.loopcongo.restApi.ApiClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
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
                R.id.bNavPublish -> {
                    // Récupérer la base de données et les DAO
                    val db = AppDatabase.getDatabase(this)
                    val userDao = db.userDao()
                    val customerDao = db.customerDao()

                    lifecycleScope.launch {
                        val vendeur = userDao.getUser()
                        val client = customerDao.getCustomer()

                        when {
                            vendeur != null -> {
                                // ✅ Si c’est un vendeur, redirection vers la page de publication
                                val url = "https://loopcongo.com/from-android/product/form/${vendeur.id}"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                                startActivity(intent)
                            }

                            client != null -> {
                                // 🚫 Si c’est un client, affichage d’un message
                                Toast.makeText(
                                    this@MainActivity,
                                    "Les clients ne peuvent pas publier d’articles.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            else -> {
                                // 🚪 Si personne n’est connecté
                                Toast.makeText(
                                    this@MainActivity,
                                    "Veuillez vous connecter pour publier un article.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }

                R.id.bNavImmo -> loadFragment(HomeImmobilierFragment())
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

}