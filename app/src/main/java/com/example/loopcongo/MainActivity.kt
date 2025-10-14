package com.example.loopcongo

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.fragments.ArticleFragment
import com.example.loopcongo.fragments.HomeFragment
import com.example.loopcongo.fragments.ImmobilierFragment
import com.example.loopcongo.fragments.article.onglets.ArticleForAllCategoriesFragment
import com.example.loopcongo.fragments.vendeurs.VendeurMainFragment
import com.example.loopcongo.models.Product
import com.google.android.material.bottomnavigation.BottomNavigationView

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

}