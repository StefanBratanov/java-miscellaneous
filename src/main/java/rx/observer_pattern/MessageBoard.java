package rx.observer_pattern;

import lombok.Getter;
import lombok.ToString;

import java.util.Observable;

@ToString
@Getter
class MessageBoard extends Observable {

    private String message;

    MessageBoard(String message) {
        this.message = message;
    }

    void setMessage(String message) {
        this.message = message;
        setChanged();
        notifyObservers(message);
    }
}
