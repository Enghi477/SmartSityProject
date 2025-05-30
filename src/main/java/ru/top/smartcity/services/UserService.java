package ru.top.smartcity.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.top.smartcity.DTOs.PasswordChangeDTO;
import ru.top.smartcity.DTOs.UserDTO;
import ru.top.smartcity.DTOs.UserFilterDTO;
import ru.top.smartcity.DTOs.UserUpdateDTO;

public interface UserService {

    Page<UserDTO> getUsers(UserFilterDTO filter, Pageable pageable);

    UserDTO getCurrentUser(String username);

    void updateUserProfile(Long userId, UserUpdateDTO updateDTO) throws Exception;

    void changePassword(Long userId, PasswordChangeDTO passwordChangeDTO) throws Exception;

    boolean emailExists(String email);

    void deleteUser(Long id);

    Page<UserDTO> getAllUsers(Pageable pageable);
}
