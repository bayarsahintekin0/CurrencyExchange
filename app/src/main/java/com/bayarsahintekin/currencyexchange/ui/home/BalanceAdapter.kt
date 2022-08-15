package com.bayarsahintekin.currencyexchange.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bayarsahintekin.currencyexchange.databinding.CardBalanceBinding

class BalanceAdapter  :RecyclerView.Adapter<BalanceAdapter.MyViewHolder>(){

    private var balanceList:MutableMap<String,Double> = mutableMapOf()
    private var balanceKeys:ArrayList<String> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
        val listItemBinding = CardBalanceBinding.inflate(v, parent, false)
        return MyViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        balanceList[balanceKeys[position]]?.let { holder.bind(name = balanceKeys[position], it) }

    }

    override fun getItemCount() = balanceKeys.size

    inner class MyViewHolder(private val view: CardBalanceBinding) : RecyclerView.ViewHolder(view.root) {

        fun bind(name: String, value: Double) {
            view.tvCurrencyName.text = name
            view.tvCurrencyValue.text = value.toString()
        }
    }

    fun setData(balanceList: MutableMap<String,Double>, balanceKeys:ArrayList<String>) {
        this.balanceList = balanceList
        this.balanceKeys = balanceKeys
        notifyDataSetChanged()
    }

}