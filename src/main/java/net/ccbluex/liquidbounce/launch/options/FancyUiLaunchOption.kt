package net.ccbluex.liquidbounce.launch.options

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.launch.EnumLaunchFilter
import net.ccbluex.liquidbounce.launch.LaunchFilterInfo
import net.ccbluex.liquidbounce.launch.LaunchOption
import net.ccbluex.liquidbounce.launch.data.ultralight.BrowseCommand
import net.ccbluex.liquidbounce.launch.data.ultralight.ClickGUIModule
import net.minecraft.client.gui.GuiMainMenu

@LaunchFilterInfo([EnumLaunchFilter.FANCY_UI])
class FancyUiLaunchOption : LaunchOption() {
    override fun start() {
//        UltralightEngine.initResources()
//        UltralightEngine.initEngine()

        LiquidBounce.mainMenu = GuiMainMenu()

        LiquidBounce.commandManager.registerCommand(BrowseCommand())
        LiquidBounce.moduleManager.registerModule(ClickGUIModule)
    }
}