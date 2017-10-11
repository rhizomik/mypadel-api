package cat.udl.eps.softarch.mypadel.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Court extends UriEntity<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	private boolean available = true;

	@NotNull
	private boolean isIndoor;

	@Override
	public String getId() {
		return String.valueOf(id);
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean isIndoor() {
		return isIndoor;
	}

	public void setIndoor(boolean indoor) {
		isIndoor = indoor;
	}


}
