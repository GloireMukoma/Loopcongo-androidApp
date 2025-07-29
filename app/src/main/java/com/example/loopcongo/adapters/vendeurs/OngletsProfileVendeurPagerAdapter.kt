import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.loopcongo.fragments.profilevendeurs.ProfilVendeursOngletArticleFragment

class OngletsProfileVendeurPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProfilVendeursOngletArticleFragment()
            1 -> ProfilVendeursOngletArticleFragment() // remplace par un autre fragment si besoin
            else -> ProfilVendeursOngletArticleFragment()
        }
    }
}
