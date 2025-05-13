fun main() {
    val redes = mutableMapOf<String, MutableList<String>>() // Mapa: red -> IPs

    while (true) {
        println("Ingresa una IP (o 0 para salir):")
        val ip = readLine() ?: break
        if (ip == "0") break
        if (!validacion(ip)) {
            println("IP no valida")
            continue
        }

        println("Ingresa la máscara de subred:")
        val mascara = readLine() ?: break
        if (!validacion(mascara)) {
            println("Mascara no valida")
            continue
        }

        val red = obtenerRed(ip, mascara)
        if (!redes.containsKey(red)) {
            redes[red] = mutableListOf()
        }
        redes[red]?.add(ip)
    }

    println("\nRedes detectadas:")
    for ((red, ips) in redes) {
        println("$red:")
        for (ip in ips) {
            println("  $ip")
        }
    }
}



fun obtenerRed(ip: String, mascara: String): String {
    val ipPartes = ip.split(".").map { it.toInt() }
    val maskPartes = mascara.split(".").map { it.toInt() }

    val red = mutableListOf<String>()
    for (i in 0..3) {
        red.add((ipPartes[i] and maskPartes[i]).toString())
    }
    return red.joinToString(".")
}
fun validacion(ip: String): Boolean {
    val partes = ip.split(".")
    if (partes.size != 4) return false

    for (parte in partes) {
        val numero = parte.toIntOrNull() ?: return false
        if (numero !in 0..255) return false
    }

    return true
}
