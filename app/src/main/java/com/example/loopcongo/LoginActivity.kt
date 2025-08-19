package com.example.loopcongo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.loopcongo.ProfileUserConnectedActivity
import com.example.loopcongo.R
import com.example.loopcongo.database.AppDatabase
import com.example.loopcongo.database.LoginResponse

import com.example.loopcongo.database.User
import com.example.loopcongo.database.UserDao
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val db = AppDatabase.getDatabase(this)
        userDao = db.userDao()

        val edtPassword = findViewById<EditText>(R.id.editTextPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val password = edtPassword.text.toString().trim()

            if (password.isEmpty()) {
                Toast.makeText(this, "Veuillez entrer le mot de passe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val body = mapOf("password" to password)

            ApiClient.instance.login(body).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        val user = response.body()!!.data
                        if (user != null) {
                            lifecycleScope.launch {
                                userDao.clearUsers()
                                userDao.insertUser(user)

                                // Aller vers ProfileActivity
                                startActivity(Intent(this@LoginActivity, ProfileUserConnectedActivity::class.java))
                                finish()
                            }
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

}
