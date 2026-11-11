import java.util.ArrayList;
import java.util.List;

class Person {
    private final String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class Firefighter extends Person {
    public Firefighter(String name) {
        super(name);
    }
}

class PoliceOfficer extends Person {
    public PoliceOfficer(String name) {
        super(name);
    }
}

abstract class Vehicle<T extends Person> {
    private final int maxSeats;
    private final List<T> passengers = new ArrayList<>();

    public Vehicle(int maxSeats) {
        if (maxSeats <= 0) {
            throw new IllegalArgumentException("Кількість місць має бути додатною");
        }
        this.maxSeats = maxSeats;
    }

    public int getMaxSeats() {
        return maxSeats;
    }

    public int getOccupiedSeats() {
        return passengers.size();
    }

    public void addPassenger(T passenger) {
        if (passenger == null) {
            throw new IllegalArgumentException("Пасажир не може бути null");
        }
        if (passengers.size() == maxSeats) {
            throw new IllegalStateException("Немає вільних місць");
        }
        passengers.add(passenger);
    }

    public void removePassenger(T passenger) {
        if (!passengers.remove(passenger)) {
            throw new IllegalArgumentException("Пасажира немає у транспорті");
        }
    }
}

abstract class Car<T extends Person> extends Vehicle<T> {
    public Car(int maxSeats) {
        super(maxSeats);
    }
}

class Bus<T extends Person> extends Vehicle<T> {
    public Bus(int maxSeats) {
        super(maxSeats);
    }
}

class Taxi<T extends Person> extends Car<T> {
    public Taxi(int maxSeats) {
        super(maxSeats);
    }
}

class FireTruck extends Car<Firefighter> {
    public FireTruck(int maxSeats) {
        super(maxSeats);
    }
}

class PoliceCar extends Car<PoliceOfficer> {
    public PoliceCar(int maxSeats) {
        super(maxSeats);
    }
}

