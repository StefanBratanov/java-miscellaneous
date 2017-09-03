package rx.observer_pattern;

import lombok.Data;

import java.util.Observable;
import java.util.Observer;

@Data
class Student implements Observer {

    private final String name;
    private final Integer age;

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MessageBoard) {
            MessageBoard messageBoard = (MessageBoard) o;
            System.out.println("Hey " + name + " ,message board has been updated:");
            System.out.println(messageBoard.getMessage());
        }
        if (o instanceof FootballBroadcaster) {
            FootballBroadcaster footballBroadcaster = (FootballBroadcaster) o;
            System.out.println("Hey " + name + " ,football broadcast update:");
            System.out.println(footballBroadcaster.getBroadcastMessage());
        }
    }
}
