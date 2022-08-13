package com.bayarsahintekin.currencyexchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.bayarsahintekin.currencyexchange.data.model.Currency
import com.bayarsahintekin.currencyexchange.databinding.ActivityMainBinding
import com.bayarsahintekin.currencyexchange.utils.CEResource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel:MainViewModel by viewModels()
    private lateinit var receiveArrayAdapter:ArrayAdapter<String>
    private lateinit var currency:Currency
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.currency_converter)

        binding.btnSubmit.setOnClickListener {
            showSuccessInfoDialog()
        }
        viewModel.getCurrencies()

        observeCurrenciesLiveData()



    }

    private fun showSuccessInfoDialog(){
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.currency_converted))
            .setMessage(resources.getString(R.string.currency_convert_success,"","",""))
            .setNeutralButton(resources.getString(R.string.done)){_ ,_ ->

            }
            .show()
    }

    private fun observeCurrenciesLiveData(){
        viewModel.currencies.observe(this) {
            when(it.status){
                CEResource.Status.LOADING -> {

                }
                CEResource.Status.SUCCESS -> {
                    if (it.data != null){
                        currency = it.data
                        fillSpinner(it.data)
                    }



                }
                CEResource.Status.ERROR -> {

                }
            }
        }
    }

    private fun fillSpinner(currency: Currency){
        val keys : ArrayList<String> = arrayListOf()
        keys.addAll(currency.rates!!.keys)
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
                    binding.tvShowReceive.text = "+ " + currency.rates!![keys[position]]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        }
        binding.spnReceive.setSelection(0)
        binding.tvShowReceive.text = "+ " + currency.rates!![keys[0]]
    }
}