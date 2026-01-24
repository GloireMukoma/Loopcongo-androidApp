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
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.loopcongo.adapters.articles.CarouselUserAnnonceAdapter
import com.example.loopcongo.adapters.immobiliers.ImmobilierAdapter
import com.example.loopcongo.adapters.immobiliers.ImmobilierGridAdapter
import com.example.loopcongo.database.*
import com.example.loopcongo.models.AnnonceResponse
import com.example.loopcongo.models.Immobilier
import com.example.loopcongo.models.ImmobilierResponse2
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImmobilierActivity : AppCompatActivity() {

    private lateinit var userDao: UserDao
    private lateinit var customerDao: CustomerDao

    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: ImmobilierAdapter
    private lateinit var searchLayout: LinearLayout
    private lateinit var searchImmobilierEditText: EditText
    private lateinit var cityImmobiliers: TextView

    private val fullList: MutableList<Immobilier> = mutableListOf()   // API
    private val displayList: MutableList<Immobilier> = mutableListOf() // UI

    private lateinit var cityName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_immobilier)
        supportActionBar?.hide()

        window.statusBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Room
        val db = AppDatabase.getDatabase(this)
        userDao = db.userDao()
        customerDao = db.customerDao()

        val avatarImgUserConnected = findViewById<ImageView>(R.id.avatarImgUserConnected)
        lifecycleScope.launch {
            updateAvatarAndListener(avatarImgUserConnected)
        }

        cityName = intent.getStringExtra("cityName") ?: "Lubumbashi"
        cityImmobiliers = findViewById(R.id.cityImmobiliers)
        cityImmobiliers.text = "Immobiliers à $cityName"

        progressBar = findViewById(R.id.progressBarImmo)
        searchImmobilierEditText = findViewById(R.id.searchImmobilierEditText)
        //searchLayout = findViewById(R.id.searchLayout)

        // ✅ ListView + ArrayAdapter
        listView = findViewById(R.id.immobiliersListView)

        adapter = ImmobilierAdapter(this, displayList)
        listView.adapter = adapter

        // 🔹 API
        fetchImmobiliersByCity(cityName)

        // 🔹 Recherche temps réel
        searchImmobilierEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val query = s.toString().trim().lowercase()

                val filtered = if (query.isEmpty()) {
                    fullList
                } else {
                    fullList.filter {
                        it.quartier.lowercase().contains(query)
                    }
                }

                displayList.clear()
                displayList.addAll(filtered)
                adapter.notifyDataSetChanged()
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

                        fullList.clear()
                        fullList.addAll(response.body()!!.data)

                        // ✅ Copier vers la liste affichée
                        displayList.clear()
                        displayList.addAll(fullList)

                        adapter.notifyDataSetChanged()

                        if (fullList.isEmpty()) {
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_immobilier, menu)
        return true
    }

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


    private suspend fun updateAvatarAndListener(avatar: ImageView) {
        // Récupère les comptes connectés
        val user = userDao.getUser()       // vendeur ou immobilier
        val customer = customerDao.getCustomer() // client
        val currentAccount: Any? = user ?: customer

        // Met à jour l'image de l'avatar
        val imageUrl = when (currentAccount) {
            is User -> currentAccount.file_url
            is Customer -> currentAccount.file_url
            else -> null
        }

        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_login)
                .error(R.drawable.ic_login)
                .circleCrop()
                .into(avatar)
        } else {
            avatar.setImageResource(R.drawable.ic_login)
        }

        // Configure le listener sur l'avatar
        avatar.setOnClickListener {
            lifecycleScope.launch {
                val latestUser = userDao.getUser()
                val latestCustomer = customerDao.getCustomer()
                val account = latestUser ?: latestCustomer

                val nextActivity = when (account) {
                    is User -> {
                        if (account.id == 1) {
                            // Redirection vers l'admin si ID = 1
                            SuperAdminConnectedActivity::class.java
                        } else {
                            when (account.type_account?.lowercase()) {
                                "vendeur" -> ProfileUserConnectedActivity::class.java
                                "immobilier" -> UserImmobilierConnectedActivity::class.java
                                else -> ProfileUserConnectedActivity::class.java
                            }
                        }
                    }
                    is Customer -> CustomerConnectedActivity::class.java
                    else -> LoginActivity::class.java
                }

                startActivity(Intent(this@ImmobilierActivity, nextActivity))
            }
        }
    }
}
