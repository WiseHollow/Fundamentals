package com.WiseHollow.Fundamentals.Tasks;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Main;
import com.WiseHollow.Fundamentals.Permissions.PermissionCheck;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

import static com.WiseHollow.Fundamentals.Language.PREFIX;

/**
 * Created by John on 10/13/2016.
 */
public class TeleportTask implements CustomTask, Listener
{
    public static HashMap<Entity, Location> PreviousLocation = new HashMap<>(); //TODO: Make listen to teleport event and update on ANY teleport on server.
    private static void teleport(Entity entity, Location target)
    {
        entity.sendMessage(Language.PREFIX_COLOR + "Teleporting...");
        entity.teleport(target);
    }

    private Entity entity;
    private Location target;
    private int seconds;

    private Location initialLocation;
    private int taskID = -1;

    public TeleportTask(Entity e, Location t, int s)
    {
        entity = e;
        target = t;
        seconds = s;
    }
    @Override
    public boolean Run()
    {
        if (PermissionCheck.HasAdminPermissions(entity))
        {
            teleport(entity, target);
            return true;
        }

        initialLocation = entity.getLocation().clone();
        Main.plugin.getServer().getPluginManager().registerEvents(this, Main.plugin);
        entity.sendMessage(Language.PREFIX_COLOR + "Teleport in " + seconds + " seconds... Do not move.");
        taskID = Main.plugin.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () ->
        {
            Disable();
            if (entity != null)
                teleport(entity, target);
        },20 * seconds);
        return true;
    }
    @Override
    public void Disable()
    {
        PlayerMoveEvent.getHandlerList().unregister(this);
        taskID = -1;
    }
    @EventHandler
    public void OnMove(PlayerMoveEvent event)
    {
        if (!event.getPlayer().equals(entity))
            return;

        double distance = initialLocation.distance(event.getPlayer().getLocation());
        if (distance > 2)
        {
            Main.plugin.getServer().getScheduler().cancelTask(taskID);
            PlayerMoveEvent.getHandlerList().unregister(this);
            entity.sendMessage(Language.PREFIX_COLOR + "Teleport cancelled.");
        }
    }
}
