package com.o7services.gurmatjeevanjaach.activity


import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.databinding.ActivityMainBinding
import setAppLocale


class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var appBar : AppBarLayout
    lateinit var ivBack : ImageView
    lateinit var tvTitle : TextView
    var mediaPlayer = MediaPlayer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!isInternetAvailable(this)) {
          //  binding.llProgress.visibility = View.VISIBLE
            val dialog = AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("Please turn on Wi-Fi or mobile data to continue.")
                .setCancelable(false)
                .setPositiveButton("Open Settings") { dialogInterface, _ ->
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                    dialogInterface.dismiss()
                }
                .setNegativeButton("Cancel") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .show()

// Use ContextCompat to get color from resources
            val positiveColor = ContextCompat.getColor(this, R.color.blue)
            val negativeColor = ContextCompat.getColor(this, R.color.blue)

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(positiveColor)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(negativeColor)

        } else {
           // binding.llProgress.visibility = View.GONE
            // proceed with loading content
        }

        appBar = binding.root.findViewById(R.id.appBarLayout)
        ivBack = binding.root.findViewById(R.id.ivBack)
        tvTitle = binding.root.findViewById(R.id.tvTitle)
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this , R.color.bg)
        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = true
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.displayCutout())
            val navBarInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.setPadding(0, statusBarInsets.top, 0 , 0)
            insets
        }
        if (Build.VERSION.SDK_INT >= 35) {
          WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = true
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        this.window.statusBarColor = resources.getColor(R.color.bg)
//        navController = this.findNavController(R.id.nav_host_fragment)
        binding.bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId)
            {
                R.id.menu_home -> navController.navigate(R.id.homeFragment)
                R.id.menu_audio -> navController.navigate(R.id.audioFragment)
                R.id.menu_smagam -> navController.navigate(R.id.samagamFragment)
                R.id.moreFragment -> navController.navigate(R.id.moreFragment)
            }
            true
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController(R.id.nav_host_fragment)
                if (navController.currentDestination?.id == R.id.homeFragment) {
                    finish() // close app if already on homeFragment
                } else {
                    navController.popBackStack() // navigate back in nav graph
                }
            }
        })
       binding.bottomNav.setOnNavigationItemSelectedListener { item->
            val currentDest = navController.currentDestination?.id
            when(item.itemId){
                R.id.menu_home -> if(currentDest != R.id.homeFragment) navController.navigate(R.id.homeFragment)
                R.id.menu_audio -> if(currentDest != R.id.audioFragment) navController.navigate(R.id.audioFragment)
                R.id.menu_smagam -> if(currentDest != R.id.samagamFragment) navController.navigate(R.id.samagamFragment)
                R.id.menu_more -> if(currentDest != R.id.moreFragment) navController.navigate(R.id.moreFragment)
            }
            return@setOnNavigationItemSelectedListener true
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    appBar.visibility = View.GONE
                    binding.bottomNav.menu.findItem(R.id.menu_home)?.isChecked = true
                }
                R.id.audioFragment -> {
                    appBar.visibility = View.VISIBLE
                    ivBack.visibility = View.GONE
                    tvTitle.visibility = View.VISIBLE
                    tvTitle.text = resources.getString(R.string.audio)
                    binding.bottomNav.menu.findItem(R.id.menu_audio)?.isChecked = true
                }
                R.id.samagamFragment -> {
                    appBar.visibility = View.VISIBLE
                    ivBack.visibility = View.GONE
                    tvTitle.visibility = View.VISIBLE
                    tvTitle.text = resources.getString(R.string.samagam)
                    binding.bottomNav.menu.findItem(R.id.menu_smagam)?.isChecked = true
                }
                R.id.moreFragment -> {
                    appBar.visibility = View.VISIBLE
                    ivBack.visibility = View.GONE
                    tvTitle.visibility = View.VISIBLE
                    tvTitle.text = resources.getString(R.string.More)
                    binding.bottomNav.menu.findItem(R.id.menu_more)?.isChecked = true
                }
                R.id.requestSamagamFragment->{
                    appBar.visibility = View.VISIBLE
                    ivBack.visibility = View.VISIBLE
                    tvTitle.visibility = View.VISIBLE
                    tvTitle.text = resources.getString(R.string.addSamagamRequest)
                    binding.bottomNav.menu.findItem(R.id.menu_more)?.isChecked = true
                }
                R.id.allYoutubeListFragment ->{
                    appBar.visibility = View.VISIBLE
                    ivBack.visibility = View.VISIBLE
                    tvTitle.visibility = View.VISIBLE
                    tvTitle.text = resources.getString(R.string.youtube_channels)
                    binding.bottomNav.menu.findItem(R.id.menu_home)?.isChecked = true
                }
                R.id.allWhatsappListFragment ->{
                    appBar.visibility = View.VISIBLE
                    ivBack.visibility = View.VISIBLE
                    tvTitle.visibility = View.VISIBLE
                    tvTitle.text = resources.getString(R.string.whatsapp_group)
                    binding.bottomNav.menu.findItem(R.id.menu_more)?.isChecked = true
                }
                R.id.allTelegramListFragment ->{
                    appBar.visibility = View.VISIBLE
                    ivBack.visibility = View.VISIBLE
                    tvTitle.visibility = View.VISIBLE
                    tvTitle.text = resources.getString(R.string.telegram_channels)
                    binding.bottomNav.menu.findItem(R.id.menu_more)?.isChecked = true
                }
                R.id.allFacebookListFragment ->{
                    appBar.visibility = View.VISIBLE
                    ivBack.visibility = View.VISIBLE
                    tvTitle.visibility = View.VISIBLE
                    tvTitle.text = resources.getString(R.string.facebook_groups)
                    binding.bottomNav.menu.findItem(R.id.menu_more)?.isChecked = true
                }
                R.id.allInstagramListFragment ->{
                    appBar.visibility = View.VISIBLE
                    ivBack.visibility = View.VISIBLE
                    tvTitle.visibility = View.VISIBLE
                    tvTitle.text = resources.getString(R.string.instagram_chanels)
                    binding.bottomNav.menu.findItem(R.id.menu_more)?.isChecked = true
                }
                R.id.samagamListFragment->{
                    appBar.visibility = View.VISIBLE
                    ivBack.visibility = View.VISIBLE
                    tvTitle.visibility = View.VISIBLE
                    binding.bottomNav.menu.findItem(R.id.menu_smagam)?.isChecked = true
                }
                R.id.playAudioFragment->{
                    appBar.visibility = View.VISIBLE
                    ivBack.visibility = View.VISIBLE
                    tvTitle.visibility = View.VISIBLE
                    binding.bottomNav.menu.findItem(R.id.menu_audio)?.isChecked = true
                }
            }
        }
        ivBack.setOnClickListener {
            navController.popBackStack()
        }

    }
    fun showProgress() {
        binding.llProgress.visibility = View.VISIBLE
        binding.tvNoDataFound.visibility = View.GONE
    }
    fun hideProgress() {
        binding.llProgress.visibility = View.GONE
    }
    fun showNoData() {
        binding.tvNoDataFound.visibility = View.VISIBLE
    }

    fun hideNoData(){
        binding.tvNoDataFound.visibility = View.GONE
    }
    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val lang = prefs.getString("language", "en") ?: "en"
        super.attachBaseContext(newBase.setAppLocale(lang))
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }
}