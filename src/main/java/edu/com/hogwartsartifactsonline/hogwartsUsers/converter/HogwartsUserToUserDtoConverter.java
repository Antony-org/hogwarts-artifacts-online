package edu.com.hogwartsartifactsonline.hogwartsUsers.converter;

import edu.com.hogwartsartifactsonline.hogwartsUsers.HogwartsUser;
import edu.com.hogwartsartifactsonline.hogwartsUsers.Dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class HogwartsUserToUserDtoConverter implements Converter<HogwartsUser, UserDto> {
    @Override
    public UserDto convert(HogwartsUser source) {
        return new UserDto(source.getId(), source.getUsername(), source.isEnabled(), source.getRoles());
    }
}