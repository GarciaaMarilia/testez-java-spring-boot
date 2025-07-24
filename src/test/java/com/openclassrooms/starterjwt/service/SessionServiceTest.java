package com.openclassrooms.starterjwt.service;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_shouldSaveAndReturnSession() {
        Session session = new Session();
        session.setId(1L);

        when(sessionRepository.save(session)).thenReturn(session);

        Session saved = sessionService.create(session);

        assertEquals(1L, saved.getId());
        verify(sessionRepository).save(session);
    }

    @Test
    void delete_shouldCallDeleteById() {
        Long id = 1L;

        doNothing().when(sessionRepository).deleteById(id);

        sessionService.delete(id);

        verify(sessionRepository).deleteById(id);
    }

    @Test
    void findAll_shouldReturnListOfSessions() {
        List<Session> sessions = Arrays.asList(new Session(), new Session());
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> result = sessionService.findAll();

        assertEquals(2, result.size());
        verify(sessionRepository).findAll();
    }

    @Test
    void getById_shouldReturnSession_whenExists() {
        Session session = new Session();
        session.setId(1L);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        Session result = sessionService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(sessionRepository).findById(1L);
    }

    @Test
    void getById_shouldReturnNull_whenNotExists() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        Session result = sessionService.getById(1L);

        assertNull(result);
        verify(sessionRepository).findById(1L);
    }

    @Test
    void update_shouldSetIdAndSave() {
        Session session = new Session();
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session updated = sessionService.update(1L, session);

        assertEquals(session, updated);
        assertEquals(1L, session.getId());
        verify(sessionRepository).save(session);
    }

    @Test
    void participate_shouldAddUserToSession() {
        Long sessionId = 1L;
        Long userId = 10L;

        User user = new User();
        user.setId(userId);

        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<>());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        sessionService.participate(sessionId, userId);

        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    void participate_shouldThrowNotFoundException_whenSessionOrUserNotFound() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));

        User user = new User();
        user.setId(1L);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(new Session()));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    void participate_shouldThrowBadRequestException_whenUserAlreadyParticipates() {
        Long sessionId = 1L;
        Long userId = 10L;

        User user = new User();
        user.setId(userId);

        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<>(Arrays.asList(user)));

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId));
    }

    @Test
    void noLongerParticipate_shouldRemoveUserFromSession() {
        Long sessionId = 1L;
        Long userId = 10L;

        User user = new User();
        user.setId(userId);

        List<User> users = new ArrayList<>();
        users.add(user);

        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(users);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        sessionService.noLongerParticipate(sessionId, userId);

        assertFalse(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    @Test
    void noLongerParticipate_shouldThrowNotFoundException_whenSessionNotFound() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }

    @Test
    void noLongerParticipate_shouldThrowBadRequestException_whenUserDoesNotParticipate() {
        Long sessionId = 1L;
        Long userId = 10L;

        User user = new User();
        user.setId(999L);

        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<>(Arrays.asList(user)));

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(sessionId, userId));
    }
}