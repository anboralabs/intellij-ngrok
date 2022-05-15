package co.anbora.labs.ngrok.service

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service

@Service
class ProjectDisposable : Disposable {
    override fun dispose() {
    }
}