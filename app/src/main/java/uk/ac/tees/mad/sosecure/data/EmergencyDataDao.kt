package uk.ac.tees.mad.sosecure.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EmergencyDataDao {
    @Query("SELECT * FROM emergency_data WHERE id = 0")
    suspend fun getEmergencyData(): EmergencyData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateEmergencyData(data: EmergencyData)
}
