package aheung.likelion.twoneone.repository;

import aheung.likelion.twoneone.domain.common.File;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByTargetTableAndTargetId(String table, Long id);
}
