package com.tomas.slotracercontroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import java.io.IOException
import java.io.PrintWriter
import java.net.Socket


class MainActivity : AppCompatActivity()  {
    //Speed is DUTY!!!
    var mainSpeed = 500
    var maxSpeed = 1023
    var minSpeed = 400
    var freq = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val seek = findViewById<SeekBar>(R.id.seekBar)
        val progressView  = findViewById<TextView>(R.id.progressDisplay)
        seek.setProgress(mainSpeed,true)
        seek?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                // write custom code for progress is changed
                progressView.text = "$progress"
                mainSpeed = progress
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
                Toast.makeText(this@MainActivity,
                    "Progress is: " + seek.progress,
                    Toast.LENGTH_SHORT).show()
                //mainSpeed = seek.progress
            }
        })

        val btnPlus = findViewById<Button>(R.id.btnplus)
        btnPlus.setOnClickListener{
            Toast.makeText(this@MainActivity, "BTN PLUS!!!!", Toast.LENGTH_SHORT).show()
            //executeCMD("import os")
        }


        btnPlus.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        println("BT+DOWN!")
                        executeCMD("pwmPin.duty($maxSpeed)")
                    }
                    MotionEvent.ACTION_UP -> {
                        println("BT+UPPPP!")//Do Something
                        executeCMD("pwmPin.duty($mainSpeed)")
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }
        })


        val btnMinus = findViewById<Button>(R.id.btnminus)
        btnMinus.setOnClickListener{
            Toast.makeText(this@MainActivity, "BTN Minus!!!!", Toast.LENGTH_SHORT).show()
        }

        btnMinus.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            println("BT-DOWN!")
                            executeCMD("pwmPin.duty($minSpeed)")
                        }
                        MotionEvent.ACTION_UP -> {
                            println("BT-UPPPP!")
                            executeCMD("pwmPin.duty($mainSpeed)")
                        }
                    }
                    return v?.onTouchEvent(event) ?: true
                }
            })

        //Initiate execution, load libs and imports to console...
        executeCMD("import time\n\rfrom machine import Pin, PWM\n\rPin12 = machine.Pin(12)\n\rpwmPin = PWM(Pin12)")
        executeCMD("pwmPin.freq($freq)\n\r")//Set frequency shall not be changed so static parameter. The speed is controlled by duty.
    }

    fun buildCmd(cmd: String): String {
        return cmd + "\n\r" //Needed for console reaction (Simulate enter hit on keyboard.)
    }

    fun executeCMD(cmd: String){
        GlobalScope.launch { // context of the parent, main runBlocking coroutine
            try {
                val socket = Socket("192.168.4.1", 23)
                val printWriter = PrintWriter(socket.getOutputStream())
                //printWriter.print("import os\n\r")
                printWriter.print(buildCmd(cmd))
                printWriter.flush()
                printWriter.close()
                socket.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
