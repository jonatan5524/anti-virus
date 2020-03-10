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
import lombok.ToString;

@Entity
@Table(name = "FolderDB")

@AllArgsConstructor
@ToString
public class FolderDB {

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
	@Transient
	private File IOFolder;

}
