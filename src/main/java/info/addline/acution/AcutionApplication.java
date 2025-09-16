package info.addline.acution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Acution 애플리케이션의 메인 클래스입니다.
 *
 * <p>이 클래스는 Spring Boot 애플리케이션의 진입점 역할을 하며,
 * 사용자 관리 기능(회원가입, 로그인, 회원탈퇴, 프로필 관리)을 제공하는
 * 웹 애플리케이션을 시작합니다.</p>
 *
 * <p>사용 기술 스택:</p>
 * <ul>
 *   <li>Java 17</li>
 *   <li>Spring Boot 3.5.5</li>
 *   <li>PostgreSQL</li>
 *   <li>JPA/Hibernate</li>
 *   <li>Swagger/OpenAPI</li>
 * </ul>
 *
 * @author Acution Development Team
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class AcutionApplication {

  /**
   * 애플리케이션의 메인 메서드입니다.
   *
   * <p>Spring Boot 애플리케이션을 시작하고 내장 서버를 구동합니다.
   * 기본적으로 8080 포트에서 서비스가 시작되며, application.properties 설정에 따라 변경 가능합니다.</p>
   *
   * @param args 명령행 인수 배열
   */
  public static void main(String[] args) {
    SpringApplication.run(AcutionApplication.class, args);
  }
}
