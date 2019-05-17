package net.prisontech.prisonbreak.loot.listeners;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.chat.ClickEvent;
import net.prisontech.prisonbreak.api.IPrisonBreakMain;
import net.prisontech.prisonbreak.api.PrisonBreak;
import net.prisontech.prisonbreak.api.kits.IKit;
import net.prisontech.prisonbreak.api.util.Strings;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachment;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TestListener implements Listener
{
    private Set<PermissionAttachment> permissions = Sets.newHashSet();

    @EventHandler
    public void test(PlayerJoinEvent joinEvent)
    {
        Player player = joinEvent.getPlayer();
    }

    @EventHandler
    public void onKitGain(InventoryClickEvent clickEvent)
    {
        Player player = (Player) clickEvent.getWhoClicked();
        ItemStack currentItem = clickEvent.getCurrentItem();
        ItemStack cursor = clickEvent.getCursor();

        if (currentItem != null && currentItem.hasItemMeta())
        {
            ItemMeta itemMeta = currentItem.getItemMeta();

            if (itemMeta.hasDisplayName())
            {
                IKit kit = PrisonBreak.getKitRegistry().getKit(ChatColor.stripColor(itemMeta.getDisplayName()));

                if (kit != null)
                {
                    clickEvent.setCancelled(true);

                    if (!PrisonBreak.getKitRegistry().getAvailableKits(player.getUniqueId()).contains(kit))
                    {
                        try
                        {
                            PrisonBreak.getKitRegistry().saveKit(kit, player.getUniqueId());
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        player.sendMessage(Strings.handleError("Sorry, you already have this kit", "already have"));
                    }

                    clickEvent.getClickedInventory().remove(currentItem);
                    player.getInventory().remove(currentItem);
                }
            }
        }
    }

    @EventHandler
    public void on(PlayerBedEnterEvent toggleFlightEvent)
    {
        try
        {
            ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
            DataOutputStream msgout = new DataOutputStream(msgbytes);
            msgout.writeInt(120);
            PrisonBreak.getChannelAPI().forward(PrisonBreak.getMainConfig().getMainServerName(), "money", msgbytes.toByteArray());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
