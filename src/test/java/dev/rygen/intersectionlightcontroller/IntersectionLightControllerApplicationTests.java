package dev.rygen.intersectionlightcontroller;

import dev.rygen.intersectionlightcontroller.config.TestConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
@Disabled
class IntersectionLightControllerApplicationTests {

	@Test
	void contextLoads() {
	}

}
