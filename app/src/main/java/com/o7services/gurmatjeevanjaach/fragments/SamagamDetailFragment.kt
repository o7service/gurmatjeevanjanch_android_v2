package com.o7services.gurmatjeevanjaach.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.activity.MainActivity
import com.o7services.gurmatjeevanjaach.consts.AppConst
import com.o7services.gurmatjeevanjaach.databinding.FragmentSamagamDetailBinding
import com.o7services.gurmatjeevanjaach.databinding.LayoutImageDialogBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.String

class SamagamDetailFragment : Fragment() {
    lateinit var binding: FragmentSamagamDetailBinding
    lateinit var mainActivity : MainActivity
    private var imageLink : String ?= ""
    private var title: String ?= ""
    private var contactNo1 : String ?= ""
    private var contact2: String ?= ""
    private var address: String ?= ""
    private var mapLink: String ?= ""
    private var description: String ?= ""
    private var startDate : String ?= ""
    private var endDate: String ?= ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {
             imageLink = it.getString("image", "")
            title = it.getString("title", "")
            contactNo1 = it.getString("contact1", "")
            contact2 = it.getString("contact2" ,"")
            address = it.getString("address", "")
            mapLink = it.getString("mapLink", "")
            description = it.getString("description", "")
            startDate = it.getString("startDate", "")
            endDate = it.getString("endDate", "")
    }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSamagamDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.tvTitle.text = title
        binding.tvTitle.setText(title)
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

// Parse and reformat
        val startDateFormat = inputFormat.parse(startDate)
        val endDateFormat = inputFormat.parse(endDate)
        val formattedStartDate = outputFormat.format(startDateFormat)
        val formattedEndDate = outputFormat.format(endDateFormat)
        binding.tvDate.setText(formattedStartDate + " to "+ formattedEndDate)
        binding.textSamagamDetail.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY))
        binding.tvLocation.setText(address)
        binding.textPhone.setText(contactNo1 + " , " + contact2)
        binding.tvContactNumber.setText(contactNo1 + " , " + contact2)
        val imageIcon = AppConst.imageBaseUrl + imageLink
        Glide.with(this)
            .load(imageIcon)
            .error(R.drawable.no_image)
            .placeholder(R.drawable.no_image)
            .into(binding.ivImage)
        binding.ivCurrentMap.setOnClickListener {
            val mapIntentUri = Uri.parse(mapLink)
            val mapIntent = Intent(Intent.ACTION_VIEW, mapIntentUri)
            startActivity(mapIntent)
        }
        binding.ivImage.setOnClickListener {
            val dialog = Dialog(mainActivity)
            val dialogBinding = LayoutImageDialogBinding.inflate(LayoutInflater.from(mainActivity))
            dialog.setContentView(dialogBinding.root)
            // Set dialog window to full screen
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.BLACK)) // or transparent if you prefer

            val imageIcon = AppConst.imageBaseUrl + imageLink

            Glide.with(mainActivity)
                .load(imageIcon)
                .error(R.drawable.no_image)
                .placeholder(R.drawable.no_image)
                .into(dialogBinding.ivAudioImage)

            dialogBinding.icClose.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }



}