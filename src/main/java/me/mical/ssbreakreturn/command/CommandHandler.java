package me.mical.ssbreakreturn.command;

import me.mical.ssbreakreturn.SSBreakReturn;
import me.mical.ssbreakreturn.command.subcommand.DelCommand;
import me.mical.ssbreakreturn.command.subcommand.SelectCommand;
import org.serverct.parrot.parrotx.api.ParrotXAPI;
import org.serverct.parrot.parrotx.command.subcommands.DebugCommand;
import org.serverct.parrot.parrotx.command.subcommands.HelpCommand;
import org.serverct.parrot.parrotx.command.subcommands.ReloadCommand;
import org.serverct.parrot.parrotx.command.subcommands.VersionCommand;
import org.serverct.parrot.parrotx.data.autoload.Autoload;

@Autoload
public class CommandHandler extends org.serverct.parrot.parrotx.command.CommandHandler {

    public CommandHandler() {
        super(ParrotXAPI.getPlugin(SSBreakReturn.class), "ssbreakreturn");
        register(new ReloadCommand(plugin, "SSBreakReturn.admin"));
        register(new DebugCommand(plugin, "SSBreakReturn.admin"));
        register(new HelpCommand(plugin));
        register(new VersionCommand(plugin));
        register(new DelCommand(plugin, "del", 0));
        register(new SelectCommand(plugin, "select", 0));
    }
}
