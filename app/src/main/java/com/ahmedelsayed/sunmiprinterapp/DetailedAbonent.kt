package com.ahmedelsayed.sunmiprinterapp
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.ahmedelsayed.sunmiprinterapp.databinding.ActivityDetailedAbonentBinding
import com.ahmedelsayed.sunmiprinterapp.recyclerview.SearchViewModel
import com.ahmedelsayed.sunmiprinterutill.PrintMe
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class DetailedAbonent : AppCompatActivity() {
    lateinit var binding : ActivityDetailedAbonentBinding
    private var printMe: PrintMe? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailedAbonentBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("Somon", MODE_PRIVATE)
        val btnZayavka = binding.btZayavka
        printMe = PrintMe(this)
        val platejHistory = binding.btHistoryOplata
        val url = "http://10.231.40.70"
        val progressBar = binding.oplataLoad
        val queue = Volley.newRequestQueue(applicationContext)

        platejHistory.setOnClickListener{
            val abonent = intent.getParcelableExtra<SearchViewModel>("abonent")
            if(abonent!=null){
                val intent = Intent(this,PlatejHistory::class.java)
                intent.putExtra("id", abonent.tvId)
                startActivity(intent)
            }
        }
        val abonent = intent.getParcelableExtra<SearchViewModel>("abonent")
        if(abonent!=null){
            progressBar.setVisibility(View.VISIBLE)
            val stringRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    progressBar.setVisibility(View.GONE)
                    try {
                        val array = JSONArray(response)
                        for (i in 0 until array.length()) {
                            val getObj = array.getJSONObject(i)
                            binding.tvAbonentSurename.text =getObj.getString("firstname")
                            binding.tvAbonentName.text =getObj.getString("name")
                            binding.tvAbonentStreet.text = getObj.getString("street")
                            binding.tvAbonentHouse.text = getObj.getString("house")
                            binding.tvAbonentKvartira.text = getObj.getString("kvartira")
                            binding.tvAbonentBalance.text = getObj.getString("balance")
                            binding.tvAbonentTarif.text = getObj.getString("tarif")
                            binding.tvAbonentMobile.text = getObj.getString("mobile")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error ->
                    progressBar.setVisibility(View.VISIBLE)

                }) {
                override fun getParams(): Map<String, String>? {
                    val paramV: MutableMap<String, String> = HashMap()
                    paramV.put("apiAbonentId", abonent.tvId )
                    paramV.put("apikey", "secretkey")
                    return paramV
                }
            }
            queue.add(stringRequest)
        }
        binding.btOplata.setOnClickListener{
            binding.btOplata.visibility=View.INVISIBLE
            progressBar.setVisibility(View.VISIBLE)
            val summa = binding.edSumma.text.toString()
            if(summa.trim().length>0){
                var number = "0"
                val stringRequest: StringRequest = object : StringRequest(
                    Method.POST, url,
                    Response.Listener { response ->
                        progressBar.setVisibility(View.GONE)
                        Log.d("MyLog",response)
                        try {
                            val jsonObject = JSONObject(response)
                            if (jsonObject.getString("status") == "error") {
                                val editor = sharedPreferences.edit()
                                editor.putString("logged", "false")
                                editor.apply()
                                val intent = Intent(applicationContext, Login::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                number=jsonObject.getString("number")
                                val surename = binding.tvAbonentSurename.text
                                val name = binding.tvAbonentName.text
                                val street = binding.tvAbonentStreet.text
                                val house = binding.tvAbonentHouse.text
                                val kvartira = binding.tvAbonentKvartira.text
                                val balance = binding.tvAbonentBalance.text.toString()
                                val edComment = binding.edComment.text.toString()
                                var tekushiy = summa.toInt()+balance.toInt()
                                val sharedPreferences = getSharedPreferences("Somon", MODE_PRIVATE)
                                val controllerFirstname = sharedPreferences.getString("firstname", "Неизвестный").toString()
                                val controllerName = sharedPreferences.getString("name", "Неизвестный").toString()
                                val dateFormat = SimpleDateFormat("d.MM.yyyy, HH:mm:ss ")
                                val date = dateFormat.format(Date())
                                printMe!!.sendTextToPrinter("   ЧДММ \"ТВН Сомониён\"", 32f, true, false, 2)
                                printMe!!.sendTextToPrinter("Номер квитанции: $number", 22f, false, false, 1)
                                printMe!!.sendTextToPrinter("Дата: $date", 22f, false, false, 1)
                                printMe!!.sendTextToPrinter("Абонент: $surename $name", 26f, false, false, 1)
                                printMe!!.sendTextToPrinter("Адрес: $street $house $kvartira", 26f, false, false, 1)
                                printMe!!.sendTextToPrinter("Баланс = $balance сомони", 26f, false, false, 1)
                                printMe!!.sendTextToPrinter("Сумма оплаты = $summa сомони", 26f, true, true, 1)
                                printMe!!.sendTextToPrinter("Текущий баланс = $tekushiy сомони", 26f, true, true, 1)
                                printMe!!.sendTextToPrinter("Получил: $controllerFirstname $controllerName", 26f, false, false, 1)
                                printMe!!.sendTextToPrinter("$edComment", 26f, false, false, 2)
                                printMe!!.sendTextToPrinter("Тел: 77-703-30-30", 32f, true, false, 1)
                                printMe!!.sendTextToPrinter("Спасибо за оплату!", 32f, true, false, 2)
                                printMe!!.sendTextToPrinter("--------------------", 32f, true, false, 4)
                                finish()
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }, Response.ErrorListener { error ->
                        progressBar.setVisibility(View.VISIBLE)

                    }) {
                    override fun getParams(): Map<String, String>? {
                        val paramV: MutableMap<String, String> = HashMap()
                        if (abonent != null) { paramV.put("apiAbonentId", abonent.tvId)
                        }
                        val surename = binding.tvAbonentSurename.text
                        val name = binding.tvAbonentName.text
                        val street = binding.tvAbonentStreet.text
                        val house = binding.tvAbonentHouse.text
                        val kvartira = binding.tvAbonentKvartira.text
                        val balance = binding.tvAbonentBalance.text.toString()
                        val edComment = binding.edComment.text.toString()
                        paramV.put("apikey", sharedPreferences.getString("apikey","none").toString())
                        paramV.put("who", sharedPreferences.getString("name","none").toString())
                        paramV.put("summa", summa)
                        paramV.put("surename", surename.toString())
                        paramV.put("name", name.toString())
                        paramV.put("street", street.toString())
                        paramV.put("house", house.toString())
                        paramV.put("kvartira", kvartira.toString())
                        paramV.put("balance", balance)
                        paramV.put("comment", edComment)
                        paramV.put("id", sharedPreferences.getString("id","none").toString())
                        return paramV
                    }
                }
                queue.add(stringRequest)


            }


        }

            btnZayavka.setOnClickListener{
                val intent = Intent(applicationContext, Zayavka::class.java)
                if (abonent != null) {
                    intent.putExtra("apiZayavkaId", abonent.tvId)
                }
                startActivity(intent)
                finish()
            }





        }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.change_number,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.changeNumber -> {
                val abonent = intent.getParcelableExtra<SearchViewModel>("abonent")
                val intent = Intent(applicationContext, ChangeNumber::class.java)
                if (abonent != null) {
                    intent.putExtra("IdChangeNumber", abonent.tvId)
                }
                startActivity(intent)
                finish()
            }
        }
        return true
    }

    }
