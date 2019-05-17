package net.prisontech.prisonbreak.core;

import net.prisontech.prisonbreak.api.IMainConfig;
import net.prisontech.prisonbreak.api.IPrisonBreakMain;
import net.prisontech.prisonbreak.api.PrisonBreak;
import net.prisontech.prisonbreak.api.blackmarket.IBlackMarketRegistry;
import net.prisontech.prisonbreak.api.commands.CommandManager;
import net.prisontech.prisonbreak.api.commands.SimpleInjector;
import net.prisontech.prisonbreak.api.kits.IKitRegistry;
import net.prisontech.prisonbreak.api.loot.IChestRegistry;
import net.prisontech.prisonbreak.api.map.IMapLoader;
import net.prisontech.prisonbreak.api.util.BungeeChannelApi;
import net.prisontech.prisonbreak.api.util.BungeeUtil;
import net.prisontech.prisonbreak.blackmarket.BlackMarketRegistry;
import net.prisontech.prisonbreak.blackmarket.listener.ShopListener;
import net.prisontech.prisonbreak.core.commands.CoreCommands;
import net.prisontech.prisonbreak.core.listeners.test;
import net.prisontech.prisonbreak.core.map.MapLoader;
import net.prisontech.prisonbreak.kits.KitRegistry;
import net.prisontech.prisonbreak.kits.listeners.KitListener;
import net.prisontech.prisonbreak.loot.ChestGenerator;
import net.prisontech.prisonbreak.loot.ChestRegistry;
import net.prisontech.prisonbreak.loot.listeners.TestListener;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.UUID;

public class PrisonBreakMain extends JavaPlugin implements IPrisonBreakMain
{
    private static final CommandManager COMMANDS = new CommandManager();

    private IMainConfig mainConfig;
    private IKitRegistry kitRegistry;
    private IChestRegistry chestRegistry;
    private IBlackMarketRegistry blackMarketRegistry;
    private IMapLoader mapLoader;
    private BungeeChannelApi channelApi;

    public void onEnable()
    {
        PrisonBreak.setPlugin(this);

        try
        {
            //load map first
            this.mapLoader = new MapLoader();

            this.mainConfig = new MainConfig();
            this.kitRegistry = new KitRegistry();
            this.chestRegistry = new ChestRegistry();
            this.blackMarketRegistry = new BlackMarketRegistry();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        new ChestGenerator();

        this.channelApi = BungeeChannelApi.of(this);
        registerListeners();

        COMMANDS.setInjector(new SimpleInjector());
        initCommands();

        new GameTimer(this, mainConfig.getDuration(), getServer().getOnlinePlayers()).runTaskTimer(PrisonBreak.getPlugin(), 20 * 10, 20);
    }

    public void onDisable()
    {
        try
        {
            Field field = JavaPlugin.class.getDeclaredField("isEnabled");
            field.setAccessible(true);
            field.set(this, true);

            for (Player player : getServer().getOnlinePlayers())
            {
                if (BungeeUtil.isServerOnline(getMainConfig().getLobbyPort()))
                {
                    BungeeUtil.sendPlayer(player, "lobby");
                }
                else
                {
                    player.kickPlayer(ChatColor.RED + "Could not connect to lobby server, please " + ChatColor.YELLOW + "reconnect");
                }
            }

            field.set(this, false);

            PrisonBreak.LOG.debug("CALLED");
            this.mapLoader.unLoadMap();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }

    private void initCommands()
    {
        COMMANDS.register(CoreCommands.class);

        getCommand("prisonbreak").setExecutor(COMMANDS);
    }

    @Override
    public BungeeChannelApi getChannelAPI()
    {
        return channelApi;
    }

    @Override
    public IMainConfig getMainConfig()
    {
        return mainConfig;
    }

    @Override
    public World getWorld()
    {
        return this.mapLoader.getWorld();
    }

    @Override
    public IKitRegistry getKitRegistry()
    {
        return kitRegistry;
    }

    @Override
    public IChestRegistry getChestRegistry()
    {
        return chestRegistry;
    }

    @Override
    public IBlackMarketRegistry getBlackMarketRegistry()
    {
        return blackMarketRegistry;
    }

    private void registerListeners()
    {

        Bukkit.getPluginManager().registerEvents(new KitListener(), this);
        Bukkit.getPluginManager().registerEvents(new TestListener(), this);
        Bukkit.getPluginManager().registerEvents(new test(), this);
        Bukkit.getPluginManager().registerEvents(new ShopListener(), this);

    }
}
