
package info.addline.acution.sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.ArgumentCaptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest는 웹 계층(Controller)에만 집중하여 테스트합니다.
// 테스트할 컨트롤러를 명시해줍니다.
@SpringBootTest
@AutoConfigureMockMvc
class SampleControllerTest {

    // MockMvc는 실제 서버를 띄우지 않고 HTTP 요청을 보내고 응답을 검증할 수 있게 해줍니다.
    @Autowired
    private MockMvc mockMvc;

    // ObjectMapper는 Java 객체와 JSON 간의 변환을 처리합니다.
    @Autowired
    private ObjectMapper objectMapper;

    // @MockBean은 실제 SampleService 대신 가짜(Mock) 객체를 Spring 컨테이너에 등록합니다.
    // 이를 통해 서비스 계층의 로직과 독립적으로 컨트롤러만 테스트할 수 있습니다.
    @MockBean
    private SampleService sampleService;

    @Test
    @DisplayName("POST /api/sample/process 요청 시, SampleData를 받아 성공 메시지를 반환한다")
    void processSampleData_ShouldReturnSuccessMessage() throws Exception {
        // given - 테스트 준비
        // 요청으로 보낼 SampleData 객체를 생성합니다.
        SampleData requestData = new SampleData("Test User", "test@example.com");
        // 객체를 JSON 문자열로 변환합니다.
        String requestBody = objectMapper.writeValueAsString(requestData);
        // 예상되는 응답 메시지를 정의합니다.
        String expectedResponse = "Data for 'Test User' processed successfully.";

        // then - 실제 요청 및 검증
        mockMvc.perform(post("/api/sample/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        // ArgumentCaptor를 사용하여 실제로 서비스 메소드에 전달된 SampleData 객체를 캡처합니다.
        ArgumentCaptor<SampleData> captor = ArgumentCaptor.forClass(SampleData.class);
        verify(sampleService).processSample(captor.capture());

        // 캡처된 객체의 내용이 올바른지 검증합니다.
        SampleData capturedData = captor.getValue();
        assertThat(capturedData.getName()).isEqualTo("Test User");
        assertThat(capturedData.getEmail()).isEqualTo("test@example.com");
    }
}
