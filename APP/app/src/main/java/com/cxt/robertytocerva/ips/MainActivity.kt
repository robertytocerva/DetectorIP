package com.cxt.robertytocerva.ips

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private val redes = mutableMapOf<String, MutableList<String>>()
    private var mascara:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val etIp = findViewById<EditText>(R.id.etIp)
        val etMascara = findViewById<EditText>(R.id.etMascara)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiar)
        val tvRedes = findViewById<TextView>(R.id.tvRedesClasificadas)


        btnAgregar.setOnClickListener {
            val ip = etIp.text.toString()
            mascara = etMascara.text.toString()

            if (!validacion(ip) || !validacion(mascara)) {
                Toast.makeText(this, "IP o máscara inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val red = obtenerRed(ip, mascara)

            if (!redes.containsKey(red)) {
                redes[red] = mutableListOf()
            }

            redes[red]?.add(ip)


            etIp.text.clear()
            etMascara.text.clear()


            mostrarRedes(tvRedes)
        }


        btnLimpiar.setOnClickListener {
            redes.clear()
            tvRedes.text = "|||"
            Toast.makeText(this, "Limpieza completada", Toast.LENGTH_SHORT).show()
        }
    }


    private fun mostrarRedes(tv: TextView) {
        val resultado = StringBuilder()
        for ((red, ips) in redes) {
            resultado.append("Red: $red\n")
            resultado.append("\t ($mascara)\n")
            for (ip in ips) {
                resultado.append("  ➤ $ip\n")
            }
        }
        tv.text = resultado.toString()
    }


    private fun validacion(ip: String): Boolean {
        val partes = ip.split(".")
        if (partes.size != 4) return false

        for (parte in partes) {
            val numero = parte.toIntOrNull() ?: return false
            if (numero !in 0..255) return false
        }

        return true
    }


    private fun obtenerRed(ip: String, mascara: String): String {
        val ipPartes = ip.split(".").map { it.toInt() }
        val maskPartes = mascara.split(".").map { it.toInt() }

        val red = mutableListOf<String>()
        for (i in 0..3) {
            red.add((ipPartes[i] and maskPartes[i]).toString())
        }
        return red.joinToString(".")
    }
}
