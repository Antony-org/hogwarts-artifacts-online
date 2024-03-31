package edu.com.hogwartsartifactsonline.hogwartsUsers;

import edu.com.hogwartsartifactsonline.hogwartsUsers.Dto.UserDto;
import edu.com.hogwartsartifactsonline.hogwartsUsers.converter.HogwartsUserToUserDtoConverter;
import edu.com.hogwartsartifactsonline.hogwartsUsers.converter.UserDtoToHogwartsUserConverter;
import edu.com.hogwartsartifactsonline.system.Result;
import edu.com.hogwartsartifactsonline.system.StatusCode;
import edu.com.hogwartsartifactsonline.wizard.dto.WizardDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {

    private final UserService userService;

    private final HogwartsUserToUserDtoConverter hogwartsUserToUserDtoConverter;

    private final UserDtoToHogwartsUserConverter userDtoToHogwartsUserConverter;

    public UserController(UserService userService, HogwartsUserToUserDtoConverter hogwartsUserToUserDtoConverter, UserDtoToHogwartsUserConverter userDtoToHogwartsUserConverter) {
        this.userService = userService;
        this.hogwartsUserToUserDtoConverter = hogwartsUserToUserDtoConverter;
        this.userDtoToHogwartsUserConverter = userDtoToHogwartsUserConverter;
    }

    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable Integer userId){
        HogwartsUser user = this.userService.findById(userId);
        UserDto userDto = this.hogwartsUserToUserDtoConverter.convert(user);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", userDto);
    }

    @GetMapping
    public Result findAllUsers(){
        List<HogwartsUser> userList = this.userService.findAll();
        // loop on all list of users and convert them to a list of Dtos return to the client.
        List<UserDto> userDtos = userList.stream().
                map(foundUser -> this.hogwartsUserToUserDtoConverter.convert(foundUser))
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Find All Success", userDtos);
    }

    @PostMapping
    public Result addUser(@Valid @RequestBody HogwartsUser user){
        HogwartsUser savedUser = this.userService.save(user);
        UserDto userDto = this.hogwartsUserToUserDtoConverter.convert(savedUser);
        return new Result(true, StatusCode.SUCCESS, "Add Success", userDto);
    }

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable Integer userId, @Valid @RequestBody UserDto userDto){
        // takes users as dto then convert them to HogwartsUser class to process.
        HogwartsUser user = this.userDtoToHogwartsUserConverter.convert(userDto);
        HogwartsUser updatedUser = this.userService.update(userId, user);
        // return them to userDto to parse as Json.
        UserDto savedUserDto = this.hogwartsUserToUserDtoConverter.convert(updatedUser);
        return new Result(true, StatusCode.SUCCESS, "Update Success", savedUserDto);
    }

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Integer userId){
        this.userService.delete(userId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success", null);
    }
}