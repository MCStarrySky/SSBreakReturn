package me.mical.ssbreakreturn.command.subcommand;

import com.sk89q.worldedit.WorldEditException;
import me.mical.ssbreakreturn.features.BuildingControl;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.command.BaseCommand;
import org.serverct.parrot.parrotx.utils.SelectionUtil;
import org.serverct.parrot.parrotx.utils.i18n.I18n;

import java.io.IOException;
import java.util.Objects;

public class DelCommand extends BaseCommand {

    public DelCommand(@NotNull PPlugin plugin, String name, int length) {
        super(plugin, name, length);
        describe("清空一块区域");
        perm("SSBreakReturn.del");
        mustPlayer(true);
    }

    @Override
    protected void call(String[] args) {
        Location loc1 = SelectionUtil.getPos1(user.getUniqueId());
        Location loc2 = SelectionUtil.getPos2(user.getUniqueId());
        if (Objects.nonNull(loc1) && Objects.nonNull(loc2)) {
            try {
                BuildingControl.deleteBuilding(user.getWorld(), loc1, loc2, user);
            } catch (IOException | WorldEditException e) {
                plugin.getLang().log.error(I18n.DELETE, user.getName() + " 选中的区域", e, null);
                I18n.send(user, plugin.getLang().data.build(I18n.Type.ERROR, "清空失败, 请联系管理员."));
            }
        } else {
            I18n.send(user, plugin.getLang().data.build(I18n.Type.WARN, "请先用木剑选区后再进行清空操作."));
        }
    }
}
