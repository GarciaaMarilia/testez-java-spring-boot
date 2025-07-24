package com.openclassrooms.starterjwt.service;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherServiceTest {

    private TeacherRepository teacherRepository;
    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        teacherRepository = Mockito.mock(TeacherRepository.class);
        teacherService = new TeacherService(teacherRepository);
    }

    @Test
    void findAll_shouldReturnListOfTeachers() {
        Teacher teacher1 = new Teacher();
        Teacher teacher2 = new Teacher();
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = teacherService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnTeacher_whenTeacherExists() {
        Long teacherId = 1L;
        Teacher teacher = new Teacher();
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        Teacher result = teacherService.findById(teacherId);

        assertNotNull(result);
        verify(teacherRepository, times(1)).findById(teacherId);
    }

    @Test
    void findById_shouldReturnNull_whenTeacherDoesNotExist() {
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        Teacher result = teacherService.findById(teacherId);

        assertNull(result);
        verify(teacherRepository, times(1)).findById(teacherId);
    }
}
