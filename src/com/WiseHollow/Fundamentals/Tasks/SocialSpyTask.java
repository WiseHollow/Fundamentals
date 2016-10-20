package com.WiseHollow.Fundamentals.Tasks;

import com.WiseHollow.Fundamentals.CustomEvents.SendPrivateMessageEvent;
import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 10/15/2016.
 */
public class SocialSpyTask implements CustomTask, Listener
{
    private static List<SocialSpyTask> socialSpyTaskList = new ArrayList<>();
    public static SocialSpyTask getSocialSpyTask(Player p)
    {
        for(SocialSpyTask s : socialSpyTaskList)
        {
            if (s.player.equals(p))
                return s;
        }

        return null;
    }

    private Player player;

    public SocialSpyTask(Player player)
    {
        this.player = player;
    }

    @Override
    public boolean Run()
    {
        player.sendMessage(Language.PREFIX + "Social Spy enabled.");
        Main.plugin.getServer().getPluginManager().registerEvents(this, Main.plugin);
        socialSpyTaskList.add(this);
        return true;
    }

    @Override
    public void Disable()
    {
        player.sendMessage(Language.PREFIX + "Social Spy disabled.");
        SendPrivateMessageEvent.getHandlerList().unregister(this);
        PlayerQuitEvent.getHandlerList().unregister(this);
        socialSpyTaskList.remove(this);
    }

    @EventHandler
    public void GetMessages(SendPrivateMessageEvent event)
    {
        if (event.isCancelled() || event.getReceiver().equals(player) || event.getSender().equals(player))
            return;

        player.sendMessage(event.getReceiverPrefix() + event.getMessage());
    }

    @EventHandler
    public void RemoveOnLogout(PlayerQuitEvent event)
    {
        if (!event.getPlayer().equals(player))
            return;

        Disable();
    }
}
