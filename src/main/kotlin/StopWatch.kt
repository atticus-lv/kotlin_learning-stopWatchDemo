import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class StopWatch {

    var formattedTime by mutableStateOf("00:00:000")

    private var coroutineScopt = CoroutineScope(Dispatchers.Default)
    private var isActive = false
    private var timeMillis = 0L
    private var lastTimestamp = 0L

    fun start() {
        if (isActive) return

        coroutineScopt.launch {
            lastTimestamp = System.currentTimeMillis()
            this@StopWatch.isActive = true
            while (this@StopWatch.isActive) {
                delay(10L)
                timeMillis += System.currentTimeMillis() - lastTimestamp
                lastTimestamp = System.currentTimeMillis()
                formattedTime = formatTime(timeMillis)
            }
        }
    }

    fun pause() {
        isActive = false
    }

    fun reset() {
        coroutineScopt.cancel()
        coroutineScopt = CoroutineScope(Dispatchers.Default)
        timeMillis = 0L
        lastTimestamp = 0L
        formattedTime = ("00:00:000")
        isActive = false
    }

    private fun formatTime(timeMillis: Long): String {
        val localDataTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timeMillis),
            ZoneId.systemDefault()
        )
        val formatter = DateTimeFormatter.ofPattern(
            "mm:ss:SSS",
            Locale.getDefault()
        )
        return localDataTime.format(formatter)
    }
}