package net.andrespr.casinorocket.games.slot;

import com.mojang.authlib.GameProfile;
import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.util.TextUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;
import java.util.function.ToLongFunction;

public class SlotUtils {

    public static Text getLeaderboardText(MinecraftServer server, String key) {
        PlayerSlotMachineData data = PlayerSlotMachineData.get(server);

        String k = key.toLowerCase(Locale.ROOT);

        ToLongFunction<UUID> valueFn = switch (k) {
            case "highest_win" -> data::getHighestWin;
            case "total_win" -> data::getTotalWon;
            case "total_lost" -> data::getTotalLost;
            default -> null;
        };

        if (valueFn == null) {
            return Text.literal("Invalid key.").formatted(Formatting.RED);
        }

        List<Map.Entry<UUID, Long>> rows = new ArrayList<>();
        for (UUID id : data.getAllKnownPlayers()) {
            long v = valueFn.applyAsLong(id);

            if (k.equals("total_lost")) {
                if (v >= 0) continue;
            } else {
                if (v <= 0) continue;
            }

            rows.add(new AbstractMap.SimpleEntry<>(id, v));
        }

        if (rows.isEmpty()) {
            return Text.literal("No leaderboard entries yet.").formatted(Formatting.GRAY);
        }

        Comparator<Map.Entry<UUID, Long>> cmp = Comparator.comparingLong(Map.Entry::getValue);
        if (!k.equals("total_lost")) cmp = cmp.reversed();
        rows.sort(cmp);

        int limit = Math.min(10, rows.size());
        List<Map.Entry<UUID, Long>> top = rows.subList(0, limit);

        String titleLabel = switch (k) {
            case "highest_win" -> "Slots - Highest Win";
            case "total_win" -> "Slots - Total Won";
            case "total_lost" -> "Slots - Total Lost";
            default -> "Slots Leaderboard";
        };

        Formatting titleColor = switch (k) {
            case "highest_win" -> Formatting.GOLD;
            case "total_win" -> Formatting.GREEN;
            case "total_lost" -> Formatting.RED;
            default -> Formatting.YELLOW;
        };

        MutableText out = Text.literal("\n")
                .append(Text.literal("Top 10 - " + titleLabel).formatted(titleColor, Formatting.BOLD))
                .append(Text.literal("\n"));

        for (int i = 0; i < top.size(); i++) {
            int rank = i + 1;
            UUID id = top.get(i).getKey();
            long value = top.get(i).getValue();

            String name = resolveName(server, id);

            out.append(Text.literal(rank + ". ").formatted(Formatting.YELLOW))
                    .append(Text.literal(name).formatted(TextUtils.rankColors(rank)))
                    .append(Text.literal(" - ").formatted(Formatting.GRAY))
                    .append(Text.literal(formatSignedMoney(value)).formatted(Formatting.YELLOW));

            if (rank < top.size()) out.append(Text.literal("\n"));
        }

        return out;
    }

    private static String resolveName(MinecraftServer server, UUID id) {
        ServerPlayerEntity p = server.getPlayerManager().getPlayer(id);
        if (p != null) return p.getName().getString();

        var cache = server.getUserCache();
        if (cache != null) {
            Optional<GameProfile> profile = cache.getByUuid(id);
            if (profile.isPresent() && profile.get().getName() != null) {
                return profile.get().getName();
            }
        }

        return shortUuid(id);
    }

    private static String shortUuid(UUID id) {
        String s = id.toString().replace("-", "");
        return s.substring(0, 8);
    }

    private static String formatSignedMoney(long value) {
        if (value < 0) return "-" + TextUtils.formatCompactNoDecimal(-value);
        return TextUtils.formatCompactNoDecimal(value);
    }

}