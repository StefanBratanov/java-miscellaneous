package rx.observer_pattern;

import lombok.Getter;

import java.util.Observable;

@Getter
public class FootballBroadcaster extends Observable {

    private String broadcastMessage;

    public FootballBroadcaster(String broadcastMessage) {
        this.broadcastMessage = broadcastMessage;
    }

    public void setBroadcastMessage(String broadcastMessage) {
        this.broadcastMessage = broadcastMessage;
        setChanged();
        notifyObservers();
    }
}
