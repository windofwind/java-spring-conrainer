package info.addline.acution.sample;

import org.springframework.stereotype.Service;

@Service
public class SampleService {

    /**
     * SampleData 객체를 받아 비즈니스 로직을 처리하는 메소드입니다.
     * 실제 애플리케이션에서는 이 곳에 데이터베이스 저장, 다른 API 호출 등의 로직이 들어갑니다.
     * @param data 처리할 데이터
     */
    public void processSample(SampleData data) {
        System.out.println("\n--- [SampleService] Processing Data ---");
        System.out.println("Received Name: " + data.name());
        System.out.println("Received Email: " + data.email());
        System.out.println("--- End of Processing ---\\n");
    }
}
