package project.photo.boundary;

import project.photo.boundary.command.FetchPhotosCommand;
import project.photo.boundary.command.UpdatePhotoCommand;
import project.photo.boundary.vo.PhotoVO;
import project.photo.control.mappers.PhotoMapper;
import project.photo.control.repository.PhotoRepository;
import project.photo.entity.PhotoEntity;
import project.photo.boundary.command.CreateNewPhotoCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotoService {

    @PersistenceContext
    private EntityManager entityManager;
    private final PhotoRepository photoRepository;

    public List<PhotoVO> getAllPoints(){
        return photoRepository.findAll()
                .stream()
                .map(e-> PhotoMapper.INSTANCE.mapEntity(e))
                .collect(Collectors.toList());
    }

    public Page<PhotoVO> getPointsPaginated(FetchPhotosCommand command){
        //it is possible to create complicated rules of sorting
        Sort sort = Sort.by(Sort.Direction.ASC, "pointKey");
        Pageable pageable = PageRequest.of(command.getPage(), command.getPerPage(), sort);
        Page<PhotoEntity> page = photoRepository.findAll(pageable);
        return page.map(PhotoMapper.INSTANCE::mapEntity);
    }

    @Transactional
    public void createPoint(CreateNewPhotoCommand command){
        PhotoEntity photoEntity = PhotoEntity.builder()
                .xsCoord(command.getXsCoord())
                .pathString(command.getPathString())
                .ysCoord(command.getYsCoord())
                .build();
        entityManager.persist(photoEntity);
    }

    /*Optional classes are used to handle possibility of null returned from function*/
    @Transactional
    public void updatePointAllAttributes(UpdatePhotoCommand command){
        //Return BAD REQUEST when update command do not have key
        Long id = Optional.ofNullable(command.getPointKey()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not passed id to update"));
        PhotoEntity photoEntity = findEntityById(id);
        photoEntity.setXsCoord(command.getXsCoord());
        photoEntity.setPathString(command.getPathString());
        photoEntity.setYsCoord(command.getYsCoord());
        entityManager.persist(photoEntity);
    }

    @Transactional
    public void updatePointSelectedAttributes(UpdatePhotoCommand command){
        // Return BAD REQUEST when update command do not have key
        Long id = Optional.ofNullable(command.getPointKey()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not passed id to update"));
        PhotoEntity photoEntity = findEntityById(id);
        // can be replaced by one-liner
        // photoEntity.setPointDate(Optional.ofNullable(command.getPointDate()).orElse(photoEntity.getPointDate()))

        if(command.getPathString()!=null) {
            photoEntity.setPathString(command.getPathString());
        }
        if(command.getYsCoord()!=null) {
            photoEntity.setYsCoord(command.getYsCoord());
        }
        if(command.getXsCoord()!=null) {
            photoEntity.setXsCoord(command.getXsCoord());
        }
        entityManager.persist(photoEntity);
    }

    @Transactional
    public void deletePoint(Long id){
        PhotoEntity photoEntity = findEntityById(id);
        entityManager.remove(photoEntity);
    }

    private PhotoEntity findEntityById(Long id){
        Optional<PhotoEntity> optionalPointEntity = photoRepository.findById(id);
        if(!optionalPointEntity.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Point with id " + id + " not found.");
        }
        return optionalPointEntity.get();
    }

}
