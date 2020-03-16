package AntiVirus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AntiVirus.entities.FileDB;

@Repository
public interface FileRepo extends JpaRepository<FileDB, Long> {
	FileDB findByPath(String Path);

	boolean existsByPath(String path);
}