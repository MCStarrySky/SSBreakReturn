package me.mical.ssbreakreturn.command.subcommand;

import me.mical.ssbreakreturn.utils.TempStorage;
import org.jetbrains.annotations.NotNull;
import org.serverct.parrot.parrotx.PPlugin;
import org.serverct.parrot.parrotx.command.BaseCommand;
import org.serverct.parrot.parrotx.utils.i18n.I18n;

public class SelectCommand extends BaseCommand {
    public SelectCommand(@NotNull PPlugin plugin, String name, int length) {
        super(plugin, name, length);
        mustPlayer(true);
        perm("SSBreakReturn.select");
        describe("开启/关闭选区模式");
    }

    @Override
    protected void call(String[] args) {
        if (!TempStorage.getDataMap().containsKey(user.getUniqueId()) || !TempStorage.getDataMap().get(user.getUniqueId())) {
            TempStorage.getDataMap().put(user.getUniqueId(), true);
            I18n.send(user, plugin.getLang().data.build(I18n.Type.INFO, "已开启选区模式."));
        } else if (TempStorage.getDataMap().get(user.getUniqueId())) {
            TempStorage.getDataMap().put(user.getUniqueId(), false);
            I18n.send(user, plugin.getLang().data.build(I18n.Type.INFO, "已关闭选区模式."));
        }
    }
}
