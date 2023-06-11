package com.ahmedelsayed.sunmiprinterapp

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.ahmedelsayed.sunmiprinterapp.databinding.ActivityZayavkaBinding
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class Zayavka : AppCompatActivity() {
    lateinit var binding:ActivityZayavkaBinding
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        binding = ActivityZayavkaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("Somon", MODE_PRIVATE)
        val arrayzayavka =  arrayListOf<String>("Нет сигнал","Настройка","Поиск","Отключение")
        var spinner = binding.spinnerZayavka
        val text = binding.tvTextSpinnerZayavka
        val url = "http://10.231.40.70"
        val abonentId=intent.getStringExtra("apiZayavkaId").toString()
        val btZayavka = binding.btZayavkaSend
        val arrayAdapter = ArrayAdapter<String>(this,
            R.layout.simple_spinner_dropdown_item,arrayzayavka)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                text.text = arrayzayavka[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        btZayavka.setOnClickListener{
            val queue = Volley.newRequestQueue(applicationContext)
            val stringRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)

                        if (jsonObject.getString("status") == "error"){
                            val editor = sharedPreferences.edit()
                            editor.putString("logged", "false")
                            editor.apply()
                            val intent = Intent(applicationContext, Login::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            finish()
                        }


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error ->
                }) {
                override fun getParams(): Map<String, String>? {
                    val paramV: MutableMap<String, String> = HashMap()
                    paramV.put("apiIdZayavkaAbonent", abonentId)
                    paramV.put("apikey", "mysecretkeyapi")
                    paramV.put("prichina", text.text.toString())
                    Log.d("MyLog",text.text.toString())
                    return paramV
                }
            }
            queue.add(stringRequest)


        }


    }
}