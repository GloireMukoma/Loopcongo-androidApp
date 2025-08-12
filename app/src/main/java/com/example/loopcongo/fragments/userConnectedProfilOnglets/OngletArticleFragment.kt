package com.example.loopcongo.fragments.userConnectedProfilOnglets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.loopcongo.R
import com.example.loopcongo.adapters.articles.UserArticleAdapter
import com.example.loopcongo.models.ApiResponse
import com.example.loopcongo.models.UserData
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OngletArticleFragment : Fragment() {

    // Ici, définis un userId (à récupérer dynamiquement si besoin)
    private val userId = 28

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.onglet_article_profil_user_connected, container, false)

        val articlesListView = view.findViewById<ListView>(R.id.ongletArticlesProfilUserConnected)

        ApiClient.instance.getUserById(userId).enqueue(object : Callback<ApiResponse<UserData>> {
            override fun onResponse(call: Call<ApiResponse<UserData>>, response: Response<ApiResponse<UserData>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.status && body.data != null) {
                        // On récupère les articles de userData
                        val userData = body.data
                        val articles = userData.articles

                        // On crée et on affecte l’adapter
                        val adapter = UserArticleAdapter(requireContext(), articles)
                        articlesListView.adapter = adapter

                    } else {
                        Toast.makeText(requireContext(), body?.message ?: "Erreur inconnue", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Erreur serveur: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<UserData>>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur réseau: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }
}
