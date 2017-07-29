package me.unraveledmc.unraveledmcmod;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import me.unraveledmc.unraveledmcmod.config.ConfigEntry;
import me.unraveledmc.unraveledmcmod.util.FUtil;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Announcer extends FreedomService
{

    private final List<String> announcements = Lists.newArrayList();
    @Getter
    private boolean enabled;
    @Getter
    private long interval;
    @Getter
    private String prefix;
    private BukkitTask announcer;

    public Announcer(UnraveledMCMod plugin)
    {
        super(plugin);
    }

    @Override
    protected void onStart()
    {
        enabled = ConfigEntry.ANNOUNCER_ENABLED.getBoolean();
        interval = ConfigEntry.ANNOUNCER_INTERVAL.getInteger() * 20L;
        prefix = FUtil.colorize(ConfigEntry.ANNOUNCER_PREFIX.getString());

        announcements.clear();
        for (Object announcement : ConfigEntry.ANNOUNCER_ANNOUNCEMENTS.getList())
        {
            announcements.add(FUtil.colorize((String) announcement));
        }

        if (!enabled)
        {
            return;
        }

        announcer = new BukkitRunnable()
        {
            private int current = 0;

            @Override
            public void run()
            {
                current++;

                if (current >= announcements.size())
                {
                    current = 0;
                }
                StringBuilder builder = new StringBuilder();
                String[] currentAnnouncement = announcements.get(current).split("\\\\n");
                for (int i = 0; i < currentAnnouncement.length; i++)
                {
                    builder.append(currentAnnouncement[i]);
                    if (i != currentAnnouncement.length)
                    {
                        builder.append("\n");
                    }
                }
                String announcement = builder.toString();

                announce(announcement);
            }
        }.runTaskTimer(plugin, interval, interval);
    }

    @Override
    protected void onStop()
    {
        if (announcer == null)
        {
            return;
        }

        FUtil.cancel(announcer);
        announcer = null;
    }

    public List<String> getAnnouncements()
    {
        return Collections.unmodifiableList(announcements);
    }

    public void announce(String message)
    {
        FUtil.bcastMsg(prefix + FUtil.colorize(message));
    }

}
