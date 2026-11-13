package com.example.recordatorios.data

import com.example.recordatorios.model.Categoria
import com.example.recordatorios.model.EstadoRecordatorio
import com.example.recordatorios.model.Prioridad
import com.example.recordatorios.model.Recordatorio

object DatosSimulados {

    private val trabajo = Categoria(1, "Trabajo")
    private val hogar = Categoria(2, "Hogar")
    private val estudios = Categoria(3, "Estudios")
    private val salud = Categoria(4, "Salud")

    // SET: "trabajo" va repetido a propósito; el Set lo descarta (quedan 4)
    val categorias: Set<Categoria> = setOf(trabajo, hogar, estudios, salud, trabajo)

    // MAP: categoría -> color (ARGB)
    val colorPorCategoria: Map<Categoria, Long> = mapOf(
        trabajo to 0xFF1E88E5,
        hogar to 0xFF43A047,
        estudios to 0xFF8E24AA,
        salud to 0xFFE53935
    )

    // MAP: prioridad -> etiqueta
    val etiquetaPorPrioridad: Map<Prioridad, String> = mapOf(
        Prioridad.Alta to "Prioridad alta",
        Prioridad.Media to "Prioridad media",
        Prioridad.Baja to "Prioridad baja"
    )

    // LIST: recordatorios simulados
    val recordatorios: List<Recordatorio> = listOf(
        Recordatorio(1, "Entregar Actividad 1", estudios, Prioridad.Alta,
            descripcion = "Programación Mobile", fechaLimite = "2026-11-16"),
        Recordatorio(2, "Pagar cuenta de luz", hogar, Prioridad.Media,
            estado = EstadoRecordatorio.Vencido(diasAtraso = 2)),
        Recordatorio(3, "Reunión con proveedor", trabajo, Prioridad.Alta,
            fechaLimite = "2026-11-13"),
        Recordatorio(4, "Comprar fruta", hogar, Prioridad.Baja,
            estado = EstadoRecordatorio.Completado),
        Recordatorio(5, "Control médico", salud, Prioridad.Media,
            descripcion = "Llevar exámenes", fechaLimite = "2026-11-20"),
        Recordatorio(6, "Repasar Kotlin", estudios, Prioridad.Baja),
        Recordatorio(7, "Revisar inventario", trabajo, Prioridad.Media,
            estado = EstadoRecordatorio.Completado)
    )

    fun pendientes(lista: List<Recordatorio> = recordatorios) =
        lista.filter { it.estado is EstadoRecordatorio.Pendiente }

    fun completados(lista: List<Recordatorio> = recordatorios) =
        lista.filter { it.estado is EstadoRecordatorio.Completado }

    fun vencidos(lista: List<Recordatorio> = recordatorios) =
        lista.filter { it.estado is EstadoRecordatorio.Vencido }

    fun porPrioridad(prioridad: Prioridad, lista: List<Recordatorio> = recordatorios) =
        lista.filter { it.prioridad == prioridad }

    // MAP con groupBy: cuántos recordatorios hay por prioridad
    fun conteoPorPrioridad(lista: List<Recordatorio> = recordatorios): Map<Prioridad, Int> =
        lista.groupBy { it.prioridad }.mapValues { it.value.size }
}