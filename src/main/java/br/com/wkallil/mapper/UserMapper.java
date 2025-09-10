package br.com.wkallil.mapper;

import br.com.wkallil.data.dto.v1.security.AccountCredentialsDTO;
import br.com.wkallil.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(AccountCredentialsDTO accountCredentialsDTO);

    AccountCredentialsDTO toDTO(User user);
}
