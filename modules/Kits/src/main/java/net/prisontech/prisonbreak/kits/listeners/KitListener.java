package net.prisontech.prisonbreak.kits.listeners;

import com.google.common.collect.Lists;
import net.prisontech.prisonbreak.api.PrisonBreak;
import net.prisontech.prisonbreak.api.kits.IKit;
import net.prisontech.prisonbreak.api.util.Strings;
import net.prisontech.prisonbreak.kits.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KitListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent joinEvent)
    {
        Player player = joinEvent.getPlayer();

        List<IKit> kits = PrisonBreak.getKitRegistry().getKits();

        for (IKit kit : kits)
        {
            player.getInventory().addItem(kit.createSlip());
        }


        player.teleport(PrisonBreak.getWorld().getSpawnLocation());
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent interactEvent)
    {
        Player player = interactEvent.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (interactEvent.getAction() == Action.RIGHT_CLICK_AIR || interactEvent.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if (item != null && item.getType() == Material.PAPER)
            {
                if (item.hasItemMeta())
                {
                    ItemMeta itemMeta = item.getItemMeta();

                    if (itemMeta.getDisplayName() != null)
                    {
                        String strippedItemName = ChatColor.stripColor(itemMeta.getDisplayName());

                        IKit kit = PrisonBreak.getKitRegistry().getKit(strippedItemName);

                        if (kit != null)
                        {
                            if (player.hasPermission(kit.getPermission()))
                            {
                                player.getInventory().remove(item);

                                for (Map.Entry<Material, Integer> kitEntry : kit.getContents().entrySet())
                                {
                                    PrisonBreak.LOG.severe(kitEntry.getKey().name());
                                    player.getInventory().addItem(new ItemStack(kitEntry.getKey(), kitEntry.getValue()));
                                }
                            } else
                            {
                                player.sendMessage(Strings.handleError("You don't have the permission for this kit", "don't have", "permission"));
                            }
                        }
                    }
                }
            }
        }
    }
}
