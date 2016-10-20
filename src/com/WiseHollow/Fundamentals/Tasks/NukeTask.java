package com.WiseHollow.Fundamentals.Tasks;

import com.WiseHollow.Fundamentals.BlockData;
import com.WiseHollow.Fundamentals.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by John on 10/16/2016.
 */
public class NukeTask implements CustomTask, Listener
{
    public static List<NukeTask> tasks = new ArrayList<>();
    public static NukeTask GetTask(Player player)
    {
        for (NukeTask n : tasks)
        {
            if (n.player.equals(player))
                return n;
        }
        return null;
    }

    private Player player;
    private int taskID;
    private List<Entity> entities;
    private HashMap<Location, BlockData> restoration;

    public NukeTask(Player player)
    {
        this.player = player;
        this.taskID = -1;
        this.entities = new ArrayList<>();
        this.restoration = new HashMap<>();
    }

    @Override
    public boolean Run()
    {
        tasks.add(this);
        Bukkit.getServer().getPluginManager().registerEvents(this, Main.plugin);

        taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.plugin, () ->
        {
            if (player == null)
                return;

            Location target = player.getLocation().clone().add(0, 3, 0);
            if (target.getBlock().getType() != Material.AIR)
                target = player.getLocation();
            entities.add(player.getWorld().spawnEntity(target, EntityType.CREEPER));
            if (entities.size() > 50)
            {
                Disable();
            }
        }, 20L, 20L);

        return true;
    }

    @SuppressWarnings( "deprecation" )
    @Override
    public void Disable()
    {
        entities.forEach(Entity::remove);
        tasks.remove(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
        EntityDeathEvent.getHandlerList().unregister(this);
        EntityExplodeEvent.getHandlerList().unregister(this);
        if (taskID != -1)
        {
            Bukkit.getServer().getScheduler().cancelTask(taskID);
            taskID = -1;
        }
        for(Location loc : restoration.keySet())
        {
            BlockData data = restoration.get(loc);
            loc.getBlock().setType(data.getMaterial());
            loc.getBlock().setData(data.getData());
        }
        restoration.clear();
    }

    @EventHandler
    public void Logout(PlayerQuitEvent event)
    {
        if (!event.getPlayer().equals(player))
            return;

        Disable();
    }

    @EventHandler
    public void OnDeath(EntityDeathEvent event)
    {
        if (!event.getEntity().equals(player))
            return;

        Disable();
    }

    @EventHandler
    public void OnEntityDeath(EntityDeathEvent event)
    {
        if (entities.contains(event.getEntity()))
        {
            entities.remove(event.getEntity());
            event.setDroppedExp(0);
            event.getDrops().clear();
        }
    }

    //TODO: Priority to where this is last
    @EventHandler
    public void OnBlockExplode(EntityExplodeEvent event)
    {
        if (event.isCancelled() && !entities.contains(event.getEntity()) || event.blockList().isEmpty())
            return;

        event.setCancelled(true);

        for(Block b : event.blockList())
        {
            BlockData data = new BlockData(b);
            restoration.put(b.getLocation(), data);
            b.setType(Material.AIR);
        }
    }
}
