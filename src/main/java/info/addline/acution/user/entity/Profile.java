package info.addline.acution.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import cool.graph.cuid.Cuid;

import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * 사용자 프로필 정보를 나타내는 JPA 엔티티 클래스입니다.
 *
 * <p>이 엔티티는 사용자의 개인 프로필 정보를 관리하며,
 * User 엔티티와 분리되어 프로필 관련 정보만을 담당합니다.</p>
 *
 * <p>주요 특징:</p>
 * <ul>
 *   <li>User 엔티티와 1:1 관계</li>
 *   <li>개인정보 (이름, 닉네임, 생년월일 등)</li>
 *   <li>연락처 정보 (전화번호, 주소 등)</li>
 *   <li>프로필 이미지 및 소개</li>
 *   <li>생성/수정 시간 자동 관리</li>
 * </ul>
 *
 * @author Acution Development Team
 * @version 1.0
 * @since 1.0
 * @see User
 */
@Entity
@Table(name = "profiles")
public class Profile {

    /**
     * 프로필의 고유 식별자입니다.
     * CUID를 사용하여 자동으로 생성되는 기본 키입니다.
     */
    @Id
    @Column(name = "id", updatable = false, nullable = false, length = 25)
    private String id;

    /**
     * 프로필이 속한 사용자입니다.
     * User 엔티티와 1:1 관계를 맺습니다.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    /**
     * 사용자의 표시 이름(닉네임)입니다.
     * 시스템에서 사용자를 식별하는 공개적인 이름입니다.
     */
    @Column(name = "display_name", length = 50)
    @Size(max = 50, message = "표시 이름은 50자를 초과할 수 없습니다")
    private String displayName;

    /**
     * 사용자의 실제 이름입니다.
     */
    @Column(name = "full_name", length = 100)
    @Size(max = 100, message = "실제 이름은 100자를 초과할 수 없습니다")
    private String fullName;

    /**
     * 사용자의 생년월일입니다.
     */
    @Column(name = "birth_date")
    private LocalDate birthDate;

    /**
     * 사용자의 성별입니다.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender;

    /**
     * 사용자의 전화번호입니다.
     */
    @Column(name = "phone_number", length = 20)
    @Size(max = 20, message = "전화번호는 20자를 초과할 수 없습니다")
    private String phoneNumber;

    /**
     * 사용자의 주소입니다.
     */
    @Column(name = "address", length = 500)
    @Size(max = 500, message = "주소는 500자를 초과할 수 없습니다")
    private String address;

    /**
     * 사용자의 프로필 이미지 URL입니다.
     */
    @Column(name = "profile_image_url", length = 500)
    @Size(max = 500, message = "프로필 이미지 URL은 500자를 초과할 수 없습니다")
    private String profileImageUrl;

    /**
     * 사용자의 자기소개입니다.
     */
    @Column(name = "bio", length = 1000)
    @Size(max = 1000, message = "자기소개는 1000자를 초과할 수 없습니다")
    private String bio;

    /**
     * 사용자의 웹사이트 URL입니다.
     */
    @Column(name = "website_url", length = 500)
    @Size(max = 500, message = "웹사이트 URL은 500자를 초과할 수 없습니다")
    private String websiteUrl;

    /**
     * 사용자의 위치 정보입니다.
     */
    @Column(name = "location", length = 100)
    @Size(max = 100, message = "위치는 100자를 초과할 수 없습니다")
    private String location;

    /**
     * 프로필 생성 시간입니다.
     * Hibernate에 의해 자동으로 설정되며, 수정 불가능합니다.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 프로필 정보 최종 수정 시간입니다.
     * Hibernate에 의해 자동으로 업데이트됩니다.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 성별을 나타내는 열거형입니다.
     */
    public enum Gender {
        MALE, FEMALE, OTHER, PREFER_NOT_TO_SAY
    }

    /**
     * 기본 생성자입니다.
     * JPA에서 요구하는 기본 생성자입니다.
     */
    public Profile() {
        this.id = Cuid.createCuid();
    }

    /**
     * 사용자와 함께 프로필을 생성하는 생성자입니다.
     *
     * @param user 프로필이 속할 사용자
     */
    public Profile(User user) {
        this();
        this.user = user;
    }

    // Getters and Setters

    /**
     * 프로필 ID를 반환합니다.
     *
     * @return 프로필의 고유 식별자(CUID)
     */
    public String getId() {
        return id;
    }

    /**
     * 프로필 ID를 설정합니다.
     *
     * @param id 설정할 프로필 CUID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 프로필이 속한 사용자를 반환합니다.
     *
     * @return 프로필 소유자
     */
    public User getUser() {
        return user;
    }

    /**
     * 프로필이 속한 사용자를 설정합니다.
     *
     * @param user 설정할 사용자
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 표시 이름을 반환합니다.
     *
     * @return 사용자의 표시 이름(닉네임)
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 표시 이름을 설정합니다.
     *
     * @param displayName 설정할 표시 이름
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 실제 이름을 반환합니다.
     *
     * @return 사용자의 실제 이름
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 실제 이름을 설정합니다.
     *
     * @param fullName 설정할 실제 이름
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * 생년월일을 반환합니다.
     *
     * @return 사용자의 생년월일
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * 생년월일을 설정합니다.
     *
     * @param birthDate 설정할 생년월일
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * 성별을 반환합니다.
     *
     * @return 사용자의 성별
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * 성별을 설정합니다.
     *
     * @param gender 설정할 성별
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * 전화번호를 반환합니다.
     *
     * @return 사용자의 전화번호
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * 전화번호를 설정합니다.
     *
     * @param phoneNumber 설정할 전화번호
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * 주소를 반환합니다.
     *
     * @return 사용자의 주소
     */
    public String getAddress() {
        return address;
    }

    /**
     * 주소를 설정합니다.
     *
     * @param address 설정할 주소
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 프로필 이미지 URL을 반환합니다.
     *
     * @return 프로필 이미지 URL
     */
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    /**
     * 프로필 이미지 URL을 설정합니다.
     *
     * @param profileImageUrl 설정할 프로필 이미지 URL
     */
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    /**
     * 자기소개를 반환합니다.
     *
     * @return 사용자의 자기소개
     */
    public String getBio() {
        return bio;
    }

    /**
     * 자기소개를 설정합니다.
     *
     * @param bio 설정할 자기소개
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * 웹사이트 URL을 반환합니다.
     *
     * @return 사용자의 웹사이트 URL
     */
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    /**
     * 웹사이트 URL을 설정합니다.
     *
     * @param websiteUrl 설정할 웹사이트 URL
     */
    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    /**
     * 위치를 반환합니다.
     *
     * @return 사용자의 위치
     */
    public String getLocation() {
        return location;
    }

    /**
     * 위치를 설정합니다.
     *
     * @param location 설정할 위치
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * 생성 시간을 반환합니다.
     *
     * @return 프로필 생성 시간
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 생성 시간을 설정합니다.
     *
     * @param createdAt 설정할 생성 시간
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 최종 수정 시간을 반환합니다.
     *
     * @return 프로필 최종 수정 시간
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 최종 수정 시간을 설정합니다.
     *
     * @param updatedAt 설정할 수정 시간
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
