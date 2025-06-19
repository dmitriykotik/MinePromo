package org.dmitriykotik.minePromo;

public class Promo {
    private String code;
    private long usesLeft;
    private long expiresAt;
    private String commands;
    private long usedCount;

    public Promo(String code, long usesLeft, long expiresAt, String commands, long usedCount) {
        this.code = code;
        this.usesLeft = usesLeft;
        this.expiresAt = expiresAt;
        this.commands = commands;
        this.usedCount = usedCount;
    }

    public String getCode() { return code; }
    public long getUsesLeft() { return usesLeft; }
    public long getExpiresAt() { return expiresAt; }
    public String getCommands() { return commands; }
    public long getUsedCount() { return usedCount; }

    public boolean isExpired() {
        return expiresAt != -1 && System.currentTimeMillis() > expiresAt;
    }

    public boolean canUse() {
        return !isExpired() && (usesLeft == -1 || usedCount < usesLeft);
    }

    public void applyUse() {
        if (usesLeft > -1) usedCount++;
    }
}