package com.example.loopcongo.adapters.prestataire

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.loopcongo.DetailPrestationActivity
import com.example.loopcongo.R
import com.example.loopcongo.models.Prestation

class FilActualitePrestataireAdapter(
    private val mContext: Context,
    private val publications: List<Prestation>
) : ArrayAdapter<Prestation>(mContext, 0, publications) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(mContext)
            .inflate(R.layout.item_fil_actualite, parent, false)

        val publication = getItem(position)

        val userProfilImg: ImageView = view.findViewById(R.id.profilUserImgFilActu)
        val image: ImageView = view.findViewById(R.id.imagePublicationFilActu)
        val username: TextView = view.findViewById(R.id.usernameFilActu)
        val profession: TextView = view.findViewById(R.id.professionFilActu)
        val textContenu: TextView = view.findViewById(R.id.texteContenuFilActu)
        val createdAt: TextView = view.findViewById(R.id.datePublicationFilActu)

        username.text = publication?.prestataire_nom ?: ""
        profession.text = publication?.prestataire_profession ?: ""
        textContenu.text = publication?.description ?: ""
        createdAt.text = publication?.date_publication ?: ""

        Glide.with(mContext)
            .load("https://loopcongo.com/" + (publication?.photo_profil ?: ""))
            .placeholder(R.drawable.avatar)
            .into(userProfilImg)

        Glide.with(mContext)
            .load("https://loopcongo.com/" + (publication?.image ?: ""))
            .placeholder(R.drawable.loading)
            .into(image)

        // ✅ Gestion du clic pour ouvrir l'activité de détail
        view.setOnClickListener {
            val intent = Intent(mContext, DetailPrestationActivity::class.java)
            intent.putExtra("prestataire_nom", publication?.prestataire_nom)
            intent.putExtra("prestataire_profession", publication?.prestataire_profession)
            intent.putExtra("description", publication?.description)
            intent.putExtra("date_publication", publication?.date_publication)
            intent.putExtra("photo_profil", publication?.photo_profil)
            intent.putExtra("image", publication?.image)
            mContext.startActivity(intent)
        }

        return view
    }
}

