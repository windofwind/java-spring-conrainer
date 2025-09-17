package info.addline.acution.user;

import info.addline.acution.user.dto.UserRegistrationDto;
import info.addline.acution.user.dto.UserLoginDto;
import info.addline.acution.user.dto.UserResponseDto;
import info.addline.acution.user.entity.UserAccount;

import java.util.List;

/**
 * 통합 사용자 관리 서비스 인터페이스입니다.
 *
 * <p>이 인터페이스는 통합 로그인을 지원하는 사용자 시스템의 핵심 비즈니스 로직을 정의하며,
 * 일반 회원가입, 소셜 로그인, 프로필 관리 등의 기능을 제공합니다.</p>
 *
 * <p>주요 기능:</p>
 * <ul>
 *   <li>통합 사용자 등록 및 관리</li>
 *   <li>소셜 계정 연동 및 해제</li>
 *   <li>사용자 정보 조회 및 검색</li>
 *   <li>사용자 상태 관리</li>
 *   <li>프로필 정보 관리</li>
 * </ul>
 *
 * @author Acution Development Team
 * @version 2.0
 * @since 2.0
 */
public interface UserServiceInterface {

    /**
     * 새로운 통합 사용자를 생성합니다.
     *
     * @param primaryEmail 주 이메일 주소
     * @return 생성된 사용자 정보
     * @throws RuntimeException 이메일이 이미 존재할 때
     */
    UserResponseDto createUser(String primaryEmail);

    /**
     * 새로운 사용자를 등록합니다.
     *
     * @param registrationDto 회원가입 요청 정보
     * @return 등록된 사용자 정보
     * @throws RuntimeException 이메일이 이미 존재하거나 유효성 검사 실패 시
     */
    UserResponseDto registerUser(UserRegistrationDto registrationDto);

    /**
     * 사용자 로그인을 처리합니다.
     *
     * @param loginDto 로그인 요청 정보 (이메일, 비밀번호)
     * @return 인증된 사용자 정보
     * @throws RuntimeException 인증 실패 시
     */
    UserResponseDto loginUser(UserLoginDto loginDto);

    /**
     * 소셜 계정으로 사용자를 생성하거나 기존 사용자를 반환합니다.
     *
     * @param provider OAuth 제공자
     * @param providerAccountId 제공자 계정 ID
     * @param providerEmail 제공자에서 제공하는 이메일
     * @param providerName 제공자에서 제공하는 이름
     * @param providerPicture 제공자에서 제공하는 프로필 이미지
     * @return 사용자 정보 (새로 생성되거나 기존 사용자)
     */
    UserResponseDto createOrGetUserFromSocial(
            UserAccount.OAuthProvider provider,
            String providerAccountId,
            String providerEmail,
            String providerName,
            String providerPicture);

    /**
     * ID로 사용자 정보를 조회합니다.
     *
     * @param id 조회할 사용자의 CUID
     * @return 사용자 정보를 담은 DTO
     * @throws RuntimeException 해당 ID의 사용자가 존재하지 않을 때
     */
    UserResponseDto getUserById(String id);

    /**
     * 주 이메일로 사용자 정보를 조회합니다.
     *
     * @param primaryEmail 조회할 주 이메일
     * @return 사용자 정보를 담은 DTO
     * @throws RuntimeException 해당 이메일의 사용자가 존재하지 않을 때
     */
    UserResponseDto getUserByPrimaryEmail(String primaryEmail);

    /**
     * 소셜 계정 정보로 사용자를 조회합니다.
     *
     * @param provider OAuth 제공자
     * @param providerAccountId 제공자 계정 ID
     * @return 사용자 정보를 담은 DTO
     * @throws RuntimeException 해당 소셜 계정의 사용자가 존재하지 않을 때
     */
    UserResponseDto getUserBySocialAccount(UserAccount.OAuthProvider provider, String providerAccountId);

    /**
     * 모든 활성 사용자 목록을 조회합니다.
     *
     * @return 활성 사용자 DTO 목록
     */
    List<UserResponseDto> getAllActiveUsers();

    /**
     * 이메일 인증된 활성 사용자 목록을 조회합니다.
     *
     * @return 인증된 활성 사용자 DTO 목록
     */
    List<UserResponseDto> getAllVerifiedUsers();

    /**
     * 이메일로 사용자를 검색합니다.
     *
     * @param keyword 검색할 키워드
     * @return 검색된 사용자 DTO 목록
     */
    List<UserResponseDto> searchUsersByEmail(String keyword);

    /**
     * 사용자의 이메일 인증 상태를 업데이트합니다.
     *
     * @param id 사용자 CUID
     * @param verified 인증 상태
     * @return 업데이트된 사용자 정보
     * @throws RuntimeException 사용자를 찾을 수 없을 때
     */
    UserResponseDto updateEmailVerificationStatus(String id, boolean verified);

    /**
     * 사용자의 주 이메일을 변경합니다.
     *
     * @param id 사용자 CUID
     * @param newPrimaryEmail 새로운 주 이메일
     * @return 업데이트된 사용자 정보
     * @throws RuntimeException 사용자를 찾을 수 없거나 이메일이 중복될 때
     */
    UserResponseDto updatePrimaryEmail(String id, String newPrimaryEmail);

    /**
     * 사용자를 논리적으로 삭제합니다.
     *
     * @param id 삭제할 사용자의 CUID
     * @throws RuntimeException 해당 ID의 사용자가 존재하지 않을 때
     */
    void deleteUser(String id);

    /**
     * 사용자를 데이터베이스에서 영구 삭제합니다.
     *
     * @param id 영구 삭제할 사용자의 CUID
     * @throws RuntimeException 해당 ID의 사용자가 존재하지 않을 때
     */
    void permanentlyDeleteUser(String id);

    /**
     * 사용자의 상태를 변경합니다.
     *
     * @param id 사용자 CUID
     * @param status 새로운 상태
     * @return 업데이트된 사용자 정보
     * @throws RuntimeException 사용자를 찾을 수 없을 때
     */
    UserResponseDto updateUserStatus(String id, String status);

    /**
     * 사용자의 연동된 소셜 계정 목록을 조회합니다.
     *
     * @param id 사용자 CUID
     * @return 연동된 계정 목록
     * @throws RuntimeException 사용자를 찾을 수 없을 때
     */
    List<UserAccount> getUserSocialAccounts(String id);

    /**
     * 소셜 계정 연동을 해제합니다.
     *
     * @param userId 사용자 CUID
     * @param provider OAuth 제공자
     * @throws RuntimeException 사용자나 연동된 계정을 찾을 수 없을 때
     */
    void unlinkSocialAccount(String userId, UserAccount.OAuthProvider provider);
}