package net.pl3x.bukkit.util;

public class SignData {
    private String[] lines;
    private final int x;
    private final int y;
    private final int z;

    public SignData(String[] lines, int x, int y, int z) {
        this.lines = lines;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String[] getLines() {
        return lines;
    }

    public void setLines(String[] lines) {
        this.lines = lines;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "SignData[Lines:[\"" + lines[0] + "\",\"" + lines[1] + "\",\"" + lines[2] + "\",\"" + lines[3] + "\"],X:" + x + ",Y:" + y + ",Z:" + z + "]";
    }
}
