package org.krayser.frontend

import org.krayser.core.Chunk
import org.krayser.core.defaultHeight
import org.krayser.core.defaultWidth
import org.lwjgl.BufferUtils

import org.lwjgl.opengl.*
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import org.lwjgl.system.MemoryUtil.NULL as LWJGL_NULL

import kotlin.system.measureNanoTime

/**
 * Frontend class implementation for LWJGL as singletone
 *
 * use [run(handler)][run] to run application with you handler
 * TODO: Divide surface on quads and assign different textures
 */
object LWJGLApp: Frontend {
    // window constants
    const val windowWidth = defaultWidth
    const val windowHeight = defaultHeight

    private var windowHandle = LWJGL_NULL
    // flag to prevent multiple LWJGL initializations
    private var running = false

    // OpenGL
    private var tex = 0

    override fun changeTitle(title: String) {
        glfwSetWindowTitle(windowHandle, title)
    }

    override fun drawChunk(chunk: Chunk) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex)
        with (chunk.workGroup) {
            var buff = BufferUtils.createByteBuffer(w * h * 3)
            buff.put(chunk.surface)
            buff.flip()
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, x, y, w, h, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buff)
        }
    }

    override fun clearSurface() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
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
        GL.createCapabilities()
    }

    private fun lwjglLoop(procFunction: (Double) -> Unit) {
        // boring stuff to init openGL for quad drawing

        // shaders
        val vertShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)
        GL20.glShaderSource(vertShader, """
            #version 130
            in vec2 position;
            in vec2 texcoord;
            out vec2 tCoord;
            void main()
            {
                tCoord = texcoord;
                gl_Position = vec4(position, 0.0, 1.0);
            }
        """)
        GL20.glCompileShader(vertShader)
        val fragShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER)
        GL20.glShaderSource(fragShader, """
            #version 130
            in vec2 tCoord;
            out vec4 outColor;
            uniform sampler2D tex;
            void main()
            {
                vec4 texColor = texture(tex, tCoord);
                outColor = vec4(texColor.xyz, 1.0);
            }
        """)
        GL20.glCompileShader(fragShader)
        val program = GL20.glCreateProgram()
        GL20.glAttachShader(program, vertShader)
        GL20.glAttachShader(program, fragShader)
        // TODO: transform to getAttrLocation
        GL20.glBindAttribLocation(program, 0, "position")
        GL20.glBindAttribLocation(program, 1, "texcoord")
        GL20.glLinkProgram(program)
        GL20.glUniform1i(GL20.glGetUniformLocation(program, "tex"), 0)
        // init buffers TODO move to class body
        val points = floatArrayOf(
                -1f, -1f, 0f, 0f,
                1f, -1f, 1f, 0f,
                1f, 1f, 1f, 1f,
                -1f, 1f, 0f, 1f
        )
        val buff = BufferUtils.createFloatBuffer(points.size)
        buff.put(points)
        buff.flip()
        val vbo = GL15.glGenBuffers()
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buff, GL15.GL_STATIC_DRAW)

        val vao = GL30.glGenVertexArrays()
        GL30.glBindVertexArray(vao)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 4 * 4, 0)
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 4 * 4, 4 * 2)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
        GL30.glBindVertexArray(0)

        // make texture
        tex = GL11.glGenTextures()
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex)
        val texBuff = BufferUtils.createByteBuffer(windowWidth * windowHeight * 3)
        texBuff.flip()

        GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D,
                0,
                GL11.GL_RGB,
                windowWidth,
                windowHeight,
                0,
                GL11.GL_RGB,
                GL11.GL_UNSIGNED_BYTE,
                texBuff)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        var dt = .0
        while (!glfwWindowShouldClose(windowHandle)) {
            // execute frame processing and return delta time
            val dt_nano = measureNanoTime {
                clearSurface()
                GL13.glActiveTexture(GL13.GL_TEXTURE0)
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex)
                GL20.glUniform1i(GL20.glGetUniformLocation(program, "tex"), 0)

                GL20.glUseProgram(program)
                GL30.glBindVertexArray(vao)
                GL20.glEnableVertexAttribArray(0)
                GL20.glEnableVertexAttribArray(1)
                GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, 4)
                GL20.glEnableVertexAttribArray(1)
                GL20.glDisableVertexAttribArray(0)

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
     * @param handler handler of application loop, implementor of [AppHandler]
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



