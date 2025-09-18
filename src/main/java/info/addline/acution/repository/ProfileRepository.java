package info.addline.acution.repository;

import info.addline.acution.user.entity.Profile;
import info.addline.acution.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 프로필 데이터 접근을 위한 JPA 리포지토리 인터페이스입니다.
 *
 * <p>이 인터페이스는 Spring Data JPA의 JpaRepository를 확장하여
 * Profile 엔티티에 대한 기본적인 CRUD 작업과 함께 프로필 관련 특화된 쿼리 메서드들을 제공합니다.</p>
 *
 * <p>주요 기능:</p>
 * <ul>
 *   <li>기본 CRUD 작업 (JpaRepository 제공)</li>
 *   <li>사용자별 프로필 조회</li>
 *   <li>표시 이름 기반 검색</li>
 *   <li>위치별 프로필 검색</li>
 * </ul>
 *
 * @author Acution Development Team
 * @version 1.0
 * @since 1.0
 * @see Profile
 * @see JpaRepository
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {

    /**
     * 사용자로 프로필을 조회합니다.
     *
     * @param user 조회할 사용자
     * @return 해당 사용자의 프로필 (Optional)
     */
    Optional<Profile> findByUser(User user);

    /**
     * 사용자 ID로 프로필을 조회합니다.
     *
     * @param userId 조회할 사용자 ID
     * @return 해당 사용자의 프로필 (Optional)
     */
    Optional<Profile> findByUserId(String userId);

    /**
     * 표시 이름으로 프로필을 조회합니다.
     *
     * @param displayName 조회할 표시 이름
     * @return 해당 표시 이름의 프로필 (Optional)
     */
    Optional<Profile> findByDisplayName(String displayName);

    /**
     * 표시 이름 존재 여부를 확인합니다.
     *
     * @param displayName 확인할 표시 이름
     * @return 표시 이름이 존재하면 true, 그렇지 않으면 false
     */
    boolean existsByDisplayName(String displayName);

    /**
     * 표시 이름에서 키워드를 포함하는 프로필들을 검색합니다.
     *
     * @param keyword 검색할 키워드
     * @return 키워드를 포함하는 프로필 목록
     */
    @Query("SELECT p FROM Profile p WHERE p.displayName LIKE %:keyword%")
    List<Profile> searchByDisplayName(@Param("keyword") String keyword);

    /**
     * 위치별로 프로필을 조회합니다.
     *
     * @param location 조회할 위치
     * @return 해당 위치의 프로필 목록
     */
    List<Profile> findByLocationContainingIgnoreCase(String location);

    /**
     * 생년월일 범위로 프로필을 조회합니다.
     *
     * @param startYear 시작 연도
     * @param endYear 종료 연도
     * @return 해당 연도 범위에 태어난 사용자의 프로필 목록
     */
    @Query("SELECT p FROM Profile p WHERE YEAR(p.birthDate) BETWEEN :startYear AND :endYear")
    List<Profile> findByBirthYearRange(@Param("startYear") int startYear, @Param("endYear") int endYear);

    /**
     * 프로필 이미지가 있는 프로필들을 조회합니다.
     *
     * @return 프로필 이미지가 설정된 프로필 목록
     */
    @Query("SELECT p FROM Profile p WHERE p.profileImageUrl IS NOT NULL AND p.profileImageUrl != ''")
    List<Profile> findProfilesWithImage();

    /**
     * 삭제된 프로필들을 조회합니다.
     *
     * @return 삭제된 프로필 목록
     */
    @Query("SELECT p FROM Profile p WHERE p.deletedAt IS NOT NULL")
    List<Profile> findAllDeleted();

    /**
     * 삭제된 프로필을 포함하여 모든 프로필을 조회합니다.
     *
     * @return 모든 프로필 목록 (삭제된 것도 포함)
     */
    @Query("SELECT p FROM Profile p")
    List<Profile> findAllIncludingDeleted();

    /**
     * ID로 프로필을 복원합니다.
     *
     * @param id 복원할 프로필 ID
     */
    @Query("UPDATE Profile p SET p.deletedAt = NULL WHERE p.id = :id")
    List<Profile> restoreById(@Param("id") String id);
}
