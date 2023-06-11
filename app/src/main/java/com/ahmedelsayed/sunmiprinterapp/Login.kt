package com.ahmedelsayed.sunmiprinterapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ahmedelsayed.sunmiprinterapp.databinding.LoginBinding
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class Login: AppCompatActivity() {
    lateinit var binding: LoginBinding
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val login = binding.login
        val password = binding.password
        val textView = binding.textError
        val btn = binding.btnEnter
        val progressBar = binding.loading
        val url = "http://10.231.40.70"
        val sharedPreferences = getSharedPreferences("Somon", MODE_PRIVATE)
        if(sharedPreferences.getString("logged", "false").toString() == "true") {
            val intent = Intent(applicationContext, Search::class.java)
            startActivity(intent)
            finish()
        }
        btn.setOnClickListener{
            progressBar.setVisibility(View.VISIBLE)
            val queue = Volley.newRequestQueue(applicationContext)
            val stringRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    progressBar.setVisibility(View.GONE)
                    try {
                        val jsonObject = JSONObject(response)
                        if (jsonObject.getString("status") == "success") {
                            val editor = sharedPreferences.edit()
                            editor.putString("logged", "true")
                            editor.putString("login", jsonObject.getString("login"))
                            editor.putString("apikey", jsonObject.getString("apikey"))
                            editor.putString("firstname", jsonObject.getString("firstname"))
                            editor.putString("name", jsonObject.getString("name"))
                            editor.putString("balance", jsonObject.getString("balance"))
                            editor.putString("id", jsonObject.getString("id"))
                            editor.apply()
                            val intent = Intent(applicationContext, Search::class.java)
                            startActivity(intent)
                            finish()
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
                    paramV.put("apiusername", login.text.toString())
                    paramV.put("apipassword", password.text.toString())
                    return paramV
                }
            }
            queue.add(stringRequest)
        }
        }

    }
