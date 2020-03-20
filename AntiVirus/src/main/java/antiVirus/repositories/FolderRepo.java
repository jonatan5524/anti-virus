package antiVirus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import antiVirus.entities.FolderDB;



@Repository
public interface FolderRepo extends JpaRepository<FolderDB, String> {
	FolderDB findByPath(String Path);

	boolean existsByPath(String path);
}