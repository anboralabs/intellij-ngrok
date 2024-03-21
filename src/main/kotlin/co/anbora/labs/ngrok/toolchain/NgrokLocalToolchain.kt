package co.anbora.labs.ngrok.toolchain

import co.anbora.labs.ngrok.settings.NgrokConfigurationUtil
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile
import kotlin.io.path.isExecutable

class NgrokLocalToolchain(
    private val version: String,
    private val rootDir: VirtualFile,
): NgrokToolchain {
    private val homePath = rootDir.path
    private val executable = rootDir.findChild(NgrokConfigurationUtil.STANDARD_V_COMPILER)

    override fun name(): String = version

    override fun version(): String = version

    override fun compiler(): VirtualFile? = executable

    override fun rootDir(): VirtualFile = rootDir

    override fun homePath(): String = homePath

    override fun isValid(): Boolean {
        return isValidDir(rootDir) && isValidExecutable(executable)
    }

    private fun isValidDir(dir: VirtualFile?): Boolean {
        return dir != null && dir.isValid
                && dir.isInLocalFileSystem && dir.isDirectory
    }

    private fun isValidExecutable(executable: VirtualFile?): Boolean {
        return executable != null && executable.isValid
                && executable.isInLocalFileSystem && executable.toNioPath().isExecutable()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NgrokLocalToolchain

        return FileUtil.comparePaths(other.homePath(), homePath()) == 0
    }

    override fun hashCode(): Int = homePath.hashCode()
}