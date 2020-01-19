package project.photo.control.mappers;

import javax.annotation.Generated;
import project.photo.boundary.vo.PhotoVO;
import project.photo.entity.PhotoEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-01-19T19:04:53+0100",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_111 (Oracle Corporation)"
)
public class PhotoMapperImpl implements PhotoMapper {

    @Override
    public PhotoVO mapEntity(PhotoEntity point) {
        if ( point == null ) {
            return null;
        }

        PhotoVO photoVO = new PhotoVO();

        photoVO.setPointKey( point.getPointKey() );
        photoVO.setPathString( point.getPathString() );
        photoVO.setXsCoord( point.getXsCoord() );
        photoVO.setYsCoord( point.getYsCoord() );

        return photoVO;
    }
}
