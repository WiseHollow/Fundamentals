package com.WiseHollow.Fundamentals.Tasks;

import com.WiseHollow.Fundamentals.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

/**
 * Created by John on 10/19/2016.
 */
public class InventorySeeTask implements CustomTask, Listener
{
    private Player viewer;
    private Player target;

    public InventorySeeTask(Player viewer, Player target)
    {
        this.viewer = viewer;
        this.target = target;
    }

    @Override
    public boolean Run()
    {
        Bukkit.getServer().getPluginManager().registerEvents(this, Main.plugin);
        if (viewer == null || target == null)
            return false;

        viewer.openInventory(target.getInventory());
        return true;
    }

    @Override
    public void Disable()
    {
        InventoryClickEvent.getHandlerList().unregister(this);
        InventoryDragEvent.getHandlerList().unregister(this);
        InventoryCloseEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void PreventClick(InventoryClickEvent event)
    {
        if (event.isCancelled())
            return;

        Bukkit.getServer().getScheduler().runTaskLater(Main.plugin, () ->
        {
            if (viewer != null)
                viewer.updateInventory();
            if (target != null)
                target.updateInventory();
        },1L);

        //if (event.getWhoClicked().equals(viewer))
            //event.setCancelled(true);
    }

    @EventHandler
    public void PreventDrag(InventoryDragEvent event)
    {
        if (event.isCancelled())
            return;

        Bukkit.getServer().getScheduler().runTaskLater(Main.plugin, () ->
        {
            if (viewer != null)
                viewer.updateInventory();
            if (target != null)
                target.updateInventory();
        },1L);

        //if (event.getWhoClicked().equals(viewer))
            //event.setCancelled(true);
    }

    @EventHandler
    public void Close(InventoryCloseEvent event)
    {
        if (event.getPlayer().equals(viewer))
            Disable();
    }
}
