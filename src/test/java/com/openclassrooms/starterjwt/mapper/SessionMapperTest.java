package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SessionMapperTest {

    private SessionMapper sessionMapper;

    private TeacherService teacherService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        teacherService = mock(TeacherService.class);
        userService = mock(UserService.class);

        sessionMapper = Mappers.getMapper(SessionMapper.class);

        sessionMapper.teacherService = teacherService;
        sessionMapper.userService = userService;
    }

    private Date currentDate() {
        return Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Test
    void shouldMapDtoToEntity() {
        Long teacherId = 100L;
        List<Long> userIds = Arrays.asList(1L, 2L);

        SessionDto dto = new SessionDto();
        dto.setId(1L);
        dto.setName("Yoga Class");
        dto.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        dto.setDescription("Relaxing session");
        dto.setTeacher_id(teacherId);
        dto.setUsers(userIds);

        Teacher mockTeacher = new Teacher();
        mockTeacher.setId(teacherId);
        when(teacherService.findById(teacherId)).thenReturn(mockTeacher);

        User user1 = new User(); user1.setId(1L);
        User user2 = new User(); user2.setId(2L);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        Session session = sessionMapper.toEntity(dto);

        assertThat(session).isNotNull();
        assertThat(session.getDescription()).isEqualTo("Relaxing session");
        assertThat(session.getTeacher()).isEqualTo(mockTeacher);
        assertThat(session.getUsers()).containsExactly(user1, user2);
    }

    @Test
    void shouldMapEntityToDto() {
        Teacher teacher = new Teacher();
        teacher.setId(42L);

        User user1 = new User(); user1.setId(1L);
        User user2 = new User(); user2.setId(2L);

        Session session = new Session();
        session.setId(9L);
        session.setName("Zumba");
        session.setDescription("Dance session");
        session.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user1, user2));

        SessionDto dto = sessionMapper.toDto(session);

        assertThat(dto).isNotNull();
        assertThat(dto.getTeacher_id()).isEqualTo(42L);
        assertThat(dto.getUsers()).containsExactly(1L, 2L);
        assertThat(dto.getDescription()).isEqualTo("Dance session");
    }

    @Test
    void shouldHandleNullTeacherAndUsers() {
        SessionDto dto = new SessionDto();
        dto.setId(10L);
        dto.setName("Meditation");
        dto.setDate(currentDate());
        dto.setDescription("Mindfulness");
        dto.setTeacher_id(null);
        dto.setUsers(null);

        Session session = sessionMapper.toEntity(dto);

        assertThat(session).isNotNull();
        assertThat(session.getTeacher()).isNull();
        assertThat(session.getUsers()).isEmpty();
    }

    @Test
    void shouldHandleNullUsersInEntityToDto() {
        Session session = new Session();
        session.setId(11L);
        session.setName("Boxing");
        session.setDate(currentDate());
        session.setDescription("High intensity");
        session.setTeacher(null);
        session.setUsers(null);

        SessionDto dto = sessionMapper.toDto(session);

        assertThat(dto).isNotNull();
        assertThat(dto.getUsers()).isEmpty();
        assertThat(dto.getTeacher_id()).isNull();
    }

    @Test
    void shouldHandleNonexistentUsersGracefully() {
        SessionDto dto = new SessionDto();
        dto.setId(12L);
        dto.setName("Abs Workout");
        dto.setDate(currentDate());
        dto.setDescription("Hardcore");
        dto.setTeacher_id(100L);
        dto.setUsers(Arrays.asList(1L, 2L));

        Teacher teacher = new Teacher(); teacher.setId(100L);
        when(teacherService.findById(100L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(null);
        when(userService.findById(2L)).thenReturn(null);

        Session session = sessionMapper.toEntity(dto);

        assertThat(session.getUsers()).containsOnlyNulls();
    }


    @Test
    void shouldMapDtoListToEntityList() {
        SessionDto dto = new SessionDto();
        dto.setId(13L);
        dto.setName("CrossFit");
        dto.setDate(currentDate());
        dto.setDescription("Full body");
        dto.setTeacher_id(101L);
        dto.setUsers(Arrays.asList(1L));

        Teacher teacher = new Teacher(); teacher.setId(101L);
        User user = new User(); user.setId(1L);

        when(teacherService.findById(101L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user);

        List<Session> sessions = sessionMapper.toEntity(Collections.singletonList(dto));

        assertThat(sessions).hasSize(1);
        assertThat(sessions.get(0).getName()).isEqualTo("CrossFit");
        assertThat(sessions.get(0).getUsers()).containsExactly(user);
    }

    @Test
    void shouldMapEntityListToDtoList() {
        Teacher teacher = new Teacher(); teacher.setId(102L);
        User user = new User(); user.setId(2L);

        Session session = new Session();
        session.setId(14L);
        session.setName("Stretching");
        session.setDate(currentDate());
        session.setDescription("Flexibility");
        session.setTeacher(teacher);
        session.setUsers(Collections.singletonList(user));

        List<SessionDto> dtos = sessionMapper.toDto(Collections.singletonList(session));

        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).getTeacher_id()).isEqualTo(102L);
        assertThat(dtos.get(0).getUsers()).containsExactly(2L);
    }

    @Test
    void shouldCallToStringOnBuilder() {
        Session.SessionBuilder builder = Session.builder()
                .id(1L)
                .name("Test");

        String str = builder.toString();
        assertThat(str).contains("Test");
    }
}
