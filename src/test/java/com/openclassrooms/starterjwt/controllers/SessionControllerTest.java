package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_Found() {
        Session session = new Session();
        SessionDto dto = new SessionDto();

        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(dto);

        ResponseEntity<?> response = sessionController.findById("1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    void testFindById_NotFound() {
        when(sessionService.getById(1L)).thenReturn(null);

        ResponseEntity<?> response = sessionController.findById("1");

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testFindById_BadRequest() {
        ResponseEntity<?> response = sessionController.findById("abc");

        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testFindAll() {
        List<Session> sessions = Arrays.asList(new Session(), new Session());
        List<SessionDto> dtos = Arrays.asList(new SessionDto(), new SessionDto());

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(dtos);

        ResponseEntity<?> response = sessionController.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dtos, response.getBody());
    }

    @Test
    void testCreate() {
        SessionDto inputDto = new SessionDto();
        Session session = new Session();
        SessionDto outputDto = new SessionDto();

        when(sessionMapper.toEntity(inputDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(outputDto);

        ResponseEntity<?> response = sessionController.create(inputDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(outputDto, response.getBody());

        verify(sessionService).create(session);
    }

    @Test
    void testUpdate_Success() {
        SessionDto inputDto = new SessionDto();
        Session session = new Session();
        SessionDto outputDto = new SessionDto();

        when(sessionMapper.toEntity(inputDto)).thenReturn(session);
        when(sessionService.update(1L, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(outputDto);

        ResponseEntity<?> response = sessionController.update("1", inputDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(outputDto, response.getBody());
    }

    @Test
    void testUpdate_BadRequest() {
        SessionDto inputDto = new SessionDto();

        ResponseEntity<?> response = sessionController.update("abc", inputDto);

        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testDelete_Success() {
        Session session = new Session();

        when(sessionService.getById(1L)).thenReturn(session);

        ResponseEntity<?> response = sessionController.save("1");

        assertEquals(200, response.getStatusCodeValue());

        verify(sessionService).delete(1L);
    }

    @Test
    void testDelete_NotFound() {
        when(sessionService.getById(1L)).thenReturn(null);

        ResponseEntity<?> response = sessionController.save("1");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDelete_BadRequest() {
        ResponseEntity<?> response = sessionController.save("abc");

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testParticipate_Success() {
        ResponseEntity<?> response = sessionController.participate("1", "2");

        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService).participate(1L, 2L);
    }

    @Test
    void testParticipate_BadRequest() {
        ResponseEntity<?> response = sessionController.participate("a", "2");

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testNoLongerParticipate_Success() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "2");

        assertEquals(200, response.getStatusCodeValue());
        verify(sessionService).noLongerParticipate(1L, 2L);
    }

    @Test
    void testNoLongerParticipate_BadRequest() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("a", "2");

        assertEquals(400, response.getStatusCodeValue());
    }


}
