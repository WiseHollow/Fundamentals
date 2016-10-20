package com.WiseHollow.Fundamentals;

import it.unimi.dsi.fastutil.Hash;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by John on 10/13/2016.
 */
public class Settings
{
    private static FileConfiguration config = Main.plugin.getConfig();
    public static int TeleportDelay = 0; // In seconds
    public static Location Spawn = Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
    public static int AFKDelay = 0;
    public static boolean SignColor = false;
    public static HashMap<String, Location> jails = new HashMap<>();
    public static String JoinMessage = "%p has joined the game.";
    public static String QuitMessage = "%p has left the game.";
    public static boolean AllowUnsafeEnchantments = false;
    public static String StarterKit = "None";
    public static String ShutdownMessage = "Server will restart in %m minutes";
    public static Boolean AllowMetrics = true;

    public static HashMap<String, Location> warps = new HashMap<>();


    public static void Load()
    {
        TeleportDelay = config.getInt("Teleport_Delay");
        AFKDelay = config.getInt("Afk_Delay");
        SignColor = config.getBoolean("Sign_Colors");
        JoinMessage = ChatColor.translateAlternateColorCodes('&', config.getString("Join_Message"));
        QuitMessage = ChatColor.translateAlternateColorCodes('&', config.getString("Quit_Message"));
        AllowUnsafeEnchantments = config.getBoolean("Allow_Unsafe_Enchantments");
        StarterKit = config.getString("Starter_Kit");
        ShutdownMessage = config.getString("Scheduled_Shutdown_Message");
        AllowMetrics = config.getBoolean("Allow_Metrics");

        if (config.getConfigurationSection("Spawn_Location") != null)
        {
            World world = Bukkit.getWorld(config.getString("Spawn_Location.World"));
            if (world != null)
                Spawn = new Location(world, config.getInt("Spawn_Location.X"), config.getInt("Spawn_Location.Y"), config.getInt("Spawn_Location.Z"), (float) config.getDouble("Spawn_Location.Yaw"), (float) config.getDouble("Spawn_Location.Pitch"));
        }

        if (config.getConfigurationSection("Jails") != null)
        {
            for (String key : config.getConfigurationSection("Jails").getKeys(false))
            {
                World world = Bukkit.getWorld(config.getString("Jails." + key + ".Location.World"));
                int x = config.getInt("Jails." + key + ".Location.X");
                int y = config.getInt("Jails." + key + ".Location.Y");
                int z = config.getInt("Jails." + key + ".Location.Z");
                Location loc = new Location(world, x, y, z);
                jails.put(key, loc);
            }
        }

        if (config.getConfigurationSection("Kits") != null)
        {
            ConfigurationSection kits = config.getConfigurationSection("Kits");
            for(String key : kits.getKeys(false))
            {
                String name = key;
                int delay = kits.getInt(key + ".delay");
                List<String> itemStrings = kits.getStringList(key + ".items");

                if (name == null || name.equalsIgnoreCase("") || delay < 0)
                    continue;

                Kit kit = new Kit(name, delay);

                if (itemStrings != null && !itemStrings.isEmpty())
                {
                    for(String s : itemStrings)
                    {
                        String[] args = s.split(" ");
                        Material mat;
                        int amount;
                        byte data;

                        String displayName = null;
                        String lore = null;

                        HashMap<Enchantment, Integer> enchantments = new HashMap<>();

                        try
                        {
                            mat = Material.getMaterial(args[0].toUpperCase());
                            amount = Integer.valueOf(args[1]);
                            if (args.length > 2)
                                data = Byte.valueOf(args[2]);
                            else
                                data = (byte) 0;

                            if (args.length > 3)
                            {
                                displayName = args[3];
                            }
                            if (args.length > 4)
                            {
                                lore = args[4];
                            }

                            if (args.length > 5)
                            {
                                for(int i = 5; i < args.length; i++)
                                {
                                    String[] eElement = args[i].split("%");
                                    Enchantment enchantment = Enchantment.getByName(eElement[0].toUpperCase());
                                    int level = Integer.valueOf(eElement[1]);
                                    enchantments.put(enchantment, level);
                                }
                            }
                        }
                        catch(Exception ex)
                        {
                            Main.logger.severe(ex.getMessage());
                            continue;
                        }

                        ItemStack item = new ItemStack(mat, amount, data);
                        ItemMeta meta = item.getItemMeta();
                        if (displayName != null && !displayName.equalsIgnoreCase("-"))
                            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName.replaceAll("_", " ")));
                        if (lore != null && !lore.equalsIgnoreCase("-"))
                        {
                            List<String> loreList = new ArrayList<>();
                            for(String l : ChatColor.translateAlternateColorCodes('&', lore.replaceAll("_", " ")).split("%n"))
                            {
                                loreList.add(l);
                            }
                            meta.setLore(loreList);
                        }

                        item.setItemMeta(meta);

                        if (!enchantments.isEmpty())
                        {
                            for(Enchantment enchantment : enchantments.keySet())
                            {
                                if (!AllowUnsafeEnchantments)
                                    item.addEnchantment(enchantment, enchantments.get(enchantment));
                                else
                                    item.addUnsafeEnchantment(enchantment, enchantments.get(enchantment));
                            }
                        }

                        kit.addItem(item);
                    }
                }

                Kit.AddKit(kit);
            }
        }

        File warpFile = new File("plugins" + File.separator + "Fundamentals" + File.separator + "warps.yml");
        if (warpFile.exists())
        {
            YamlConfiguration warpConfig = YamlConfiguration.loadConfiguration(warpFile);
            if (warpConfig.getConfigurationSection("Warps") != null)
            {
                ConfigurationSection section = warpConfig.getConfigurationSection("Warps");
                for(String key : section.getKeys(false))
                {
                    int x;
                    int y;
                    int z;
                    float pitch;
                    float yaw;

                    x = section.getInt(key + ".Location.X");
                    y = section.getInt(key + ".Location.Y");
                    z = section.getInt(key + ".Location.Z");
                    pitch = (float) section.getDouble(key + ".Location.Pitch");
                    yaw = (float) section.getDouble(key + ".Location.Yaw");

                    Location loc = new Location(Bukkit.getWorld(section.getString(key + ".Location.World")), x, y, z, yaw, pitch);

                    warps.put(key, loc);
                }
            }
        }
    }
    public static void Save()
    {
        //
        // General configuration
        //

        config.set("Teleport_Delay", TeleportDelay);
        config.set("Afk_Delay", AFKDelay);
        config.set("Spawn_Location.World", Spawn.getWorld().getName());
        config.set("Spawn_Location.X", Spawn.getBlockX());
        config.set("Spawn_Location.Y", Spawn.getBlockY());
        config.set("Spawn_Location.Z", Spawn.getBlockZ());
        config.set("Spawn_Location.Yaw", Spawn.getYaw());
        config.set("Spawn_Location.Pitch", Spawn.getPitch());

        //
        // Jail saves
        //

        config.set("Jails", null);
        for(String key : jails.keySet())
        {
            Location loc = jails.get(key);
            config.set("Jails." + key + ".Location.World", loc.getWorld().getName());
            config.set("Jails." + key + ".Location.X", loc.getBlockX());
            config.set("Jails." + key + ".Location.Y", loc.getBlockY());
            config.set("Jails." + key + ".Location.Z", loc.getBlockZ());
        }

        //
        // Warp saves
        //

        File warpFile = new File("plugins" + File.separator + "Fundamentals" + File.separator + "warps.yml");
        if (!warpFile.exists())
        {
            try
            {
                warpFile.createNewFile();
            }
            catch(IOException ex)
            {
                Main.logger.severe(ex.getMessage());
            }
        }
        YamlConfiguration warpConfig = YamlConfiguration.loadConfiguration(warpFile);
        for(String key : warps.keySet())
        {
            Location loc = warps.get(key);
            warpConfig.set("Warps." + key + ".Location.World", loc.getWorld().getName());
            warpConfig.set("Warps." + key + ".Location.X", loc.getBlockX());
            warpConfig.set("Warps." + key + ".Location.Y", loc.getBlockY());
            warpConfig.set("Warps." + key + ".Location.Z", loc.getBlockZ());
            warpConfig.set("Warps." + key + ".Location.Yaw", loc.getYaw());
            warpConfig.set("Warps." + key + ".Location.Pitch", loc.getPitch());
        }

        //
        // Save to file
        //

        try
        {
            warpConfig.save(warpFile);
        }
        catch(Exception ex)
        {
            Main.logger.severe(ex.getMessage());
        }

        Main.plugin.saveConfig();
    }
}
