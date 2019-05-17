package net.prisontech.prisonbreak.blackmarket.listener;

import net.prisontech.prisonbreak.api.PrisonBreak;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class ShopListener implements Listener
{
    private Inventory inventory;

    @EventHandler
    public void onNPCClick(PlayerInteractAtEntityEvent interactEvent)
    {
        Entity entity = interactEvent.getRightClicked();
        Player player = interactEvent.getPlayer();

        if (entity.getType() == EntityType.VILLAGER)
        {
            for (Map.Entry<Location, String> npcEntry : PrisonBreak.getBlackMarketRegistry().getNPCs().entrySet())
            {
                if (entity.getLocation().distanceSquared(npcEntry.getKey()) <= 1 && entity.getCustomName().equalsIgnoreCase(npcEntry.getValue()))
                {
                    interactEvent.setCancelled(true);

                    double gainedMoney = 0;

                    for (ItemStack item : player.getInventory().getContents())
                    {
                        if (item != null && item.getType() != null)
                        {
                            int itemPrice = PrisonBreak.getBlackMarketRegistry().getItemPrices().get(item.getType());
                            double boostPercent = 1 + ((double) PrisonBreak.getBlackMarketRegistry().getBoostPercent(player) / 100);

                            double newItemPrice = itemPrice * boostPercent;

                            gainedMoney += newItemPrice;
                            player.getInventory().remove(item);
                        }
                    }

                    final double finalGainedMoney = gainedMoney;

                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
                                DataOutputStream msgout = new DataOutputStream(msgbytes);

                                msgout.writeDouble(finalGainedMoney);

                                PrisonBreak.getChannelAPI().forward(PrisonBreak.getMainConfig().getMainServerName(), "money", msgbytes.toByteArray());
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }.runTaskLater(PrisonBreak.getPlugin(), 40);
                }
            }
        }
    }
}
