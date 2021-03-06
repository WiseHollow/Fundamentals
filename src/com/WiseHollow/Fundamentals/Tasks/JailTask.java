package com.WiseHollow.Fundamentals.Tasks;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Main;
import com.WiseHollow.Fundamentals.Permissions.PermissionCheck;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 10/15/2016.
 */
public class JailTask implements CustomTask, Listener
{
    public static List<JailTask> jailTasks = new ArrayList<>();
    public static JailTask GetTask(Player player)
    {
        for (JailTask j : jailTasks)
        {
            if (j.player.equals(player))
                return j;
        }
        return null;
    }
    public static void DisableAll()
    {
        if (jailTasks.isEmpty())
            return;
        List<JailTask> tasks = new ArrayList<>();
        tasks.addAll(jailTasks);
        tasks.forEach(task -> task.Disable());
    }

    private Player player;
    private Location location;
    private Location oldLocation;
    private int seconds;
    private int taskID;

    public JailTask(Player player, Location location, int seconds)
    {
        this.player = player;
        this.oldLocation = player.getLocation().clone();
        this.location = location.clone();
        this.seconds = seconds;
        this.taskID = -1;
    }

    @Override
    public boolean Run()
    {
        if (PermissionCheck.HasModeratorPermissions(player))
        {
            return false;
        }

        jailTasks.add(this);
        Bukkit.getServer().getPluginManager().registerEvents(this, Main.plugin);
        if (seconds <= 0)
        {
            player.sendMessage(Language.PREFIX_COLOR + "You have been jailed!");
            return true;
        }
        else if (seconds < 60)
            player.sendMessage(Language.PREFIX_COLOR + "You have been jailed for " + seconds + " seconds!");
        else
            player.sendMessage(Language.PREFIX_COLOR + "You have been jailed for " + seconds / 60 + " minutes!");
        player.teleport(location);
        taskID = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () ->
        {
            Disable();
        },20L * seconds);
        return true;
    }

    @Override
    public void Disable()
    {
        if (taskID != -1)
        {
            Bukkit.getServer().getScheduler().cancelTask(taskID);
            taskID = -1;
        }
        jailTasks.remove(this);
        PlayerInteractEvent.getHandlerList().unregister(this);
        BlockBreakEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
        PlayerRespawnEvent.getHandlerList().unregister(this);
        PlayerTeleportEvent.getHandlerList().unregister(this);
        EntityDamageByEntityEvent.getHandlerList().unregister(this);
        if (player != null && player.isOnline())
        {
            player.teleport(oldLocation);
            player.sendMessage(Language.PREFIX_COLOR + "You have been released from jail!");
        }
    }

    @EventHandler
    public void PreventAccess(PlayerInteractEvent event)
    {
        if (event.isCancelled() || !event.getPlayer().equals(player))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void PreventBreak(BlockBreakEvent event)
    {
        if (event.isCancelled() || !event.getPlayer().equals(player))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void PreventPlace(BlockPlaceEvent event)
    {
        if (event.isCancelled() || !event.getPlayer().equals(player))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void PreventLeave(PlayerRespawnEvent event)
    {
        if (!event.getPlayer().equals(player))
            return;

        Bukkit.getServer().getScheduler().runTaskLater(Main.plugin, () ->
        {
            if (event.getPlayer() != null && event.getPlayer().isOnline())
                event.getPlayer().teleport(location);
        },1L);
    }

    @EventHandler
    public void PreventTeleport(PlayerTeleportEvent event)
    {
        if (event.isCancelled() || !event.getPlayer().equals(player))
            return;

        if (event.getTo().getWorld().getName().equalsIgnoreCase(location.getWorld().getName()))
        {
            if (event.getTo().distance(location) <= 1)
                return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void PreventHitOthers(EntityDamageByEntityEvent event)
    {
        if (event.isCancelled() || !event.getDamager().equals(player))
            return;

        event.setCancelled(true);
    }
}
