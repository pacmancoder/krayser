package org.krayser

import org.krayser.app.KrayserApp
import org.krayser.core.KrayserConfig
import org.krayser.core.KrayserController
import org.krayser.core.Scene
import org.krayser.frontend.LWJGLApp

fun main(args: Array<String>) {
    //LWJGLApp.run(KrayserApp())
    val krayser = KrayserController(Scene(), KrayserConfig(chunkSize = 256))
    krayser.awaitTrace()
}