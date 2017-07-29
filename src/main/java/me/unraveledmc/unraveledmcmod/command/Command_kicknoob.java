package me.unraveledmc.unraveledmcmod.command;

import me.unraveledmc.unraveledmcmod.rank.Rank;
import me.unraveledmc.unraveledmcmod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.SENIOR_ADMIN, source = SourceType.BOTH, blockHostConsole = true)
@CommandParameters(description = "Kick all non-staff on server.", usage = "/<command>")
public class Command_kicknoob extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        FUtil.staffAction(sender.getName(), "Disconnecting all non-staff.", true);

        for (Player player : server.getOnlinePlayers())
        {
            if (!plugin.al.isStaffMember(player))
            {
                player.kickPlayer(ChatColor.RED + "All non-staff were kicked by " + sender.getName() + ".");
            }
        }

        return true;
    }
}
