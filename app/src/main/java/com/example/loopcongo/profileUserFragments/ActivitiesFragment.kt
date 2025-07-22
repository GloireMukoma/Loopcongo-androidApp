package com.example.loopcongo.profileUserFragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment


class ActivitiesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val textView = TextView(context)
        textView.text = "Contenu de l'onglet NOTES"
        textView.gravity = Gravity.CENTER
        textView.textSize = 18f
        return textView
    }
}
