package me.unraveledmc.unraveledmcmod.command;

import java.util.ArrayList;
import java.util.List;
import me.unraveledmc.unraveledmcmod.rank.Rank;
import me.unraveledmc.unraveledmcmod.util.FUtil;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH)
@CommandParameters(description = "Quick Op - op someone based on a partial name.", usage = "/<command> <partialname>")
public class Command_qop extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length < 1)
        {
            return false;
        }

        boolean silent = false;
        if (args.length == 2)
        {
            silent = args[1].equalsIgnoreCase("-s");
        }

        final String targetName = args[0].toLowerCase();

        final List<String> matchedPlayerNames = new ArrayList<>();
        for (final Player player : server.getOnlinePlayers())
        {
            if (player.getName().toLowerCase().contains(targetName) || player.getDisplayName().toLowerCase().contains(targetName))
            {
                if (!player.isOp())
                {
                    if (!plugin.al.isStaffMember(sender) && plugin.da.isAdminDeopped(player.getName()))
                    {
                        msg("You can not op that player because that player has been deopped by an administrator.", ChatColor.RED);
                    }
                    matchedPlayerNames.add(player.getName());
                    player.setOp(true);
                    plugin.da.setAdminDeopped(player.getName(), false);
                    player.sendMessage(FreedomCommand.YOU_ARE_OP);
                }
            }
        }

        if (!matchedPlayerNames.isEmpty())
        {
            if (!silent)
            {
                FUtil.staffAction(sender.getName(), "Opping " + StringUtils.join(matchedPlayerNames, ", "), false);
            }
        }
        else
        {
            msg("No targets matched.");
        }

        return true;
    }
}
