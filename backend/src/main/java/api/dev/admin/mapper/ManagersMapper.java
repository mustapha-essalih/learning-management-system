package api.dev.admin.mapper;

import api.dev.admin.dto.response.ManagerDto;
import api.dev.managers.model.Managers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ManagersMapper {

    @Mapping(target = "managerId", source = "userId")
    ManagerDto toManagerDto(Managers managers);

    List<ManagerDto> toManagerDtos(List<Managers> managers);

}
