package com.example.myapplication.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.Empleado
import com.example.myapplication.model.EmpleadoContratista
import com.example.myapplication.model.EmpleadoPorHora
import com.example.myapplication.model.EmpleadoTiempoCompleto
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.util.Locale

enum class TipoEmpleadoOption {
    TIEMPO_COMPLETO,
    POR_HORA,
    CONTRATISTA
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadoScreen(
    viewModel: EmpleadoViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var mostrarDialogoAgregar by remember { mutableStateOf(false) }
    var mostrarExplicacionPoo by remember { mutableStateOf(false) }

    EmpleadoScreenContent(
        uiState = uiState,
        mostrarExplicacionPoo = mostrarExplicacionPoo,
        onToggleExplicacion = { mostrarExplicacionPoo = !mostrarExplicacionPoo },
        onAbrirDialogoAgregar = { mostrarDialogoAgregar = true },
        onEliminarEmpleado = { id -> viewModel.eliminarEmpleado(id) },
        modifier = modifier
    )

    if (mostrarDialogoAgregar) {
        AgregarEmpleadoDialog(
            onDismiss = { mostrarDialogoAgregar = false },
            onConfirm = { tipo, nombre, departamento, p1, p2 ->
                when (tipo) {
                    TipoEmpleadoOption.TIEMPO_COMPLETO -> {
                        viewModel.agregarEmpleadoTiempoCompleto(
                            nombre = nombre,
                            departamento = departamento,
                            salarioBase = p1.toDoubleOrNull() ?: 0.0,
                            bono = p2.toDoubleOrNull() ?: 0.0
                        )
                    }
                    TipoEmpleadoOption.POR_HORA -> {
                        viewModel.agregarEmpleadoPorHora(
                            nombre = nombre,
                            departamento = departamento,
                            horas = p1.toIntOrNull() ?: 0,
                            tarifa = p2.toDoubleOrNull() ?: 0.0
                        )
                    }
                    TipoEmpleadoOption.CONTRATISTA -> {
                        viewModel.agregarEmpleadoContratista(
                            nombre = nombre,
                            departamento = departamento,
                            monto = p1.toDoubleOrNull() ?: 0.0
                        )
                    }
                }
                mostrarDialogoAgregar = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadoScreenContent(
    uiState: EmpleadoUiState,
    mostrarExplicacionPoo: Boolean,
    onToggleExplicacion: () -> Unit,
    onAbrirDialogoAgregar: () -> Unit,
    onEliminarEmpleado: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Gestión de Empleados",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Demostración de POO y Almacenamiento JSON",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onToggleExplicacion) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Información POO",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAbrirDialogoAgregar,
                icon = { Icon(Icons.Default.Add, contentDescription = "Agregar Empleado") },
                text = { Text("Nuevo Empleado") },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Banner Explicativo de Conceptos POO
            AnimatedVisibility(visible = mostrarExplicacionPoo) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "🧩 Conceptos de Programación Orientada a Objetos",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "• Abstracción: Clase base abstracta 'Empleado' e interfaz 'EmpleadoRepository'.\n" +
                                    "• Encapsulamiento: Repositorio en archivo JSON privado y estado ViewModel protegido.\n" +
                                    "• Herencia: EmpleadoTiempoCompleto, EmpleadoPorHora y EmpleadoContratista heredan de Empleado.\n" +
                                    "• Polimorfismo: Cada tipo calcula su salario con su propio método 'calcularSalarioTotal()'.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tarjetas de Métricas Resumen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    titulo = "Total Empleados",
                    valor = "${uiState.listaEmpleados.size}",
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.primaryContainer
                )
                MetricCard(
                    titulo = "Nómina Total",
                    valor = String.format(Locale.getDefault(), "$%.2f", uiState.totalNomina),
                    modifier = Modifier.weight(1.2f),
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Lista de Empleados Registrados",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.listaEmpleados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay empleados registrados.\nHaz clic en 'Nuevo Empleado' para agregar uno.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(
                        items = uiState.listaEmpleados,
                        key = { it.id }
                    ) { empleado ->
                        EmpleadoItemCard(
                            empleado = empleado,
                            onEliminar = { onEliminarEmpleado(empleado.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    titulo: String,
    valor: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = valor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun EmpleadoItemCard(
    empleado: Empleado,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        when (empleado) {
                            is EmpleadoTiempoCompleto -> MaterialTheme.colorScheme.primary
                            is EmpleadoPorHora -> MaterialTheme.colorScheme.secondary
                            is EmpleadoContratista -> MaterialTheme.colorScheme.tertiary
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = empleado.nombre,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = empleado.obtenerTipoEmpleado(),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = "Depto: ${empleado.departamento}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                val detalleCalculo = when (empleado) {
                    is EmpleadoTiempoCompleto -> String.format(Locale.getDefault(), "Base: $%.2f + Bono: $%.2f", empleado.salarioBase, empleado.bono)
                    is EmpleadoPorHora -> String.format(Locale.getDefault(), "%dh x $%.2f/h", empleado.horasTrabajadas, empleado.tarifaPorHora)
                    is EmpleadoContratista -> String.format(Locale.getDefault(), "Monto de Contrato: $%.2f", empleado.montoContrato)
                    else -> ""
                }

                Text(
                    text = detalleCalculo,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = String.format(Locale.getDefault(), "Salario Total: $%.2f", empleado.calcularSalarioTotal()),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onEliminar) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun AgregarEmpleadoDialog(
    onDismiss: () -> Unit,
    onConfirm: (tipo: TipoEmpleadoOption, nombre: String, departamento: String, p1: String, p2: String) -> Unit
) {
    var tipoSeleccionado by remember { mutableStateOf(TipoEmpleadoOption.TIEMPO_COMPLETO) }
    var nombre by remember { mutableStateOf("") }
    var departamento by remember { mutableStateOf("") }
    var parametro1 by remember { mutableStateOf("") }
    var parametro2 by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar Nuevo Empleado") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Tipo de Empleado (Herencia POO):", style = MaterialTheme.typography.labelMedium)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FilterChip(
                        selected = tipoSeleccionado == TipoEmpleadoOption.TIEMPO_COMPLETO,
                        onClick = { tipoSeleccionado = TipoEmpleadoOption.TIEMPO_COMPLETO },
                        label = { Text("Completo", fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = tipoSeleccionado == TipoEmpleadoOption.POR_HORA,
                        onClick = { tipoSeleccionado = TipoEmpleadoOption.POR_HORA },
                        label = { Text("Por Hora", fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = tipoSeleccionado == TipoEmpleadoOption.CONTRATISTA,
                        onClick = { tipoSeleccionado = TipoEmpleadoOption.CONTRATISTA },
                        label = { Text("Contratista", fontSize = 11.sp) }
                    )
                }

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = departamento,
                    onValueChange = { departamento = it },
                    label = { Text("Departamento") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                when (tipoSeleccionado) {
                    TipoEmpleadoOption.TIEMPO_COMPLETO -> {
                        OutlinedTextField(
                            value = parametro1,
                            onValueChange = { parametro1 = it },
                            label = { Text("Salario Base ($)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = parametro2,
                            onValueChange = { parametro2 = it },
                            label = { Text("Bono Adicional ($)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                    TipoEmpleadoOption.POR_HORA -> {
                        OutlinedTextField(
                            value = parametro1,
                            onValueChange = { parametro1 = it },
                            label = { Text("Horas Trabajadas") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = parametro2,
                            onValueChange = { parametro2 = it },
                            label = { Text("Tarifa por Hora ($)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                    TipoEmpleadoOption.CONTRATISTA -> {
                        OutlinedTextField(
                            value = parametro1,
                            onValueChange = { parametro1 = it },
                            label = { Text("Monto Total Contrato ($)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nombre.isNotBlank() && departamento.isNotBlank() && parametro1.isNotBlank()) {
                        onConfirm(tipoSeleccionado, nombre, departamento, parametro1, parametro2)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmpleadoScreenPreview() {
    MyApplicationTheme {
        val sampleList = listOf(
            EmpleadoTiempoCompleto("1", "Carlos Mendoza", "Tecnología", 2500.0, 300.0),
            EmpleadoPorHora("2", "Ana Gómez", "Diseño", 120, 18.5),
            EmpleadoContratista("3", "Roberto Silva", "Consultoría", 1800.0)
        )
        EmpleadoScreenContent(
            uiState = EmpleadoUiState(
                listaEmpleados = sampleList,
                totalNomina = sampleList.sumOf { it.calcularSalarioTotal() }
            ),
            mostrarExplicacionPoo = true,
            onToggleExplicacion = {},
            onAbrirDialogoAgregar = {},
            onEliminarEmpleado = {}
        )
    }
}
