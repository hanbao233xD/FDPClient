package net.ccbluex.liquidbounce.ui.cef

import me.friwi.jcefmaven.CefAppBuilder
import me.friwi.jcefmaven.EnumProgress
import me.friwi.jcefmaven.IProgressHandler
import me.friwi.jcefmaven.impl.step.check.CefInstallationChecker
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.utils.ClientUtils
import org.cef.CefApp
import org.cef.CefClient
import java.io.File
import java.util.logging.Logger
import kotlin.concurrent.thread


object CefRenderManager {

    lateinit var cefApp: CefApp
        private set
    lateinit var cefClient: CefClient
        private set

    val dataDir = File(LiquidBounce.fileManager.cacheDir, "cef")
    val cefArgs = arrayOf("--disable-gpu")

    fun initializeAsync(progressHandler: IProgressHandler?) {
        thread {
            initialize(progressHandler)
        }
    }

    fun initialize(progressHandler: IProgressHandler?) {
        if(!dataDir.exists()) {
            dataDir.mkdirs()
        }

        // use jcef maven CefAppBuilder, it can download resources automatically
        val builder = CefAppBuilder()

        builder.setInstallDir(dataDir)
        if(progressHandler != null) builder.setProgressHandler(progressHandler)
        builder.addJcefArgs(*cefArgs)
        builder.cefSettings.windowless_rendering_enabled = true

        cefApp = builder.build()
        cefClient = cefApp.createClient()

        val version = cefApp.version
        ClientUtils.logInfo("Cef Loaded (jcefVersion=${version.jcefVersion}, cefVersion=${version.cefVersion}, chromeVersion=${version.chromeVersion})")
    }

    fun stop() {
        cefApp.dispose()
    }
}