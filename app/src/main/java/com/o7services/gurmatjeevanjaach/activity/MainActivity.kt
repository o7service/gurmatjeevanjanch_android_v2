package com.o7services.gurmatjeevanjaach.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.appbar.AppBarLayout
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var appBar : AppBarLayout
    lateinit var ivBack : ImageView
    lateinit var tvTitle : TextView
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
        appBar = binding.root.findViewById(R.id.appBarLayout)
        ivBack = binding.root.findViewById(R.id.ivBack)
        tvTitle = binding.root.findViewById(R.id.tvTitle)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
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
                    ivBack.visibility = View.VISIBLE
                    tvTitle.visibility = View.VISIBLE
                    tvTitle.text = resources.getString(R.string.audio)
                    binding.bottomNav.menu.findItem(R.id.menu_audio)?.isChecked = true
                }
                R.id.samagamFragment -> {
                    appBar.visibility = View.VISIBLE
                    ivBack.visibility = View.VISIBLE
                    tvTitle.visibility = View.VISIBLE
                    tvTitle.text = resources.getString(R.string.samagam)
                    binding.bottomNav.menu.findItem(R.id.menu_smagam)?.isChecked = true
                }
                R.id.moreFragment -> {
                    appBar.visibility = View.VISIBLE
                    ivBack.visibility = View.VISIBLE
                    tvTitle.visibility = View.VISIBLE
                    tvTitle.text = resources.getString(R.string.More)
                    binding.bottomNav.menu.findItem(R.id.menu_more)?.isChecked = true
                }
            }
        }

        ivBack.setOnClickListener {
            navController.popBackStack()
        }



    }
}