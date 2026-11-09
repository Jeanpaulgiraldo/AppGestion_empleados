package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.model.Empleado
import com.example.myapplication.model.EmpleadoContratista
import com.example.myapplication.model.EmpleadoPorHora
import com.example.myapplication.model.EmpleadoTiempoCompleto
import com.example.myapplication.repository.EmpleadoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * CONCEPTO POO: ENCAPSULAMIENTO DE ESTADO Y POLIMORFISMO EN CAPA DE NEGOCIO
 */
data class EmpleadoUiState(
    val listaEmpleados: List<Empleado> = emptyList(),
    val totalNomina: Double = 0.0
)

class EmpleadoViewModel(
    private val repository: EmpleadoRepository
) : ViewModel() {

    // Encapsulamiento del estado interno mutable (_uiState)
    private val _uiState = MutableStateFlow(EmpleadoUiState())
    val uiState: StateFlow<EmpleadoUiState> = _uiState.asStateFlow()

    init {
        cargarEmpleados()
    }

    fun cargarEmpleados() {
        val empleados = repository.obtenerEmpleados()
        actualizarEstado(empleados)
    }

    fun agregarEmpleadoTiempoCompleto(
        nombre: String,
        departamento: String,
        salarioBase: Double,
        bono: Double
    ) {
        val nuevo = EmpleadoTiempoCompleto(
            id = UUID.randomUUID().toString(),
            nombre = nombre,
            departamento = departamento,
            salarioBase = salarioBase,
            bono = bono
        )
        repository.guardarEmpleado(nuevo)
        cargarEmpleados()
    }

    fun agregarEmpleadoPorHora(
        nombre: String,
        departamento: String,
        horas: Int,
        tarifa: Double
    ) {
        val nuevo = EmpleadoPorHora(
            id = UUID.randomUUID().toString(),
            nombre = nombre,
            departamento = departamento,
            horasTrabajadas = horas,
            tarifaPorHora = tarifa
        )
        repository.guardarEmpleado(nuevo)
        cargarEmpleados()
    }

    fun agregarEmpleadoContratista(
        nombre: String,
        departamento: String,
        monto: Double
    ) {
        val nuevo = EmpleadoContratista(
            id = UUID.randomUUID().toString(),
            nombre = nombre,
            departamento = departamento,
            montoContrato = monto
        )
        repository.guardarEmpleado(nuevo)
        cargarEmpleados()
    }

    fun eliminarEmpleado(id: String) {
        repository.eliminarEmpleado(id)
        cargarEmpleados()
    }

    /**
     * DEMOSTRACIÓN DE POLIMORFISMO:
     * `it.calcularSalarioTotal()` invoca la implementación específica de cada tipo
     * (Tiempo Completo, Por Hora o Contratista) dinámicamente sin condicionales extra.
     */
    private fun actualizarEstado(empleados: List<Empleado>) {
        val total = empleados.sumOf { it.calcularSalarioTotal() }
        _uiState.value = EmpleadoUiState(
            listaEmpleados = empleados,
            totalNomina = total
        )
    }
}

class EmpleadoViewModelFactory(
    private val repository: EmpleadoRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EmpleadoViewModel(repository) as T
    }
}
