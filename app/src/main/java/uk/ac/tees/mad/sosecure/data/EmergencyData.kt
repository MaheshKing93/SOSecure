package uk.ac.tees.mad.sosecure.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emergency_data")
data class EmergencyData(
    @PrimaryKey val id: Int = 0,
    val emergencyContact: String,
    val lastKnownLatitude: Double?,
    val lastKnownLongitude: Double?
)
