package com.example.loopcongo.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.loopcongo.R

class AccueilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // On "inflate" le layout du fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        // Charger lien du site sur un fragment android
        val myWebview: WebView = view?.findViewById<View>(R.id.accueilWebView) as WebView
        myWebview.loadUrl("https://www.loopcongo.com/android/home")

        val webSetting: WebSettings = myWebview.settings
        webSetting.javaScriptEnabled = true
        myWebview.webViewClient = WebViewClient()

        myWebview.canGoBack()
        myWebview.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_BACK && event.action == MotionEvent.ACTION_UP && myWebview.canGoBack()){
                myWebview.goBack()
                return@OnKeyListener true
            }
            false

        })

        return view
        //Item des profils users(statut whatsapp design)
        /*val recyclerView = view.findViewById<RecyclerView>(R.id.statusRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val statusList = listOf(
            StatusItem("https://loopcongo.com/users/1750783444_3ff387ef2853d3c0ba95.jpg", "https://loopcongo.com/uploads/1751350505_f26dbadea6e331d44916.jpg", "User 1", "2h"),
            StatusItem("https://loopcongo.com/users/1750782970_5e2fbfbba8bd0e7dcaef.jpg", "https://loopcongo.com/uploads/1751350505_f26dbadea6e331d44916.jpg", "User 2", "30min"),
            StatusItem("https://loopcongo.com/users/1750783444_3ff387ef2853d3c0ba95.jpg", "https://loopcongo.com/uploads/1751350505_f26dbadea6e331d44916.jpg", "User 1", "2h"),
            StatusItem("https://loopcongo.com/users/1750782970_5e2fbfbba8bd0e7dcaef.jpg", "https://loopcongo.com/uploads/1751350505_f26dbadea6e331d44916.jpg", "User 2", "30min"),
            StatusItem("https://loopcongo.com/users/1750783444_3ff387ef2853d3c0ba95.jpg", "https://loopcongo.com/users/1750783444_3ff387ef2853d3c0ba95.jpg", "User 1", "2h"),
            StatusItem("https://loopcongo.com/users/1750782970_5e2fbfbba8bd0e7dcaef.jpg", "https://loopcongo.com/users/1750782970_5e2fbfbba8bd0e7dcaef.jpg", "User 2", "30min"),
            // Ajoute plus ici
        )

        recyclerView.adapter = StatusAdapter(statusList)


        // On récupère la ListView depuis la vue du fragment
        val listView: ListView = view.findViewById(R.id.productListView)

        // Liste de produits
        val productList = listOf(
            ProductItem(R.drawable.img, "Boardshort Jaune", "Toujours en stock", "€25.50"),
            ProductItem(R.drawable.img_1, "Boardshort Rouge", "Toujours en stock", "€25.50"),
            ProductItem(R.drawable.img, "Planche de surf Amigo", "En stock 7", "€350.00"),
            ProductItem(R.drawable.annonce, "Chemise ample cyan", "Toujours en stock", "€15.50"),
            ProductItem(R.drawable.img, "Robe Corrie", "En stock 5", "€45.50"),
            ProductItem(R.drawable.images, "Planche de surf Bastard", "En stock 10", "€320.00")
        )

        // Création et affectation de l'adaptateur
        val adapter = ProductArrayAdapter(requireContext(), R.layout.item_product_, productList)
        listView.adapter = adapter*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Accueil"
    }
}
