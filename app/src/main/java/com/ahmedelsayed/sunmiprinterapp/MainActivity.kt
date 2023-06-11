package com.ahmedelsayed.sunmiprinterapp

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.ahmedelsayed.sunmiprinterutill.PrintMe


class MainActivity : AppCompatActivity() {
    private var printMe: PrintMe? = null
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContentView(R.layout.activity_main)
        printMe = PrintMe(this)
    }

    fun printText(v: View?) {
        printMe!!.sendTextToPrinter("Testing .. PrintMe", 18f, true, false, 2)
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun printImage(v:View?){
        printMe!!.sendImageToPrinter(
            printMe!!.convertDrawableToBitmap(AppCompatResources.getDrawable(this,R.mipmap.ic_launcher_round),100,100)
        );
    }

    fun printLayout(v: View?) {
        val view = findViewById<View>(R.id.print_me_layout)
        printMe!!.sendViewToPrinter(view)
    }
}