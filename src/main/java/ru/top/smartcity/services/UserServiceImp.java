package ru.top.smartcity.services;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.top.smartcity.DTOs.PasswordChangeDTO;
import ru.top.smartcity.DTOs.UserDTO;
import ru.top.smartcity.DTOs.UserFilterDTO;
import ru.top.smartcity.DTOs.UserUpdateDTO;
import ru.top.smartcity.entities.User;
import ru.top.smartcity.repositories.UserRepository;
import ru.top.smartcity.securities.JwtService;
import ru.top.smartcity.services.mapper.UserMapper;

import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .collect(Collectors.toList())
        );
    }


    @Override
    public Page<UserDTO> getUsers(UserFilterDTO filter, Pageable pageable) {
        Specification<User> spec = Specification.where(null);

        if (StringUtils.hasText(filter.getFirstname())) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("firstname")), "%" + filter.getFirstname().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filter.getLastname())) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("lastname")), "%" + filter.getLastname().toLowerCase() + "%"));
        }
        if (filter.getMinAge() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("age"), filter.getMinAge()));
        }
        if (filter.getMaxAge() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("age"), filter.getMaxAge()));
        }
        if (StringUtils.hasText(filter.getPhone())) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("phone"), "%" + filter.getPhone() + "%"));
        }
        if (StringUtils.hasText(filter.getEmail())) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("email")), "%" + filter.getEmail().toLowerCase() + "%"));
        }

        return userRepository.findAll(spec, pageable).map(this::convertToDTO);
    }


    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .age(user.getAge())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();
    }


    @Override
    @Transactional
    public void changePassword(Long userId, PasswordChangeDTO passwordChangeDTO) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(passwordChangeDTO.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords don't match");
        }

        user.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
        userRepository.save(user);
        refreshAuthentication(user.getEmail());
    }

    @Override
    @Transactional
    public void updateUserProfile(Long userId, UserUpdateDTO updateDTO) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        boolean emailChanged = !user.getEmail().equals(updateDTO.getEmail());

        if (emailChanged && userRepository.existsByEmail(updateDTO.getEmail())) {
            throw new Exception("Email is already in use");
        }

        user.setFirstname(updateDTO.getFirstname());
        user.setLastname(updateDTO.getLastname());
        user.setAge(updateDTO.getAge());
        user.setPhone(updateDTO.getPhone());

        if (emailChanged) {
            user.setEmail(updateDTO.getEmail());
        }

        userRepository.save(user);
        refreshAuthentication(user.getEmail());
    }

    @Override
    public UserDTO getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return convertToDTO(user);
    }


    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void logoutUser(String email) {
        SecurityContextHolder.clearContext();
    }

    public String refreshToken(String email) {
        UserDetails userDetails = loadUserByUsername(email);
        return jwtService.generationToken(userDetails);
    }

    public void refreshAuthentication(String email) {
        UserDetails userDetails = loadUserByUsername(email);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public boolean verifyCurrentPassword(Long userId, String rawPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }


    public Page<User> findAllPaginated(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<User> findAllPaginated(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 10);
        return userRepository.findAll(pageable);
    }

}