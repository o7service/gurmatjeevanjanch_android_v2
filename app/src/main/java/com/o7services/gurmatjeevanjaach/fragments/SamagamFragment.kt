package com.o7services.gurmatjeevanjaach.fragments

import android.graphics.Typeface
import android.icu.lang.UCharacter
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.SpannedItemDecor
import com.o7services.gurmatjeevanjaach.activity.MainActivity
import com.o7services.gurmatjeevanjaach.adapter.SamagamAdapter
import com.o7services.gurmatjeevanjaach.databinding.FragmentSamagamBinding
import com.o7services.gurmatjeevanjaach.dataclass.AllProgramResponse
import com.o7services.gurmatjeevanjaach.dataclass.AllSingerResponse
import com.o7services.gurmatjeevanjaach.dataclass.AudioDataClass
import com.o7services.gurmatjeevanjaach.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Response


class SamagamFragment : Fragment(), SamagamAdapter.onItemSamagamListener {
   lateinit var binding : FragmentSamagamBinding
   var item = ArrayList<Map<String , List<AllProgramResponse.SamagamItem>>>()
   lateinit var adapter : SamagamAdapter
   lateinit var mainActivity: MainActivity
   lateinit var linearLayoutManager: LinearLayoutManager
   lateinit var gridLayoutManager: GridLayoutManager
    private var startPoint = 0
    private val limit = 10
    private var isLoading = false
    private var isLastPage = false

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
        binding = FragmentSamagamBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SamagamAdapter(item, this)
        linearLayoutManager = LinearLayoutManager(mainActivity)
        gridLayoutManager = GridLayoutManager(mainActivity, 3, GridLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = GridLayoutManager(mainActivity,3)
        binding.recyclerView.addItemDecoration(SpannedItemDecor(resources.getDimensionPixelOffset(
            com.intuit.sdp.R.dimen._12sdp),3,true))
        binding.recyclerView.adapter = adapter
        getAllPrograms()
        binding.swipeRefreshLayout.setOnRefreshListener {
            mainActivity.showProgress()
            resetPagination()
            getAllPrograms()
        }
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                        ) {
                            getAllPrograms()
                        }
                    }
                }
            }
        })

    }
    // implement here all pagination in getAllPrograms with start point and limit
    fun getAllPrograms() {

        if (isLoading || isLastPage) return

        isLoading = true

        if (startPoint == 0) {
            mainActivity.showProgress()
        }

        RetrofitClient.instance(mainActivity)
            .getAllPrograms(startPoint, limit)
            .enqueue(object : retrofit2.Callback<AllProgramResponse> {

                override fun onResponse(
                    call: Call<AllProgramResponse>,
                    response: Response<AllProgramResponse>
                ) {

                    binding.swipeRefreshLayout.isRefreshing = false
                    isLoading = false
                    mainActivity.hideProgress()

                    if (response.body()?.success == true) {

                        val data = response.body()?.data ?: emptyMap()

                        if (data.isEmpty()) {
                            isLastPage = true
                            return
                        }

                        for (key in data.keys) {
                            val newMap = mapOf(
                                key to (data[key]
                                    ?: emptyList<AllProgramResponse.SamagamItem>())
                            )
                            item.add(newMap)
                        }

                        adapter.notifyDataSetChanged()

                        if (data.size < limit) {
                            isLastPage = true
                        } else {
                            startPoint += limit
                        }

                        mainActivity.hideNoData()
                    } else {
                        if (startPoint == 0) {
                            mainActivity.showNoData()
                        }
                    }
                }

                override fun onFailure(
                    call: Call<AllProgramResponse>,
                    t: Throwable
                ) {
                    binding.swipeRefreshLayout.isRefreshing = false
                    isLoading = false
                    mainActivity.hideProgress()

                    if (startPoint == 0) {
                        mainActivity.showNoData()
                    }
                }
            })
    }
    private fun resetPagination() {
        startPoint = 0
        isLastPage = false
        isLoading = false
        item.clear()
        adapter.notifyDataSetChanged()
    }


//    fun getAllPrograms(){
//        mainActivity.showProgress()
//        RetrofitClient.instance(mainActivity).getAllPrograms().enqueue(object : retrofit2.Callback<AllProgramResponse>{
//            override fun onResponse(
//                call: Call<AllProgramResponse?>,
//                response: Response<AllProgramResponse?>
//            ) {
//                binding.swipeRefreshLayout.isRefreshing = false
//                if (response.body()?.status == 200){
//                    if (response.body()?.success == true){
//                        val data = response.body()?.data
//                        if (data != null){
//                            mainActivity.hideProgress()
//                            item.clear()
//                            for (key in data.keys) {
//                                val newMap = mapOf(key to (data[key] ?: emptyList<AllProgramResponse.SamagamItem>()))
//                                item.add(newMap)
//                                Log.d("Response", newMap.toString())
//                            }
//                            mainActivity.hideNoData()
//                            adapter.notifyDataSetChanged()
//                        }else{
//                            mainActivity.hideProgress()
//                            mainActivity.showNoData()
//                            Log.d("Response", response.body()?.message.toString())
//                        }
//                    }else{
//                        mainActivity.hideProgress()
//                        mainActivity.showNoData()
//                        Log.d("Response", response.body()?.message.toString())
//                    }
//                }else if(response.body()?.status == 404){
//                    (requireActivity() as MainActivity).hideProgress()
//                    mainActivity.showNoData()
//                    Log.d("Response", response.body()?.message.toString())
//
//                }
//            }
//
//            override fun onFailure(
//                call: Call<AllProgramResponse?>,
//                t: Throwable
//            ) {
//                binding.swipeRefreshLayout.isRefreshing = false
//                mainActivity.hideProgress()
//                mainActivity.showNoData()
//                Log.d("Response", t.message.toString())
//            }
//
//        })
//    }

    fun setFormattedDateText(
        textView: TextView,
        day: String,
        month: String,
        year: String
    ) {
        val fullText = "$day\n$month\n$year"
        val dayEnd = day.length
        val monthStart = dayEnd + 1
        val monthEnd = monthStart + month.length + 1
        val yearStart = monthEnd
        val yearEnd = fullText.length

        val spannable = SpannableStringBuilder(fullText)
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            dayEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            RelativeSizeSpan(1.4f),
            0,
            dayEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            RelativeSizeSpan(0.8f),
            yearStart,
            yearEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = spannable
    }

    override fun onSamagamClick(date : String, values : List<AllProgramResponse.SamagamItem>) {
        var bundle = Bundle()
        bundle.putString("date", date)
        bundle.putParcelableArrayList("list", ArrayList(values))
        findNavController().navigate(R.id.samagamListFragment, bundle)
        Log.d("Response", "samagam navigation")
    }
    // how to send values in bundle

}