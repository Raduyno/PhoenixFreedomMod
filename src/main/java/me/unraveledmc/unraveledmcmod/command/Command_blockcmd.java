package me.unraveledmc.unraveledmcmod.command;

import me.unraveledmc.unraveledmcmod.player.FPlayer;
import me.unraveledmc.unraveledmcmod.rank.Rank;
import me.unraveledmc.unraveledmcmod.util.FUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.MOD, source = SourceType.BOTH)
@CommandParameters(description = "Block all commands for a specific player.", usage = "/<command> <-a | purge | <player>>", aliases = "blockcommands,blockcommand,bc,bcmd")
public class Command_blockcmd extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length != 1)
        {
            return false;
        }

        if (args[0].equals("purge"))
        {
            FUtil.staffAction(sender.getName(), "Unblocking commands for all players", true);
            int counter = 0;
            for (Player player : server.getOnlinePlayers())
            {
                FPlayer playerdata = plugin.pl.getPlayer(player);
                if (playerdata.allCommandsBlocked())
                {
                    counter += 1;
                    playerdata.setCommandsBlocked(false);
                }
            }
            msg("Unblocked commands for " + counter + " players.");
            return true;
        }

        if (args[0].equals("-a"))
        {
            FUtil.staffAction(sender.getName(), "Blocking commands for all non-staff", true);
            int counter = 0;
            for (Player player : server.getOnlinePlayers())
            {
                if (isStaffMember(player))
                {
                    continue;
                }

                counter += 1;
                plugin.pl.getPlayer(player).setCommandsBlocked(true);
                msg(player, "Your commands have been blocked by " + sender.getName(), ChatColor.RED);
            }

            msg("Blocked commands for " + counter + " players.");
            return true;
        }

        final Player player = getPlayer(args[0]);

        if (player == null)
        {
            msg(FreedomCommand.PLAYER_NOT_FOUND);
            return true;
        }

        if (isStaffMember(player))
        {
            msg(player.getName() + " is a staff member and cannot have their commands blocked.");
            return true;
        }

        FPlayer playerdata = plugin.pl.getPlayer(player);

        playerdata.setCommandsBlocked(!playerdata.allCommandsBlocked());

        FUtil.staffAction(sender.getName(), (playerdata.allCommandsBlocked() ? "B" : "Unb") + "locking all commands for " + player.getName(), true);
        msg((playerdata.allCommandsBlocked() ? "B" : "Unb") + "locked all commands.");

        return true;
    }
}
