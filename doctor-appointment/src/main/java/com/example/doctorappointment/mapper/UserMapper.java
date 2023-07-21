package com.example.doctorappointment.mapper;

import com.example.doctorappointment.dto.UserDto;
import com.example.doctorappointment.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<User, UserDto>{
}
