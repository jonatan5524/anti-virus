package AntiVirus.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "filedb")
@ToString
@NoArgsConstructor
public class FileDB {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long Id;

	@Getter
	@Setter
	@Column(name = "hash")
	private String hash;

	@Getter
	@Setter
	@Column(name = "name")
	private String name;

	@Column(name = "path", unique = true)
	@Getter
	private String path;

	@Getter
	@Setter
	@Transient
	private ResultScan resultScan;

	public FileDB(String hash, String name, String path) {
		this.hash = hash;
		this.path = path;
		this.name = name;
	}

}
