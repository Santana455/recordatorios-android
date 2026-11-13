package com.example.recordatorios.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recordatorios.data.DatosSimulados
import com.example.recordatorios.model.EstadoRecordatorio
import com.example.recordatorios.model.EstadoUi
import com.example.recordatorios.model.Recordatorio

private val filtros = listOf("Todos", "Pendientes", "Completados", "Vencidos")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRecordatorios(estadoUi: EstadoUi) {
    var filtro by remember { mutableStateOf("Todos") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis recordatorios") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (estadoUi) {
                is EstadoUi.Cargando -> Text("Cargando...", Modifier.align(Alignment.Center))
                is EstadoUi.Vacio -> Text("No hay recordatorios", Modifier.align(Alignment.Center))
                is EstadoUi.Error -> Text("Error: ${estadoUi.mensaje}", Modifier.align(Alignment.Center))
                is EstadoUi.Exito -> {
                    val lista = when (filtro) {
                        "Pendientes" -> DatosSimulados.pendientes(estadoUi.recordatorios)
                        "Completados" -> DatosSimulados.completados(estadoUi.recordatorios)
                        "Vencidos" -> DatosSimulados.vencidos(estadoUi.recordatorios)
                        else -> estadoUi.recordatorios
                    }.sortedByDescending { it.prioridad.nivel }

                    Column {
                        Resumen(estadoUi.recordatorios)
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filtros) { f ->
                                FilterChip(
                                    selected = filtro == f,
                                    onClick = { filtro = f },
                                    label = { Text(f) }
                                )
                            }
                        }
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(lista, key = { it.id }) { TarjetaRecordatorio(it) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Resumen(lista: List<Recordatorio>) {
    val conteo = DatosSimulados.conteoPorPrioridad(lista)
    Text(
        text = "Total: ${lista.size} - Categorías: ${DatosSimulados.categorias.size} - " +
                conteo.entries.sortedByDescending { it.key.nivel }
                    .joinToString(" - ") { "${it.key.nombre}: ${it.value}" },
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun TarjetaRecordatorio(r: Recordatorio) {
    val colorCategoria = Color(DatosSimulados.colorPorCategoria[r.categoria] ?: 0xFF9E9E9E)
    val textoEstado = when (val e = r.estado) {
        is EstadoRecordatorio.Pendiente -> "Pendiente"
        is EstadoRecordatorio.Completado -> "Completado"
        is EstadoRecordatorio.Vencido -> "Vencido (${e.diasAtraso} días)"
    }

    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text(r.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            r.descripcion?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
            Spacer(Modifier.size(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(12.dp).background(colorCategoria, CircleShape))
                Spacer(Modifier.width(6.dp))
                Text(r.categoria.nombre, style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.width(12.dp))
                AssistChip(
                    onClick = {},
                    label = { Text(DatosSimulados.etiquetaPorPrioridad[r.prioridad] ?: r.prioridad.nombre) }
                )
            }
            Text("Estado: $textoEstado", style = MaterialTheme.typography.bodySmall)
            r.fechaLimite?.let { Text("Fecha límite: $it", style = MaterialTheme.typography.bodySmall) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantalla() {
    MaterialTheme {
        PantallaRecordatorios(EstadoUi.Exito(DatosSimulados.recordatorios))
    }
}