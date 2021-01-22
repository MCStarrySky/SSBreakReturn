package me.mical.ssbreakreturn.utils;

import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

public class TempStorage {
    @Getter
    private static final HashMap<UUID, Boolean> dataMap = new HashMap<>();
}
