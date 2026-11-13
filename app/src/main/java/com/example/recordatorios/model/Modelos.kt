package com.example.recordatorios.model

sealed class Prioridad(val nombre: String, val nivel: Int) {
    data object Alta : Prioridad("Alta", 3)
    data object Media : Prioridad("Media", 2)
    data object Baja : Prioridad("Baja", 1)
}

sealed class EstadoRecordatorio(val nombre: String) {
    data object Pendiente : EstadoRecordatorio("Pendiente")
    data object Completado : EstadoRecordatorio("Completado")
    data class Vencido(val diasAtraso: Int) : EstadoRecordatorio("Vencido") {
        init {
            require(diasAtraso > 0) { "Un recordatorio vencido debe tener al menos 1 día de atraso" }
        }
    }
}

sealed class EstadoUi {
    data object Cargando : EstadoUi()
    data object Vacio : EstadoUi()
    data class Exito(val recordatorios: List<Recordatorio>) : EstadoUi()
    data class Error(val mensaje: String) : EstadoUi()
}

data class Categoria(val id: Int, val nombre: String) {
    init {
        require(id > 0) { "El id de la categoría debe ser mayor a 0" }
        require(nombre.isNotBlank()) { "La categoría debe tener nombre" }
    }
}

data class Ubicacion(val nombre: String, val latitud: Double, val longitud: Double) {
    init {
        require(latitud in -90.0..90.0) { "Latitud fuera de rango" }
        require(longitud in -180.0..180.0) { "Longitud fuera de rango" }
    }
}

data class Recordatorio(
    val id: Int,
    val titulo: String,
    val categoria: Categoria,
    val prioridad: Prioridad,
    val estado: EstadoRecordatorio = EstadoRecordatorio.Pendiente,
    val descripcion: String? = null,
    val fechaLimite: String? = null,   // formato AAAA-MM-DD
    val ubicacion: Ubicacion? = null
) {
    init {
        require(id > 0) { "El id debe ser mayor a 0" }
        require(titulo.isNotBlank()) { "El título no puede estar vacío" }
        require(titulo.length <= 60) { "El título no puede superar 60 caracteres" }
        require(descripcion == null || descripcion.length <= 200) {
            "La descripción no puede superar 200 caracteres"
        }
        require(fechaLimite == null || fechaLimite.matches(Regex("""\d{4}-\d{2}-\d{2}"""))) {
            "La fecha debe tener formato AAAA-MM-DD"
        }
    }
}

fun crearRecordatorio(
    id: Int,
    titulo: String,
    categoria: Categoria,
    prioridad: Prioridad,
    estado: EstadoRecordatorio = EstadoRecordatorio.Pendiente,
    descripcion: String? = null,
    fechaLimite: String? = null
): Result<Recordatorio> = runCatching {
    Recordatorio(id, titulo, categoria, prioridad, estado, descripcion, fechaLimite)
}