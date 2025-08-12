package com.example.loopcongo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.prestataire.PrestationAdapter
import com.example.loopcongo.models.Prestataire
import com.example.loopcongo.models.Prestation
import com.example.loopcongo.restApi.ApiClient
import com.example.loopcongo.restApi.ApiService

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrestationsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PrestationAdapter
    private var publications: List<Prestation> = emptyList()

    companion object {
        private const val ARG_PUBLICATIONS = "publications"

        fun newInstance(publications: List<Prestation>): PrestationsFragment {
            val fragment = PrestationsFragment()
            val bundle = Bundle()
            bundle.putSerializable(ARG_PUBLICATIONS, ArrayList(publications))
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        publications = (arguments?.getSerializable(ARG_PUBLICATIONS) as? ArrayList<Prestation>) ?: emptyList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.onglet_prestations_view_by_users, container, false)
        recyclerView = view.findViewById(R.id.ongletPrestationsGridRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = PrestationAdapter(requireContext(), publications)
        recyclerView.adapter = adapter
        return view
    }
}
