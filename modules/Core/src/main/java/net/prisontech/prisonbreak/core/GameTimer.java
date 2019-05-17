package net.prisontech.prisonbreak.core;

import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.prisontech.prisonbreak.api.PrisonBreak;
import net.prisontech.prisonbreak.api.util.BungeeUtil;
import net.prisontech.prisonbreak.api.util.Titles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.Socket;
import java.util.Collection;

public class GameTimer extends BukkitRunnable
{
    private PrisonBreakMain plugin;

    private int totalSeconds;
    private int counter;
    private int minute;
    private int timerSeconds;
    private BossBar bossBar;

    private Collection<? extends Player> players;

    public GameTimer(PrisonBreakMain plugin, int seconds, Collection<? extends Player> players)
    {
        this.plugin = plugin;

        this.totalSeconds = seconds;
        this.counter = seconds;
        this.minute = seconds / 60;
        this.timerSeconds = counter % 60;

        this.players = players;
        this.bossBar = Bukkit.createBossBar("Time left: " + minute + ":" + timerSeconds, BarColor.BLUE, BarStyle.SOLID);
        bossBar.setVisible(true);
    }

    @Override
    public void run()
    {
        if (counter == 0)
        {
            new GameEnder().runTaskTimer(plugin, 0, 20);

            bossBar.removeAll();
            bossBar.setVisible(false);

            cancel();
        }

        for (Player player : players)
        {
            bossBar.addPlayer(player);
        }

        minute = counter / 60;
        timerSeconds = counter % 60;

        double progress = (double) counter / totalSeconds;

        if (timerSeconds < 10)
        {
            bossBar.setTitle("Time Left: " + minute + ":0" + timerSeconds);
        } else
        {
            bossBar.setTitle("Time Left: " + minute + ":" + timerSeconds);
        }

        bossBar.setProgress(progress);

        counter--;
    }

    private class GameEnder extends BukkitRunnable
    {
        int countdown = 10;

        @Override
        public void run()
        {
            for (Player player : players)
            {
                if (countdown <= 0)
                {
                    if (BungeeUtil.isServerOnline(plugin.getMainConfig().getLobbyPort()))
                    {
                        BungeeUtil.sendPlayer(player, PrisonBreak.getMainConfig().getMainServerName());
                    }
                    else
                    {
                        player.kickPlayer(ChatColor.RED + "Could not connect to lobby server, please " + ChatColor.YELLOW + "reconnect");
                    }

                    cancel();
                }

                if (countdown == 10)
                {
                    player.sendTitle(ChatColor.RED + "The game has ended!", "You will be transported back in: " + countdown, 10, 20 * countdown, 20);
                }

                sendSubtitle(player, ChatColor.GRAY + "You will be transported back in: " + countdown, 10, 20, 10);
            }

            countdown--;
        }
    }

    private static void sendSubtitle(Player p, String subtitleMsg, int fadeIn, int stayTime, int fadeOut)
    {
        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitleMsg + "\"}"), fadeIn, stayTime, fadeOut);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(subtitle);
    }
}
