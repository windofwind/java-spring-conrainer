package info.addline.acution.sample;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class SampleServiceTest {

    @Autowired
    private SampleService sampleService;

    @Test
    @DisplayName("SampleData를 처리할 때 예외가 발생하지 않는다")
    void processSample_ShouldNotThrowException() {
        // given
        SampleData sampleData = new SampleData("Test Name", "test@example.com");

        // when & then
        assertDoesNotThrow(() -> {
            sampleService.processSample(sampleData);
        });
    }
}
