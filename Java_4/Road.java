import java.util.ArrayList;
import java.util.List;

public class Road {
    private final List<Car<? extends Person>> carsInRoad = new ArrayList<>();

    public void addCarToRoad(Car<? extends Person> car) {
        carsInRoad.add(car);
    }

    public int getCountOfHumans() {
        int count = 0;
        for (Car<? extends Person> car : carsInRoad) {
            count += car.getOccupiedSeats();
        }
        return count;
    }
}
