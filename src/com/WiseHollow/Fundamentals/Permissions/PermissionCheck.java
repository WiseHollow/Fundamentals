package com.WiseHollow.Fundamentals.Permissions;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by John on 10/13/2016.
 */
public class PermissionCheck
{
    public static boolean HasAdminPermissions(CommandSender sender)
    {
        return sender.hasPermission("Fundamentals.Admin");
    }
    public static boolean HasModeratorPermissions(CommandSender sender)
    {
        return sender.hasPermission("Fundamentals.Moderator");
    }
    public static boolean HasPoweruserPermissions(CommandSender sender)
    {
        return sender.hasPermission("Fundamentals.Poweruser");
    }
    public static boolean HasPlayerPermissions(CommandSender sender)
    {
        return sender.hasPermission("Fundamentals.Player");
    }
}
