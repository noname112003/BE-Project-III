package sapo.com.controller.auth;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import sapo.com.model.dto.response.ResponseObject;
import sapo.com.model.entity.Role;
import sapo.com.model.entity.User;
import sapo.com.service.UserService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/admin")
public class AdminController {
    @Autowired
    private UserService userService ;
    @PutMapping("/reset_password/{id}")
    public ResponseEntity<?> resetPassword(@PathVariable Long id ) throws Exception {
        User user = userService.resetPassword(id);
        return ResponseEntity.ok().body(ResponseObject.builder()
                        .message("Successfully")
                        .status(HttpStatus.OK)
                        .data(user)
                .build()) ;
    }
    @PutMapping("/role/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Long id , @RequestBody @Valid Role role , BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().stream().map(ObjectError :: getDefaultMessage ).collect(Collectors.joining("\n")));
        }
        User user = userService.updateRole(id , role);
        return ResponseEntity.ok().body(ResponseObject.builder()
                        .message("Successfully")
                        .status(HttpStatus.OK)
                        .data(user)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) throws Exception {
        userService.deleteById(id);
        return ResponseEntity.ok().body(ResponseObject.builder()
                        .message("Successfully")
                        .status(HttpStatus.OK)
                .build()

        );
    }

}
