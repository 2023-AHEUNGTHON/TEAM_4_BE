package aheung.likelion.twoneone.repository;

import aheung.likelion.twoneone.domain.common.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
