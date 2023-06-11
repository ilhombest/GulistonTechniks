package com.ahmedelsayed.sunmiprinterapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.ahmedelsayed.sunmiprinterapp.databinding.ActivitySearchBinding
import com.ahmedelsayed.sunmiprinterapp.recyclerview.SearchViewModel
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import java.util.HashMap

class Search : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val arrayStreets =  arrayListOf<String>("Выберите улицу")
        val spinner = binding.spinnerStreet
        val queue = Volley.newRequestQueue(applicationContext)
        val url = "http://10.231.40.70"
        val text = binding.tvForSpinner
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                try {
                    val array = JSONArray(response)

                    for (i in 0 until array.length()) {
                        val getObj = array.getJSONObject(i)
                        val streetname = getObj.getString("name")
                        arrayStreets.add(streetname)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error ->
            }) {
            override fun getParams(): Map<String, String>? {
                val paramV: MutableMap<String, String> = HashMap()
                paramV.put("apistreet", "spinner")
                paramV.put("apikey", "mysecretkey")
                return paramV
            }
        }
        queue.add(stringRequest)

        val arrayAdapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,arrayStreets)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               text.text = arrayStreets[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        binding.btnSearch.setOnClickListener{
            val intent = Intent(this,Main::class.java)
            val street = binding.tvForSpinner.text.toString()
            val house = binding.ptHouse.text.toString()
            intent.putExtra("street", street )
            intent.putExtra("house", house )
            startActivity(intent)
        }
    }
}