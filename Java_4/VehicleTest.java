public class VehicleTest {
    private static int passed = 0;

    private static void check(boolean result) {
        if (!result) {
            throw new AssertionError("Тест не пройдено");
        }
        passed++;
    }

    private static void expectError(Class<? extends Exception> type, Runnable action) {
        try {
            action.run();
            throw new AssertionError("Очікувався виняток " + type.getSimpleName());
        } catch (Exception e) {
            check(type.isInstance(e));
        }
    }

    public static void main(String[] args) {
        Bus<Person> bus = new Bus<>(3);
        Person person = new Person("Анна");
        Firefighter firefighter = new Firefighter("Олег");
        PoliceOfficer officer = new PoliceOfficer("Ігор");

        bus.addPassenger(person);
        bus.addPassenger(firefighter);
        bus.addPassenger(officer);

        check(bus.getMaxSeats() == 3);
        check(bus.getOccupiedSeats() == 3);
        expectError(IllegalStateException.class, () -> bus.addPassenger(new Person("Марта")));

        bus.removePassenger(person);
        check(bus.getOccupiedSeats() == 2);
        expectError(IllegalArgumentException.class, () -> bus.removePassenger(person));

        Taxi<Person> taxi = new Taxi<>(2);
        taxi.addPassenger(new Person("Марія"));
        taxi.addPassenger(new PoliceOfficer("Павло"));
        check(taxi.getOccupiedSeats() == 2);

        FireTruck truck = new FireTruck(1);
        truck.addPassenger(new Firefighter("Максим"));
        check(truck.getOccupiedSeats() == 1);
        expectError(IllegalStateException.class, () -> truck.addPassenger(new Firefighter("Юрій")));

        PoliceCar policeCar = new PoliceCar(2);
        PoliceOfficer policeOfficer = new PoliceOfficer("Сергій");
        policeCar.addPassenger(policeOfficer);
        check(policeCar.getOccupiedSeats() == 1);

        policeCar.removePassenger(policeOfficer);
        check(policeCar.getOccupiedSeats() == 0);

        Road road = new Road();
        road.addCarToRoad(taxi);
        road.addCarToRoad(truck);
        road.addCarToRoad(policeCar);

        check(road.getCountOfHumans() == 3);
        expectError(IllegalArgumentException.class, () -> taxi.removePassenger(new PoliceOfficer("Інший")));
        expectError(IllegalArgumentException.class, () -> new Taxi<Person>(0));

        System.out.println("Успішно пройдено тестів: " + passed);
    }
}

