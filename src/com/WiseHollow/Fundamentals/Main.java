package com.WiseHollow.Fundamentals;

import com.WiseHollow.Fundamentals.Commands.*;
import com.WiseHollow.Fundamentals.DataCollection.MetricsLite;
import com.WiseHollow.Fundamentals.Listeners.PlayerEvents;
import com.WiseHollow.Fundamentals.Listeners.SignColorEvents;
import com.WiseHollow.Fundamentals.Tasks.JailTask;
import com.WiseHollow.Fundamentals.Tasks.LagTask;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
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
    @Override
    public void saveDefaultConfig()
    {
        loadConfigFromJar();
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
        this.getCommand("Warp").setExecutor(new CommandWarp());
        this.getCommand("SetWarp").setExecutor(new CommandSetWarp());
        this.getCommand("DelWarp").setExecutor(new CommandDelWarp());
        this.getCommand("Home").setExecutor(new CommandHome());
        this.getCommand("SetHome").setExecutor(new CommandSetHome());
        this.getCommand("DelHome").setExecutor(new CommandDelHome());
        this.getCommand("Workbench").setExecutor(new CommandWorkbench());
        this.getCommand("Sudo").setExecutor(new CommandSudo());
        this.getCommand("Fundamentals").setExecutor(new CommandFundamentals());
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
    @Override
    public FileConfiguration getConfig()
    {
        File file = new File("plugins" + File.separator + "Fundamentals" + File.separator + "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        return config;
    }
    private boolean setupChat()
    {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }
    public static void LoadConfigFromJar(String path, Plugin plugin)
    {
        File configFile = new File(path);
        File dir = new File(configFile.getAbsolutePath().substring(0,configFile.getAbsolutePath().lastIndexOf(File.separator)));
        if (!dir.isDirectory())
            dir.mkdirs();

        if (!configFile.exists())
        {
            InputStream fis = plugin.getResource("config.yml");
            FileOutputStream fos = null;
            try
            {
                configFile.createNewFile();
                fos = new FileOutputStream(configFile);
                byte[] buf = new byte[1024];
                int i;
                while ((i = fis.read(buf)) != -1)
                {
                    fos.write(buf, 0, i);
                }
            } catch (Exception e)
            {
                plugin.getLogger().severe("Failed to load config from JAR");
            } finally
            {
                try
                {
                    if (fis != null)
                    {
                        fis.close();
                    }
                    if (fos != null)
                    {
                        fos.close();
                    }
                } catch (Exception e)
                {
                    plugin.getLogger().severe(e.getMessage());
                }
            }
        }
    }
    private void loadConfigFromJar()
    {
        File configFile = new File("plugins" + File.separator + "Fundamentals" + File.separator + "config.yml");
        File dir = new File("plugins" + File.separator + "Fundamentals");
        if (!dir.isDirectory())
            dir.mkdirs();

        if (!configFile.exists())
        {
            InputStream fis = plugin.getResource("config.yml");
            FileOutputStream fos = null;
            try
            {
                configFile.createNewFile();
                fos = new FileOutputStream(configFile);
                byte[] buf = new byte[1024];
                int i;
                while ((i = fis.read(buf)) != -1)
                {
                    fos.write(buf, 0, i);
                }
            } catch (Exception e)
            {
                logger.severe("Failed to load config from JAR");
            } finally
            {
                try
                {
                    if (fis != null)
                    {
                        fis.close();
                    }
                    if (fos != null)
                    {
                        fos.close();
                    }
                } catch (Exception e)
                {
                    logger.severe(e.getMessage());
                }
            }
        }
    }
}
