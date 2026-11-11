public class Main {
    public static void main(String[] args) {
        Bus<Person> bus = new Bus<>(20);
        Taxi<Person> taxi = new Taxi<>(4);
        FireTruck fireTruck = new FireTruck(5);
        PoliceCar policeCar = new PoliceCar(4);

        bus.addPassenger(new Person("Андрій"));
        bus.addPassenger(new Firefighter("Олег"));
        bus.addPassenger(new PoliceOfficer("Іван"));

        Person maria = new Person("Марія");
        taxi.addPassenger(maria);
        taxi.addPassenger(new Firefighter("Петро"));
        fireTruck.addPassenger(new Firefighter("Максим"));
        policeCar.addPassenger(new PoliceOfficer("Сергій"));

        System.out.println("Автобус: " + bus.getOccupiedSeats() + "/" + bus.getMaxSeats());
        System.out.println("Таксі: " + taxi.getOccupiedSeats() + "/" + taxi.getMaxSeats());
        System.out.println("Пожежна машина: " + fireTruck.getOccupiedSeats() + "/" + fireTruck.getMaxSeats());
        System.out.println("Поліцейська машина: " + policeCar.getOccupiedSeats() + "/" + policeCar.getMaxSeats());

        Road road = new Road();
        road.addCarToRoad(taxi);
        road.addCarToRoad(fireTruck);
        road.addCarToRoad(policeCar);
        System.out.println("Людей в автомобілях на дорозі: " + road.getCountOfHumans());

        taxi.removePassenger(maria);
        System.out.println("Після висадки Марії: " + road.getCountOfHumans());
    }
}

