package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void shouldMapUserToUserDto() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password");
        user.setAdmin(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        UserDto userDto = userMapper.toDto(user);

        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDto.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userDto.getLastName()).isEqualTo(user.getLastName());
        assertThat(userDto.getPassword()).isEqualTo(user.getPassword());
        assertThat(userDto.isAdmin()).isEqualTo(user.isAdmin());
    }

    @Test
    void shouldMapUserDtoToUser() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");
        userDto.setFirstName("Jane");
        userDto.setLastName("Smith");
        userDto.setPassword("pass123");
        userDto.setAdmin(false);
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());

        User user = userMapper.toEntity(userDto);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userDto.getId());
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(user.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(user.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(user.getPassword()).isEqualTo(userDto.getPassword());
        assertThat(user.isAdmin()).isEqualTo(userDto.isAdmin());
    }

    @Test
    void shouldMapUserListToUserDtoList() {
        User user = new User();
        user.setId(3L);
        user.setEmail("user@example.com");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setPassword("123");
        user.setAdmin(false);

        List<UserDto> dtos = userMapper.toDto(Arrays.asList(user));

        assertThat(dtos).hasSize(1);
        UserDto dto = dtos.get(0);
        assertThat(dto.getId()).isEqualTo(user.getId());
        assertThat(dto.getEmail()).isEqualTo(user.getEmail());
        assertThat(dto.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(dto.getLastName()).isEqualTo(user.getLastName());
        assertThat(dto.getPassword()).isEqualTo(user.getPassword());
        assertThat(dto.isAdmin()).isEqualTo(user.isAdmin());
    }

    @Test
    void shouldMapUserDtoListToUserList() {
        UserDto dto = new UserDto(
                4L,
                "list.user@example.com",
                "User",
                "List",
                true,
                "pass789",
                null,
                null
        );


        List<User> users = userMapper.toEntity(Arrays.asList(dto));

        assertThat(users).hasSize(1);
        User user = users.get(0);
        assertThat(user.getId()).isEqualTo(dto.getId());
        assertThat(user.getEmail()).isEqualTo(dto.getEmail());
        assertThat(user.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(user.getLastName()).isEqualTo(dto.getLastName());
        assertThat(user.getPassword()).isEqualTo(dto.getPassword());
        assertThat(user.isAdmin()).isEqualTo(dto.isAdmin());
    }
}
