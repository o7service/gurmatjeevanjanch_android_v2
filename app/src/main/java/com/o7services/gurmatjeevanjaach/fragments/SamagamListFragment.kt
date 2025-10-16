package com.o7services.gurmatjeevanjaach.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.activity.MainActivity
import com.o7services.gurmatjeevanjaach.adapter.SamagamAdapter
import com.o7services.gurmatjeevanjaach.adapter.SamagamListAdapter
import com.o7services.gurmatjeevanjaach.databinding.FragmentSamagamListBinding
import com.o7services.gurmatjeevanjaach.dataclass.AudioDataClass
import com.o7services.gurmatjeevanjaach.dataclass.ProgramSingleDateRequest
import com.o7services.gurmatjeevanjaach.dataclass.ProgramSingleDateResponse
import com.o7services.gurmatjeevanjaach.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class SamagamListFragment : Fragment(), SamagamListAdapter.samagamListInterface {
    lateinit var binding : FragmentSamagamListBinding
    lateinit var mainActivity: MainActivity
    lateinit var tvTitle : TextView
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var adapter : SamagamListAdapter
    lateinit var appBar: AppBarLayout
    var samagamDate = ""
    var item = ArrayList<ProgramSingleDateResponse.Data>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {
            samagamDate = it.getString("date", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSamagamListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val formattedDate = formatDateToFull(samagamDate)
        mainActivity.tvTitle.text = formattedDate
        adapter = SamagamListAdapter(item , this)
        linearLayoutManager = LinearLayoutManager(mainActivity)
        binding.recyclerView.layoutManager = linearLayoutManager
        binding.recyclerView.adapter = adapter
        getAllSamagamList()
        binding.swipeRefreshLayout.setOnRefreshListener {
            mainActivity.showProgress()
            getAllSamagamList()
        }
    }

    fun getAllSamagamList(){
        mainActivity.showProgress()
        RetrofitClient.instance.getSingleProgramByDate(ProgramSingleDateRequest(date = samagamDate)).enqueue(object : retrofit2.Callback<ProgramSingleDateResponse>{
            override fun onResponse(
                call: Call<ProgramSingleDateResponse?>,
                response: Response<ProgramSingleDateResponse?>
            ) {
                binding.swipeRefreshLayout.isRefreshing = false
                if (response.body()?.status == 200){
                    if (response.body()?.success == true){
                        val data = response.body()?.data
                        mainActivity.hideProgress()
                        if (data != null){
                            item.clear()
                            item.add(data)
                            mainActivity.hideNoData()
                            adapter.notifyDataSetChanged()
                        }else{
                            mainActivity.hideProgress()
                            mainActivity.showNoData()
                            Log.d("Response", response.body()?.message.toString())
                        }
                    }else{
                        mainActivity.hideProgress()
                        mainActivity.showNoData()
                        Log.d("Response", response.body()?.message.toString())
                    }
                }else if (response.body()?.status == 404){
                    mainActivity.hideProgress()
                    mainActivity.showNoData()
                    Log.d("Response", response.body()?.message.toString())
                }
            }

            override fun onFailure(
                call: Call<ProgramSingleDateResponse?>,
                t: Throwable
            ) {
                binding.swipeRefreshLayout.isRefreshing = false
                mainActivity.hideProgress()
                mainActivity.showNoData()
                Log.d("Response", t.message.toString())
            }

        })
    }

    override fun onMapClick(mapLink : String){
        val mapIntentUri = Uri.parse(mapLink)
        val mapIntent = Intent(Intent.ACTION_VIEW, mapIntentUri)
        startActivity(mapIntent)
    }
    fun formatDateToFull(input: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(input)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            input // fallback if parsing fails
        }
    }

}