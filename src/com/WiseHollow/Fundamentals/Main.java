package com.WiseHollow.Fundamentals;

import com.WiseHollow.Fundamentals.Commands.*;
import com.WiseHollow.Fundamentals.Listeners.PlayerEvents;
import com.WiseHollow.Fundamentals.Listeners.SignColorEvents;
import com.WiseHollow.Fundamentals.Tasks.JailTask;
import com.WiseHollow.Fundamentals.Tasks.LagTask;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

public class Main extends JavaPlugin
{
    public static Main plugin = null;
    public static Logger logger = null;
    public static Economy economy = null;
    public static Chat chat = null;

    @Override
    public void onEnable()
    {
        plugin = this;
        logger = this.getLogger();

        logger.info("Registering Economy: " + setupEconomy());
        logger.info("Registering Chat: " + setupChat());

        saveDefaultConfig();
        Settings.Load();

        setupMetrics();

        registerCommands();
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        getServer().getPluginManager().registerEvents(new SignColorEvents(), this);

        PlayerEvents.Refresh();

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new LagTask(), 100L, 1L);

        logger.info(plugin.getName() + " is now enabled!");
    }
    @Override
    public void onDisable()
    {
        JailTask.DisableAll();
        logger.info(plugin.getName() + " is now disabled!");
    }
    private void registerCommands()
    {
        this.getCommand("SetSpawn").setExecutor(new CommandSetSpawn());
        this.getCommand("Spawn").setExecutor(new CommandSpawn());
        this.getCommand("Back").setExecutor(new CommandBack());
        this.getCommand("TP").setExecutor(new CommandTeleport());
        this.getCommand("TPPOS").setExecutor(new CommandTeleportPosition());
        this.getCommand("TPA").setExecutor(new CommandTPA());
        this.getCommand("TPAccept").setExecutor(new CommandTPAccept());
        this.getCommand("TPDeny").setExecutor(new CommandTPDeny());
        this.getCommand("Vanish").setExecutor(new CommandVanish());
        this.getCommand("Msg").setExecutor(new CommandMessage());
        this.getCommand("R").setExecutor(new CommandReply());
        this.getCommand("Time").setExecutor(new CommandTime());
        this.getCommand("Day").setExecutor(new CommandDay());
        this.getCommand("Night").setExecutor(new CommandNight());
        this.getCommand("Weather").setExecutor(new CommandWeather());
        this.getCommand("Heal").setExecutor(new CommandHeal());
        this.getCommand("Kill").setExecutor(new CommandKill());
        this.getCommand("Feed").setExecutor(new CommandFeed());
        CommandBalance cb = new CommandBalance();
        this.getCommand("Balance").setExecutor(cb);
        this.getCommand("Bal").setExecutor(cb);
        this.getCommand("CLS").setExecutor(new CommandClearChat());
        CommandGameMode cg = new CommandGameMode();
        this.getCommand("GameMode").setExecutor(cg);
        this.getCommand("GM").setExecutor(cg);
        CommandWho cw = new CommandWho();
        this.getCommand("Who").setExecutor(cw);
        this.getCommand("List").setExecutor(cw);
        this.getCommand("AFK").setExecutor(new CommandAFK());
        CommandSpawnEntity se = new CommandSpawnEntity();
        this.getCommand("SpawnEntity").setExecutor(se);
        this.getCommand("SpawnMob").setExecutor(se);
        this.getCommand("Butcher").setExecutor(new CommandButcher());
        CommandTPS tps = new CommandTPS();
        this.getCommand("TPS").setExecutor(tps);
        this.getCommand("Lag").setExecutor(tps);
        this.getCommand("Give").setExecutor(new CommandGive());
        this.getCommand("Kick").setExecutor(new CommandKick());
        this.getCommand("SocialSpy").setExecutor(new CommandSocialSpy());
        this.getCommand("God").setExecutor(new CommandGod());
        this.getCommand("Seed").setExecutor(new CommandSeed());
        this.getCommand("Fly").setExecutor(new CommandFly());
        this.getCommand("Speed").setExecutor(new CommandSpeed());
        this.getCommand("SetMaxHealth").setExecutor(new CommandSetMaxHealth());
        this.getCommand("SetJail").setExecutor(new CommandSetJail());
        this.getCommand("DelJail").setExecutor(new CommandDelJail());
        this.getCommand("Jail").setExecutor(new CommandJail());
        this.getCommand("UnJail").setExecutor(new CommandUnjail());
        this.getCommand("ListJail").setExecutor(new CommandListJail());
        this.getCommand("WhoIs").setExecutor(new CommandWhoIs());
        this.getCommand("Nuke").setExecutor(new CommandNuke());
        this.getCommand("Smite").setExecutor(new CommandSmite());
        this.getCommand("Sun").setExecutor(new CommandSun());
        this.getCommand("InvSee").setExecutor(new CommandInvSee());
        this.getCommand("Ban").setExecutor(new CommandBan());
        this.getCommand("Unban").setExecutor(new CommandUnban());
        this.getCommand("Burn").setExecutor(new CommandBurn());
        this.getCommand("Kit").setExecutor(new CommandKit());
        this.getCommand("Enchant").setExecutor(new CommandEnchant());
        this.getCommand("Nametag").setExecutor(new CommandNametag());
        this.getCommand("Stop").setExecutor(new CommandStop());
        this.getCommand("I").setExecutor(new CommandI());
    }
    public boolean setupMetrics()
    {
        if (!Settings.AllowMetrics)
            return false;
        try
        {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
            return true;
        } catch (IOException e)
        {
            return false;
        }
    }
    private boolean setupEconomy()
    {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
        {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
        {
            return false;
        }
        economy = rsp.getProvider();
        return (economy != null);
    }
    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }
}
