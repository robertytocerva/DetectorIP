package com.cxt.robertytocerva.ips

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private val redes = mutableMapOf<String, MutableList<String>>()

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
            val mascara = etMascara.text.toString()

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
            for (ip in ips) {
                resultado.append("  ➤ $ip\n")
            }
        }
        tv.text = resultado.toString()
    }


    private fun validacion(ip: String): Boolean {
        val partes = ip.split(".")
        if (partes.size != 4) return false
        return partes.all {
            val n = it.toIntOrNull() ?: return false
            n in 0..255
        }
    }


    private fun obtenerRed(ip: String, mascara: String): String {
        val ipPartes = ip.split(".").map { it.toInt() }
        val maskPartes = mascara.split(".").map { it.toInt() }

        return ipPartes.zip(maskPartes)
            .map { (ipNum, maskNum) -> ipNum and maskNum }
            .joinToString(".")
    }
}
