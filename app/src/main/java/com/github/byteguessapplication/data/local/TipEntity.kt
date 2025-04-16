package com.github.byteguessapplication.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tips",
    indices = [
        Index(value = ["card_id"]),
        Index(value = ["text"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = ["id"],
            childColumns = ["card_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TipEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,

    @ColumnInfo(name = "card_id")
    val cardId: Long
)
