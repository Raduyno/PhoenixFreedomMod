package me.unraveledmc.unraveledmcmod;

import me.unraveledmc.unraveledmcmod.fun.Trailer;
import java.io.File;
import me.unraveledmc.unraveledmcmod.staff.StaffList;
import me.unraveledmc.unraveledmcmod.banning.BanManager;
import me.unraveledmc.unraveledmcmod.banning.PermbanList;
import me.unraveledmc.unraveledmcmod.blocking.BlockBlocker;
import me.unraveledmc.unraveledmcmod.blocking.DisguiseBlocker;
import me.unraveledmc.unraveledmcmod.blocking.EventBlocker;
import me.unraveledmc.unraveledmcmod.blocking.InteractBlocker;
import me.unraveledmc.unraveledmcmod.blocking.MobBlocker;
import me.unraveledmc.unraveledmcmod.blocking.PotionBlocker;
import me.unraveledmc.unraveledmcmod.blocking.command.CommandBlocker;
import me.unraveledmc.unraveledmcmod.bridge.BukkitTelnetBridge;
import me.unraveledmc.unraveledmcmod.bridge.CoreProtectBridge;
import me.unraveledmc.unraveledmcmod.bridge.EssentialsBridge;
import me.unraveledmc.unraveledmcmod.bridge.LibsDisguisesBridge;
import me.unraveledmc.unraveledmcmod.bridge.WorldEditBridge;
import me.unraveledmc.unraveledmcmod.caging.Cager;
import me.unraveledmc.unraveledmcmod.command.CommandLoader;
import me.unraveledmc.unraveledmcmod.config.MainConfig;
import me.unraveledmc.unraveledmcmod.discord.Discord;
import me.unraveledmc.unraveledmcmod.freeze.Freezer;
import me.unraveledmc.unraveledmcmod.fun.ItemFun;
import me.unraveledmc.unraveledmcmod.fun.Jumppads;
import me.unraveledmc.unraveledmcmod.fun.Landminer;
import me.unraveledmc.unraveledmcmod.fun.Lightning;
import me.unraveledmc.unraveledmcmod.fun.CrescentRose;
import me.unraveledmc.unraveledmcmod.fun.MP44;
import me.unraveledmc.unraveledmcmod.fun.Minigun;
import me.unraveledmc.unraveledmcmod.httpd.HTTPDaemon;
import me.unraveledmc.unraveledmcmod.leveling.LevelManager;
import me.unraveledmc.unraveledmcmod.player.PlayerList;
import me.unraveledmc.unraveledmcmod.rank.RankManager;
import me.unraveledmc.unraveledmcmod.shop.Shop;
import me.unraveledmc.unraveledmcmod.shop.ShopGUIListener;
import me.unraveledmc.unraveledmcmod.util.FLog;
import me.unraveledmc.unraveledmcmod.util.FUtil;
import me.unraveledmc.unraveledmcmod.util.MethodTimer;
import me.unraveledmc.unraveledmcmod.world.WorldManager;
import net.pravian.aero.component.service.ServiceManager;
import net.pravian.aero.plugin.AeroPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class UnraveledMCMod extends AeroPlugin<UnraveledMCMod>
{

    public static final String CONFIG_FILENAME = "config.yml";
    //
    public static String pluginName;
    public static String pluginVersion = "2.0.3";
    public static String buildDate = "29/07/2017";
    public static String compiledBy = "Raduino";
    //
    public MainConfig config;
    //
    // Services
    public ServiceManager<UnraveledMCMod> services;
    public ServerInterface si;
    public SavedFlags sf;
    public WorldManager wm;
    public StaffList al;
    public RankManager rm;
    public CommandLoader cl;
    public CommandBlocker cb;
    public EventBlocker eb;
    public BlockBlocker bb;
    public DisguiseBlocker db;
    public MobBlocker mb;
    public InteractBlocker ib;
    public PotionBlocker pb;
    public LoginProcess lp;
    public AntiNuke nu;
    public AntiSpam as;
    public AntiSpamBot asb;
    public PlayerList pl;
    public Shop sh;
    public ShopGUIListener sl;
    public Announcer an;
    public ChatManager cm;
    public Data da;
    public Discord dc;
    public BanManager bm;
    public PermbanList pm;
    public ProtectArea pa;
    public ServiceChecker sc;
    public GameRuleHandler gr;
    public CommandSpy cs;
    public Cager ca;
    public Freezer fm;
    public Orbiter or;
    public Muter mu;
    public Fuckoff fo;
    public AutoKick ak;
    public MovementValidator mv;
    public EntityWiper ew;
    public ServerPing sp;
    public ItemFun it;
    public Landminer lm;
    public MP44 mp;
    public Minigun mg;
    public Jumppads jp;
    public Trailer tr;
    public HTTPDaemon hd;
    public Lightning ln;
    public CrescentRose cr;
    public LevelManager lvm;
    //
    // Bridges
    public ServiceManager<UnraveledMCMod> bridges;
    public BukkitTelnetBridge btb;
    public CoreProtectBridge cpb;
    public EssentialsBridge esb;
    public LibsDisguisesBridge ldb;
    public WorldEditBridge web;

    @Override
    public void load()
    {
        UnraveledMCMod.pluginName = plugin.getDescription().getName();

        FLog.setPluginLogger(plugin.getLogger());
        FLog.setServerLogger(server.getLogger());
    }

    @Override
    public void enable()
    {
        FLog.info("Created by Madgeek1450 and Prozza");
        FLog.info("Modified by CreeperSeth, AwesomePinch, and aggelosQQ");
        FLog.info("Version " + pluginVersion);

        final MethodTimer timer = new MethodTimer();
        timer.start();

        // Warn if we're running on a wrong version
        ServerInterface.warnVersion();

        // Delete unused files
        FUtil.deleteCoreDumps();
        FUtil.deleteFolder(new File("./_deleteme"));

        // Convert old config files
        new ConfigConverter(plugin).convert();

        BackupManager backups = new BackupManager(this);
        backups.createBackups(UnraveledMCMod.CONFIG_FILENAME, true);
        backups.createBackups(StaffList.CONFIG_FILENAME);
        backups.createBackups(PermbanList.CONFIG_FILENAME);

        config = new MainConfig(this);
        config.load();

        // Start services
        services = new ServiceManager<>(plugin);
        si = services.registerService(ServerInterface.class);
        sf = services.registerService(SavedFlags.class);
        wm = services.registerService(WorldManager.class);
        al = services.registerService(StaffList.class);
        rm = services.registerService(RankManager.class);
        lvm = services.registerService(LevelManager.class);
        cl = services.registerService(CommandLoader.class);
        cb = services.registerService(CommandBlocker.class);
        eb = services.registerService(EventBlocker.class);
        bb = services.registerService(BlockBlocker.class);
        db = services.registerService(DisguiseBlocker.class);
        mb = services.registerService(MobBlocker.class);
        ib = services.registerService(InteractBlocker.class);
        pb = services.registerService(PotionBlocker.class);
        lp = services.registerService(LoginProcess.class);
        nu = services.registerService(AntiNuke.class);
        as = services.registerService(AntiSpam.class);
        asb = services.registerService(AntiSpamBot.class);

        pl = services.registerService(PlayerList.class);
        sh = services.registerService(Shop.class);
        sl = services.registerService(ShopGUIListener.class);
        an = services.registerService(Announcer.class);
        cm = services.registerService(ChatManager.class);
        da = services.registerService(Data.class);
        dc = services.registerService(Discord.class);
        bm = services.registerService(BanManager.class);
        pm = services.registerService(PermbanList.class);
        pa = services.registerService(ProtectArea.class);
        sc = services.registerService(ServiceChecker.class);
        gr = services.registerService(GameRuleHandler.class);

        // Single admin utils
        cs = services.registerService(CommandSpy.class);
        ca = services.registerService(Cager.class);
        fm = services.registerService(Freezer.class);
        or = services.registerService(Orbiter.class);
        mu = services.registerService(Muter.class);
        fo = services.registerService(Fuckoff.class);
        ak = services.registerService(AutoKick.class);

        mv = services.registerService(MovementValidator.class);
        ew = services.registerService(EntityWiper.class);
        sp = services.registerService(ServerPing.class);

        // Fun
        it = services.registerService(ItemFun.class);
        lm = services.registerService(Landminer.class);
        ln = services.registerService(Lightning.class);
        cr = services.registerService(CrescentRose.class);
        mp = services.registerService(MP44.class);
        mg = services.registerService(Minigun.class);
        jp = services.registerService(Jumppads.class);
        tr = services.registerService(Trailer.class);

        // HTTPD
        hd = services.registerService(HTTPDaemon.class);
        services.start();

        // Start bridges
        bridges = new ServiceManager<>(plugin);
        btb = bridges.registerService(BukkitTelnetBridge.class);
        cpb = bridges.registerService(CoreProtectBridge.class);
        esb = bridges.registerService(EssentialsBridge.class);
        ldb = bridges.registerService(LibsDisguisesBridge.class);
        web = bridges.registerService(WorldEditBridge.class);
        bridges.start();

        timer.update();
        FLog.info("Version " + pluginVersion + " for " + ServerInterface.COMPILE_NMS_VERSION + " enabled in " + timer.getTotal() + "ms");

        // Add spawnpoints later - https://github.com/TotalFreedom/TotalFreedomMod/issues/438
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                plugin.pa.autoAddSpawnpoints();
            }
        }.runTaskLater(plugin, 60L);
    }

    @Override
    public void disable()
    {
        // Stop services and bridges
        bridges.stop();
        services.stop();

        server.getScheduler().cancelTasks(plugin);

        FLog.info("Plugin disabled");
    }

    public static UnraveledMCMod plugin()
    {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
        {
            if (plugin.getName().equalsIgnoreCase(pluginName))
            {
                return (UnraveledMCMod) plugin;
            }
        }
        return null;
    }

}
