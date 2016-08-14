package org.krayser.frontend

import org.krayser.core.Chunk
import org.lwjgl.opengl.*
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL as LWJGL_NULL

import kotlin.system.measureNanoTime

object LWJGLApp: Frontend{
    // window constants
    const val windowWidth = 800
    const val windowHeight = 600

    private var windowHandle = LWJGL_NULL
    // flag to prevent multiple LWJGL initializations
    private var running = false

    override fun changeTitle(title: String) {
        glfwSetWindowTitle(windowHandle, title)
    }

    override fun drawChunk(chunk: Chunk) {

    }

    private fun lwjglInit() {
        if (!glfwInit()) {
            throw IllegalStateException("GLFW init error")
        }
        // set window hints
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        // create window and check errors
        windowHandle = glfwCreateWindow(
                windowWidth,
                windowHeight,
                "krayser v0.1.0",
                LWJGL_NULL,
                LWJGL_NULL)
        if (windowHandle == LWJGL_NULL) {
            throw RuntimeException("Error during glfw window creation")
        }
        // place window in center
        val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
        glfwSetWindowPos(
                windowHandle,
                (videoMode.width() - windowWidth) / 2,
                (videoMode.height() - windowHeight) / 2)
        // Activate OpenGL context
        glfwMakeContextCurrent(windowHandle)
        // V-Sync on
        glfwSwapInterval(1)
        glfwShowWindow(windowHandle)
    }

    private fun lwjglLoop(procFunction: (Double) -> Unit) {
        GL.createCapabilities()
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        var dt = .0
        while (!glfwWindowShouldClose(windowHandle)) {
            // execute frame processing and return delta time
            val dt_nano = measureNanoTime {
                glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
                procFunction(dt)
                glfwSwapBuffers(windowHandle)
                glfwPollEvents()
            }
            // convert nanos to ms
            dt = dt_nano.toDouble() / 1000000.0
        }
    }

    /**
     * Launches application
     */
    fun run(handler: AppHandler) {
        if (!running) {
            running = true
            try {
                // main application life cycle
                lwjglInit()
                handler.init(this)
                lwjglLoop { handler.proc(this, it) }
                handler.exit(this)
                // free all glfw-related things
                glfwFreeCallbacks(windowHandle)
                glfwDestroyWindow(windowHandle)
            } finally {
                // Terminate GLFW and free the error callback
                glfwTerminate()
                // we are done. application can be launched again
                running = false
            }
        } else {
            throw IllegalStateException("Can't run LWJGL frontend more than one time during app life cycle")
        }
    }
}



