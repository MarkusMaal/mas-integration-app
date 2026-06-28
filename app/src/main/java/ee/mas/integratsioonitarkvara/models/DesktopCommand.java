package ee.mas.integratsioonitarkvara.models;

public class DesktopCommand {
    private String Type;
    private String Arguments;

    public String getArguments() {
        return Arguments;
    }

    public void setArguments(String arguments) {
        Arguments = arguments;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
