package com.example.myapplication.model

import org.json.JSONObject

/**
 * CONCEPTO POO: ABSTRACCIÓN Y HERENCIA (Clase Base Abstracta)
 * Modela un empleado genérico especificando los atributos esenciales y la interfaz
 * polimórfica para el cálculo de salarios.
 */
abstract class Empleado(
    val id: String,
    val nombre: String,
    val departamento: String
) {
    /**
     * CONCEPTO POO: POLIMORFISMO
     * Cada subclase debe proporcionar su propia implementación para calcular el salario.
     */
    abstract fun calcularSalarioTotal(): Double

    /**
     * Devuelve el nombre del tipo de empleado para la UI.
     */
    abstract fun obtenerTipoEmpleado(): String

    /**
     * Encapsula la lógica de serialización JSON para el almacenamiento de datos.
     */
    abstract fun toJson(): JSONObject

    companion object {
        fun fromJson(json: JSONObject): Empleado {
            return when (json.getString("tipo")) {
                "TIEMPO_COMPLETO" -> EmpleadoTiempoCompleto(
                    id = json.getString("id"),
                    nombre = json.getString("nombre"),
                    departamento = json.getString("departamento"),
                    salarioBase = json.getDouble("salarioBase"),
                    bono = json.getDouble("bono")
                )
                "POR_HORA" -> EmpleadoPorHora(
                    id = json.getString("id"),
                    nombre = json.getString("nombre"),
                    departamento = json.getString("departamento"),
                    horasTrabajadas = json.getInt("horasTrabajadas"),
                    tarifaPorHora = json.getDouble("tarifaPorHora")
                )
                "CONTRATISTA" -> EmpleadoContratista(
                    id = json.getString("id"),
                    nombre = json.getString("nombre"),
                    departamento = json.getString("departamento"),
                    montoContrato = json.getDouble("montoContrato")
                )
                else -> throw IllegalArgumentException("Tipo de empleado desconocido: ${json.optString("tipo")}")
            }
        }
    }
}

/**
 * CONCEPTO POO: HERENCIA Y POLIMORFISMO (Subclase 1)
 * Empleado con salario base mensual más bonificación opcional.
 */
class EmpleadoTiempoCompleto(
    id: String,
    nombre: String,
    departamento: String,
    val salarioBase: Double,
    val bono: Double
) : Empleado(id, nombre, departamento) {

    override fun calcularSalarioTotal(): Double {
        return salarioBase + bono
    }

    override fun obtenerTipoEmpleado(): String = "Tiempo Completo"

    override fun toJson(): JSONObject {
        return JSONObject().apply {
            put("tipo", "TIEMPO_COMPLETO")
            put("id", id)
            put("nombre", nombre)
            put("departamento", departamento)
            put("salarioBase", salarioBase)
            put("bono", bono)
        }
    }
}

/**
 * CONCEPTO POO: HERENCIA Y POLIMORFISMO (Subclase 2)
 * Empleado cuyo salario depende de las horas trabajadas y su tarifa por hora.
 */
class EmpleadoPorHora(
    id: String,
    nombre: String,
    departamento: String,
    val horasTrabajadas: Int,
    val tarifaPorHora: Double
) : Empleado(id, nombre, departamento) {

    override fun calcularSalarioTotal(): Double {
        return horasTrabajadas * tarifaPorHora
    }

    override fun obtenerTipoEmpleado(): String = "Por Hora"

    override fun toJson(): JSONObject {
        return JSONObject().apply {
            put("tipo", "POR_HORA")
            put("id", id)
            put("nombre", nombre)
            put("departamento", departamento)
            put("horasTrabajadas", horasTrabajadas)
            put("tarifaPorHora", tarifaPorHora)
        }
    }
}

/**
 * CONCEPTO POO: HERENCIA Y POLIMORFISMO (Subclase 3)
 * Empleado contratista con un pago fijo por contrato.
 */
class EmpleadoContratista(
    id: String,
    nombre: String,
    departamento: String,
    val montoContrato: Double
) : Empleado(id, nombre, departamento) {

    override fun calcularSalarioTotal(): Double {
        return montoContrato
    }

    override fun obtenerTipoEmpleado(): String = "Contratista"

    override fun toJson(): JSONObject {
        return JSONObject().apply {
            put("tipo", "CONTRATISTA")
            put("id", id)
            put("nombre", nombre)
            put("departamento", departamento)
            put("montoContrato", montoContrato)
        }
    }
}
