package com.example.loopcongo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.loopcongo.adapters.articles.CarouselUserAnnonceAdapter
import com.example.loopcongo.adapters.immobiliers.ImmobilierGridAdapter
import com.example.loopcongo.models.AnnonceResponse
import com.example.loopcongo.models.Immobilier
import com.example.loopcongo.models.ImmobilierResponse2
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImmobilierActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2
    private val sliderHandler = Handler(Looper.getMainLooper())

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: ImmobilierGridAdapter
    private lateinit var searchLayout: LinearLayout
    private lateinit var searchImmobilierEditText: EditText

    private var fullList: List<Immobilier> = listOf()
    private lateinit var cityName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_immobilier)

        // Couleur de la status bar (en haut)
        window.statusBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)
        // Couleur de la navigation bar (en bas)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)

        recyclerView = findViewById(R.id.immobiliersRecyclerView)
        progressBar = findViewById(R.id.progressBarImmo)
        searchLayout = findViewById(R.id.searchLayout)
        searchImmobilierEditText = findViewById(R.id.searchImmobilierEditText)

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = ImmobilierGridAdapter(emptyList())
        recyclerView.adapter = adapter

        // 🔹 Titre de la barre d'action
        cityName = intent.getStringExtra("cityName") ?: "Lubumbashi"
        title = "Biens à $cityName"

        // 🔹 Récupération des biens depuis l'API
        fetchImmobiliersByCity(cityName)

        // 🔹 Filtrage en temps réel par quartier
        searchImmobilierEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim().lowercase()
                val filteredList = fullList.filter { it.quartier.lowercase().contains(query) }
                adapter = ImmobilierGridAdapter(filteredList)
                recyclerView.adapter = adapter
            }
        })
    }

    private fun fetchImmobiliersByCity(city: String) {
        progressBar.visibility = View.VISIBLE

        ApiClient.instance.getImmobiliersByCity(city)
            .enqueue(object : Callback<ImmobilierResponse2> {
                override fun onResponse(
                    call: Call<ImmobilierResponse2>,
                    response: Response<ImmobilierResponse2>
                ) {
                    progressBar.visibility = View.GONE

                    if (response.isSuccessful && response.body() != null) {
                        fullList = response.body()!!.data

                        if (fullList.isNotEmpty()) {
                            adapter = ImmobilierGridAdapter(fullList)
                            recyclerView.adapter = adapter
                        } else {
                            Toast.makeText(
                                this@ImmobilierActivity,
                                "Aucun bien trouvé pour ${response.body()!!.city}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@ImmobilierActivity,
                            "Réponse invalide du serveur",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ImmobilierResponse2>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@ImmobilierActivity,
                        "Erreur réseau : ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    // 🔹 Menu de la TopBar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_immobilier, menu)
        return true
    }

    // 🔹 Action sur la loupe
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                searchLayout.visibility =
                    if (searchLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
