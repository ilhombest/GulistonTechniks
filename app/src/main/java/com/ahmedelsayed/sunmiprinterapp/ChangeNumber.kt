package com.ahmedelsayed.sunmiprinterapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ahmedelsayed.sunmiprinterapp.databinding.ActivityChangeNumberBinding
import com.ahmedelsayed.sunmiprinterapp.databinding.ActivityDetailedAbonentBinding
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class ChangeNumber : AppCompatActivity() {
    lateinit var binding : ActivityChangeNumberBinding
    override fun onCreate(s: Bundle?) {
        binding = ActivityChangeNumberBinding.inflate(layoutInflater)
        super.onCreate(s)
        setContentView(binding.root)
        binding.btnChangeNumber.setOnClickListener{
            val edChangeNumber = binding.edChangeNumber
            val url = "http://10.231.40.70"
            val queue = Volley.newRequestQueue(applicationContext)
            val abonentId=intent.getStringExtra("IdChangeNumber").toString()
            val stringRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)
                        if (jsonObject.getString("status") == "correct") {
                            finish()
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error ->
                }) {
                override fun getParams(): Map<String, String>? {
                    val paramV: MutableMap<String, String> = HashMap()
                    paramV.put("apiIdMobile", abonentId)
                    paramV.put("apikey", "mysecretkeyapi123")
                    paramV.put("apiMobile", edChangeNumber.text.toString())
                    return paramV
                }
            }
            queue.add(stringRequest)
        }
    }
}