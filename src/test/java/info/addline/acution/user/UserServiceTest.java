package info.addline.acution.user;

import info.addline.acution.repository.ProfileRepository;
import info.addline.acution.repository.UserAccountRepository;
import info.addline.acution.repository.UserRepository;
import info.addline.acution.user.dto.UserRegistrationDto;
import info.addline.acution.user.dto.UserResponseDto;
import info.addline.acution.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Test
    @DisplayName("새로운 사용자를 성공적으로 등록한다")
    void registerUser_Success() {
        // given
        UserRegistrationDto registrationDto = new UserRegistrationDto("testuser", "test@example.com", "password123");
        registrationDto.setFullName("Test User");

        when(userRepository.existsByPrimaryEmail(registrationDto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId("test-cuid");
            return user;
        });

        // when
        UserResponseDto responseDto = userService.registerUser(registrationDto);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getPrimaryEmail()).isEqualTo(registrationDto.getEmail());
        verify(profileRepository).save(any(info.addline.acution.user.entity.Profile.class));
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 등록 시 예외가 발생한다")
    void registerUser_EmailAlreadyExists_ShouldThrowException() {
        // given
        UserRegistrationDto registrationDto = new UserRegistrationDto("testuser", "test@example.com", "password123");
        when(userRepository.existsByPrimaryEmail(registrationDto.getEmail())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.registerUser(registrationDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 존재하는 이메일입니다: " + registrationDto.getEmail());
    }
}
