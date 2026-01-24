package com.example.loopcongo.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loopcongo.R
import com.example.loopcongo.models.ImmoUserDemande
import com.example.loopcongo.models.User

class MesAbonnesAdapter(
    context: Context,
    private val abonnes: List<User>
) : ArrayAdapter<User>(context, 0, abonnes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_mes_abonnes_account, parent, false)

        val item = abonnes[position]

        val avatar = view.findViewById<ImageView>(R.id.avatarImgUserAccountMesAbonnesItem)
        val username = view.findViewById<TextView>(R.id.usernameMesAbonnesItem)
        val contact = view.findViewById<TextView>(R.id.usernameContactMesAbonnesItem)

        username.text = item.username
        contact.text = item.contact ?: ""

        Glide.with(context)
            .load(item.file_url)
            .placeholder(R.drawable.user1)
            .circleCrop()
            .into(avatar)

        view.setOnClickListener{}

        return view
    }
}
