package com.lihan.smartstep.core.data.datastore

import android.content.Context
import com.lihan.smartstep.core.domain.FileLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class LocalFileLogger(
    private val context: Context
): FileLogger{

    override suspend fun writeText(text: String) {
        withContext(Dispatchers.IO){
            val file = File(context.filesDir,"logger.txt")
            file.appendText(text)
            file.appendText("\n")
        }
    }
}