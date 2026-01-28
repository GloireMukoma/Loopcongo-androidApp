package com.example.loopcongo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.loopcongo.models.Customer
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileCustomerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_profile_customer)

        val imgProfil = findViewById<ImageView>(R.id.imgCustomerAvatar)
        val txtUsername = findViewById<TextView>(R.id.txtCustomerName)
        val txtPhone = findViewById<TextView>(R.id.txtCustomerContact)
        //val txtGenre = findViewById<TextView>(R.id.txtGenre)
        //val txtCreatedAt = findViewById<TextView>(R.id.txtCreatedAt)
        val txtCity = findViewById<TextView>(R.id.txtCustomerCity)
        val txtInterets = findViewById<TextView>(R.id.txtCustomerAbout)

        val customerId = intent.getIntExtra("customerId", -1)

        ApiClient.instance.getCustomerInfo(customerId)
            .enqueue(object : Callback<Customer> {

                override fun onResponse(
                    call: Call<Customer>,
                    response: Response<Customer>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val customer = response.body()!!

                        txtUsername.text = customer.username
                        txtPhone.text = customer.contact
                        txtCity.text = customer.city
                        txtInterets.text = customer.interets
                        //txtCreatedAt.text = "Inscrit le : ${customer.created_at}"

                        Glide.with(this@ProfileCustomerActivity)
                            .load("https://loopcongo.com/${customer.file_url}")
                            .placeholder(R.drawable.ic_user)
                            .into(imgProfil)
                    }
                }

                override fun onFailure(call: Call<Customer>, t: Throwable) {
                    Toast.makeText(
                        this@ProfileCustomerActivity,
                        "Erreur de chargement",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

}