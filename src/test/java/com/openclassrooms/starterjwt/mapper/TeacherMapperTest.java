package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherMapperTest {

    private TeacherMapper teacherMapper;

    @BeforeEach
    void setUp() {
        teacherMapper = Mappers.getMapper(TeacherMapper.class);
    }

    @Test
    void shouldMapTeacherToTeacherDto() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Marie");
        teacher.setLastName("Curie");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());

        TeacherDto dto = teacherMapper.toDto(teacher);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(teacher.getId());
        assertThat(dto.getFirstName()).isEqualTo(teacher.getFirstName());
        assertThat(dto.getLastName()).isEqualTo(teacher.getLastName());
        assertThat(dto.getCreatedAt()).isEqualTo(teacher.getCreatedAt());
        assertThat(dto.getUpdatedAt()).isEqualTo(teacher.getUpdatedAt());
    }

    @Test
    void shouldMapTeacherDtoToTeacher() {
        TeacherDto dto = new TeacherDto(
                2L,
                "Albert",
                "Einstein",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Teacher teacher = teacherMapper.toEntity(dto);

        assertThat(teacher).isNotNull();
        assertThat(teacher.getId()).isEqualTo(dto.getId());
        assertThat(teacher.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(teacher.getLastName()).isEqualTo(dto.getLastName());
        assertThat(teacher.getCreatedAt()).isEqualTo(dto.getCreatedAt());
        assertThat(teacher.getUpdatedAt()).isEqualTo(dto.getUpdatedAt());
    }

    @Test
    void shouldMapTeacherListToDtoList() {
        Teacher teacher = new Teacher();
        teacher.setId(3L);
        teacher.setFirstName("Isaac");
        teacher.setLastName("Newton");

        List<TeacherDto> dtos = teacherMapper.toDto(Arrays.asList(teacher));

        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).getId()).isEqualTo(teacher.getId());
        assertThat(dtos.get(0).getFirstName()).isEqualTo(teacher.getFirstName());
        assertThat(dtos.get(0).getLastName()).isEqualTo(teacher.getLastName());
    }

    @Test
    void shouldMapTeacherDtoListToEntityList() {
        TeacherDto dto = new TeacherDto(
                4L,
                "Ada",
                "Lovelace",
                null,
                null
        );

        List<Teacher> teachers = teacherMapper.toEntity(Arrays.asList(dto));

        assertThat(teachers).hasSize(1);
        assertThat(teachers.get(0).getId()).isEqualTo(dto.getId());
        assertThat(teachers.get(0).getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(teachers.get(0).getLastName()).isEqualTo(dto.getLastName());
    }
}
