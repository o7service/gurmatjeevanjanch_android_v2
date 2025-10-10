package com.o7services.gurmatjeevanjaach.fragments
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.app.DatePickerDialog
import android.content.DialogInterface
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.o7services.gurmatjeevanjaach.R
import com.o7services.gurmatjeevanjaach.activity.MainActivity
import com.o7services.gurmatjeevanjaach.databinding.FragmentRequestSamagamBinding
import com.o7services.gurmatjeevanjaach.dataclass.AddSamagamRequest
import com.o7services.gurmatjeevanjaach.dataclass.AddSamagamResponse
import com.o7services.gurmatjeevanjaach.dataclass.AllProgramResponse
import com.o7services.gurmatjeevanjaach.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
class RequestSamagamFragment : Fragment() {
    lateinit var binding : FragmentRequestSamagamBinding
    lateinit var mainActivity : MainActivity
    var startCalendar = Calendar.getInstance()
    var endCalendar = Calendar.getInstance()
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
       binding = FragmentRequestSamagamBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvFrom.setOnClickListener {
            startDate()
        }
        binding.tvTo.setOnClickListener {
            endDate()
        }
        binding.saveUpdate.setOnClickListener {
            binding.tvFrom.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(startCalendar.time)
            binding.tvTo.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(endCalendar.time)
            if (binding.etTitle.text.toString().isNullOrBlank()) {
                Toast.makeText(
                    mainActivity,
                    mainActivity.resources.getString(R.string.enter_your_name),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (binding.etDescription.text.toString().isNullOrBlank()) {
                Toast.makeText(
                    mainActivity,
                    mainActivity.resources.getString(R.string.enter_program_description),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (binding.etAddress.text.toString().isNullOrBlank()) {
                Toast.makeText(
                    mainActivity,
                    mainActivity.resources.getString(R.string.enter_address),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            /*  if (binding.etGoogleMapLink.text.toString().isNullOrBlank()) {
              Toast.makeText(
                  mainActivity,
                  mainActivity.resources.getString(R.string.enter_google_map_link),
                  Toast.LENGTH_LONG
              ).show()
              return
          }*/
            if (binding.etContactNumber.text.toString().isNullOrBlank()) {
                Toast.makeText(
                    mainActivity,
                    mainActivity.resources.getString(R.string.enter_contact_number),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (binding.etContactNumber.text.toString().length < 10) {
                Toast.makeText(
                    mainActivity,
                    mainActivity.resources.getString(R.string.enter_contact_number),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            /* if (binding.etEmail.text.toString().isNullOrBlank()) {
             Toast.makeText(
                 mainActivity,
                 mainActivity.resources.getString(R.string.enter_email),
                 Toast.LENGTH_LONG
             ).show()
             return
         }*/


            if (binding.etEmail.text.toString()
                    .isNullOrBlank() == false && !Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString())
                    .find()
            ) {
                Toast.makeText(
                    mainActivity,
                    mainActivity.resources.getString(R.string.enter_valid_email),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (endCalendar.before(startCalendar)) {
                Toast.makeText(
                    mainActivity,
                    mainActivity.resources.getString(R.string.end_date_start_date_validation),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            RetrofitClient.instance.addSamagam(
                AddSamagamRequest(
                    organizerName = binding.etTitle.text.toString(),
                    address = binding.etAddress.text.toString(),
                    details = binding.etDescription.text.toString(),
                    phone = binding.etContactNumber.text.toString(),
                    mapLink = binding.etGoogleMapLink.text.toString(),
                    email = binding.etEmail.text.toString(),
                    startDate = binding.tvFrom.text.toString() ,
                    endDate = binding.tvTo.text.toString()
            )).enqueue(object : retrofit2.Callback<AddSamagamResponse>{
            override fun onResponse(
                call: Call<AddSamagamResponse?>,
                response: Response<AddSamagamResponse?>
            ) {
            Log.d("Response", response.body()?.message.toString())
            if (response.body()?.success == true) {
                val data = response.body()?.data
                if (data != null) {
                    Toast.makeText(mainActivity, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                } else {
                    Log.d("Response", response.body()?.message.toString())
                }
            } else {
                Log.d("Response", response.body()?.message.toString())
            }
            }

            override fun onFailure(
                call: Call<AddSamagamResponse?>,
                t: Throwable
            ) {
                Log.d("Response", t.message.toString())
            }

        })
        }

    }

    fun startDate() {
        var today = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            mainActivity,
            { _, year, month, dayOfMonth ->
                startCalendar.set(year, month, dayOfMonth)
                Log.d("DatePicker", "Start Date: $year-$month-$dayOfMonth")
                binding.tvFrom.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(startCalendar.time)
            },
            startCalendar.get(Calendar.YEAR),
            startCalendar.get(Calendar.MONTH),
            startCalendar.get(Calendar.DAY_OF_MONTH)
        )
        val okButton = datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        val cancelButton = datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE)

        okButton?.setTextColor(ContextCompat.getColor(context, R.color.blue))
        cancelButton?.setTextColor(ContextCompat.getColor(context, R.color.blue))
        datePickerDialog.datePicker.minDate = today.timeInMillis
        datePickerDialog.show()
    }
    fun endDate() {
        var today = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            mainActivity,
            { _, year, month, dayOfMonth ->
                endCalendar.set(year, month, dayOfMonth)
                Log.d("DatePicker", "End Date: $year-$month-$dayOfMonth")
                binding.tvTo.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(endCalendar.time)
            },
            endCalendar.get(Calendar.YEAR),
            endCalendar.get(Calendar.MONTH),
            endCalendar.get(Calendar.DAY_OF_MONTH)
        )
        val okButton = datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        val cancelButton = datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        okButton?.setTextColor(ContextCompat.getColor(context, R.color.blue))
        cancelButton?.setTextColor(ContextCompat.getColor(context, R.color.blue))
        datePickerDialog.datePicker.minDate = today.timeInMillis
        datePickerDialog.show()
    }
}