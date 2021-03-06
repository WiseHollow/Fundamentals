package com.WiseHollow.Fundamentals.DataCollection;

import com.WiseHollow.Fundamentals.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by John on 10/20/2016.
 */
public class PlayerData implements Listener
{
    private static final String directory = "plugins" + File.separator + "Fundamentals" + File.separator + "Player Data";
    private static List<PlayerData> data = new ArrayList<>();
    public static PlayerData GetPlayerData(Player player)
    {
        for (PlayerData pd : data)
            if (pd.uuid.equalsIgnoreCase(player.getUniqueId().toString()))
                return pd;
        return null;
    }
    public static PlayerData GetPlayerData(String uuid)
    {
        for (PlayerData pd : data)
            if (pd.uuid.equalsIgnoreCase(uuid))
                return pd;
        return null;
    }
    public static void UnloadPlayerData(Player player)
    {
        data.remove(GetPlayerData(player));
    }
    public static void UnloadPlayerData(PlayerData player)
    {
        data.remove(player);
    }
    public static void LoadPlayerData(Player player)
    {
        File dir = new File(directory);
        if (!dir.isDirectory())
            dir.mkdirs();
        File file = new File(directory + File.separator + player.getUniqueId().toString() + ".yml");
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
                Main.logger.info("Creating player data for: " + player.getName() + " ID: " + player.getUniqueId().toString());
            }
            catch(IOException ex)
            {
                Main.logger.severe(ex.getMessage());
                return;
            }
        }

        PlayerData profile = new PlayerData(player);

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (config.getConfigurationSection("Homes") != null)
        {
            HashMap<String, Location> homes = new HashMap<>();
            for(String key : config.getConfigurationSection("Homes").getKeys(false))
            {
                World world = Bukkit.getWorld(config.getString("Homes." + key + ".Location.World"));
                int x = config.getInt("Homes." + key + ".Location.X");
                int y = config.getInt("Homes." + key + ".Location.Y");
                int z = config.getInt("Homes." + key + ".Location.Z");
                float pitch = (float) config.getDouble("Homes." + key + ".Location.Pitch");
                float yaw = (float) config.getDouble("Homes." + key + ".Location.Yaw");

                Location loc = new Location(world, x, y, z, yaw, pitch);
                homes.put(key, loc);
            }

            profile.setHomes(homes);

        }

        Bukkit.getPluginManager().registerEvents(profile, Main.plugin);
        data.add(profile);
    }

    private String uuid;
    private String name;
    private HashMap<String, Location> homes;
    private Location lastLocation;

    public PlayerData(Player player)
    {
        this.uuid = player.getUniqueId().toString();
        this.name = player.getName();
        this.homes = new HashMap<>();
        this.lastLocation = player.getLocation();
    }
    public HashMap<String, Location> getHomes() { return homes; }
    public boolean setHome(String name)
    {
        name = name.toLowerCase();
        Player player = Bukkit.getPlayer(this.name);
        if (player == null)
            return false;
        Location loc = player.getLocation();
        homes.put(name.toLowerCase(), loc);
        save();
        return true;
    }
    public boolean deleteHome(String name)
    {
        name = name.toLowerCase();
        if (!homes.containsKey(name))
            return false;

        homes.remove(name);
        save();
        return true;
    }
    public Location getHome(String name)
    {
        name = name.toLowerCase();
        if (!homes.containsKey(name))
            return null;
        return homes.get(name);
    }
    public void save()
    {
        File dir = new File(directory);
        if (!dir.isDirectory())
            dir.mkdirs();
        File file = new File(directory + File.separator + uuid + ".yml");
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
                Main.logger.info("Creating player data for: " + name + " ID: " + uuid);
            }
            catch(IOException ex)
            {
                Main.logger.severe(ex.getMessage());
                return;
            }
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("LastPosition.Location.World", lastLocation.getWorld().getName());
        config.set("LastPosition.Location.X", lastLocation.getBlockX());
        config.set("LastPosition.Location.Y", lastLocation.getBlockY());
        config.set("LastPosition.Location.Z", lastLocation.getBlockZ());
        config.set("LastPosition.Location.Yaw", lastLocation.getYaw());
        config.set("LastPosition.Location.Pitch", lastLocation.getPitch());

        config.set("Homes", null);
        for(String key : homes.keySet())
        {
            Location loc = homes.get(key);
            config.set("Homes." + key + ".Location.World", loc.getWorld().getName());
            config.set("Homes." + key + ".Location.X", loc.getBlockX());
            config.set("Homes." + key + ".Location.Y", loc.getBlockY());
            config.set("Homes." + key + ".Location.Z", loc.getBlockZ());
            config.set("Homes." + key + ".Location.Yaw", loc.getYaw());
            config.set("Homes." + key + ".Location.Pitch", loc.getPitch());
        }

        try
        {
            config.save(file);
        }
        catch(IOException ex)
        {
            Main.logger.severe(ex.getMessage());
            return;
        }
    }
    private void setHomes(HashMap<String, Location> homes)
    {
        this.homes = homes;
    }
    @EventHandler
    public void SaveOnExit(PlayerQuitEvent event)
    {
        save();
        UnloadPlayerData(this);
        event.getHandlers().unregister(this);
    }
}
