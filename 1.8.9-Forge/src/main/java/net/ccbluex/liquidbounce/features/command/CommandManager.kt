package net.ccbluex.liquidbounce.features.command

import net.ccbluex.liquidbounce.features.command.commands.*
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * LiquidBounce Hacked Client
 * A minecraft forge injection client using Mixin
 *
 * @game Minecraft
 * @author CCBlueX
 */
@SideOnly(Side.CLIENT)
class CommandManager {

    val commands = mutableListOf<Command>()

    var prefix = '.'

    /**
     * Register all default commands
     */
    fun registerCommands() {
        registerCommand(BindCommand())
        registerCommand(VClipCommand())
        registerCommand(HClipCommand())
        registerCommand(HelpCommand())
        registerCommand(SayCommand())
        registerCommand(FriendCommand())
        registerCommand(AutoSettingsCommand())
        registerCommand(LocalAutoSettingsCommand())
        registerCommand(ServerInfoCommand())
        registerCommand(ToggleCommand())
        registerCommand(HurtCommand())
        registerCommand(GiveCommand())
        registerCommand(UsernameCommand())
        registerCommand(TargetCommand())
        registerCommand(TacoCommand())
        registerCommand(BindsCommand())
        registerCommand(HoloStandCommand())
        registerCommand(PanicCommand())
        registerCommand(PingCommand())
        registerCommand(RenameCommand())
        registerCommand(EnchantCommand())
        registerCommand(ReloadCommand())
        registerCommand(LoginCommand())
        registerCommand(ScriptManagerCommand())
        registerCommand(RemoteViewCommand())
        registerCommand(PrefixCommand())
        registerCommand(ShortcutCommand())
    }

    /**
     * Execute command by given [input]
     */
    fun executeCommands(input: String) {
        for (command in commands) {
            val args = input.split(" ").toTypedArray()

            if (args[0].equals(prefix.toString() + command.command, ignoreCase = true)) {
                command.execute(args)
                return
            }

            for (alias in command.alias) {
                if (!args[0].equals(prefix.toString() + alias, ignoreCase = true))
                    continue

                command.execute(args)
                return
            }
        }
    }

    /**
     * Get command instance by given [name]
     */
    fun getCommand(name: String): Command? {
        return commands.find { it.command.equals(name, ignoreCase = true) }
    }

    /**
     * Register [command] by just adding it to the commands registry
     */
    fun registerCommand(command: Command) = commands.add(command)

    fun registerShortcut(name: String, script: String) {
        if (getCommand(name) == null) {
            registerCommand(Shortcut(name, script.split(';').map {
                val args = it.trim().split(' ')

                val command = getCommand(args[0]) ?: throw IllegalArgumentException("Command ${args[0]} not found!")

                Pair(command, args.toTypedArray())
            }))
        } else {
            throw IllegalArgumentException("Command already exists!")
        }
    }

    /**
     * Unregister [command] by just removing it from the commands registry
     */
    fun unregisterCommand(command: Command?) = commands.remove(command)

    fun unregisterCommand(name: String) = commands.removeIf { it.command.equals(name, ignoreCase = true) }

}
