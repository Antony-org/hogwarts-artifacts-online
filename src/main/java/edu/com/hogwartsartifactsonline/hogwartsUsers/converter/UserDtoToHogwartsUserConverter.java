package edu.com.hogwartsartifactsonline.hogwartsUsers.converter;

import edu.com.hogwartsartifactsonline.hogwartsUsers.HogwartsUser;
import edu.com.hogwartsartifactsonline.hogwartsUsers.Dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToHogwartsUserConverter implements Converter<UserDto, HogwartsUser> {
    @Override
    public HogwartsUser convert(UserDto source) {
        HogwartsUser hogwartsUser = new HogwartsUser();
        hogwartsUser.setId(source.id());
        hogwartsUser.setUsername(source.username());
        hogwartsUser.setEnabled(source.enabled());
        hogwartsUser.setRoles(source.roles());

        return hogwartsUser;
    }
}