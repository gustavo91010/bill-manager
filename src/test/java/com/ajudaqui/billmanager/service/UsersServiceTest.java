package com.ajudaqui.billmanager.service;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.times;

import com.ajudaqui.billmanager.config.serucity.JwtUtils;
import com.ajudaqui.billmanager.entity.Users;
import com.ajudaqui.billmanager.exception.NotFoundEntityException;
import com.ajudaqui.billmanager.repository.UsersRepository;
import com.ajudaqui.billmanager.service.vo.UserUpdateVo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
public class UsersServiceTest {
  @InjectMocks
  private UsersService usersService;
  @Mock
  private UsersRepository usersRepository;

  @Mock
  private JwtUtils jwtUtils;
  @Captor
  ArgumentCaptor<Users> usersCaptor;

  @Test
  void shouldReturnTrue_whenUserIsCreatedSuccessfully() {
    String auth = "valid-secret";
    String accessToken = "token123";

    ReflectionTestUtils.setField(usersService, "secretKey", auth);

    when(usersRepository.findByAccessToken(accessToken)).thenReturn(empty());
    when(usersRepository.save(any())).thenAnswer(inv -> {
      Users u = inv.getArgument(0);
      u.setId(1L);
      return u;
    });
    assertTrue(usersService.register(auth, accessToken));
  }

  @Test
  void shouldCallFindByAccessToken_whenRegisterIsCalled() {
    String auth = "valid-secret";
    String accessToken = "token123";

    ReflectionTestUtils.setField(usersService, "secretKey", auth);

    when(jwtUtils.getAccessTokenFromJwt(accessToken)).thenCallRealMethod();
    when(usersRepository.findByAccessToken(accessToken)).thenReturn(empty());
    when(usersRepository.save(any())).thenAnswer(inv -> {
      Users u = inv.getArgument(0);
      u.setId(1L);
      return u;
    });
    usersService.register(auth, accessToken);
    verify(usersRepository, times(1)).findByAccessToken(accessToken);
  }

  @Test
  void shouldCallSave_whenUserDoesNotExist() {
    String auth = "valid-secret";
    String accessToken = "token123";

    ReflectionTestUtils.setField(usersService, "secretKey", auth);

    when(usersRepository.findByAccessToken(accessToken)).thenReturn(empty());
    when(usersRepository.save(any())).thenAnswer(inv -> {
      Users u = inv.getArgument(0);
      u.setId(1L);
      return u;
    });
    usersService.register(auth, accessToken);
    verify(usersRepository, times(1)).save(any());
  }

  @Test
  void shouldReturnNotNullUser_whenUserDoesNotExist() {
    String accessToken = "token123";
    String message = String.format("{\"access_token\":\"%s\"}", accessToken);
    when(usersRepository.findByAccessToken(accessToken)).thenReturn(empty());
    when(usersRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    assertNotNull(usersService.registerUserBySqs(message));
  }

  @Test
  void shouldReturnActiveUser_whenUserDoesNotExist() {
    String accessToken = "token123";
    String message = String.format("{\"access_token\":\"%s\"}", accessToken);
    when(usersRepository.findByAccessToken(accessToken)).thenReturn(empty());
    when(usersRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    assertTrue(usersService.registerUserBySqs(message).getActive());
  }

  @Test
  void shouldReturnUserWithCorrectAccessToken_whenUserDoesNotExist() {
    String accessToken = "token123";
    String message = String.format("{\"access_token\":\"%s\"}", accessToken);
    when(usersRepository.findByAccessToken(accessToken)).thenReturn(empty());
    when(usersRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    assertEquals(accessToken, usersService.registerUserBySqs(message).getAccessToken());
  }

  @Test
  void shouldCallFindByAccessTokenOnce_whenUserDoesNotExist() {
    String accessToken = "token123";
    String message = String.format("{\"access_token\":\"%s\"}", accessToken);
    when(usersRepository.findByAccessToken(accessToken)).thenReturn(empty());
    when(usersRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    usersService.registerUserBySqs(message);
    verify(usersRepository, times(1)).findByAccessToken(accessToken);
  }

  @Test
  void shouldCallSaveOnce_whenUserDoesNotExist() {
    String accessToken = "token123";
    String message = String.format("{\"access_token\":\"%s\"}", accessToken);
    when(usersRepository.findByAccessToken(accessToken)).thenReturn(empty());
    when(usersRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
    usersService.registerUserBySqs(message);
    verify(usersRepository, times(1)).save(any());
  }

  @Test()
  void mustChangeStausByUser() {
    Long userId = 7l;
    Users users = new Users();
    users.setId(userId);
    users.setActive(true);
    when(usersRepository.findById(userId)).thenReturn(of(users));
    usersService.changeStats(userId);
    verify(usersRepository, times(1)).save(usersCaptor.capture());
    verify(usersRepository, times(1)).findById(userId);
    assertFalse(usersCaptor.getValue().getActive());
  }

  @Test
  void mustSaveInUpdateUsers() {
    Long id = 8l;
    when(usersRepository.findById(id)).thenReturn(of(new Users()));
    usersService.update(id, new UserUpdateVo("Novo nome", "email@emailcom"));
    verify(usersRepository, times(1)).findById(id);
    verify(usersRepository, times(1)).save(any());
  }

  @Test
  void mustNotThrowExceptionIfFoundUserAccessToken() {
    String accessToken = "accessToken";
    when(jwtUtils.getAccessTokenFromJwt(accessToken)).thenCallRealMethod();
    when(usersRepository.findByAccessToken(accessToken)).thenReturn(of(new Users()));
    assertDoesNotThrow(() -> usersService.findByAccessToken(accessToken));
  }

  @Test
  void mustThrowExceptionIfNotFoundUserByAccessToken() {
    String accessToken = "accessToken";
    when(usersRepository.findByAccessToken(accessToken)).thenReturn(empty());
    NotFoundEntityException message = assertThrows(NotFoundEntityException.class,
        () -> usersService.findByAccessToken(accessToken));
    assertEquals("Usuario não encontrado.", message.getMessage());
  }

  @Test
  void mustNotThrowExceptionIfFoundUserById() {
    Long userId = 7l;
    when(usersRepository.findById(userId)).thenReturn(of(new Users()));
    assertDoesNotThrow(() -> usersService.findById(userId));
  }

  @Test
  void mustThrowExceptionIfNotFoundUserById() {
    Long userId = 7l;
    when(usersRepository.findById(userId)).thenReturn(empty());
    NotFoundEntityException message = assertThrows(NotFoundEntityException.class, () -> usersService.findById(userId));
    assertEquals("Usuario não encontrado.", message.getMessage());
  }
}
