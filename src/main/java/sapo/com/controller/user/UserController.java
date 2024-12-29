package sapo.com.controller.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import sapo.com.exception.UserException;
import sapo.com.model.dto.request.PasswordRequest;
import sapo.com.model.dto.request.UpdateUserRequest;
import sapo.com.model.dto.response.ResponseObject;
import sapo.com.model.entity.User;
import sapo.com.service.UserService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    @Autowired
    private UserService userService ;
    @GetMapping()
    public ResponseEntity<?> findAll (@RequestParam(defaultValue = "10" , name = "limit") int limit ,
                                      @RequestParam(defaultValue = "0" , name = "page") int page ,
                                      @RequestParam(defaultValue = "name" , name = "sort") String sort ,
                                      @RequestParam(defaultValue = "asc" , name = "order") String order,
                                      @RequestParam(value = "role" , required = false) String role ,
                                      @RequestParam(value = "search", required = false) String search
                                      ){
        Pageable pageable ;
        if (order.equals("asc")){
            pageable = PageRequest.of(page,limit , Sort.by(sort).ascending() );
        }else {
            pageable = PageRequest.of(page , limit , Sort.by(sort).descending() );
        }
        Page<User> users;

        if (search!= null){
            users = userService.findAllBySearch(pageable , search);
        }
        else if (role != null && !role.isEmpty()) {
            users = userService.findAllByRolesName( pageable, role); // Filter users by role
        } else {
            users = userService.findAll(pageable); // No role filter applied
        }
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("successfully")
                .status(HttpStatus.OK)
                .data(users)
                .build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) throws UserException {
        User user = userService.findById(id);
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Successfully")
                .status(HttpStatus.OK)
                .data(user)
                .build());
    }
    @GetMapping("/check-phoneNumber/{phoneNumber}")
    public ResponseEntity<?> findByPhoneNumber(@PathVariable String phoneNumber) throws Exception {
        User user = userService.findByPhoneNumber(phoneNumber);
        return ResponseEntity.ok().body(ResponseObject.builder()
                        .message("Successfully")
                        .status(HttpStatus.OK)
                        .data(user)
                .build());
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<?>findByEmail(@PathVariable String email) throws Exception{
        User user = userService.findByEmail(email);
        return ResponseEntity.ok().body(ResponseObject.builder()
                        .message("Successfully")
                        .status(HttpStatus.OK)
                        .data(user)
                .build());
    }

    @PutMapping("/{id}")

    public ResponseEntity<?> update(@RequestBody @Valid User user , BindingResult bindingResult , @PathVariable Long id) throws Exception {

        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().stream().map(ObjectError:: getDefaultMessage ).collect(Collectors.joining("\n")));
        }
        User updateUser = userService.update(id , user);
        return ResponseEntity.ok().body(ResponseObject.builder()
                        .message("Successfully")
                        .status(HttpStatus.OK)
                        .data(updateUser)
                .build());
    }

    @PutMapping("/change_password/{id}")
    public ResponseEntity<?> changePassword (@PathVariable Long id , @RequestBody PasswordRequest passwordRequest) throws Exception {
        User user = userService.changPassword(id , passwordRequest);
        return ResponseEntity.ok().body(ResponseObject.builder()
                        .message("Successfully")
                        .status(HttpStatus.OK)
                        .data(user)
                .build());

    }

//    @GetMapping("/check-email/{email}")
//    public ResponseEntity<?> existEmail (@PathVariable String email) throws Exception {
//        userService.existEmail(email);
//        return ResponseEntity.ok().body(ResponseObject.builder()
//                        .message("Successfully")
//                        .status(HttpStatus.OK)
//                .build());
//    }
//
//    @GetMapping("/check-phoneNumber/{phoneNumber}")
//    public ResponseEntity<?> existPhoneNumber(@PathVariable String phoneNumber) throws Exception {
//        userService.existPhoneNumber(phoneNumber);
//        return ResponseEntity.ok().body(ResponseObject.builder()
//                        .message("Successfully")
//                        .status(HttpStatus.OK)
//                .build());
//    }


}
