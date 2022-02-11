package net.ccbluex.liquidbounce.launch.options

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.launch.EnumLaunchFilter
import net.ccbluex.liquidbounce.launch.LaunchFilterInfo
import net.ccbluex.liquidbounce.launch.LaunchOption
import net.ccbluex.liquidbounce.launch.data.fancyui.BrowseCommand
import net.ccbluex.liquidbounce.launch.data.fancyui.ClickGUIModule
import net.ccbluex.liquidbounce.launch.data.fancyui.GuiPrepare
import net.ccbluex.liquidbounce.ui.cef.CefRenderManager
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiMainMenu
import java.io.File

@LaunchFilterInfo([EnumLaunchFilter.FANCY_UI])
class FancyUiLaunchOption : LaunchOption() {
    override fun start() {
        // old cache dir will be auto deleted cuz we changed the allowed folder in FileManager

        // start loading
        val progressHandler = GuiPrepare.DynamicProgressHandler()
        CefRenderManager.initializeAsync(progressHandler)

        // display loading screen
        val mc = Minecraft.getMinecraft()
        mc.displayGuiScreen(GuiPrepare(progressHandler) {
            // I think LiquidBounce.mainMenu must be initialized here
            mc.displayGuiScreen(LiquidBounce.mainMenu)
        })

        LiquidBounce.mainMenu = GuiMainMenu()

        LiquidBounce.commandManager.registerCommand(BrowseCommand())
        LiquidBounce.moduleManager.registerModule(ClickGUIModule)
    }

    override fun stop() {
        CefRenderManager.stop()
    }
}