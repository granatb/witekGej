package project.photo.control.repository;

import project.photo.entity.PhotoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*extends JpaRepository<RelatedEntity, Class of RelatedEntity Id>*/
@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {

    Page<PhotoEntity> findAll(Pageable pageable);

    Optional<PhotoEntity> findById(Long id);

    List<PhotoEntity> findByPathString(String value);

    List<PhotoEntity> findByysCoord(String value);

    List<PhotoEntity> findByPathStringStartsWith(String substring);

    List<PhotoEntity> findByPathStringAndXsCoord(String string, Double number);

    List<PhotoEntity> findByPathStringOrXsCoord(String string, Double number);
}
