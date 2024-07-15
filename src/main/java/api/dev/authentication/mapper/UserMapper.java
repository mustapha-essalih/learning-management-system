package api.dev.authentication.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import api.dev.admin.dto.response.ManagerDto;
import api.dev.authentication.dto.UserDto;
import api.dev.authentication.model.User;
import api.dev.managers.model.Managers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    UserDto toManagerDto(User user);

}
