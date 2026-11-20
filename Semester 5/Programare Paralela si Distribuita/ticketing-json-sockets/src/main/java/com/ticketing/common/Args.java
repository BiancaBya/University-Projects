package com.ticketing.common;

import java.util.HashMap;
import java.util.Map;

public final class Args {
    private Args() {}

    public static Map<String, String> parse(String[] args) {
        Map<String, String> m = new HashMap<>();
        for (String a : args) {
            if (a.startsWith("--")) {
                int idx = a.indexOf('=');
                if (idx > 2) {
                    m.put(a.substring(2, idx), a.substring(idx + 1));
                } else {
                    // allow: --key value
                    m.put(a.substring(2), "true");
                }
            }
        }
        // support --key value form
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].startsWith("--") && !args[i].contains("=")) {
                String k = args[i].substring(2);
                String v = args[i + 1];
                if (!v.startsWith("--")) {
                    m.put(k, v);
                }
            }
        }
        return m;
    }

    public static int getInt(Map<String, String> m, String key, int def) {
        try { return Integer.parseInt(m.getOrDefault(key, String.valueOf(def))); }
        catch (Exception e) { return def; }
    }

    public static long getLong(Map<String, String> m, String key, long def) {
        try { return Long.parseLong(m.getOrDefault(key, String.valueOf(def))); }
        catch (Exception e) { return def; }
    }

    public static String getStr(Map<String, String> m, String key, String def) {
        return m.getOrDefault(key, def);
    }
}
