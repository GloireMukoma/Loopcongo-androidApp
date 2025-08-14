package com.example.loopcongo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.bumptech.glide.Glide

class ArticleDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_article)
        supportActionBar?.title = "Article"

        val articleImg = findViewById<ImageView>(R.id.articleDetailImage)
        val articleNom = findViewById<TextView>(R.id.articleDetailNom)
        val articleDescription = findViewById<TextView>(R.id.articleDetaildescription)
        val articlePrix = findViewById<TextView>(R.id.articleDetailPrix)
        val articleAuteur = findViewById<TextView>(R.id.auteurDetailArticle)
        val avatarUser = findViewById<ImageView>(R.id.userAvatarArticleDetail)
        val articleNbLike = findViewById<TextView>(R.id.articleDetailNbLike)
        val timeAgo = findViewById<TextView>(R.id.timeAgoArticleDetail)


        // Récupérer les extras
        val nom = intent.getStringExtra("article_nom")
        val prixValue = intent.getStringExtra("article_prix")
        val devise = intent.getStringExtra("article_devise")
        val description = intent.getStringExtra("article_description")
        val auteur = intent.getStringExtra("article_auteur")
        val image = intent.getStringExtra("article_photo")
        val userAvatar = intent.getStringExtra("user_avatar")
        val createdAt = intent.getStringExtra("article_created_at")
        val nbLike = intent.getStringExtra("article_nbLike")

        articleNom.text = nom
        articlePrix.text = "Prix: $prixValue $devise"
        articleDescription.text = description
        articleAuteur.text = auteur
        articleNbLike.text = " • ${nbLike ?: 0} Likes"

        timeAgo.text = "Il y 48min"

        Glide.with(this)
            .load("https://loopcongo.com/$image")
            .placeholder(R.drawable.shoes)
            .into(articleImg)

        Glide.with(this)
            .load("https://loopcongo.com/$userAvatar")
            .placeholder(R.drawable.shoes)
            .into(avatarUser)

        val discuterBtn = findViewById<LinearLayout>(R.id.contactButtonWhatsappDetailArticle)
        //val numeroWhatsapp = intent.getStringExtra("numero_whatsapp") // récupéré depuis l’intent
        val numeroWhatsapp = "243971737160"

        discuterBtn.setOnClickListener {
            if (!numeroWhatsapp.isEmpty()) {
                val message = "Bonjour, je suis intéressé par votre article sur LoopCongo."
                val url = "https://wa.me/$numeroWhatsapp?text=${Uri.encode(message)}"

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
}