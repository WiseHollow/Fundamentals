package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Permissions.PermissionCheck;
import com.WiseHollow.Fundamentals.Tasks.SocialSpyTask;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by John on 10/13/2016.
 */
public class CommandSocialSpy implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(Language.YouMustBeLoggedIn);
            return true;
        }

        Player player = (Player) sender;
        if (!sender.hasPermission("Fundamentals.SocialSpy"))
        {
            player.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        SocialSpyTask task = SocialSpyTask.getSocialSpyTask(player);
        if (task != null)
        {
            task.Disable();
        }
        else
        {
            task = new SocialSpyTask(player);
            task.Run();
        }

        return true;
    }
}
