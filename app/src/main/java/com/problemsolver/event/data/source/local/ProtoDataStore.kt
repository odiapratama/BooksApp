package com.problemsolver.event.data.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.problemsolver.event.Settings

val Context.dataStore: DataStore<Settings> by dataStore(
    fileName = "settings.pb",
    serializer = SettingsSerializer
)

object SettingsSerializer: Serializer<Settings> {

    override val defaultValue: Settings = Settings.getDefaultInstance()

    override suspend fun readFrom(input: java.io.InputStream): Settings {
        return try {
            Settings.parseFrom(input)
        } catch (exception: Exception) {
            throw IllegalArgumentException("Error reading proto", exception)
        }
    }

    override suspend fun writeTo(t: Settings, output: java.io.OutputStream) {
        t.writeTo(output)
    }
}