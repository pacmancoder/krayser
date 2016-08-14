package org.krayser.frontend

/**
 * Application handler interface
 *
 * It used by frontend object to make platform-agnostic things
 */
interface AppHandler {

    /**
     * Invoked by application class on start
     */

    fun init(frontend: Frontend)
    /**
     * Invoked by application class on each
     * frame processing
     * @param dt *delta-time*, difference between previous and current frame in *ms*
     */
    fun proc(frontend: Frontend, dt: Double)

    /**
     * Invoked by application class on exiting action. Use it to finalize all resources
     */
    fun exit(frontend: Frontend)
}