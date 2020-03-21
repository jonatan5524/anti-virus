package antiVirus.entities;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "folderdb")

@ToString
@NoArgsConstructor
public class FolderDB {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long Id;

	@Getter
	@Setter
	@Column(name = "name")
	private String name;

	@Column(name = "path", unique = true)
	@Getter
	private String path;


	public FolderDB(String name, String path) {
		this.name = name;
		this.path = path;
	}

}