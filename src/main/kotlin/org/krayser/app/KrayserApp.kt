package org.krayser.app

import org.krayser.core.KrayserConfig
import org.krayser.core.KrayserController
import org.krayser.core.Scene
import org.krayser.frontend.AppHandler
import org.krayser.frontend.Frontend

class KrayserApp: AppHandler {

    private var krayser = KrayserController(Scene(), KrayserConfig())

    override fun init(frontend: Frontend) {
        krayser.startTrace()
    }

    override fun proc(frontend: Frontend, dt: Double) {
        frontend.changeTitle("Processing with $dt time")
        for (chunk in krayser.chunks) {
            frontend.drawChunk(chunk)
        }
    }

    override fun exit(frontend: Frontend) {
        krayser.abortTrace()
    }
}