package sapo.com.controller.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sapo.com.model.dto.response.ResponseObject;
import sapo.com.model.entity.Role;
import sapo.com.service.RoleService;

import java.util.Optional;

@RestController
@RequestMapping("/v1/role")
public class RoleController {
    @Autowired
    private RoleService roleService ;
    @GetMapping()
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "10" ,name = "limit") int limit ,
                                     @RequestParam(defaultValue = "0" , name ="page" ) int page ,
                                     @RequestParam(defaultValue = "name" , name = "sort") String sort ,
                                     @RequestParam(defaultValue = "asc" , name = "order") String order){
        Pageable pageable ;
        if (order.equals("asc")){
            pageable = PageRequest.of(page,limit , Sort.by(sort).ascending() );
        }else {
            pageable = PageRequest.of(page , limit , Sort.by(sort).descending() );
        }
        Page<Role> products = roleService.findAll(pageable);
        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Successfully")
                .status(HttpStatus.OK)
                .data(products)
                .build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById (@PathVariable Long id) throws Exception {
        Optional<Role> role = roleService.findById(id);
        return ResponseEntity.ok().body(ResponseObject.builder()
                        .message("Successfully")
                        .status(HttpStatus.OK)
                        .data(role)
                .build());
    }
}
