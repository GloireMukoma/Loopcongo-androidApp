package com.example.loopcongo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.loopcongo.adapters.customerConnected.OngletsCustomerConnectedViewPagerAdapter
import com.example.loopcongo.database.AppDatabase
import com.example.loopcongo.database.Customer
import com.example.loopcongo.database.CustomerDao
import com.example.loopcongo.restApi.ApiClient
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomerConnectedActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter: OngletsCustomerConnectedViewPagerAdapter

    private lateinit var profileImage: ShapeableImageView
    private lateinit var nameCustomer: TextView
    private lateinit var phoneCustomer: TextView
    private lateinit var cityCustomer: TextView
    private lateinit var interetsCustomer: TextView
    private lateinit var logoutBtn: ImageView

    private lateinit var customerDao: CustomerDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_customer_connected)
        window.statusBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)

        // 🔹 Initialisation vues
        profileImage = findViewById(R.id.profileImgCustomerConnected)
        nameCustomer = findViewById(R.id.usernameCustomerConnected)
        phoneCustomer = findViewById(R.id.phoneCustomerConnected)
        cityCustomer = findViewById(R.id.cityCustomerConnected)
        interetsCustomer = findViewById(R.id.interetsCustomerConnected)
        logoutBtn = findViewById(R.id.logoutBtnCustomerConnected)
        tabLayout = findViewById(R.id.customerConnectedtabLayout)
        viewPager = findViewById(R.id.customerConnectedviewPager)

        // 🔹 DB
        val db = AppDatabase.getDatabase(this)
        customerDao = db.customerDao()

        // 🔹 Charger client
        lifecycleScope.launch {
            val localCustomer = customerDao.getCustomer()
            localCustomer?.let { customer ->
                // Afficher infos
                afficherInfosClient(customer)

                // 🔹 Attacher adapter avec l'ID du client
                viewPagerAdapter = OngletsCustomerConnectedViewPagerAdapter(this@CustomerConnectedActivity, customer.id)
                viewPager.adapter = viewPagerAdapter

                // 🔹 Attacher TabLayoutMediator seulement après que l’adapter soit assigné
                val tabTitles = arrayOf("Abonnements", "Articles")
                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    tab.text = tabTitles.getOrElse(position) { "Onglet ${position + 1}" }
                }.attach()

                // 🔹 Actualiser depuis API
                actualiserInfosDepuisApi(customer.id)
            }
        }

        // 🔹 Gestion déconnexion
        logoutBtn.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Déconnexion")
                .setMessage("Voulez-vous vraiment vous déconnecter ?")
                .setPositiveButton("Oui") { dialog, _ ->
                    lifecycleScope.launch {
                        customerDao.clearCustomers()
                        val intent = Intent(this@CustomerConnectedActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Non") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    private fun afficherInfosClient(customer: Customer) {
        nameCustomer.text = customer.username
        phoneCustomer.text = customer.contact ?: "--"
        cityCustomer.text = customer.city ?: "--"
        interetsCustomer.text = customer.interets ?: "--"

        if (!customer.file_url.isNullOrEmpty()) {
            Glide.with(this).load(customer.file_url).placeholder(R.drawable.avatar).into(profileImage)
        } else {
            profileImage.setImageResource(R.drawable.avatar)
        }
    }

    private suspend fun actualiserInfosDepuisApi(customerId: Int) {
        try {
            val response = ApiClient.instance.getCustomerById(customerId)
            if (response.isSuccessful) {
                val apiCustomer = response.body()?.data
                apiCustomer?.let {
                    val dbCustomer = Customer(
                        id = it.id,
                        username = it.username,
                        contact = it.contact,
                        city = it.city ?: "",
                        interets = it.interets ?: "",
                        file_url = it.file_url ?: "",
                        type_account = "customer"
                    )
                    customerDao.clearCustomers()
                    customerDao.insertCustomer(dbCustomer)
                    withContext(Dispatchers.Main) { afficherInfosClient(dbCustomer) }
                }
            } else {
                Log.e("API", "Erreur API : ${response.code()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@CustomerConnectedActivity, "Erreur : ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
