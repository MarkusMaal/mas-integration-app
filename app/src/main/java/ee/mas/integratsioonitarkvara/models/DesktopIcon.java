package ee.mas.integratsioonitarkvara.models;

public class DesktopIcon {
    private String Icon;
    private String Executable;
    private int LocationX;
    private int LocationY;

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getExecutable() {
        return Executable;
    }

    public void setExecutable(String executable) {
        Executable = executable;
    }

    public int getLocationX() {
        return LocationX;
    }

    public void setLocationX(int locationX) {
        LocationX = locationX;
    }

    public int getLocationY() {
        return LocationY;
    }

    public void setLocationY(int locationY) {
        LocationY = locationY;
    }
}
