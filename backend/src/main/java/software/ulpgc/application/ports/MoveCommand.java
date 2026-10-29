package software.ulpgc.application.ports;

public record MoveCommand(String from, String to, String promotion) {
    public MoveCommand (String from, String to){
        this(from, to, null);
    }
}
