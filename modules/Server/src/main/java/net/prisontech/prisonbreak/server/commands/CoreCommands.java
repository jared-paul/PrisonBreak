package net.prisontech.prisonbreak.server.commands;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.prisontech.prisonbreak.server.ServerMain;
import net.prisontech.prisonbreak.server.api.commands.Command;
import net.prisontech.prisonbreak.server.api.commands.CommandClass;
import net.prisontech.prisonbreak.server.api.commands.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class CoreCommands implements CommandClass, Listener
{
    private boolean startGame = false;

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
        Bukkit.broadcastMessage("PrisonBreak Mini-game will start in 1 minute");

        Bukkit.getScheduler().runTaskLater(ServerMain.getPlugin(), () -> new BukkitRunnable()
        {
            int countdown = 10;

            @Override
            public void run()
            {
                for (Player player : Bukkit.getOnlinePlayers())
                {
                    if (isWithinRegion(player.getLocation(), ServerMain.getRegistry().getRegionID()))
                    {
                        if (countdown <= 0)
                        {
                            ServerMain.getChannelAPI().connect(player, ServerMain.getRegistry().getServerName());

                            cancel();
                        }

                        if (countdown == 10)
                        {
                            player.sendTitle(ChatColor.RED + "The game is starting!", "You will be transported in: " + countdown, 10, 20 * countdown, 20);

                            try
                            {
                                Runtime.getRuntime().exec("cmd /c start.bat", null, new File("C:\\Users\\Jared\\Desktop\\BungeeCord\\Server2"));
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }

                        sendSubtitle(player, ChatColor.GRAY + "You will be transported in: " + countdown, 10, 20, 10);
                    }
                }

                countdown--;
            }
        }.runTaskTimer(ServerMain.getPlugin(), 0, 20), 20 * 60);
    }

    private static void sendSubtitle(Player p, String subtitleMsg, int fadeIn, int stayTime, int fadeOut)
    {
        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitleMsg + "\"}"), fadeIn, stayTime, fadeOut);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(subtitle);
    }

    public boolean isWithinRegion(Location location, String regionID)
    {
        ProtectedRegion region = WGBukkit.getRegionManager(location.getWorld()).getRegion(regionID);

        return region.contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
