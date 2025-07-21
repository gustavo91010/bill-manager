package com.ajudaqui.billmanager.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.actuate.info.Info;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
public class CustomInfoContribuitorTest {
@Test
    @DisplayName("Should contribute app and developer details without error")
    void shouldContributeInfo() {
        // Arrange
        CustomInfoContributor contributor = new CustomInfoContributor();

        // injetar valores nos @Value usando reflection
        ReflectionTestUtils.setField(contributor, "environment", "test");
        ReflectionTestUtils.setField(contributor, "description", "Test App");
        ReflectionTestUtils.setField(contributor, "applicationName", "BillManager");
        ReflectionTestUtils.setField(contributor, "version", "1.0.0");
        ReflectionTestUtils.setField(contributor, "contact", "develpd@email.com");

        Info.Builder builder = new Info.Builder();

        // Act
        contributor.contribute(builder);
        Info info = builder.build();

        // Assert
        assertNotNull(info.get("app"));
        assertNotNull(info.get("developer"));
        assertEquals("test", info.get("environment"));
    }
  
}
