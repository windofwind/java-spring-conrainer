package info.addline.acution.user;

import info.addline.acution.user.dto.UserRegistrationDto;
import info.addline.acution.user.dto.UserResponseDto;
import info.addline.acution.user.dto.UserUpdateDto;
import info.addline.acution.user.entity.Profile;
import info.addline.acution.user.entity.User;
import info.addline.acution.user.entity.UserAccount;
import info.addline.acution.repository.ProfileRepository;
import info.addline.acution.repository.UserAccountRepository;
import info.addline.acution.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 통합 사용자 관리 비즈니스 로직을 처리하는 서비스 클래스입니다.
 *
 * <p>이 서비스는 통합 로그인을 지원하는 사용자 시스템의 핵심 비즈니스 로직을 담당하며,
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
 * @see UserRepository
 * @see ProfileRepository
 * @see UserAccountRepository
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final UserAccountRepository userAccountRepository;

    /**
     * UserService 생성자입니다.
     *
     * @param userRepository 사용자 데이터 액세스 레포지토리
     * @param profileRepository 프로필 데이터 액세스 레포지토리
     * @param userAccountRepository 계정 연동 데이터 액세스 레포지토리
     */
    @Autowired
    public UserService(UserRepository userRepository,
                      ProfileRepository profileRepository,
                      UserAccountRepository userAccountRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.userAccountRepository = userAccountRepository;
    }

    /**
     * 새로운 통합 사용자를 생성합니다.
     *
     * @param primaryEmail 주 이메일 주소
     * @return 생성된 사용자 정보
     * @throws RuntimeException 이메일이 이미 존재할 때
     */
    public UserResponseDto createUser(String primaryEmail) {
        if (userRepository.existsByPrimaryEmail(primaryEmail)) {
            throw new RuntimeException("이미 존재하는 이메일입니다: " + primaryEmail);
        }

        User user = new User(primaryEmail);
        user.setStatus(User.UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        // 기본 프로필 생성
        Profile profile = new Profile(savedUser);
        profileRepository.save(profile);

        return new UserResponseDto(savedUser);
    }

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
    public UserResponseDto createOrGetUserFromSocial(
            UserAccount.OAuthProvider provider,
            String providerAccountId,
            String providerEmail,
            String providerName,
            String providerPicture) {

        // 기존 소셜 계정 연동 확인
        Optional<UserAccount> existingAccount = userAccountRepository
                .findByProviderAndProviderAccountId(provider, providerAccountId);

        if (existingAccount.isPresent()) {
            return new UserResponseDto(existingAccount.get().getUser());
        }

        // 이메일로 기존 사용자 확인
        User user;
        Optional<User> existingUser = userRepository.findByPrimaryEmail(providerEmail);

        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            // 새 사용자 생성
            user = new User(providerEmail);
            user.setEmailVerified(true); // 소셜 계정은 기본적으로 인증됨
            user = userRepository.save(user);

            // 기본 프로필 생성
            Profile profile = new Profile(user);
            if (providerName != null) {
                profile.setDisplayName(providerName);
                profile.setFullName(providerName);
            }
            if (providerPicture != null) {
                profile.setProfileImageUrl(providerPicture);
            }
            profileRepository.save(profile);
        }

        // 소셜 계정 연동 생성
        UserAccount userAccount = new UserAccount(user, provider, providerAccountId);
        userAccount.setProviderEmail(providerEmail);
        userAccount.setProviderName(providerName);
        userAccount.setProviderPicture(providerPicture);
        userAccountRepository.save(userAccount);

        return new UserResponseDto(user);
    }

    /**
     * ID로 사용자 정보를 조회합니다.
     *
     * @param id 조회할 사용자의 CUID
     * @return 사용자 정보를 담은 DTO
     * @throws RuntimeException 해당 ID의 사용자가 존재하지 않을 때
     */
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));
        return new UserResponseDto(user);
    }

    /**
     * 주 이메일로 사용자 정보를 조회합니다.
     *
     * @param primaryEmail 조회할 주 이메일
     * @return 사용자 정보를 담은 DTO
     * @throws RuntimeException 해당 이메일의 사용자가 존재하지 않을 때
     */
    @Transactional(readOnly = true)
    public UserResponseDto getUserByPrimaryEmail(String primaryEmail) {
        User user = userRepository.findByPrimaryEmail(primaryEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + primaryEmail));
        return new UserResponseDto(user);
    }

    /**
     * 소셜 계정 정보로 사용자를 조회합니다.
     *
     * @param provider OAuth 제공자
     * @param providerAccountId 제공자 계정 ID
     * @return 사용자 정보를 담은 DTO
     * @throws RuntimeException 해당 소셜 계정의 사용자가 존재하지 않을 때
     */
    @Transactional(readOnly = true)
    public UserResponseDto getUserBySocialAccount(UserAccount.OAuthProvider provider, String providerAccountId) {
        User user = userRepository.findByProviderAccount(provider, providerAccountId)
                .orElseThrow(() -> new RuntimeException("연동된 사용자를 찾을 수 없습니다: " + provider + "/" + providerAccountId));
        return new UserResponseDto(user);
    }

    /**
     * 모든 활성 사용자 목록을 조회합니다.
     *
     * @return 활성 사용자 DTO 목록
     */
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllActiveUsers() {
        return userRepository.findActiveUsers()
                .stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 이메일 인증된 활성 사용자 목록을 조회합니다.
     *
     * @return 인증된 활성 사용자 DTO 목록
     */
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllVerifiedUsers() {
        return userRepository.findActiveVerifiedUsers()
                .stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 이메일로 사용자를 검색합니다.
     *
     * @param keyword 검색할 키워드
     * @return 검색된 사용자 DTO 목록
     */
    @Transactional(readOnly = true)
    public List<UserResponseDto> searchUsersByEmail(String keyword) {
        return userRepository.searchByEmail(keyword)
                .stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 사용자의 이메일 인증 상태를 업데이트합니다.
     *
     * @param id 사용자 CUID
     * @param verified 인증 상태
     * @return 업데이트된 사용자 정보
     * @throws RuntimeException 사용자를 찾을 수 없을 때
     */
    public UserResponseDto updateEmailVerificationStatus(String id, boolean verified) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));

        user.setEmailVerified(verified);
        User updatedUser = userRepository.save(user);
        return new UserResponseDto(updatedUser);
    }

    /**
     * 사용자의 주 이메일을 변경합니다.
     *
     * @param id 사용자 CUID
     * @param newPrimaryEmail 새로운 주 이메일
     * @return 업데이트된 사용자 정보
     * @throws RuntimeException 사용자를 찾을 수 없거나 이메일이 중복될 때
     */
    public UserResponseDto updatePrimaryEmail(String id, String newPrimaryEmail) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));

        if (!newPrimaryEmail.equals(user.getPrimaryEmail())) {
            if (userRepository.existsByPrimaryEmail(newPrimaryEmail)) {
                throw new RuntimeException("이미 존재하는 이메일입니다: " + newPrimaryEmail);
            }
            user.setPrimaryEmail(newPrimaryEmail);
            user.setEmailVerified(false); // 새 이메일은 재인증 필요
        }

        User updatedUser = userRepository.save(user);
        return new UserResponseDto(updatedUser);
    }

    /**
     * 사용자를 논리적으로 삭제합니다.
     *
     * @param id 삭제할 사용자의 CUID
     * @throws RuntimeException 해당 ID의 사용자가 존재하지 않을 때
     */
    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));

        user.setStatus(User.UserStatus.DELETED);
        userRepository.save(user);
    }

    /**
     * 사용자를 데이터베이스에서 영구 삭제합니다.
     *
     * @param id 영구 삭제할 사용자의 CUID
     * @throws RuntimeException 해당 ID의 사용자가 존재하지 않을 때
     */
    public void permanentlyDeleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * 사용자의 상태를 변경합니다.
     *
     * @param id 사용자 CUID
     * @param status 새로운 상태
     * @return 업데이트된 사용자 정보
     * @throws RuntimeException 사용자를 찾을 수 없을 때
     */
    public UserResponseDto updateUserStatus(String id, User.UserStatus status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));

        user.setStatus(status);
        User updatedUser = userRepository.save(user);
        return new UserResponseDto(updatedUser);
    }

    /**
     * 사용자의 연동된 소셜 계정 목록을 조회합니다.
     *
     * @param id 사용자 CUID
     * @return 연동된 계정 목록
     * @throws RuntimeException 사용자를 찾을 수 없을 때
     */
    @Transactional(readOnly = true)
    public List<UserAccount> getUserSocialAccounts(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));

        return userAccountRepository.findByUser(user);
    }

    /**
     * 소셜 계정 연동을 해제합니다.
     *
     * @param userId 사용자 CUID
     * @param provider OAuth 제공자
     * @throws RuntimeException 사용자나 연동된 계정을 찾을 수 없을 때
     */
    public void unlinkSocialAccount(String userId, UserAccount.OAuthProvider provider) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userId));

        UserAccount account = userAccountRepository.findByUserAndProvider(user, provider)
                .orElseThrow(() -> new RuntimeException("연동된 계정을 찾을 수 없습니다: " + provider));

        account.setStatus(UserAccount.AccountStatus.REVOKED);
        userAccountRepository.save(account);
    }
}