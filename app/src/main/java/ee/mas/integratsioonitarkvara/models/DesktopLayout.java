package ee.mas.integratsioonitarkvara.models;

public class DesktopLayout {
    private boolean ShowIcons;
    private boolean ShowLogo;
    private boolean ShowActions;
    private boolean LockIcons;
    private boolean AcceptCommands;
    private int IconCountX;
    private int IconCountY;
    private int IconSize;
    private int IconPadding;
    private String DesktopDir;
    private DesktopIcon[] Children;
    private SpecialIcon[] SpecialIcons;
    private SpecialIcon Logo;

    public boolean isShowIcons() {
        return ShowIcons;
    }

    public void setShowIcons(boolean showIcons) {
        ShowIcons = showIcons;
    }

    public boolean isShowLogo() {
        return ShowLogo;
    }

    public void setShowLogo(boolean showLogo) {
        ShowLogo = showLogo;
    }

    public boolean isShowActions() {
        return ShowActions;
    }

    public void setShowActions(boolean showActions) {
        ShowActions = showActions;
    }

    public boolean isLockIcons() {
        return LockIcons;
    }

    public void setLockIcons(boolean lockIcons) {
        LockIcons = lockIcons;
    }

    public boolean isAcceptCommands() {
        return AcceptCommands;
    }

    public void setAcceptCommands(boolean acceptCommands) {
        AcceptCommands = acceptCommands;
    }

    public int getIconCountY() {
        return IconCountY;
    }

    public void setIconCountY(int iconCountY) {
        IconCountY = iconCountY;
    }

    public int getIconCountX() {
        return IconCountX;
    }

    public void setIconCountX(int iconCountX) {
        IconCountX = iconCountX;
    }

    public int getIconSize() {
        return IconSize;
    }

    public void setIconSize(int iconSize) {
        IconSize = iconSize;
    }

    public int getIconPadding() {
        return IconPadding;
    }

    public void setIconPadding(int iconPadding) {
        IconPadding = iconPadding;
    }

    public String getDesktopDir() {
        return DesktopDir;
    }

    public void setDesktopDir(String desktopDir) {
        DesktopDir = desktopDir;
    }

    public DesktopIcon[] getChildren() {
        return Children;
    }

    public void setChildren(DesktopIcon[] children) {
        Children = children;
    }

    public SpecialIcon[] getSpecialIcons() {
        return SpecialIcons;
    }

    public void setSpecialIcons(SpecialIcon[] specialIcons) {
        SpecialIcons = specialIcons;
    }

    public SpecialIcon getLogo() {
        return Logo;
    }

    public void setLogo(SpecialIcon logo) {
        Logo = logo;
    }
}
