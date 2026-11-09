package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.model.Empleado
import org.json.JSONArray
import java.io.File

/**
 * CONCEPTO POO: ENCAPSULAMIENTO
 * Implementación concreta del almacenamiento de datos en disco (Archivo JSON local).
 * Aísla completamente la persistencia I/O del resto de la aplicación.
 */
class LocalEmpleadoRepository(private val context: Context) : EmpleadoRepository {

    private val archivo: File
        get() = File(context.filesDir, "empleados_datos.json")

    override fun obtenerEmpleados(): List<Empleado> {
        if (!archivo.exists()) return emptyList()
        return try {
            val jsonStr = archivo.readText()
            if (jsonStr.isBlank()) return emptyList()
            val jsonArray = JSONArray(jsonStr)
            val lista = mutableListOf<Empleado>()
            for (i in 0 until jsonArray.length()) {
                val jsonObj = jsonArray.getJSONObject(i)
                lista.add(Empleado.fromJson(jsonObj))
            }
            lista
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override fun guardarEmpleado(empleado: Empleado) {
        val empleadosActuales = obtenerEmpleados().toMutableList()
        val index = empleadosActuales.indexOfFirst { it.id == empleado.id }
        if (index >= 0) {
            empleadosActuales[index] = empleado
        } else {
            empleadosActuales.add(empleado)
        }
        guardarLista(empleadosActuales)
    }

    override fun eliminarEmpleado(id: String) {
        val empleadosActuales = obtenerEmpleados().filterNot { it.id == id }
        guardarLista(empleadosActuales)
    }

    override fun limpiarTodos() {
        if (archivo.exists()) {
            archivo.delete()
        }
    }

    private fun guardarLista(lista: List<Empleado>) {
        try {
            val jsonArray = JSONArray()
            lista.forEach { jsonArray.put(it.toJson()) }
            archivo.writeText(jsonArray.toString(2))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
