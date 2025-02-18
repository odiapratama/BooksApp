package com.problemsolver.event.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.problemsolver.event.data.model.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM event")
    fun getFavoriteEvents(): Flow<List<Event>>

    @Insert
    suspend fun insertFavoriteEvents(events: Event)

    @Query("DELETE FROM event WHERE id = :id")
    suspend fun deleteFavoriteEvent(id: Int)

    @Query("SELECT * FROM event WHERE id = :id")
    suspend fun getFavoriteEventById(id: Int): Event?
}