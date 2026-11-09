package com.example.myapplication

import com.example.myapplication.model.EmpleadoContratista
import com.example.myapplication.model.EmpleadoPorHora
import com.example.myapplication.model.EmpleadoTiempoCompleto
import org.junit.Assert.assertEquals
import org.junit.Test

class EmpleadoTest {

    @Test
    fun testPolimorfismoCalculoSalarioTiempoCompleto() {
        val empleado = EmpleadoTiempoCompleto(
            id = "1",
            nombre = "Juan Mendoza",
            departamento = "Ventas",
            salarioBase = 2000.0,
            bono = 500.0
        )
        // Salario total polimórfico = Salario base + Bono (2000 + 500 = 2500)
        assertEquals(2500.0, empleado.calcularSalarioTotal(), 0.001)
        assertEquals("Tiempo Completo", empleado.obtenerTipoEmpleado())
    }

    @Test
    fun testPolimorfismoCalculoSalarioPorHora() {
        val empleado = EmpleadoPorHora(
            id = "2",
            nombre = "Maria Garcia",
            departamento = "Soporte",
            horasTrabajadas = 160,
            tarifaPorHora = 15.0
        )
        // Salario total polimórfico = Horas * Tarifa (160 * 15 = 2400)
        assertEquals(2400.0, empleado.calcularSalarioTotal(), 0.001)
        assertEquals("Por Hora", empleado.obtenerTipoEmpleado())
    }

    @Test
    fun testPolimorfismoCalculoSalarioContratista() {
        val empleado = EmpleadoContratista(
            id = "3",
            nombre = "Luis Lopez",
            departamento = "Legal",
            montoContrato = 3500.0
        )
        // Salario total polimórfico = Monto de contrato (3500)
        assertEquals(3500.0, empleado.calcularSalarioTotal(), 0.001)
        assertEquals("Contratista", empleado.obtenerTipoEmpleado())
    }
}
