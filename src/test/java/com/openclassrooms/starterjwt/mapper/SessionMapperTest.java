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
}
