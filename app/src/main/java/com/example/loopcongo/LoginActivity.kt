package com.example.loopcongo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.loopcongo.database.AppDatabase
import com.example.loopcongo.database.UserDao
import com.example.loopcongo.database.CustomerDao
import com.example.loopcongo.database.User as DbUser
import com.example.loopcongo.database.Customer as DbCustomer
import com.example.loopcongo.models.ApiUser
import com.example.loopcongo.models.LoginResponse
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var userDao: UserDao
    private lateinit var customerDao: CustomerDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        // 🔹 DB Room
        val db = AppDatabase.getDatabase(this)
        userDao = db.userDao()
        customerDao = db.customerDao()

        val edtPassword = findViewById<EditText>(R.id.editTextPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val password = edtPassword.text.toString().trim()
            if (password.isEmpty()) {
                Toast.makeText(this, "Veuillez entrer le mot de passe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val body = mapOf("password" to password)

            // 🔹 Appel API pour login
            ApiClient.instance.login(body).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        val apiUser: ApiUser? = response.body()?.data
                        if (apiUser != null) {
                            lifecycleScope.launch {
                                saveAccount(apiUser) // Sauvegarde dans Room

                                // 🔹 Redirection selon type
                                val nextActivity = when (apiUser.type?.lowercase()) {
                                    "user" -> when (apiUser.type_account?.lowercase()) {
                                        "vendeur" -> ProfileUserConnectedActivity::class.java
                                        "immobilier" -> UserImmobilierConnectedActivity::class.java
                                        else -> ProfileUserConnectedActivity::class.java
                                    }
                                    "customer" -> CustomerConnectedActivity::class.java
                                    else -> ProfileUserConnectedActivity::class.java
                                }

                                startActivity(Intent(this@LoginActivity, nextActivity))
                                finish()
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, "Erreur: données utilisateur manquantes", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Mot de passe incorrect", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Erreur réseau: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    // 🔹 Fonction pour sauvegarder User ou Customer dans Room
    private suspend fun saveAccount(apiUser: ApiUser) {
        when (apiUser.type?.lowercase()) {
            "user" -> {
                val dbUser = DbUser(
                    id = apiUser.id,
                    type = apiUser.type,
                    type_account = apiUser.type_account,
                    username = apiUser.username,
                    is_certified = apiUser.is_certified,
                    contact = apiUser.contact,
                    city = apiUser.city ?: "",
                    file_url = apiUser.file_url ?: "",
                    about = apiUser.about,
                    created_at = apiUser.created_at
                )
                userDao.clearUsers()
                userDao.insertUser(dbUser)
            }
            "customer" -> {
                val dbCustomer = DbCustomer(
                    id = apiUser.id,
                    username = apiUser.username ?: "",
                    contact = apiUser.contact ?: "",
                    city = apiUser.city ?: "",
                    interets = apiUser.interets ?: "",
                    file_url = apiUser.file_url ?: "",
                    type_account = "customer"
                )
                customerDao.clearCustomers()
                customerDao.insertCustomer(dbCustomer)
            }
        }
    }
}
