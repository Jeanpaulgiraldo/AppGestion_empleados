package com.example.myapplication.repository

import com.example.myapplication.model.Empleado

/**
 * CONCEPTO POO: ABSTRACCIÓN (Interfaz de Repositorio)
 * Oculta la complejidad del almacenamiento persistente y expone solo operaciones CRUD fundamentales.
 */
interface EmpleadoRepository {
    fun obtenerEmpleados(): List<Empleado>
    fun guardarEmpleado(empleado: Empleado)
    fun eliminarEmpleado(id: String)
    fun limpiarTodos()
}
