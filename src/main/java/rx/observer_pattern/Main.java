package rx.observer_pattern;

class Main {

    public static void main(String[] args) {

        MessageBoard messageBoard = new MessageBoard("No homework");
        FootballBroadcaster footballBroadcaster = new FootballBroadcaster("No update");
        Student student = new Student("Stefan",12);

        Student student2 = new Student("John",44);

        messageBoard.addObserver(student);
        messageBoard.addObserver(student2);
        footballBroadcaster.addObserver(student);
        footballBroadcaster.addObserver(student2);

        messageBoard.setMessage("More homework");
        footballBroadcaster.setBroadcastMessage("Man Utd on top of the league!!!");
    }
}
