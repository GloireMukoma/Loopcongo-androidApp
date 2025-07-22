package com.example.loopcongo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.fragments.AccueilFragment
import com.example.loopcongo.fragments.ArticleFragment
import com.example.loopcongo.fragments.FragmentTabVendeur
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

        /*supportActionBar?.title = "Acceuil"
        val textView = findViewById<TextView>(R.id.toolbarTitle) // si tu utilises custom layout
        textView.setTextColor(Color.RED)*/

        loadFragment(ArticleFragment())
        // import la barre de navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bNavHome -> loadFragment(ArticleFragment())
                R.id.bNavArticle -> loadFragment(FragmentTabVendeur())
                R.id.bNavImmo -> loadFragment(ArticleFragment()) // <-- ici
                R.id.bNavUsers -> loadFragment(ArticleFragment())
                 R.id.bNavAnnonce -> loadFragment(ArticleFragment())
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