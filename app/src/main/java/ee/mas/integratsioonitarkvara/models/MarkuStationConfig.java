package ee.mas.integratsioonitarkvara.models;

public class MarkuStationConfig {
    private boolean creepypastaIntro;
    private boolean specialIntro;
    private boolean playIntros;
    private boolean legacyIntro;
    private int monitorMode;

    public boolean isCreepypastaIntro() {
        return creepypastaIntro;
    }

    public void setCreepypastaIntro(boolean creepypastaIntro) {
        this.creepypastaIntro = creepypastaIntro;
    }

    public boolean isSpecialIntro() {
        return specialIntro;
    }

    public void setSpecialIntro(boolean specialIntro) {
        this.specialIntro = specialIntro;
    }

    public boolean isPlayIntro() {
        return playIntros;
    }

    public void setPlayIntro(boolean playIntro) {
        this.playIntros = playIntro;
    }

    public boolean isLegacyIntro() {
        return legacyIntro;
    }

    public void setLegacyIntro(boolean legacyIntro) {
        this.legacyIntro = legacyIntro;
    }

    public int getMonitorMode() {
        return monitorMode;
    }

    public void setMonitorMode(int monitorMode) {
        this.monitorMode = monitorMode;
    }

    public enum MonitorModes {
        INTERNAL,
        EXTERNAL,
        EXTEND,
        CLONE
    }
}
