package com.example.loopcongo.fragments.userImmobilierConnectedOnglets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.userImmobilierConnected.UserConnectedOngletImmobilierAdapter
import com.example.loopcongo.models.ImmobilierResponse
import com.example.loopcongo.restApi.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OngletImmobilierFragment : Fragment() {

    private var userId: Int = 0 // sera passé via arguments
    private lateinit var immobilierRecyclerView: RecyclerView

    companion object {
        private const val ARG_USER_ID = "user_id"

        fun newInstance(userId: Int): OngletImmobilierFragment {
            val fragment = OngletImmobilierFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_USER_ID, userId)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getInt(ARG_USER_ID, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.onglet_immobilier_profil_user_connected, container, false)
        immobilierRecyclerView = view.findViewById(R.id.userImmobilierConnectedRecyclerView)
        fetchUserImmobiliers()
        return view
    }

    private fun fetchUserImmobiliers() {
        ApiClient.instance.getImmobiliersByUser(userId)
            .enqueue(object : Callback<ImmobilierResponse> {
                override fun onResponse(
                    call: Call<ImmobilierResponse>,
                    response: Response<ImmobilierResponse>
                ) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        val biens = response.body()?.data ?: emptyList()

                        immobilierRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
                        immobilierRecyclerView.adapter =
                            UserConnectedOngletImmobilierAdapter(requireContext(), biens.toMutableList())

                    } else {
                        Toast.makeText(
                            requireContext(),
                            response.body()?.message ?: "Erreur serveur",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ImmobilierResponse>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Erreur réseau : ${t.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }


}
