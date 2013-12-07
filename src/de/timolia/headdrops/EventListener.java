/*
 *  Copyright:
 *  2013 Darius Mewes
 */

package de.timolia.headdrops;

import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Skull;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import de.timolia.headdrops.cmds.headinfo;

public class EventListener implements Listener {

    private HeadDrops instance;
    private Random rand = new Random();

    public EventListener(HeadDrops instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event) {
        if (event.getBlock().getType() == Material.SKULL && headinfo.isActive(event.getPlayer())) {
            Skull skull = (Skull) event.getBlock().getState();
            if (skull.getSkullType() == SkullType.PLAYER && skull.hasOwner() && !SkullManager.isSkullCustom(skull.getOwner())) {
                event.getPlayer().sendMessage(HeadDrops.PREFIX + (skull.getOwner() != null ? ("This is " + skull.getOwner() + (skull.getOwner().endsWith("s") || skull.getOwner().endsWith("S") ? "'" : "'s") + " head.") : "This head is unknown..."));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.SKULL && event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            Skull skull = (Skull) event.getBlock().getState();
            if (headinfo.isActive(event.getPlayer())) {
                if (skull.getSkullType() == SkullType.PLAYER && skull.hasOwner() && !SkullManager.isSkullCustom(skull.getOwner())) {
                    event.getPlayer().sendMessage(HeadDrops.PREFIX + (skull.getOwner() != null ? ("This is " + skull.getOwner() + (skull.getOwner().endsWith("s") || skull.getOwner().endsWith("S") ? "'" : "'s") + " head.") : "This head is unknown..."));
                    event.setCancelled(true);
                }
            } else {
                if (skull.getSkullType() == SkullType.PLAYER) {
                    if (skull.getOwner().equalsIgnoreCase("_Luna00_") || skull.getOwner().equalsIgnoreCase("Ex_PS3Zocker")) {
                        event.setCancelled(true);
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), SkullManager.getCustomSkull(CustomSkullType.SLIME));
                    } else if (skull.getOwner().equalsIgnoreCase("Blaze_Head")) {
                        event.setCancelled(true);
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), SkullManager.getCustomSkull(CustomSkullType.BLAZE));
                    } else if (skull.getOwner().equalsIgnoreCase("Kelevra_V")) {
                        event.setCancelled(true);
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), SkullManager.getCustomSkull(CustomSkullType.SPIDER));
                    } else if (skull.getOwner().equalsIgnoreCase("Violit")) {
                        event.setCancelled(true);
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), SkullManager.getCustomSkull(CustomSkullType.ENDERMAN));
                    } else if (SkullManager.isSkullCustom(skull.getOwner())) {
                        event.setCancelled(true);
                        event.getBlock().setType(Material.AIR);
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), SkullManager.getCustomSkull(CustomSkullType.forSkinName(skull.getOwner())));
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (HeadDrops.updateAvailable && (event.getPlayer().isOp() || event.getPlayer().hasPermission("headdrops.update"))) {
            event.getPlayer().sendMessage(HeadDrops.PREFIX + "A new version is available!");
            event.getPlayer().sendMessage(HeadDrops.PREFIX + "Get it at http://dev.bukkit.org/server-mods/head-drops");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getLastDamageCause() == null)
            return;

        if (event.getEntity().getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK && event.getEntity().getKiller() != null) {
            if (instance.getConfig().getBoolean("permissionCheckMob") && !event.getEntity().getKiller().hasPermission("headdrops.mobhead"))
                return;

            EntityType t = event.getEntity().getType();
            if (t == EntityType.SKELETON && ((Skeleton) event.getEntity()).getSkeletonType() == SkeletonType.NORMAL && chance("skeleton"))
                event.getDrops().add(new ItemStack(Material.SKULL_ITEM, 1, (byte) 0));
            else if (t == EntityType.SKELETON && ((Skeleton) event.getEntity()).getSkeletonType() == SkeletonType.WITHER && chance("witherskeleton"))
                event.getDrops().add(new ItemStack(Material.SKULL_ITEM, 1, (byte) 1));
            else if (t == EntityType.ZOMBIE && chance("zombie"))
                event.getDrops().add(new ItemStack(Material.SKULL_ITEM, 1, (byte) 2));
            else if (t == EntityType.BLAZE && chance("blaze"))
                event.getDrops().add(SkullManager.getCustomSkull(CustomSkullType.BLAZE));
            else if (t == EntityType.CAVE_SPIDER && chance("cavespider"))
                event.getDrops().add(SkullManager.getCustomSkull(CustomSkullType.CAVE_SPIDER));
            else if (t == EntityType.CHICKEN && chance("chicken"))
                event.getDrops().add(SkullManager.getCustomSkull(CustomSkullType.CHICKEN));
            else if (t == EntityType.COW && chance("cow"))
                event.getDrops().add(SkullManager.getCustomSkull(CustomSkullType.COW));
            else if (t == EntityType.ENDERMAN && chance("enderman"))
                event.getDrops().add(SkullManager.getCustomSkull(CustomSkullType.ENDERMAN));
            else if (t == EntityType.GHAST && chance("ghast"))
                event.getDrops().add(SkullManager.getCustomSkull(CustomSkullType.GHAST));
            else if (t == EntityType.IRON_GOLEM && chance("irongolem"))
                event.getDrops().add(SkullManager.getCustomSkull(CustomSkullType.IRON_GOLEM));
            else if (t == EntityType.MAGMA_CUBE && chance("magmacube"))
                event.getDrops().add(SkullManager.getCustomSkull(CustomSkullType.MAGMA_CUBE));
            else if (t == EntityType.MUSHROOM_COW && chance("mushroomcow"))
                event.getDrops().add(SkullManager.getCustomSkull(CustomSkullType.MUSHROOM_COW));
            else if (t == EntityType.OCELOT && chance("ocelot"))
                event.getDrops().add(SkullManager.getCustomSkull(CustomSkullType.OCELOT));
            else if (t == EntityType.PIG && chance("pig"))
                event.getDrops().add(SkullManager.getCustomSkull(CustomSkullType.PIG));
            else if (t == EntityType.PIG_ZOMBIE && chance("pigzombie"))
                event.getDrops().add(SkullManager.getCustomSkull(CustomSkullType.PIG_ZOMBIE));
            else if (t == EntityType.SHEEP && chance("sheep"))
                event.getDrops().add(SkullManager.getCustomSkull(CustomSkullType.SHEEP));
            else if (t == EntityType.SLIME && chance("slime"))
                event.getDrops().add(SkullManager.getCustomSkull(CustomSkullType.SLIME));
            else if (t == EntityType.SPIDER && chance("spider"))
                event.getDrops().add(SkullManager.getCustomSkull(CustomSkullType.SPIDER));
            else if (t == EntityType.SQUID && chance("squid"))
                event.getDrops().add(SkullManager.getCustomSkull(CustomSkullType.SQUID));
            else if (t == EntityType.VILLAGER && chance("villager"))
                event.getDrops().add(SkullManager.getCustomSkull(CustomSkullType.VILLAGER));
            else if (t == EntityType.WITHER && chance("wither"))
                event.getDrops().add(SkullManager.getCustomSkull(CustomSkullType.WITHER));
            else if (t == EntityType.CREEPER && chance("creeper"))
                event.getDrops().add(new ItemStack(Material.SKULL_ITEM, 1, (byte) 4));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (chance("player") && event.getEntity().getLastDamageCause() != null && event.getEntity().getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK && killer != null) {
            if ((instance.getConfig().getBoolean("permissionCheckPlayer") && !event.getEntity().getKiller().hasPermission("headdrops.playerhead")) || (instance.getConfig().getBoolean("ironanddiamond") && !(killer.getItemInHand().getType() == Material.IRON_SWORD || killer.getItemInHand().getType() == Material.DIAMOND_SWORD || (instance.getConfig().getBoolean("axeenabled") && (killer.getItemInHand().getType() == Material.IRON_AXE || killer.getItemInHand().getType() == Material.DIAMOND_AXE)))))
                return;

            event.getDrops().add(instance.getConfig().getBoolean("dropBlank") ? new ItemStack(Material.SKULL_ITEM, 1, (byte) 3) : SkullManager.getSkinnedHead(event.getEntity().getName()));
        }
    }

    private boolean chance(String name) {
        return rand.nextInt(100) < instance.getConfig().getInt(name + "drop");
    }

}