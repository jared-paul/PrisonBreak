package net.prisontech.prisonbreak.core.commands;

import net.prisontech.prisonbreak.api.commands.Command;
import net.prisontech.prisonbreak.api.commands.CommandClass;
import net.prisontech.prisonbreak.api.commands.CommandContext;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;

public class CoreCommands implements CommandClass
{
    @Command(
            aliases = {"prisonbreak", "pb"},
            usage = "/prisonbreak start",
            description = "starts the minigame",
            initializer = "start",
            minArgs = 0,
            maxArgs = 0
    )
    public void start(CommandContext args, CommandSender sender)
    {

        try
        {
            Process p =  Runtime.getRuntime().exec("cmd /c start.bat", null, new File("C:\\Users\\Jared\\Desktop\\BungeeCord\\Server2"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
