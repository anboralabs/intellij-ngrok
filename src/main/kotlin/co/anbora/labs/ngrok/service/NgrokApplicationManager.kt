package co.anbora.labs.ngrok.service

import co.anbora.labs.ngrok.runtimes.NgrokApplicationRuntime
import co.anbora.labs.ngrok.runtimes.NgrokBaseRuntime
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import java.util.concurrent.ConcurrentHashMap

class NgrokApplicationManager: Disposable {

    private val applications: ConcurrentHashMap<String, NgrokApplicationRuntime> = ConcurrentHashMap()

    fun runApplication(token: String): NgrokApplicationRuntime {
        val application = applications.getOrPut(token) {
            val newApplicationRuntime = NgrokApplicationRuntime("Ngrok Application")
            newApplicationRuntime.run(token)
            newApplicationRuntime
        }
        return application
    }

    fun refreshApplication(token: String): List<NgrokBaseRuntime> {

        val applicationRuntime = applications.getOrPut(token) {
            NgrokApplicationRuntime("Ngrok Application")
        }

        if (!applicationRuntime.isAlive()) {
            applications.remove(token)
            return emptyList()
        }

        return applicationRuntime.refresh()
    }

    override fun dispose() {
        applications.forEach { (_, application) ->
            application.shutdown()
        }
    }
}