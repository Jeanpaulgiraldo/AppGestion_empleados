package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.myapplication.repository.LocalEmpleadoRepository
import com.example.myapplication.ui.EmpleadoScreen
import com.example.myapplication.ui.EmpleadoViewModel
import com.example.myapplication.ui.EmpleadoViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Instanciar el Repositorio (Encapsulamiento del Almacenamiento JSON)
        val repository = LocalEmpleadoRepository(applicationContext)

        // 2. Sembrar datos de prueba iniciales si el almacenamiento está vacío
        if (repository.obtenerEmpleados().isEmpty()) {
            repository.guardarEmpleado(
                com.example.myapplication.model.EmpleadoTiempoCompleto(
                    id = java.util.UUID.randomUUID().toString(),
                    nombre = "Carlos Mendoza",
                    departamento = "Tecnología",
                    salarioBase = 2500.0,
                    bono = 300.0
                )
            )
            repository.guardarEmpleado(
                com.example.myapplication.model.EmpleadoPorHora(
                    id = java.util.UUID.randomUUID().toString(),
                    nombre = "Ana Gómez",
                    departamento = "Diseño",
                    horasTrabajadas = 120,
                    tarifaPorHora = 18.5
                )
            )
            repository.guardarEmpleado(
                com.example.myapplication.model.EmpleadoContratista(
                    id = java.util.UUID.randomUUID().toString(),
                    nombre = "Roberto Silva",
                    departamento = "Consultoría",
                    montoContrato = 1800.0
                )
            )
        }

        // 3. Crear el ViewModel pasando la fábrica de dependencias
        val viewModel: EmpleadoViewModel by viewModels {
            EmpleadoViewModelFactory(repository)
        }

        setContent {
            com.example.myapplication.ui.theme.MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EmpleadoScreen(viewModel = viewModel)
                }
            }
        }
    }
}
