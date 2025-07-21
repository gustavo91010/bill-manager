package com.ajudaqui.billmanager.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class PaymentTest {

  @Test
@DisplayName("Equals e hashCode devem funcionar para os mesmos valores")
void equalsAndHashCode_shouldBeEqualForSameValues() {
    Payment p1 = new Payment();
    p1.setDescription("desc");
    p1.setValue(BigDecimal.TEN);
    p1.setDueDate(LocalDate.now());

    Payment p2 = new Payment();
    p2.setDescription("desc");
    p2.setValue(BigDecimal.TEN);
    p2.setDueDate(p1.getDueDate());

    assertEquals(p1, p2);
    assertEquals(p1.hashCode(), p2.hashCode());
}

@Test
@DisplayName("Equals deve devolver false para valores diferentes")
void equals_shouldReturnFalseForDifferentValues() {
    Payment p1 = new Payment();
    p1.setDescription("desc1");

    Payment p2 = new Payment();
    p2.setDescription("desc2");

    assertNotEquals(p1, p2);
}

}
