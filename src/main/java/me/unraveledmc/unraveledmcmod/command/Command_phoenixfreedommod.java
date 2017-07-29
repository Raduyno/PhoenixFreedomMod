package me.unraveledmc.unraveledmcmod.command;

import me.unraveledmc.unraveledmcmod.UnraveledMCMod;
import me.unraveledmc.unraveledmcmod.config.ConfigEntry;
import me.unraveledmc.unraveledmcmod.rank.Rank;
import me.unraveledmc.unraveledmcmod.util.FLog;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.NON_OP, source = SourceType.BOTH)
@CommandParameters(description = "Shows information about UnraveledMCMod or reloads it.", usage = "/<command> [reload]", aliases = "umcm")
public class Command_phoenixfreedommod extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 1)
        {
            if (!args[0].equals("reload"))
            {
                return false;
            }

            if (!plugin.al.isStaffMember(sender))
            {
                noPerms();
                return true;
            }

            plugin.config.load();
            plugin.services.stop();
            plugin.services.start();

            final String message = String.format("%s v%s reloaded.",
                    UnraveledMCMod.pluginName,
                    UnraveledMCMod.pluginVersion);

            msg(message);
            FLog.info(message);
            return true;
        }
        msg("PhoenixFreedomMod for 'PhoenixFreedom', an all-op server.", ChatColor.GOLD);
        msg("Version: " + UnraveledMCMod.pluginVersion, ChatColor.GOLD);
        msg("Compiled by: " + UnraveledMCMod.compiledBy, ChatColor.GOLD);
        msg("Compile date: " + UnraveledMCMod.buildDate, ChatColor.GOLD);
        msg("Running on " + ConfigEntry.SERVER_NAME.getString() + ".", ChatColor.GOLD);
        msg("Created by Raduino.", ChatColor.GOLD);
        msg("Visit " + ChatColor.AQUA + "http://phoenixmc.gq" + ChatColor.GREEN + " for more information.", ChatColor.GREEN);
        return true;
    }
}
