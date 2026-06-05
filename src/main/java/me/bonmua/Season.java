package me.bonmua;

public enum Season {

    XUAN("Xuân", "<gradient:#a8edea:#fed6e3>🌸 Mùa Xuân 🌸</gradient>"),
    HA("Hạ", "<gradient:#f7971e:#ffd200>☀️ Mùa Hạ ☀️</gradient>"),
    THU("Thu", "<gradient:#f4791f:#659999>🍂 Mùa Thu 🍂</gradient>"),
    DONG("Đông", "<gradient:#e0eafc:#cfdef3>❄️ Mùa Đông ❄️</gradient>");

    private final String displayName;
    private final String miniMessageTag;

    Season(String displayName, String miniMessageTag) {
        this.displayName = displayName;
        this.miniMessageTag = miniMessageTag;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getMiniMessageTag() {
        return miniMessageTag;
    }

    public Season next() {
        Season[] values = Season.values();
        return values[(this.ordinal() + 1) % values.length];
    }
}
