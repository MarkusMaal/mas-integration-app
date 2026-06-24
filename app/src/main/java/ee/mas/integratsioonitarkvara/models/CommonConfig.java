package ee.mas.integratsioonitarkvara.models;

public class CommonConfig {
    private boolean AutostartNotes;
    private boolean AllowScheduledTasks;
    private boolean ShowLogo;
    private int PollRate;

    public boolean isAutostartNotes() {
        return AutostartNotes;
    }

    public void setAutostartNotes(boolean autostartNotes) {
        AutostartNotes = autostartNotes;
    }

    public boolean isAllowScheduledTasks() {
        return AllowScheduledTasks;
    }

    public void setAllowScheduledTasks(boolean allowScheduledTasks) {
        AllowScheduledTasks = allowScheduledTasks;
    }

    public boolean isShowLogo() {
        return ShowLogo;
    }

    public void setShowLogo(boolean showLogo) {
        ShowLogo = showLogo;
    }

    public int getPollRate() {
        return PollRate;
    }

    public void setPollRate(int pollRate) {
        PollRate = pollRate;
    }
}
