package com.WiseHollow.Fundamentals.Tasks;

import com.WiseHollow.Fundamentals.Main;
import com.WiseHollow.Fundamentals.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;

/**
 * Created by John on 10/20/2016.
 */
public class StopTask implements CustomTask, Listener
{
    public static StopTask stopTask = null;

    private int minutes;
    private int taskID;

    public StopTask(int minutes)
    {
        this.minutes = minutes;
        this.taskID = -1;
    }

    @Override
    public boolean Run()
    {
        if (minutes <= 0)
        {
            Bukkit.getServer().shutdown();
            return true;
        }

        if (stopTask != null)
            return false;

        stopTask = this;
        Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + Settings.ShutdownMessage.replaceAll("%m", String.valueOf(minutes)));

        taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, () ->
        {
            Bukkit.getServer().shutdown();
        }, minutes * 20 * 60);

        return true;
    }

    @Override
    public void Disable()
    {
        Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD +"Shutdown task has been cancelled!");

        stopTask = null;
        if (taskID != -1)
        {
            Bukkit.getScheduler().cancelTask(taskID);
            taskID = -1;
        }
    }
}
