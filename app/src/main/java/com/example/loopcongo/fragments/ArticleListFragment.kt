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
            Article("Créer des interfaces animées...", 1736, 48, 5, R.drawable.images),
            Article("Pourquoi j’ai choisi Flutter...", 872, 82, 9, R.drawable.img),
            Article("8 astuces cachées en CSS", 734, 69, 2, R.drawable.images),
            Article("Créer des interfaces animées...", 1736, 48, 5, R.drawable.img_1),
            Article("Pourquoi j’ai choisi Flutter...", 872, 82, 9, R.drawable.images),
            Article("8 astuces cachées en CSS", 734, 69, 2, R.drawable.img_1),
            Article("Créer des interfaces animées...", 1736, 48, 5, R.drawable.img_1),
            Article("Pourquoi j’ai choisi Flutter...", 872, 82, 9, R.drawable.images),
            Article("8 astuces cachées en CSS", 734, 69, 2, R.drawable.img),
        )
    }
}
