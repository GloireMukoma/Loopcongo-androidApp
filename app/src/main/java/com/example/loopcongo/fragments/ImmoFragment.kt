package com.example.loopcongo.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.loopcongo.R

/*class ImmoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_immo, container, false)
        /*val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewProperties)
        val propertyList = listOf(
            PropertyItem(R.drawable.img_1, "Avenue Bukavu", "$120,000", "Maison moderne avec balcon."),
            PropertyItem(R.drawable.img, "Gombe, Kinshasa", "$250,000", "Villa de luxe 4 chambres."),
            PropertyItem(R.drawable.images, "Matete, Kinshasa", "$95,000", "Maison familiale rénovée."),
            PropertyItem(R.drawable.img_1, "Bandalungwa", "$175,000", "Maison spacieuse avec parking.")
        )
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = PropertyAdapter(propertyList)*/
        // Charger lien du site sur un fragment android
        val myWebview: WebView = view?.findViewById<View>(R.id.immoWebView) as WebView
        myWebview.loadUrl("https://www.loopcongo.com/android/immo/index")

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
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.title = "Immobilier"

        // Appliquer une couleur au titre
        //val textColor = "<font color='#fff'>Immobiliers</font>" // couleur personnalisée
        //activity.supportActionBar?.title = HtmlCompat.fromHtml(textColor, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }



}*/

