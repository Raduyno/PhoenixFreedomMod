package me.unraveledmc.unraveledmcmod.command;

import java.util.ArrayList;
import java.util.List;
import me.unraveledmc.unraveledmcmod.rank.Rank;
import me.unraveledmc.unraveledmcmod.util.FUtil;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

@CommandPermissions(level = Rank.MOD, source = SourceType.BOTH)
@CommandParameters(description = "Shows (optionally smites) invisisible players", usage = "/<command> (smite)")
public class Command_invis extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        boolean smite = false;
        if (args.length >= 1)
        {
            if (args[0].equalsIgnoreCase("smite"))
            {
                FUtil.staffAction(sender.getName(), "Smiting all invisible players", true);
                smite = true;
            }
            else
            {
                return false;
            }
        }

        List<String> players = new ArrayList<>();
        int smites = 0;

        for (Player player : server.getOnlinePlayers())
        {
            if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
            {
                players.add(player.getName());
                if (smite && !plugin.al.isStaffMember(player))
                {
                    player.setHealth(0.0);
                    smites++;
                }
            }
        }

        if (players.isEmpty())
        {
            msg("There are no invisible players");
            return true;
        }

        if (smite)
        {
            msg("Smitten " + smites + " players");
        }
        else
        {
            msg("Invisible players (" + players.size() + "): " + StringUtils.join(players, ", "));
        }

        return true;
    }
}
