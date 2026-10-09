package co.anbora.labs.ngrok.toolchain

import co.anbora.labs.ngrok.settings.NgrokConfigurationUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import java.nio.file.Path

interface NgrokToolchain {
    fun name(): String
    fun version(): String
    fun compiler(): VirtualFile?
    fun rootDir(): VirtualFile?
    fun homePath(): String
    fun binPath(): String
    fun isValid(): Boolean

    companion object {
        /**
         * Pass [version] when it is already known to avoid running `ngrok --version` again.
         */
        fun fromPath(homePath: String, version: String? = null): NgrokToolchain {
            if (homePath == "") {
                return NULL
            }

            val virtualFileManager = VirtualFileManager.getInstance()
            val rootDir = virtualFileManager.findFileByNioPath(Path.of(homePath)) ?: return NULL
            return fromDirectory(rootDir, version)
        }

        private fun fromDirectory(rootDir: VirtualFile, version: String?): NgrokToolchain {
            return NgrokLocalToolchain(version ?: NgrokConfigurationUtil.guessToolchainVersion(rootDir.path), rootDir)
        }

        val NULL = object : NgrokToolchain {
            override fun name(): String = ""
            override fun version(): String = ""
            override fun compiler(): VirtualFile? = null
            override fun rootDir(): VirtualFile? = null
            override fun homePath(): String = ""
            override fun binPath(): String = ""
            override fun isValid(): Boolean = false
        }
    }
}
