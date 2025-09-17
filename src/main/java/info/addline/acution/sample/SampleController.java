package info.addline.acution.sample;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/sample")
@Tag(name = "Sample", description = "테스트용 API 목록")
public class SampleController {

    private final SampleService sampleService;

    // 생성자를 통한 의존성 주입 (권장 방식)
    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    /**
     * JSON 형태의 요청 본문을 SampleData 객체로 변환하여 받는 POST 엔드포인트입니다.
     * @param data Spring에 의해 JSON에서 Java 객체로 자동 변환된 데이터
     * @return 처리 성공 메시지
     */
    @PostMapping("/process")
    public ResponseEntity<String> processSampleData(@RequestBody SampleData data) {
        sampleService.processSample(data);
        String responseMessage = "Data for '" + data.name() + "' processed successfully.";
        return ResponseEntity.ok(responseMessage);
    }
}
