package com.o7services.gurmatjeevanjaach.fragments

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.activity.MainActivity
import com.o7services.gurmatjeevanjaach.databinding.DialogChangeLanguageBinding
import com.o7services.gurmatjeevanjaach.databinding.FragmentMoreBinding


class MoreFragment : Fragment() {
    lateinit var mainActivity: MainActivity
    lateinit var binding : FragmentMoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentMoreBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.hideNoData()
        binding.btnRequestSangam.setOnClickListener {
            findNavController().navigate(R.id.requestSamagamFragment)
        }
        binding.tvNumber.setOnClickListener {
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+1-77888-31925")
            startActivity(intent)
        }
        binding.tvNumber1.setOnClickListener {
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:8728006100")
            startActivity(intent)
        }
        binding.tvNumber2.setOnClickListener {
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+91-78888-75285")
            startActivity(intent)
        }
        binding.tvPrivacyPolicy.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.privacy_policy_url))
            )
            startActivity(intent)
        }

        binding.tvChangeLanguage.setOnClickListener {
            var dialog = BottomSheetDialog(mainActivity, R.style.BottomSheetDialog)
            var dialogBinding = DialogChangeLanguageBinding.inflate(layoutInflater)
            dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            dialog.setContentView(dialogBinding.root)
            val prefs = mainActivity.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val lang = prefs.getString("language", "pa") ?: "pa"
            if (lang == "en"){
                dialogBinding.checkEnglish.visibility = View.VISIBLE
                dialogBinding.checkHindi.visibility = View.GONE
                dialogBinding.checkPunjabi.visibility = View.GONE
            } else if (lang == "pa"){
                dialogBinding.checkEnglish.visibility = View.GONE
                dialogBinding.checkHindi.visibility = View.GONE
                dialogBinding.checkPunjabi.visibility = View.VISIBLE
            }else{
                dialogBinding.checkEnglish.visibility = View.GONE
                dialogBinding.checkHindi.visibility = View.VISIBLE
                dialogBinding.checkPunjabi.visibility = View.GONE
            }
            dialogBinding.cardEnglish.setOnClickListener {
                dialogBinding.checkEnglish.visibility = View.VISIBLE
                dialogBinding.checkHindi.visibility = View.GONE
                dialogBinding.checkPunjabi.visibility = View.GONE
                changeLanguage("en")
                dialog.dismiss()
            }

            dialogBinding.cardPunjabi.setOnClickListener {
                dialogBinding.checkEnglish.visibility = View.GONE
                dialogBinding.checkHindi.visibility = View.GONE
                dialogBinding.checkPunjabi.visibility = View.VISIBLE
                changeLanguage("pa")
                dialog.dismiss()
            }

            dialogBinding.cardHindi.setOnClickListener {
                dialogBinding.checkEnglish.visibility = View.GONE
                dialogBinding.checkHindi.visibility = View.VISIBLE
                dialogBinding.checkPunjabi.visibility = View.GONE
                changeLanguage("hi")
                dialog.dismiss()
            }
            dialog.show()
//                val prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
//                val currentLang = prefs.getString("language", "en")
//                val newLang = if (currentLang == "en") "pa" else "en"
//                prefs.edit().putString("language", newLang).apply()
//                val intent = requireActivity().intent
//                requireActivity().finish()
//                startActivity(intent)
        }


    }
    private fun changeLanguage(langCode: String) {
        val prefs = mainActivity.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putString("language", langCode).apply()
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(langCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
        val intent = requireActivity().intent
        requireActivity().finish()
        startActivity(intent)
    }
}