package com.maskordev.postgres.user.controller;

import com.maskordev.postgres.user.dto.request.CreateUserRequest;
import com.maskordev.postgres.user.dto.request.EditUserRequest;
import com.maskordev.postgres.user.dto.response.UserResponse;
import com.maskordev.postgres.user.entity.UserEntity;
import com.maskordev.postgres.user.exception.UserNotFoundException;
import com.maskordev.postgres.user.repository.UserRepository;
import com.maskordev.postgres.user.routes.UserRoutes;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class UserApiController {
    private final UserRepository userRepository;

    @GetMapping("/")
    public UserEntity test() {
        UserEntity user = UserEntity.builder()
                .firstName("Artur")
                .lastName("Ponomaryov")
                .build();

        user = userRepository.save(user);

        return user;
    }

    @PostMapping(UserRoutes.CREATE)
    public UserResponse create(@RequestBody CreateUserRequest createUserRequest) {
        UserEntity user = UserEntity.builder()
                .firstName(createUserRequest.getFirstName())
                .lastName(createUserRequest.getLastName())
                .build();

        user = userRepository.save(user);
        return UserResponse.of(user);
    }

    @GetMapping(UserRoutes.BY_ID)
    public UserResponse findUserById(@PathVariable Long id) throws UserNotFoundException {
        return (UserResponse.of(userRepository.findById(id).orElseThrow(UserNotFoundException::new)));
    }

    @GetMapping(UserRoutes.ALL_USERS)
    public List<UserResponse> findAllUser(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "") String query
    ) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<UserEntity> example = Example.of(
                UserEntity.builder()
                        .firstName(query)
                        .lastName(query)
                        .build(), matcher
        );


        return userRepository.findAll(example, pageable).stream().map(UserResponse::of).collect(Collectors.toList());
    }

    @PutMapping(UserRoutes.BY_ID)
    public UserResponse edit(@PathVariable Long id, @RequestBody EditUserRequest request) throws UserNotFoundException {
        UserEntity user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        user = userRepository.save(user);

        return UserResponse.of(user);
    }

    @DeleteMapping(UserRoutes.BY_ID)
    public String delete(@PathVariable Long id) {
        userRepository.deleteById(id);

        return HttpStatus.OK.name();
    }
}
