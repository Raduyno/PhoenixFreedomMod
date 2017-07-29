package me.unraveledmc.unraveledmcmod.discord;

import me.unraveledmc.unraveledmcmod.discord.MessageListener;
import me.unraveledmc.unraveledmcmod.util.FLog;
import me.unraveledmc.unraveledmcmod.staff.StaffMember;
import me.unraveledmc.unraveledmcmod.config.ConfigEntry;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.MessageChannel;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import javax.security.auth.login.LoginException;
import me.unraveledmc.unraveledmcmod.FreedomService;
import me.unraveledmc.unraveledmcmod.UnraveledMCMod;

public class Discord extends FreedomService
{
    public static HashMap<String, StaffMember> LINK_CODES = new HashMap<>();
    public static List<String> VERIFY_CODES = new ArrayList();
    public static JDA bot = null;
    public static Boolean enabled = false;

    public Discord(UnraveledMCMod plugin)
    {
        super(plugin);
    }

     public void startBot()
     {
        if (ConfigEntry.DISCORD_VERIFICATION_ENABLED.getBoolean())
        {
            if (!ConfigEntry.DISCORD_VERIFICATION_BOT_TOKEN.getString().isEmpty())
            {
                enabled = true;
            }
            else
            {
                FLog.warning("No bot token was specified in the config, discord verification bot will not enable.");
            }
        }
        if (bot != null)
        {
            for (Object o : bot.getRegisteredListeners())
            {
               bot.removeEventListener(o);
            }
        }
        try
        {
            if (enabled)
            {
                bot = new JDABuilder().setBotToken(ConfigEntry.DISCORD_VERIFICATION_BOT_TOKEN.getString()).addListener(new MessageListener()).setAudioEnabled(false).setAutoReconnect(true).buildBlocking();
                FLog.info("Discord verification bot has successfully enabled!");
            }
        }
        catch (LoginException e)
        {
            FLog.warning("An invalid token for the discord verification bot, the bot will not enable.");
        }
        catch (IllegalArgumentException | InterruptedException e)
        {
            FLog.warning("Discord verification bot failed to start.");
        }
    }

    @Override
    protected void onStart()
    {
        startBot();
    }
    
    public static void sendMessage(MessageChannel channel, String message)
    {
        channel.sendMessage(message);
    }
    
    public static String getCodeForAdmin(StaffMember staffMember)
    {
        for (String code: LINK_CODES.keySet())
        {
            if (LINK_CODES.get(code).equals(staffMember))
            {
                return code;
            }
        }
        return null;
    }

    @Override
    protected void onStop()
    {
        if (bot != null)
        {
            bot.shutdown();
        }
        FLog.info("Discord verification bot has successfully shutdown.");
    }
}
