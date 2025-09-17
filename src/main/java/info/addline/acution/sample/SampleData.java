package info.addline.acution.sample;

/**
 * 클라이언트로부터 받을 name, email 데이터를 담는 record 입니다.
 * Java 16+ 부터 사용 가능하며, 불변(immutable) 데이터 객체를 간결하게 생성할 수 있습니다.
 *
 * @param name 사용자 이름
 * @param email 사용자 이메일
 */
public record SampleData(String name, String email) {}
