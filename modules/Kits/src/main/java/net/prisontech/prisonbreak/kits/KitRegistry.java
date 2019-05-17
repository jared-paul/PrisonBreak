package net.prisontech.prisonbreak.kits;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.prisontech.prisonbreak.api.PrisonBreak;
import net.prisontech.prisonbreak.api.config.Registry;
import net.prisontech.prisonbreak.api.kits.IKit;
import net.prisontech.prisonbreak.api.kits.IKitRegistry;
import org.apache.commons.io.FileUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KitRegistry extends Registry implements IKitRegistry
{
    private FileConfiguration playerData;
    private File playerDataFile;
    private List<IKit> kits = Lists.newArrayList();

    Map<UUID, List<IKit>> playerKits = Maps.newHashMap();

    public KitRegistry() throws IOException
    {
        super("Kits");

        this.playerDataFile = new File(PrisonBreak.getPlugin().getDataFolder() + "\\Kits\\playerData.yml");

        if (!playerDataFile.exists())
        {
            playerDataFile.getParentFile().mkdirs();
            playerDataFile.createNewFile();
        }

        this.playerData = YamlConfiguration.loadConfiguration(playerDataFile);
    }

    @Override
    public void saveKit(IKit kit, UUID playerUUID) throws IOException
    {
        if (!playerData.isSet(playerUUID.toString()))
        {
            playerData.set(playerUUID.toString(), Lists.newArrayList(kit.getPermission()));
        }
        else
        {
            List<String> kits = playerData.getStringList(playerUUID.toString());
            if (!kits.contains(kit.getPermission()))
            {
                kits.add(kit.getPermission());
            }
            playerData.set(playerUUID.toString(), kits);
        }

        playerData.save(playerDataFile);
    }

    @Override
    public List<IKit> getAvailableKits(UUID playerUUID)
    {
        if (playerKits.get(playerUUID) == null || playerKits.get(playerUUID).isEmpty())
        {
            List<IKit> kits = Lists.newArrayList();

            if (playerData.isSet(playerUUID.toString()))
            {
                List<String> availableKits = playerData.getStringList(playerUUID.toString());
                for (String kitName : availableKits)
                {
                    kits.add(getKit(kitName));
                }
            }

            playerKits.put(playerUUID, kits);
        }

        return playerKits.get(playerUUID);
    }

    @Override
    public IKit getKit(String kitName)
    {
        for (IKit kit : getKits())
        {
            PrisonBreak.LOG.warning(kit.getName());

            if (kit.getName().equalsIgnoreCase(kitName))
                return kit;
        }

        return null;
    }

    @Override
    public List<IKit> getKits()
    {
        if (this.kits.isEmpty())
        {
            String path = "PrisonBreak.Kits";

            //loops through kit names in config
            for (String kitName : this.config.getConfigurationSection(path).getKeys(false))
            {
                String kitPath = path + "." + kitName;

                String permission = config.getString(kitPath + ".Permission");

                //defaults kit
                Kit kit = new Kit(kitName, permission);

                //loops through values under the kit name and adds them to the kits contents
                for (String item : this.config.getConfigurationSection(kitPath + ".Items").getKeys(false))
                {
                    String itemPath = kitPath + ".Items." + item;

                    try
                    {
                        Material material = Material.valueOf(item.toUpperCase());
                        int amount = this.config.getInt(itemPath);

                        kit.getContents().put(material, amount);
                    } catch (IllegalArgumentException exception)
                    {
                        PrisonBreak.LOG.severe(item + " is not a valid material");
                    }
                }
                //add kit to general all kits list
                this.kits.add(kit);
            }
        }

        return this.kits;
    }
}
