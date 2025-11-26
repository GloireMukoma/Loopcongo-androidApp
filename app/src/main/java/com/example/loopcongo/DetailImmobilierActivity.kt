package com.example.loopcongo

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.loopcongo.models.DetailImage
import com.example.loopcongo.restApi.ApiClient
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailImmobilierActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        //Couleur de la status bar
        window.statusBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.BleuClairPrimaryColor)

        setContentView(R.layout.activity_detail_immobilier2)
        supportActionBar?.title = "Détail de l'immobilier"

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // ferme l'activité actuelle pour ne pas revenir avec le bouton "Retour"
        }

        // ✅ Récupération des vues
        val imagePrincipale = findViewById<ImageView>(R.id.imagePrincipaldetailImmobilier)
        val auteur = findViewById<TextView>(R.id.nomAuteurdetailImmobilier)
        val addressImmo = findViewById<TextView>(R.id.adresseImmobilierDetail)
        val type = findViewById<TextView>(R.id.detailTypeImmobilier)

        val description = findViewById<TextView>(R.id.detailImmobilierDescription)
        val avatar = findViewById<ShapeableImageView>(R.id.userAvatarImmoDetail)
        val status = findViewById<TextView>(R.id.statutDetailImmobilier)

        val userContact = findViewById<TextView>(R.id.userContactImmoDetail)
        val prix = findViewById<TextView>(R.id.prixDetailImmobilier)

        // ✅ Récupération des extras
        val immoId = intent.getIntExtra("immoId", 0)
        // ID du vendeur
        val userId = intent.getIntExtra("userId", 0)

        val typeimmo = intent.getStringExtra("typeImmo")
        val statut = intent.getStringExtra("statut")

        val city = intent.getStringExtra("city")
        val quartier = intent.getStringExtra("quartier")

        val immoPrix = intent.getStringExtra("prix")
        val address = intent.getStringExtra("address")

        val immoDescription = intent.getStringExtra("description")
        val image = intent.getStringExtra("ImmoImage")

        val username = intent.getStringExtra("username")
        val userImageProfil = intent.getStringExtra("userImage")

        val contact = intent.getStringExtra("userContact")

        type.text = typeimmo
        description.text = immoDescription
        prix.text = "$ ${immoPrix}"

        status.text = " • ${statut}"
        auteur.text = username

        userContact.text = contact
        addressImmo.text = address

        // ✅ Charger l'image principale
        Glide.with(this)
            .load("https://loopcongo.com/$image")
            .placeholder(R.drawable.loading)
            .into(imagePrincipale)

        // Charger l'image du profil user
        Glide.with(this)
            .load("https://loopcongo.com/$userImageProfil")
            .placeholder(R.drawable.loading)
            .into(avatar)

        // ✅ Charger les images de détail depuis l'API
        if (userId != 0) {
            fetchDetailImagesImmobiliers(immoId)
        } else {
            Toast.makeText(this, "ID de l'article invalide", Toast.LENGTH_SHORT).show()
        }

        // Redirection vers le profile du vendeur
        val btnVoirProfil = findViewById<Button>(R.id.btnVoirProfilDetailImmo)
        btnVoirProfil.setOnClickListener {

            // Appel API dans une coroutine
            lifecycleScope.launch {
                try {
                    // Appel de l'API pour récupérer le vendeur par son ID
                    val response = ApiClient.instance.userById(userId)
                    if (response.isSuccessful && response.body() != null) {
                        val vendeur = response.body()!!.data

                        // Préparer l'Intent pour ProfileVendeurActivity
                        val intent = Intent(this@DetailImmobilierActivity, ProfileVendeurImmobilierActivity::class.java)
                        intent.putExtra("vendeurId", vendeur.id)
                        intent.putExtra("vendeurUsername", vendeur.username)
                        intent.putExtra("vendeurContact", vendeur.contact)
                        intent.putExtra("vendeurCity", vendeur.city)
                        intent.putExtra("vendeurDescription", vendeur.about)
                        intent.putExtra("vendeurTypeAccount", vendeur.type_account)
                        intent.putExtra("vendeurAvatarImg", vendeur.file_url)
                        intent.putExtra("isCertifiedVendeur", vendeur.is_certified ?: 0)
                        intent.putExtra("vendeurTotalArticles", vendeur.total_articles ?: 0)
                        intent.putExtra("vendeurTotalLikes", vendeur.total_articles ?: 0)
                        intent.putExtra("vendeurNbAbonner", vendeur.nb_abonner ?: 0)

                        // Lancer l'activité
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@DetailImmobilierActivity, "Impossible de récupérer le profil", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@DetailImmobilierActivity, "Erreur réseau", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Redirection vers le profile du vendeur

        val discuterBtn = findViewById<LinearLayout>(R.id.contactButtonWhatsappDetailImmo)

        discuterBtn.setOnClickListener {
            if (!contact.isNullOrEmpty()) {

                val immoLink = "https://loopcongo.com/immo/detail/$immoId"
                val message = "Bonjour, je suis intéressé par votre immobilier sur LoopCongo.\n" +
                        "Lien de l'immobilier : $immoLink"

                val url = "https://wa.me/$userContact?text=${Uri.encode(message)}"

                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "WhatsApp n’est pas installé", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Numéro WhatsApp introuvable", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 🔹 Récupération asynchrone des images de détail
    private fun fetchDetailImagesImmobiliers(immoId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.instance.getImmobiliersDetailImages(immoId)
                if (response.isSuccessful) {
                    val images = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        displayDetailImages(images)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@DetailImmobilierActivity, "Erreur de chargement des images", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailImmobilierActivity, "Erreur réseau", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 🔹 Affichage dynamique des images en lignes de 2
    private fun displayDetailImages(images: List<DetailImage>) {
        val container = findViewById<LinearLayout>(R.id.containerImagesDetailImmobilier)
        container.removeAllViews()

        if (images.isEmpty()) return

        val inflater = LayoutInflater.from(this)

        for (i in images.indices step 2) {
            val rowLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.bottomMargin = 8.dpToPx()
                layoutParams = params
            }

            for (j in 0..1) {
                if (i + j >= images.size) break

                val imageUrl = images[i + j].file_url
                val imageView = ImageView(this).apply {
                    val imgParams = LinearLayout.LayoutParams(0, 150.dpToPx())
                    imgParams.weight = 1f
                    imgParams.marginEnd = if (j == 0) 6.dpToPx() else 0
                    layoutParams = imgParams
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    background = ContextCompat.getDrawable(
                        this@DetailImmobilierActivity,
                        R.drawable.rounded_image_background
                    )
                    clipToOutline = true // ✅ arrondir les coins
                    setOnClickListener { showImagePopup(imageUrl) }
                }

                Glide.with(this)
                    .load("https://loopcongo.com/${imageUrl}")
                    .placeholder(R.drawable.loading)
                    .into(imageView)

                rowLayout.addView(imageView)
            }

            container.addView(rowLayout)
        }
    }

    // 🔹 Conversion dp -> px
    private fun Int.dpToPx(): Int =
        (this * resources.displayMetrics.density).toInt()

    // 🔹 Popup d’affichage de l’image
    private fun showImagePopup(imageUrl: String) {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.popup_image, null)

        val popupWindow = PopupWindow(
            view,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        )

        val imageView = view.findViewById<ImageView>(R.id.popupImageView)
        val closeBtn = view.findViewById<ImageView>(R.id.closePopup)

        // ✅ Chargement fluide de l'image
        Glide.with(this)
            .load("https://loopcongo.com/${imageUrl}")
            .placeholder(R.drawable.loading)
            .into(imageView)

        // ✅ Fermer la popup
        closeBtn.setOnClickListener { popupWindow.dismiss() }
        view.setOnClickListener { popupWindow.dismiss() }

        // ✅ Transition douce
        popupWindow.animationStyle = R.style.PopupAnimation

        // ✅ Afficher au centre
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

}