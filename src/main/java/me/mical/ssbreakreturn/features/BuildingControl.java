package me.mical.ssbreakreturn.features;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import lombok.NonNull;
import me.mical.ssbreakreturn.SSBreakReturn;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Campfire;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.serverct.parrot.parrotx.api.ParrotXAPI;
import org.serverct.parrot.parrotx.utils.i18n.I18n;

import java.io.IOException;
import java.util.*;

public class BuildingControl {

    public static void deleteBuilding(@NonNull org.bukkit.World world, @NonNull Location loc1, @NonNull Location loc2, @NonNull Player user) throws WorldEditException, IOException {
        SSBreakReturn plugin = ParrotXAPI.getPlugin(SSBreakReturn.class);

        com.sk89q.worldedit.world.World WEWorld = BukkitAdapter.adapt(world);
        BlockVector3 min = BlockVector3.at(loc1.getX(), loc1.getY(), loc1.getZ());
        BlockVector3 max = BlockVector3.at(loc2.getX(), loc2.getY(), loc2.getZ());
        CuboidRegion region = new CuboidRegion(WEWorld, min, max);

        boolean pass = true;

        Map<Material, Integer> blocks = new HashMap<>();
        List<Material> inventoryBlocks = new LinkedList<>();

        root_loop:
        for (BlockVector3 v : region) {
            Material material = BukkitAdapter.adapt(world, v).getBlock().getType();
            if (material == Material.BEDROCK) {
                pass = false;
                I18n.send(user, plugin.getLang().data.build(I18n.Type.WARN, "区域内存在基岩, 清空建筑失败."));
                break;
            }
            if (material == Material.BARRIER) {
                pass = false;
                I18n.send(user, plugin.getLang().data.build(I18n.Type.WARN, "区域内存在屏障, 清空建筑失败."));
                break;
            }
            if (material == Material.COMMAND_BLOCK || material == Material.CHAIN_COMMAND_BLOCK || material == Material.REPEATING_COMMAND_BLOCK) {
                pass = false;
                I18n.send(user, plugin.getLang().data.build(I18n.Type.WARN, "区域内存在命令方块, 清空建筑失败."));
                break;
            } else if (material == Material.STRUCTURE_BLOCK || material == Material.STRUCTURE_VOID) {
                pass = false;
                I18n.send(user, plugin.getLang().data.build(I18n.Type.WARN, "区域内存在结构方块, 清空建筑失败."));
                break;
            }
            if (material == Material.END_PORTAL_FRAME) {
                pass = false;
                I18n.send(user, plugin.getLang().data.build(I18n.Type.WARN, "区域内存在末地传送门, 清空建筑失败."));
                break;
            }
            {
                final BlockState state = BukkitAdapter.adapt(world, v).getBlock().getState();
                if (state instanceof Jukebox) {
                    Jukebox jukebox = (Jukebox) state;
                    final ItemStack record = jukebox.getRecord();
                    if (!Objects.equals(record.getType(), Material.AIR)) {
                        if (!inventoryBlocks.contains(material)) {
                            pass = false;
                            inventoryBlocks.add(material);
                            I18n.send(user, plugin.getLang().data.build(I18n.Type.WARN, "区域内存在可存放物品的方块(&c" + material.name() + "&r), 清空建筑失败."));
                        }
                    }
                }
                if (state instanceof InventoryHolder) {
                    Inventory inv = ((InventoryHolder) state).getInventory();
                    for (ItemStack itemStack : inv.getContents()) {
                        if (itemStack == null || itemStack.getType() == Material.AIR) continue;
                        if (!inventoryBlocks.contains(material)) {
                            pass = false;
                            inventoryBlocks.add(material);
                            I18n.send(user, plugin.getLang().data.build(I18n.Type.WARN, "区域内存在可存放物品的方块(&c" + material.name() + "&r), 清空建筑失败."));
                            continue root_loop;
                        }
                    }
                }
                if (state instanceof Campfire) {
                    Campfire campfire = (Campfire) state;
                    int size = campfire.getSize();
                    for (int index = 0; index < size; index++) {
                        ItemStack item = campfire.getItem(index);
                        if (item != null && item.getType() != Material.AIR) {
                            if (!inventoryBlocks.contains(material)) {
                                pass = false;
                                inventoryBlocks.add(material);
                                I18n.send(user, plugin.getLang().data.build(I18n.Type.WARN, "区域内存在可存放物品的方块(&c" + material.name() + "&r), 清空建筑失败."));
                                continue root_loop;
                            }
                        }
                    }
                }
            }

            blocks.put(material, blocks.getOrDefault(material, 0) + 1);
        }

        if (pass) {
            I18n.send(user, plugin.getLang().data.build(I18n.Type.INFO, "方块数量统计:"));
            for (Material material : blocks.keySet()) {
                String report = " &c" + material + " &r▶ " + "&c" + blocks.get(material) + " &r块.";
                I18n.send(user, I18n.color(report));
            }

            region.forEach(blockVector3 -> BukkitAdapter.adapt(world, blockVector3).getBlock().setType(Material.AIR));
            if (Objects.nonNull(plugin.getCoreProtect())) {
                for (BlockVector3 v : region) {
                    Material m = BukkitAdapter.adapt(user.getWorld(), v).getBlock().getType();
                    plugin.getCoreProtect().logRemoval("#[SSBreakReturn]" + user.getName(), new Location(user.getWorld(), v.getBlockX(), v.getBlockY(), v.getBlockZ()), m, new Location(user.getWorld(), v.getBlockX(), v.getBlockY(), v.getBlockZ()).getBlock().getBlockData());
                }
            }


            I18n.send(user, plugin.getLang().data.build(I18n.Type.INFO, "清空区域成功, 原材料将添加至您的背包. 如果您的背包已满, 会丢在地上."));
            blocks.forEach((material, integer) -> {
                if (material != Material.AIR) {
                    ItemStack item = new ItemStack(material, integer);
                    HashMap<Integer, ItemStack> out = user.getInventory().addItem(item);

                    if (!out.isEmpty()) {
                        for (Map.Entry<Integer, ItemStack> entry : out.entrySet()) {
                            user.getWorld().dropItem(user.getLocation(), entry.getValue());
                        }
                    }
                }
            });
        }
    }
}
