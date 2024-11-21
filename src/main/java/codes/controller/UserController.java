package codes.controller;

import codes.entity.User;
import codes.entity.UserRequestBody;
import codes.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }
   @GetMapping()
   public List<User> getLisOfUsers(){
      return userService.selectAllUsers();
   }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable int id){
      return userService.selectUserById(id);
    }

    @PostMapping("/send")
    public void addUser(@RequestBody UserRequestBody userRequestBody){
       userService.createUser(userRequestBody);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable int id){
       userService.removeUserById(id);
    }

    @PutMapping("/{id}")
    public void updateUserById( @RequestBody UserRequestBody userRequestBody, @PathVariable int id){
         userService.changeUser(userRequestBody, id);
    }


}
