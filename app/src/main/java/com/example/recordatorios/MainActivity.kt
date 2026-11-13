package com.example.recordatorios

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.recordatorios.data.DatosSimulados
import com.example.recordatorios.model.Categoria
import com.example.recordatorios.model.EstadoUi
import com.example.recordatorios.model.Prioridad
import com.example.recordatorios.model.crearRecordatorio
import com.example.recordatorios.ui.PantallaRecordatorios
import com.example.recordatorios.ui.theme.RecordatoriosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        probarValidaciones()

        val estado = if (DatosSimulados.recordatorios.isEmpty()) EstadoUi.Vacio
        else EstadoUi.Exito(DatosSimulados.recordatorios)

        setContent {
            RecordatoriosTheme {
                PantallaRecordatorios(estado)
            }
        }
    }

    private fun probarValidaciones() {
        val cat = Categoria(1, "Trabajo")
        val pruebas = listOf(
            crearRecordatorio(10, "Válido", cat, Prioridad.Media),
            crearRecordatorio(11, "", cat, Prioridad.Alta),
            crearRecordatorio(0, "Id inválido", cat, Prioridad.Baja),
            crearRecordatorio(12, "Fecha mala", cat, Prioridad.Baja, fechaLimite = "16/11/2026")
        )
        pruebas.forEach { r ->
            r.onSuccess { Log.d("VALIDACION", "OK: ${it.titulo}") }
                .onFailure { Log.e("VALIDACION", "Rechazado: ${it.message}") }
        }
        Log.d("VALIDACION", "Categorías únicas (Set): ${DatosSimulados.categorias.size}")
    }
}