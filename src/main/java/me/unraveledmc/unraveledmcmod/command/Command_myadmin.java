package me.unraveledmc.unraveledmcmod.command;

import java.util.Arrays;
import me.unraveledmc.unraveledmcmod.staff.StaffMember;
import me.unraveledmc.unraveledmcmod.rank.Rank;
import me.unraveledmc.unraveledmcmod.util.FUtil;
import net.pravian.aero.util.Ips;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = Rank.OP, source = SourceType.BOTH)
@CommandParameters(description = "Manage your staff member entry", usage = "/<command> [-o <staff member>] <clearips | clearip <ip> | setlogin <message> | clearlogin | setshoutcolor | settag | cleartag>")
public class Command_myadmin extends FreedomCommand
{

    @Override
    protected boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        checkPlayer();
        checkRank(Rank.MOD);

        if (args.length < 1)
        {
            return false;
        }

        Player init = null;
        StaffMember target = getStaffMember(playerSender);
        Player targetPlayer = playerSender;
        String targetIp = Ips.getIp(targetPlayer);

        // -o switch
        if (args[0].equals("-o"))
        {
            checkRank(Rank.SENIOR_ADMIN);
            init = playerSender;
            targetPlayer = getPlayer(args[1]);
            if (targetPlayer == null)
            {
                msg(FreedomCommand.PLAYER_NOT_FOUND);
                return true;
            }
            target = getStaffMember(targetPlayer);
            if (target == null)
            {
                msg("That player is not a staff member", ChatColor.RED);
                return true;
            }

            // Shift 2
            args = Arrays.copyOfRange(args, 2, args.length);
            if (args.length < 1)
            {
                return false;
            }
        }

        switch (args[0])
        {
            case "clearips":
            {
                if (args.length != 1)
                {
                    return false; // Double check: the player might mean "clearip"
                }

                if (init == null)
                {
                    FUtil.staffAction(sender.getName(), "Clearing my IPs", true);
                }
                else
                {
                    FUtil.staffAction(sender.getName(), "Clearing " + target.getName() + "' IPs", true);
                }

                int counter = target.getIps().size() - 1;
                target.clearIPs();
                target.addIp(targetIp);

                plugin.al.save();

                msg(counter + " IPs removed.");
                msg(targetPlayer, target.getIps().get(0) + " is now your only IP address");
                return true;
            }

            case "clearip":
            {
                if (args.length != 2)
                {
                    return false; // Double check: the player might mean "clearips"
                }

                if (!target.getIps().contains(args[1]))
                {
                    if (init == null)
                    {
                        msg("That IP is not registered to you.");
                    }
                    else
                    {
                        msg("That IP does not belong to that player.");
                    }
                    return true;
                }

                if (targetIp.equals(args[1]))
                {
                    if (init == null)
                    {
                        msg("You cannot remove your current IP.");
                    }
                    else
                    {
                        msg("You cannot remove that staff member's current IP.");
                    }
                    return true;
                }

                FUtil.staffAction(sender.getName(), "Removing an IP" + (init == null ? "" : " from " + targetPlayer.getName() + "'s IPs"), true);

                target.removeIp(args[1]);
                plugin.al.save();
                plugin.al.updateTables();

                msg("Removed IP " + args[1]);
                msg("Current IPs: " + StringUtils.join(target.getIps(), ", "));
                return true;
            }

            case "setlogin":
            {
                if (args.length < 2)
                {
                    return false;
                }

                String msg = StringUtils.join(args, " ", 1, args.length);
                FUtil.staffAction(sender.getName(), "Setting personal login message" + (init == null ? "" : " for " + targetPlayer.getName()), false);
                target.setLoginMessage(msg);
                msg((init == null ? "Your" : targetPlayer.getName() + "'s") + " login message is now: ");
                msg("> " + ChatColor.AQUA + targetPlayer.getName() + " is " + FUtil.colorize(target.getLoginMessage()));
                plugin.al.save();
                plugin.al.updateTables();
                return true;
            }

            case "clearlogin":
            {
                FUtil.staffAction(sender.getName(), "Clearing personal login message" + (init == null ? "" : " for " + targetPlayer.getName()), false);
                target.setLoginMessage(null);
                plugin.al.save();
                plugin.al.updateTables();
                return true;
            }
            
            case "setshoutcolor":
            {
                if (args.length < 2)
                {
                    return false;
                }

                if (!FUtil.isExecutive(target.getName()))
                {
                    msg("Only executives can set custom shout colors!", ChatColor.RED);
                    return true;
                }
                else
                {
                    FUtil.staffAction(sender.getName(), "Setting shoutcolor" + (init == null ? "" : " for " + targetPlayer.getName()), false);
                    target.setShoutColor(args[1]);
                    plugin.al.save();
                    plugin.al.updateTables();
                    return true;
                }
            }
            
            case "settag":
            {
                FUtil.staffAction(sender.getName(), "Setting personal default tag" + (init == null ? "" : " for " + targetPlayer.getName()), false);
                String tag = StringUtils.join(args, " ", 1, args.length);
                target.setTag(tag);
                msg((init == null ? "Your" : targetPlayer.getName() + "'s") + " default tag is now: " + FUtil.colorize(target.getTag()));
                plugin.al.save();
                plugin.al.updateTables();
                return true;
            }
            
            case "cleartag":
            {
                FUtil.staffAction(sender.getName(), "Clearing personal default tag" + (init == null ? "" : " for " + targetPlayer.getName()), false);
                String tag = StringUtils.join(args, " ", 1, args.length);
                target.setTag(null);
                plugin.al.save();
                plugin.al.updateTables();
                return true;
            }

            default:
            {
                return false;
            }
        }
    }

}
