package ipvc.estg.plantme.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "notes_table")

class Planta (
    @ColumnInfo(name = "nome") val nomePlanta: String,
    @ColumnInfo(name = "especie") val especie: String,
    @ColumnInfo(name = "temperatura") val temperatura: Float
)