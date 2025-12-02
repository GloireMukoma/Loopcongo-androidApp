package com.example.loopcongo.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.loopcongo.*
import com.example.loopcongo.adapters.ImmoUserDemandeAdapter
import com.example.loopcongo.adapters.ImmoUserDemandeRecyclerAdapter
import com.example.loopcongo.adapters.articles.CarouselUserAnnonceAdapter
import com.example.loopcongo.adapters.immobiliers.CarouselImmobiliersHomePageAdapter
import com.example.loopcongo.adapters.immobiliers.ItemCityImmobilierAdapter
import com.example.loopcongo.database.*
import com.example.loopcongo.database.Customer
import com.example.loopcongo.database.User
import com.example.loopcongo.models.*
import com.example.loopcongo.restApi.ApiClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeImmobilierFragment : Fragment() {

    private lateinit var userDao: UserDao
    private lateinit var customerDao: CustomerDao

    private lateinit var viewPager2: ViewPager2
    private val sliderHandler = Handler(Looper.getMainLooper())

    private lateinit var recyclerItemImmobilierCity: RecyclerView
    private lateinit var adapter: ItemCityImmobilierAdapter

    //private lateinit var demandeRecycler: RecyclerView
    private lateinit var tvNoDemande: TextView
    private lateinit var demandeRecycler: RecyclerView

    private lateinit var demandeAdapter: ImmoUserDemandeRecyclerAdapter
    private val demandes = mutableListOf<ImmoUserDemande>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_immobilier, container, false)

        // Instance de la BD Room
        val db = AppDatabase.getDatabase(requireContext())
        userDao = db.userDao()
        customerDao = db.customerDao()

        // Récupère l'ImageView de l'avatar de l'utilisateur connecté
        val avatarIconUserConnected = view.findViewById<ImageView>(R.id.avatarImgProfileUserConnectedImmo)

        // Met à jour l'avatar et configure le clic
        lifecycleScope.launch {
            updateAvatarAndListener(avatarIconUserConnected)
        }

        // Icon (etoile) d'abonnement
        val iconSubscription = view.findViewById<ImageView>(R.id.iconSubscription)
        iconSubscription.setOnClickListener {
            val intent = Intent(requireContext(), SubscriptionActivity::class.java)
            startActivity(intent)
        }

        val btnVoirOffres = view.findViewById<Button>(R.id.btnVoirDemandesUsers)
        btnVoirOffres.setOnClickListener {
            val intent = Intent(requireContext(), DemandesUsersActivity::class.java)
            startActivity(intent)
        }

        // Gere la caroussel des annonces
        viewPager2 = view.findViewById(R.id.carouselImmobilierAnnonce)
        viewPager2.offscreenPageLimit = 5
        viewPager2.isNestedScrollingEnabled = false

        ApiClient.instance.getImmosSubscribeUsers().enqueue(object : Callback<List<Immobilier>> {
            override fun onResponse(call: Call<List<Immobilier>>, response: Response<List<Immobilier>>) {
                if (response.isSuccessful && response.body() != null) {

                    var annonces = response.body()!!

                    // ✔️ Si aucune donnée reçue → injecter une annonce par défaut
                    if (annonces.isEmpty()) {
                        annonces = listOf(
                            Immobilier(
                                id = 0,
                                account_id = 1,
                                typeimmo = "Aucun bien disponible",
                                statut = "",
                                city = "RD Congo",
                                quartier = "—",
                                prix = "—",
                                address = "—",
                                about = "Revenez plus tard, de nouveaux biens seront disponibles.",
                                file_url = "uploads/annonces/default_annonce.jpg",

                                cityName = "—",
                                nbImmoPublish = "0",
                                imgUrl = "uploads/annonces/default_annonce.jpg",

                                username = "LoopCongo",
                                subscription_type = "null",
                                userImage = "uploads/users/loop_icon.jpg",
                                user_avatar = "uploads/users/loop_icon.jpg",
                                contact = "+243977718960"
                            )
                        )
                    }

                    viewPager2.adapter = CarouselImmobiliersHomePageAdapter(requireContext(), annonces)

                    val sliderRunnable = Runnable {
                        val size = annonces.size
                        if (size > 0) {
                            viewPager2.currentItem = (viewPager2.currentItem + 1) % size
                        }
                    }

                    viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            sliderHandler.removeCallbacks(sliderRunnable)
                            sliderHandler.postDelayed(sliderRunnable, 3000)
                        }
                    })

                    sliderHandler.postDelayed(sliderRunnable, 3000)

                } else {
                    Toast.makeText(requireContext(), "Réponse vide ou invalide", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Immobilier>>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        tvNoDemande = view.findViewById(R.id.tvNoDemande)
        demandeRecycler = view.findViewById(R.id.immoUserDemandeRecyclerView)

        demandeRecycler.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        demandeAdapter = ImmoUserDemandeRecyclerAdapter(demandes, R.layout.item_demande)
        demandeRecycler.adapter = demandeAdapter

        // Vérifier si liste vide au départ
        if (demandes.isEmpty()) {
            tvNoDemande.visibility = View.VISIBLE
            demandeRecycler.visibility = View.GONE
        } else {
            tvNoDemande.visibility = View.GONE
            demandeRecycler.visibility = View.VISIBLE
        }


        recyclerItemImmobilierCity = view.findViewById(R.id.itemCityImmobilierHomePageRecyclerView)
        recyclerItemImmobilierCity.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Appel API
        ApiClient.instance.getCities().enqueue(object : Callback<ImmobilierResponse> {
            override fun onResponse(call: Call<ImmobilierResponse>, response: Response<ImmobilierResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val cities = response.body()!!.data

                    val adapter = ItemCityImmobilierAdapter(cities)
                    recyclerItemImmobilierCity.adapter = adapter
                } else {
                    Toast.makeText(requireContext(), "Erreur de chargement des villes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ImmobilierResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        loadDemandes()

        return view
    }
    private fun loadDemandes() {
        ApiClient.instance.getUserImmoDemandes().enqueue(object : Callback<ApiResponseDemande> {
            override fun onResponse(
                call: Call<ApiResponseDemande>,
                response: Response<ApiResponseDemande>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()?.demandes ?: emptyList()
                    demandes.clear()
                    demandes.addAll(result)

                    // Met à jour l'affichage
                    demandeAdapter.notifyDataSetChanged()

                    // Gérer la visibilité selon qu'il y ait des demandes
                    if (demandes.isEmpty()) {
                        tvNoDemande.visibility = View.VISIBLE
                        demandeRecycler.visibility = View.GONE
                    } else {
                        tvNoDemande.visibility = View.GONE
                        demandeRecycler.visibility = View.VISIBLE
                    }
                }
            }


            override fun onFailure(call: Call<ApiResponseDemande>, t: Throwable) {
                Toast.makeText(requireContext(), "Erreur de chargement des demandes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private suspend fun updateAvatarAndListener(avatar: ImageView) {
        // Récupère les comptes connectés
        val user = userDao.getUser()       // vendeur ou immobilier
        val customer = customerDao.getCustomer() // client
        val currentAccount: Any? = user ?: customer

        // Met à jour l'image de l'avatar
        val imageUrl = when (currentAccount) {
            is User -> currentAccount.file_url
            is Customer -> currentAccount.file_url
            else -> null
        }

        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(requireContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_login)
                .error(R.drawable.ic_login)
                .circleCrop()
                .into(avatar)
        } else {
            avatar.setImageResource(R.drawable.ic_login)
        }

        // Configure le listener sur l'avatar
        avatar.setOnClickListener {
            lifecycleScope.launch {
                val latestUser = userDao.getUser()
                val latestCustomer = customerDao.getCustomer()
                val account = latestUser ?: latestCustomer

                val nextActivity = when (account) {
                    is User -> {
                        if (account.id == 1) {
                            // Redirection vers l'admin si ID = 1
                            SuperAdminConnectedActivity::class.java
                        } else {
                            when (account.type_account?.lowercase()) {
                                "vendeur" -> ProfileUserConnectedActivity::class.java
                                "immobilier" -> UserImmobilierConnectedActivity::class.java
                                else -> ProfileUserConnectedActivity::class.java
                            }
                        }
                    }
                    is Customer -> CustomerConnectedActivity::class.java
                    else -> LoginActivity::class.java
                }

                startActivity(Intent(requireContext(), nextActivity))
            }
        }
    }
}
