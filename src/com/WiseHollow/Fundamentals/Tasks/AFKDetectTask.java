package com.WiseHollow.Fundamentals.Tasks;

import com.WiseHollow.Fundamentals.Main;
import com.WiseHollow.Fundamentals.Settings;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 10/14/2016.
 */
public class AFKDetectTask implements CustomTask, Listener
{
    private static List<AFKDetectTask> taskList = new ArrayList<>();
    public static AFKDetectTask GetTask(Player player)
    {
        for (AFKDetectTask t : taskList)
        {
            if (t.player.equals(player))
                return t;
        }

        return null;
    }

    private Player player;
    private int taskID;

    public AFKDetectTask(Player player)
    {
        this.player = player;
        this.taskID = -1;
    }

    @Override
    public boolean Run()
    {
        Main.plugin.getServer().getPluginManager().registerEvents(this, Main.plugin);
        taskList.add(this);
        return true;
    }
    @Override
    public void Disable()
    {
        PlayerMoveEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
        AsyncPlayerChatEvent.getHandlerList().unregister(this);
        taskList.remove(this);
        if (taskID != 1)
            Main.plugin.getServer().getScheduler().cancelTask(taskID);
    }

    @EventHandler
    public void WaitForMovement(PlayerMoveEvent event)
    {
        if (event.isCancelled() || !event.getPlayer().equals(player))
            return;
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockY() == event.getTo().getBlockY() && event.getFrom().getBlockZ() == event.getTo().getBlockZ())
            return;

        Refresh();
    }

    @EventHandler
    public void WaitForSpeaking(AsyncPlayerChatEvent event)
    {
        if (event.isCancelled() || !event.getPlayer().equals(player))
            return;

        Refresh();
    }

    @EventHandler
    public void RemoveOnLogout(PlayerQuitEvent event)
    {
        if (!event.getPlayer().equals(player))
            return;

        Disable();
    }

    private void Refresh()
    {
        if (taskID != 1)
            Main.plugin.getServer().getScheduler().cancelTask(taskID);
        taskID = Main.plugin.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () ->
        {
            if (player == null || !player.isOnline())
                return;

            AFKTask task = new AFKTask(player);
            task.Run();
        },20*60* Settings.AFKDelay);
    }
}
