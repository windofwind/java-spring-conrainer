
package info.addline.acution.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * 클라이언트로부터 받을 name, email 데이터를 담는 DTO 클래스입니다.
 */
public final class SampleData {
    private String name;
    private String email;

    // Jackson 역직렬화를 위한 기본 생성자
    private SampleData() {}

    // 테스트 및 다른 곳에서 사용할 생성자
    @JsonCreator
    public SampleData(@JsonProperty("name") String name, @JsonProperty("email") String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SampleData) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }

    @Override
    public String toString() {
        return "SampleData[" +
                "name=" + name + ", " +
                "email=" + email + ']';
    }
}
