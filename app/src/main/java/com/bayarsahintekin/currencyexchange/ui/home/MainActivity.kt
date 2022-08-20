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
    private lateinit var currency: Currency
    private lateinit var binding: ActivityMainBinding
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private var uid = ""
    private lateinit var balanceAdapter: BalanceAdapter
    private var currencyMap: MutableMap<String, Double>? = mutableMapOf()
    private var feeCountMap: MutableMap<String, Int>? = mutableMapOf()
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
        binding.rvMyBalance.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvMyBalance.adapter = balanceAdapter

        uid = intent.getStringExtra(UID).toString()

        binding.btnSubmit.setOnClickListener {


            val actualAmount = currencyMap?.get(soldCurrency).toString().toDouble()

            if (binding.etSell.text.toString().toDouble() < actualAmount) {
                val updatedCurrencyMap: MutableMap<String, Double> = currencyMap!!
                if (actualAmount - soldAmount > 0) {
                    updatedCurrencyMap[soldCurrency] = actualAmount - soldAmount
                } else {
                    updatedCurrencyMap.remove(soldCurrency)
                }
                updatedCurrencyMap[purchasedCurrency] = purchasedAmount

                val updatedFeeCount = feeCountMap
                var isFeeFree = false

                isFeeFree = if (!feeCountMap!!.keys.contains(soldCurrency)) {
                    updatedFeeCount?.set(soldCurrency, 4)
                    true
                } else {
                    if (feeCountMap!![soldCurrency]!! > 0) {
                        updatedFeeCount?.set(soldCurrency, feeCountMap!![soldCurrency]!! - 1)
                        true
                    } else
                        false
                }


                var feePrice = "0"
                if (!isFeeFree) {
                    updatedCurrencyMap[soldCurrency]!! - (updatedCurrencyMap[soldCurrency]!! * 0.07)
                    feePrice = (updatedCurrencyMap[soldCurrency]!! * 0.07).toString()
                }
                updatedFeeCount?.let { it1 -> addCurrency(updatedCurrencyMap, it1,feePrice) }

            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.insufficient_balance),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        getUserCurrencies()
/*
         val currency = mutableMapOf<String, Double>()
         currency["EUR"] = 1000.0
         val feeCount = mutableMapOf<String, Int>()
         feeCount["EUR"] = 5
         addCurrency(currency, feeCount) */


    }

    private fun addCurrency(
        currency: MutableMap<String, Double>,
        feeCount: MutableMap<String, Int>,
        feePrice: String? = null
    ) {
        val user = Users(feeCount, currency)

        firestore.collection("users").document(uid).set(user).addOnSuccessListener {
            showSuccessInfoDialog(feePrice)
        }.addOnFailureListener {

        }
    }

    data class Users(
        var feeCount: MutableMap<String, Int>,
        var currencies: MutableMap<String, Double>
    )

    private fun showSuccessInfoDialog(feePrice: String?) {
        val fee = feePrice ?: "0"
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.currency_converted))
            .setMessage(
                resources.getString(
                    R.string.currency_convert_success,
                    "$soldAmount $soldCurrency", "$purchasedAmount $purchasedCurrency", "$fee $soldCurrency"
                )
            )
            .setNeutralButton(resources.getString(R.string.done)) { _, _ ->
                getUserCurrencies()
            }
            .show()
    }

    private fun observeCurrenciesLiveData() {
        viewModel.currencies.observe(this) {
            when (it.status) {
                CEResource.Status.LOADING -> {
                    Log.i(TAG, "loading")
                }
                CEResource.Status.SUCCESS -> {
                    if (it.data != null) {
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

    private fun getUserCurrencies() {
        firestore.collection("users").document(uid).get().addOnSuccessListener {
            currencyMap = it.data?.get("currencies") as MutableMap<String, Double>?
            feeCountMap = it.data?.get("feeCount") as MutableMap<String, Int>?
            val userCurrenciesList: ArrayList<String> = arrayListOf()
            if (currencyMap?.keys != null && currencyMap != null)
                userCurrenciesList.addAll(currencyMap!!.keys)

            currencyMap?.let { it1 -> balanceAdapter.setData(it1, userCurrenciesList) }

            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                userCurrenciesList
            )
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


            binding.etSell.addTextChangedListener(object : TextWatcher {
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

    private fun fillSpinner(currency: Currency) {

        val keys: ArrayList<String> = arrayListOf()
        currency.rates?.keys?.let { keys.addAll(it) }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, keys)
        binding.spnReceive.apply {

            this.adapter = adapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedReceiveValue = currency.rates?.get(selectedItem.toString())!!
                        .toDouble() * binding.etSell.text.toString().toDouble()
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