package com.WiseHollow.Fundamentals.Tasks;

import com.WiseHollow.Fundamentals.Kit;
import com.WiseHollow.Fundamentals.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 10/20/2016.
 */
public class KitTask implements CustomTask, Listener
{
    private static List<KitTask> taskList = new ArrayList<>();
    public static KitTask GetTask(Player player)
    {
        for (KitTask task : taskList)
        {
            if (task.player.equals(player))
                return task;
        }

        return null;
    }

    private Player player;
    private Kit kit;
    private int taskID;

    public KitTask(Player player, Kit kit)
    {
        this.player = player;
        this.kit = kit;
        this.taskID = -1;
    }

    @Override
    public boolean Run()
    {
        if (taskList.contains(this))
            return false;

        kit.give(player);

        if (kit.getDelay() > 0)
        {
            taskList.add(this);

            taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, () ->
            {
                Disable();
            }, 20*kit.getDelay());
        }

        return true;
    }

    @Override
    public void Disable()
    {
        taskList.remove(this);
        if (taskID != -1)
        {
            Bukkit.getScheduler().cancelTask(taskID);
            taskID = -1;
        }
    }
}
