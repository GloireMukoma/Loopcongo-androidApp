package com.example.loopcongo

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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

class DetailArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Couleur de la status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.BleuFoncePrimaryColor)
        }

        setContentView(R.layout.activity_detail_article)
        supportActionBar?.title = "Détail de l'article"

        // ✅ Récupération des vues
        val imagePrincipale = findViewById<ImageView>(R.id.imagePrincipaldetailArticle)
        val auteur = findViewById<TextView>(R.id.nomAuteurdetailImage)

        val contact = findViewById<TextView>(R.id.userContactArticleDetail)
        //val sponsoredbadje = findViewById<TextView>(R.id.numeroAuteurdetailImage)

        val nomArticle = findViewById<TextView>(R.id.detailNomArticle)
        val description = findViewById<TextView>(R.id.detailArticleDescription)
        //val avatarAuteur = findViewById<ShapeableImageView>(R.id.avatarAuteurdetailArticle)

        val prix = findViewById<TextView>(R.id.prixDetailArticle)

        // ✅ Récupération des extras
        val id = intent.getIntExtra("article_id", 0)
        val nom = intent.getStringExtra("article_nom")
        val image = intent.getStringExtra("article_photo")

        val user = intent.getStringExtra("article_auteur")
        val auteurAvatarUrl = intent.getStringExtra("user_avatar")

        val userContact = intent.getStringExtra("user_contact")

        val article_prix = intent.getStringExtra("article_prix")
        val devise = intent.getStringExtra("article_devise")

        val desc = intent.getStringExtra("article_description")
        val nbLike = intent.getStringExtra("article_nbLike") ?: "0"

        nomArticle.text = nom
        auteur.text = user
        contact.text = userContact
        description.text = desc

        prix.text = "$article_prix $devise"

        // Charger l'image de profil de l'auteur
        /*if (!auteurAvatarUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load("https://loopcongo.com/$auteurAvatarUrl")
                .placeholder(R.drawable.user_profile)
                .error(R.drawable.user_profile)
                .into(avatarAuteur)
        }*/

        // ✅ Charger l'image principale
        Glide.with(this)
            .load("https://loopcongo.com/$image")
            .placeholder(R.drawable.loading)
            .into(imagePrincipale)

        // ✅ Charger les images de détail depuis l'API
        if (id != 0) {
            fetchDetailImages(id)
        } else {
            Toast.makeText(this, "ID de l'article invalide", Toast.LENGTH_SHORT).show()
        }

        // Redirection vers la conversation WhatsApp
        val discuterBtn = findViewById<LinearLayout>(R.id.btnWhatsappDetailArticle)

        discuterBtn.setOnClickListener {
            if (!userContact.isNullOrEmpty()) {
                val message = "Bonjour, je suis intéressé par votre article sur LoopCongo."
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




        // Redirection vers le profile du vendeur
        /*val btnVoirProfil = findViewById<Button>(R.id.contactButtonWhatsappDetailArticle)
        btnVoirProfil.setOnClickListener {

            val userId = intent.getIntExtra("user_id", 0) // ID du vendeur passé depuis l'Intent

            // Appel API dans une coroutine
            lifecycleScope.launch {
                try {
                    // Appel de l'API pour récupérer le vendeur par son ID
                    val response = ApiClient.instance.userById(userId)
                    if (response.isSuccessful && response.body() != null) {
                        val vendeur = response.body()!!.data

                        // Préparer l'Intent pour ProfileVendeurActivity
                        val intent = Intent(this@DetailArticleActivity, ProfileVendeurActivity::class.java)
                        intent.putExtra("vendeurId", vendeur.id)
                        intent.putExtra("vendeurUsername", vendeur.nom)
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
                        Toast.makeText(this@DetailArticleActivity, "Impossible de récupérer le profil", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@DetailArticleActivity, "Erreur réseau", Toast.LENGTH_SHORT).show()
                }
            }
        }*/

        // Redirection vers le profile du vendeur
    }

    // 🔹 Récupération asynchrone des images de détail
    private fun fetchDetailImages(articleId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.instance.getArticleDetailImages(articleId)
                if (response.isSuccessful) {
                    val images = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        displayDetailImages(images)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@DetailArticleActivity, "Erreur de chargement des images", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailArticleActivity, "Erreur réseau", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 🔹 Affichage dynamique des images en lignes de 2
    private fun displayDetailImages(images: List<DetailImage>) {
        val container = findViewById<LinearLayout>(R.id.containerImagesDetail)
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
                        this@DetailArticleActivity,
                        R.drawable.rounded_image_background
                    )
                    clipToOutline = true // ✅ arrondir les coins
                    setOnClickListener { showImagePopup(imageUrl) }
                }

                Glide.with(this)
                    .load(imageUrl)
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
            .load(imageUrl)
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