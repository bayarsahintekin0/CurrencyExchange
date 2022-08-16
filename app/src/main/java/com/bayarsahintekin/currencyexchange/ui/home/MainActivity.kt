package com.bayarsahintekin.currencyexchange.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.bayarsahintekin.currencyexchange.R
import com.bayarsahintekin.currencyexchange.data.model.Currency
import com.bayarsahintekin.currencyexchange.databinding.ActivityMainBinding
import com.bayarsahintekin.currencyexchange.utils.CEResource
import com.bayarsahintekin.currencyexchange.utils.Constants.Companion.UID
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private val viewModel: MainViewModel by viewModels()
    private lateinit var currency:Currency
    private lateinit var binding: ActivityMainBinding
    private val firestore :FirebaseFirestore by lazy {   FirebaseFirestore.getInstance() }
    private var uid = ""
    private lateinit var balanceAdapter: BalanceAdapter
    private var userData:MutableMap<String,Any>? = mutableMapOf()
    private var soldAmount = 0.0
    private var soldCurrency = ""
    private var purchasedAmount = 0.0
    private var purchasedCurrency = "0.0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.currency_converter)

        balanceAdapter = BalanceAdapter()
        binding.rvMyBalance.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvMyBalance.adapter = balanceAdapter

        uid = intent.getStringExtra(UID).toString()

        binding.btnSubmit.setOnClickListener {
            val actualAmount = userData?.get(soldCurrency).toString().toDouble()

            if (binding.etSell.text.toString().toDouble() < actualAmount){
                val updatedData : MutableMap<String, Any>? = userData
                if ( actualAmount - soldAmount > 0){
                    updatedData?.set(soldCurrency, actualAmount - soldAmount)
                }else{
                    updatedData?.remove(soldCurrency)
                }
                updatedData?.set(purchasedCurrency, purchasedAmount)
                if (updatedData != null) {
                    firestore.collection("users").document(uid).set(updatedData).addOnSuccessListener {
                        getUserCurrencies()
                        showSuccessInfoDialog()
                    }.addOnFailureListener {

                    }
                }
            }else {
                Toast.makeText(this,resources.getString(R.string.insufficient_balance),Toast.LENGTH_SHORT).show()
            }

        }

        getUserCurrencies()





    }

    private fun addCurrency(currency: MutableMap<String,Double>,feeCount: Int){
        val user = Users(feeCount,currency)

        firestore.collection("users").document(uid).set(user).
        addOnSuccessListener {

        }.addOnFailureListener {

        }
    }

    data class Users(var freeCount:Int,var currencies:MutableMap<String,Double>)

    private fun showSuccessInfoDialog(){
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.currency_converted))
            .setMessage(resources.getString(R.string.currency_convert_success,
                "$soldAmount $soldCurrency","$purchasedAmount $purchasedCurrency",""))
            .setNeutralButton(resources.getString(R.string.done)){ _, _ ->

            }
            .show()
    }

    private fun observeCurrenciesLiveData(){
        viewModel.currencies.observe(this) {
            when(it.status){
                CEResource.Status.LOADING -> {
                    Log.i(TAG,"loading")
                }
                CEResource.Status.SUCCESS -> {
                    if (it.data != null){
                        currency = it.data
                        fillSpinner(currency)
                    }
                }
                CEResource.Status.ERROR -> {
                    it.message?.let { it1 -> Log.e(TAG, it1) }
                }
            }
        }
    }

    private fun getUserCurrencies(){
        firestore.collection("users").document(uid).get().addOnSuccessListener {
            this.userData = it.data
            val userCurrenciesList: ArrayList<String> = arrayListOf()
            if (userData?.keys != null && userData != null)
                userCurrenciesList.addAll(userData!!.keys)

            balanceAdapter.setData(userData as MutableMap<String, Double>,userCurrenciesList)

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,userCurrenciesList)
            binding.spnSell.apply {

                this.adapter = adapter
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        viewModel.getCurrencies(base = selectedItem.toString())
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

            }


            binding.etSell.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val selectedBalance = binding.spnSell.selectedItem.toString()

                    viewModel.getCurrencies(base = selectedBalance)
                    observeCurrenciesLiveData()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })


        }.addOnFailureListener {

        }
    }

    private fun fillSpinner(currency: Currency){

        val keys:ArrayList<String> = arrayListOf()
        currency.rates?.keys?.let { keys.addAll(it) }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,keys)
        binding.spnReceive.apply {

            this.adapter = adapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedReceiveValue = currency.rates?.get(selectedItem.toString())!!.toDouble() * binding.etSell.text.toString().toDouble()
                    binding.tvShowReceive.text = selectedReceiveValue.toString()
                    soldAmount = binding.etSell.text.toString().toDouble()
                    soldCurrency = binding.spnSell.selectedItem.toString()
                    purchasedAmount = selectedReceiveValue
                    purchasedCurrency = binding.spnReceive.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        }
        binding.spnReceive.setSelection(0)
    }
}