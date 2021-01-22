package me.mical.ssbreakreturn.listener;

import me.mical.ssbreakreturn.SSBreakReturn;
import me.mical.ssbreakreturn.config.ConfigManager;
import me.mical.ssbreakreturn.utils.TempStorage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.serverct.parrot.parrotx.api.ParrotXAPI;
import org.serverct.parrot.parrotx.data.autoload.Autoload;
import org.serverct.parrot.parrotx.utils.BasicUtil;
import org.serverct.parrot.parrotx.utils.SelectionUtil;
import org.serverct.parrot.parrotx.utils.i18n.I18n;

import java.util.Objects;
import java.util.UUID;

@Autoload
public class SelectionListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player user = event.getPlayer();
        UUID uuid = user.getUniqueId();
        if (TempStorage.getDataMap().containsKey(uuid) && TempStorage.getDataMap().get(uuid)) {
            if (Objects.nonNull(event.getItem())) {
                if (Objects.equals(event.getItem().getType(), ConfigManager.getInstance().getMaterial())) {
                    if (Objects.nonNull(event.getClickedBlock())) {
                        Location loc = event.getClickedBlock().getLocation();
                        if (Objects.equals(event.getAction(), Action.LEFT_CLICK_BLOCK)) {
                            SelectionUtil.setPos1(uuid, loc);
                            I18n.send(user, ParrotXAPI.getPlugin(SSBreakReturn.class).getLang().data.build(I18n.Type.INFO, "已选择点&c{0}&7(&c{1}&7).", 1, BasicUtil.formatLocation(loc)));
                        } else {
                            SelectionUtil.setPos2(uuid, loc);
                            I18n.send(user, ParrotXAPI.getPlugin(SSBreakReturn.class).getLang().data.build(I18n.Type.INFO, "已选择点&c{0}&7(&c{1}&7).", 2, BasicUtil.formatLocation(loc)));
                        }
                    }
                }
            }
        }
    }
}
