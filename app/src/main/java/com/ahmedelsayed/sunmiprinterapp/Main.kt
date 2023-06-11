package com.ahmedelsayed.sunmiprinterapp
import SearchAdaptor
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmedelsayed.sunmiprinterapp.databinding.MainBinding
import com.ahmedelsayed.sunmiprinterapp.recyclerview.SearchViewModel
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import java.util.HashMap

class Main: AppCompatActivity() {
    lateinit var binding: MainBinding
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerview = binding.recyclerview
        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)
        //Intent
        val street = intent.getStringExtra("street").toString()
        val house = intent.getStringExtra("house").toString()
        // ArrayList of class ItemsViewModel
        val data = ArrayList<SearchViewModel>()
        val adapter = SearchAdaptor(data)

        recyclerview.adapter = adapter
        adapter.onItemClick = {
            val intent = Intent(this,DetailedAbonent::class.java)
            intent.putExtra("abonent", it)
            startActivity(intent)
        }
//volley
        val queue = Volley.newRequestQueue(applicationContext)
        val url = "http://10.231.40.70"
        val progressBar = binding.mainLoading
        val textView = binding.tvMainError
        progressBar.setVisibility(View.VISIBLE)
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                progressBar.setVisibility(View.GONE)
                try {
                    val array = JSONArray(response)
                    for (i in 0 until array.length()) {
                        val getObj = array.getJSONObject(i)
                        val firstname = getObj.getString("firstname")+' '+getObj.getString("name")
                        val address = getObj.getString("street")+" Дом: "+getObj.getString("house")+
                                " кв: "+getObj.getString("kvartira")
                        val balance = "Баланс: "+getObj.getString("balance")
                        val tvId = getObj.getString("id")
                        data.add(SearchViewModel(firstname, address,balance,tvId))
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
                paramV.put("apistreet", street)
                paramV.put("apihouse", house)
                return paramV
            }
        }
        queue.add(stringRequest)
    }
        }

