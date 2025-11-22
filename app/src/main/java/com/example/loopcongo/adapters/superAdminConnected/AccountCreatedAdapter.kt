package com.example.loopcongo.adapters.superAdminConnected

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.User

class AccountCreatedAdapter(
    context: Context,
    private val users: List<User>
) : ArrayAdapter<User>(context, 0, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_account, parent, false)

        val user = users[position]

        val imgProfile = view.findViewById<ImageView>(R.id.imgProfileUser)
        val txtUsername = view.findViewById<TextView>(R.id.username)
        val txtPhone = view.findViewById<TextView>(R.id.phone)
        val txtLocation = view.findViewById<TextView>(R.id.location)
        val txtAbout = view.findViewById<TextView>(R.id.about)
        val badge = view.findViewById<ImageView>(R.id.vendeurBadgeSponsor)

        // Remplissage
        txtUsername.text = user.username
        txtPhone.text = user.contact
        txtLocation.text = user.city
        txtAbout.text = user.about

        // Badge SPONSOR
        badge.visibility = if (user.is_certified == "1") View.VISIBLE else View.GONE

        // Image
        Glide.with(context)
            .load("https://loopcongo.com/${user.file_url}")
            .placeholder(R.drawable.avatar)
            .centerCrop()
            .into(imgProfile)

        return view
    }
}
