package com.ahmedelsayed.sunmiprinterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmedelsayed.sunmiprinterapp.databinding.ActivityPlatejHistoryBinding
import com.ahmedelsayed.sunmiprinterapp.history.HistoryPlatejAdapter
import com.ahmedelsayed.sunmiprinterapp.history.HistoryViewModel
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import java.util.HashMap

class PlatejHistory : AppCompatActivity() {
    lateinit var binding : ActivityPlatejHistoryBinding
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        binding=ActivityPlatejHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerview = binding.recyclerviewHistory
        recyclerview.layoutManager = LinearLayoutManager(this)
        val abonentid = intent.getStringExtra("id").toString()

        val data = ArrayList<HistoryViewModel>()
        val adapter = HistoryPlatejAdapter(data)
        recyclerview.adapter = adapter
        val queue = Volley.newRequestQueue(applicationContext)
        val url = "http://10.231.40.70"
        val progressBar = binding.historyloading
        val textView = binding.tvHistoryError
        progressBar.setVisibility(View.VISIBLE)
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                Log.d("MyLog",response)
                progressBar.setVisibility(View.GONE)
                try {
                    val array = JSONArray(response)
                    for (i in 0 until array.length()) {
                        val getObj = array.getJSONObject(i)
                        val summa = getObj.getString("summa")
                        val date = getObj.getString("date")
                        val who = getObj.getString("who")
                        val comment = getObj.getString("comment")
                        data.add(HistoryViewModel(summa, date,who,comment))
                        Log.d("MyLog",summa)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error ->
                progressBar.setVisibility(View.GONE)
                textView.setText(error.localizedMessage)
                textView.setVisibility(View.VISIBLE)
            }) {
            override fun getParams(): Map<String, String>? {
                val paramV: MutableMap<String, String> = HashMap()
                paramV.put("apikey", "mysecretapi")
                paramV.put("apiPlatejId", abonentid)
                return paramV
            }
        }
        queue.add(stringRequest)
    }

    }
