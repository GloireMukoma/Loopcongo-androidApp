package com.example.loopcongo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers LIMIT 1")
    suspend fun getCustomer(): Customer?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: Customer)

    @Query("DELETE FROM customers")
    suspend fun clearCustomers()
}

