import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class PersonTest {

    @Test
    public void testEqualsAndHashCode() {
        // Тестування контракту equals та hashCode за допомогою бібліотеки EqualsVerifier
        EqualsVerifier.forClass(Person.class)
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
                .verify();
    }
}
