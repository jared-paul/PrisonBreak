package net.prisontech.prisonbreak.api.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by JPaul on 8/16/2016.
 */
public class Preconditions
{
    public static boolean checkNotNull(Object objectToCheck, CommandSender playerToSendError, String error)
    {
        return checkNotNull(objectToCheck, (Player) playerToSendError, error);
    }

    public static boolean checkNotNull(Object objectToCheck, Player playerToSendError, String error)
    {
        if (objectToCheck == null)
        {
            playerToSendError.sendMessage(error);
            return true;
        }
        else
        {
            return false;
        }
    }
}
