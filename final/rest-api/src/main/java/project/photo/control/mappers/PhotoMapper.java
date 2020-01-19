package project.photo.control.mappers;

import project.photo.boundary.vo.PhotoVO;
import project.photo.entity.PhotoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PhotoMapper {

    PhotoMapper INSTANCE = Mappers.getMapper(PhotoMapper.class);

    PhotoVO mapEntity(PhotoEntity point);
}
