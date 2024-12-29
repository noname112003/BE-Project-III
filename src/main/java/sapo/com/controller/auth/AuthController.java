package sapo.com.controller.auth;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import sapo.com.exception.UserException;
import sapo.com.model.dto.request.UserRequest;
import sapo.com.model.dto.response.ResponseObject;
import sapo.com.model.dto.response.UserResponse;
import sapo.com.model.entity.User;
import sapo.com.service.UserService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    @Autowired
    private UserService userService ;

    @PostMapping("/login")
    public ResponseEntity<?>login(@RequestBody @Valid  UserRequest userRequest , BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().stream().map(ObjectError :: getDefaultMessage ).collect(Collectors.joining("\n")));
        }
        UserResponse userResponse = userService.login(userRequest);
        System.out.println(userResponse);
        return ResponseEntity.ok().body(ResponseObject.builder()
                        .message("Successfully")
                        .status(HttpStatus.OK)
                        .data(userResponse)
                .build());
    }
    @PostMapping("/register")
    public ResponseEntity<?>register(@RequestBody @Valid User user , BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().stream().map(ObjectError:: getDefaultMessage ).collect(Collectors.joining("\n")));
        }
        return new ResponseEntity<>(userService.register(user), HttpStatus.CREATED);
    }

}
