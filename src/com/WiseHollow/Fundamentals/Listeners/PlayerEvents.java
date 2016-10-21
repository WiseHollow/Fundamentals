package com.WiseHollow.Fundamentals.Listeners;

import com.WiseHollow.Fundamentals.DataCollection.PlayerData;
import com.WiseHollow.Fundamentals.Kit;
import com.WiseHollow.Fundamentals.Main;
import com.WiseHollow.Fundamentals.PlayerUtil;
import com.WiseHollow.Fundamentals.Settings;
import com.WiseHollow.Fundamentals.Tasks.AFKDetectTask;
import org.apache.logging.log4j.core.net.Priority;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.PluginEnableEvent;

import static com.WiseHollow.Fundamentals.Tasks.TeleportTask.PreviousLocation;

/**
 * Created by John on 10/14/2016.
 */
public class PlayerEvents implements Listener
{
    public static void Refresh()
    {
        for(Player p : Bukkit.getOnlinePlayers())
        {
            if (AFKDetectTask.GetTask(p) != null)
                return;

            AFKDetectTask task = new AFKDetectTask(p);
            task.Run();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void FirstJoinSpawn(PlayerJoinEvent event)
    {
        if (event.getPlayer().hasPlayedBefore() || Settings.SpawnFirstJoin == null || !Settings.EnableSpawnFirstJoin)
            return;

        Bukkit.getScheduler().runTaskLater(Main.plugin, () ->
        {
            event.getPlayer().teleport(Settings.SpawnFirstJoin);
        }, 1L);
    }

    @EventHandler
    public void StarterKit(PlayerJoinEvent event)
    {
        if (event.getPlayer().hasPlayedBefore() || Settings.StarterKit.equalsIgnoreCase("None"))
            return;

        Kit kit = Kit.GetKit(Settings.StarterKit);
        if (kit != null)
            kit.give(event.getPlayer());
    }

    @EventHandler
    public void AFKTaskOnLogin(PlayerJoinEvent event)
    {
        if (AFKDetectTask.GetTask(event.getPlayer()) != null)
            return;

        AFKDetectTask task = new AFKDetectTask(event.getPlayer());
        task.Run();
    }

    @EventHandler
    public void LoginMessage(PlayerJoinEvent event)
    {
        event.setJoinMessage(Settings.JoinMessage.replace("%p", PlayerUtil.GetPlayerPrefix(event.getPlayer()) + event.getPlayer().getName()) + ChatColor.RESET);
    }

    @EventHandler
    public void QuitMessage(PlayerQuitEvent event)
    {
        event.setQuitMessage(Settings.QuitMessage.replace("%p", PlayerUtil.GetPlayerPrefix(event.getPlayer()) + event.getPlayer().getName()) + ChatColor.RESET);
    }

    @EventHandler
    public void OnQuit(PlayerQuitEvent event)
    {
        AFKDetectTask task = AFKDetectTask.GetTask(event.getPlayer());
        if (task == null)
            return;
        task.Disable();
    }

    @EventHandler
    public void RecordTeleport(PlayerTeleportEvent event)
    {
        if (event.isCancelled())
            return;

        PreviousLocation.put(event.getPlayer(), event.getFrom().clone());
    }

    @EventHandler
    public void RecordTeleportOnDeath(PlayerDeathEvent event)
    {
        if (event.getEntity().hasPermission("Fundamentals.Back.OnDeath"))
            PreviousLocation.put(event.getEntity(), event.getEntity().getLocation());
    }

    @EventHandler
    public void TeleportWithHorse(PlayerTeleportEvent event)
    {
        if (event.isCancelled())
            return;

        if (event.getCause() != PlayerTeleportEvent.TeleportCause.COMMAND && event.getCause() != PlayerTeleportEvent.TeleportCause.PLUGIN)
            return;

        if (event.getPlayer().getVehicle() != null)
        {
            Vehicle v = (Vehicle) event.getPlayer().getVehicle();
            event.getPlayer().leaveVehicle();
            v.teleport(event.getTo());
            event.getPlayer().teleport(event.getTo());
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () ->
            {
                if (event.getPlayer() == null || !event.getPlayer().isOnline() || v == null)
                    return;
                v.setPassenger(event.getPlayer());
            },5L);
        }
    }

    @EventHandler
    public void InitializePlayerDataOnJoin(PlayerJoinEvent event)
    {
        if(PlayerData.GetPlayerData(event.getPlayer()) == null)
        {
            PlayerData.LoadPlayerData(event.getPlayer());
        }
    }

    @EventHandler
    public void InitializePlayerDataOnEnable(PluginEnableEvent event)
    {
        if (event.getPlugin() != Main.plugin)
            return;

        for(Player p : Bukkit.getOnlinePlayers())
        {
            PlayerData.LoadPlayerData(p);
        }
    }
}
