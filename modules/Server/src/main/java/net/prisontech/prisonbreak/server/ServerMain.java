package net.prisontech.prisonbreak.server;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import io.netty.handler.logging.LogLevel;
import net.milkbowl.vault.economy.Economy;
import net.prisontech.prisonbreak.server.api.commands.CommandManager;
import net.prisontech.prisonbreak.server.api.commands.SimpleInjector;
import net.prisontech.prisonbreak.server.commands.CoreCommands;
import net.prisontech.prisonbreak.server.config.Registry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;

/*
this is such a bad way of doing things and is so unorganized, I just wanted to get it done quick
 */

public class ServerMain extends JavaPlugin implements Listener
{
    private static ServerMain instance;

    private static final CommandManager COMMANDS = new CommandManager();

    private static Registry registry;
    private static BungeeChannelApi channelApi;
    private Economy economy;

    public void onEnable()
    {
        if (instance == null)
        {
            instance = this;
        }

        if (registry == null)
        {
            try
            {
                registry = new Registry();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        setupEconomy();

        COMMANDS.setInjector(new SimpleInjector());
        initCommands();

        this.getServer().getPluginManager().registerEvents(this, this);

        if (channelApi == null)
        {
            channelApi = BungeeChannelApi.of(this);
        }

        channelApi.registerForwardListener("money", (channel, player, data) ->
        {
            DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(data));
            try
            {
                economy.depositPlayer(player, inputStream.readDouble());
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        });
    }

    public static Registry getRegistry()
    {
        return registry;
    }

    public static BungeeChannelApi getChannelAPI()
    {
        return channelApi;
    }

    private void initCommands()
    {
        COMMANDS.register(CoreCommands.class);

        getCommand("prisonbreak").setExecutor(COMMANDS);
    }

    private void setupEconomy()
    {
        RegisteredServiceProvider<Economy> registeredServiceProvider = getServer().getServicesManager().getRegistration(Economy.class);
        this.economy = registeredServiceProvider.getProvider();
    }

    @EventHandler
    public void test(PlayerJoinEvent joinEvent)
    {
    }

    public Economy getEconomy()
    {
        return economy;
    }

    public static ServerMain getPlugin()
    {
        return instance;
    }
}
