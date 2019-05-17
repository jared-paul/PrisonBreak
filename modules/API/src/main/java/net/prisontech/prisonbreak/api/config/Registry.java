package net.prisontech.prisonbreak.api.config;

import net.prisontech.prisonbreak.api.PrisonBreak;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class Registry implements IRegistry
{
    protected FileConfiguration config;
    protected File configFile;
    protected File configFolder;

    public Registry(String name) throws IOException
    {
        this.configFolder = new File(PrisonBreak.getPlugin().getDataFolder() + "\\Configs");
        this.configFile = new File(PrisonBreak.getPlugin().getDataFolder() + "\\Configs\\" + name + "Config.yml");

        if (!this.configFile.exists())
        {
            this.configFolder.mkdirs();
            this.configFile.createNewFile();
            FileUtils.copyToFile(PrisonBreak.getPlugin().getResource(name + "Config.yml"), configFile);
        }

        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        this.config.options().copyDefaults(true);
    }
}
