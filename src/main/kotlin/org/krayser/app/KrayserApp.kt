package org.krayser.app

import org.krayser.frontend.AppHandler
import org.krayser.frontend.Frontend

class KrayserApp: AppHandler {
    init {

    }
    override fun init(frontend: Frontend) { }

    override fun proc(frontend: Frontend, dt: Double) {
        frontend.changeTitle("Processing with $dt time")
    }

    override fun exit(frontend: Frontend) { }
}