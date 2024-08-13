package co.anbora.labs.ngrok.settings

import co.anbora.labs.ngrok.utils.toPath
import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessAdapter
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.util.SystemInfo
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.io.path.exists

object NgrokConfigurationUtil {
    private val LOG = logger<NgrokConfigurationUtil>()

    const val TOOLCHAIN_NOT_SETUP = "ngrok executable not found, toolchain not setup correctly?"
    const val UNDEFINED_VERSION = "N/A"
    val STANDARD_V_COMPILER = if (SystemInfo.isWindows) "ngrok.exe" else "ngrok"

    fun guessToolchainVersion(path: String): String {
        if (path.isBlank()) {
            return UNDEFINED_VERSION
        }

        val exePath = "$path/$STANDARD_V_COMPILER"
        if (!exePath.toPath().exists()) {
            return UNDEFINED_VERSION
        }

        val result = executeAndReturnOutput(exePath, "--version")

        val pattern = Pattern.compile("(\\d+(.\\d+)*)")
        val matcher: Matcher = pattern.matcher(result)

        var version = ""

        if (matcher.find()) {
            version = matcher.group()
        }

        return version.trim().ifEmpty { UNDEFINED_VERSION }
    }

    fun executeAndReturnOutput(exePath: String, vararg arguments: String): String {
        val cmd = GeneralCommandLine()
            .withExePath(exePath)
            .withParameters(*arguments)
            .withCharset(StandardCharsets.UTF_8)

        val processOutput = StringBuilder()
        try {
            val handler = OSProcessHandler(cmd)
            handler.addProcessListener(object : CapturingProcessAdapter() {
                override fun processTerminated(event: ProcessEvent) {
                    if (event.exitCode != 0) {
                        LOG.warn("Couldn't get ngrok toolchain version: " + output.stderr)
                    } else {
                        processOutput.append(output.stdout)
                    }
                }
            })
            handler.startNotify()
            val future = ApplicationManager.getApplication().executeOnPooledThread {
                handler.waitFor()
            }
            future.get(2000, TimeUnit.MILLISECONDS)
        } catch (e: ExecutionException) {
            LOG.warn("Can't execute command for getting ngrok toolchain version", e)
        } catch (e: TimeoutException) {
            LOG.warn("Can't execute command for getting ngrok toolchain version", e)
        }
        return processOutput.toString()
    }
}
