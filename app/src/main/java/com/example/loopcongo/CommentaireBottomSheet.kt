package com.example.loopcongo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.adapters.CommentaireAdapter
import com.example.loopcongo.models.*
import com.example.loopcongo.restApi.ApiClient
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentaireBottomSheet(
    private val articleId: Int
) : BottomSheetDialogFragment() {

    private lateinit var adapter: CommentaireAdapter
    private val commentaires = mutableListOf<Commentaire>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottomsheet_commentaires, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerCommentaires)
        val edt = view.findViewById<EditText>(R.id.edtCommentaire)
        val btnSend = view.findViewById<ImageButton>(R.id.btnEnvoyer)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = CommentaireAdapter(commentaires)
        recycler.adapter = adapter

        chargerCommentaires()

        btnSend.setOnClickListener {
            val message = edt.text.toString().trim()
            if (message.isNotEmpty()) {
                publierCommentaire(message)
                edt.setText("")
            }
        }
    }
    private fun chargerCommentaires() {

        ApiClient.instance.getCommentaires(articleId)
            .enqueue(object : Callback<CommentaireResponse> {

                override fun onResponse(
                    call: Call<CommentaireResponse>,
                    response: Response<CommentaireResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {

                        commentaires.clear()
                        commentaires.addAll(response.body()!!.commentaires)
                        adapter.notifyDataSetChanged()

                        Log.d("COMMENTAIRES_API", commentaires.toString())
                    }
                }

                override fun onFailure(call: Call<CommentaireResponse>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Erreur de chargement des commentaires",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun publierCommentaire(message: String) {

        val request = PublierCommentaireRequest(
            article_id = articleId,
            user_id = 28, // ⚠️ À remplacer par l'utilisateur connecté
            commentaire = message
        )

        ApiClient.instance.publierCommentaire(request)
            .enqueue(object : Callback<ApiCommentaireResponse> {

                override fun onResponse(
                    call: Call<ApiCommentaireResponse>,
                    response: Response<ApiCommentaireResponse>
                ) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        chargerCommentaires() // 🔄 rafraîchir la liste
                    }
                }

                override fun onFailure(call: Call<ApiCommentaireResponse>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Impossible d'envoyer le commentaire",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

}
