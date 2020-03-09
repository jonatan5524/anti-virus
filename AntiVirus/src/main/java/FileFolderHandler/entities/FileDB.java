package FileFolderHandler.entities;

import java.io.File;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "FileDB")

public class FileDB {

	@Getter
	@Setter
	private String hash;

	@OneToOne
	@Getter
	private FolderDB father;

	@Getter
	@Setter
	private String name;

	@Id
	@Getter
	private String path;

	@Getter
	@Setter
	@Transient
	private ResultScan resultScan;

	@Getter
	@Transient
	private File IOFile;

	public FileDB(String hash, FolderDB father, String name, String path, File IOFile) {
		this.hash = hash;
		this.father = father;
		this.name = name;
	}

}
