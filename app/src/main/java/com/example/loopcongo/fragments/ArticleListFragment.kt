package com.example.loopcongo.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loopcongo.R
import com.example.loopcongo.adapters.ArticleAdapter
import com.example.loopcongo.models.Article

class ArticleListFragment : Fragment() {

    companion object {
        fun newInstance(index: Int): ArticleListFragment {
            val fragment = ArticleListFragment()
            val args = Bundle()
            args.putInt("index", index)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_article_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ArticleAdapter(mockData())
        return view
    }

    private fun mockData(): List<Article> {
        return listOf(
            Article("Créer des interfaces animées...", "10 $", "Tatiana moda", "Il y a 1min",1736, 48, 5, R.drawable.images),
            Article("Pourquoi j’ai choisi Flutter...", "21000 CDF", "Gloire mukoma","Il y a 19min",872, 82, 9, R.drawable.img),
            Article("8 astuces cachées en CSS", "85 $", "Tatiana moda","Il y a 1min",734, 69, 2, R.drawable.images),
            Article("Créer des interfaces animées...", "120 $", "Etienne mulenda","Il y a 2h",1736, 48, 5, R.drawable.img_1),
            Article("Pourquoi j’ai choisi Flutter...","8500 CDF", "Tatiana moda", "Il y a 1min",872, 82, 9, R.drawable.images),
            Article("8 astuces cachées en CSS", "45 $", "Elisée kab","Il y a 1min",734, 69, 2, R.drawable.img_1),
            Article("Créer des interfaces animées...","1500 CDF", "Tatiana moda", "Il y a 1h",1736, 48, 5, R.drawable.img_1),
            Article("Pourquoi j’ai choisi Flutter...", "40 $", "Tatiana moda","Il y a 1min",872, 82, 9, R.drawable.images),
            Article("8 astuces cachées en CSS", "22500 CDF", "Tatiana moda","Il y a 3h",734, 69, 2, R.drawable.img),
        )
    }
}
