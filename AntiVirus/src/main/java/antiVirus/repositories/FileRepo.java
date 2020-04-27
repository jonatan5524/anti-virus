package antiVirus.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import antiVirus.entities.FileDB;

@Repository
public interface FileRepo extends JpaRepository<FileDB, Long> {
	
	public FileDB findByPath(String Path);

	public boolean existsByPath(String path);
	
	public List<FileDB> findByPathStartingWith(String prefix);
	
	public long count();
	
	public long countByPathStartingWith(@Param("prefix")String prefix);
}
