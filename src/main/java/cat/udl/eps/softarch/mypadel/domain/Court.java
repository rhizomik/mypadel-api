package cat.udl.eps.softarch.mypadel.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Court extends UriEntity<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	private boolean available = true;

	@NotNull
	private boolean isIndoor;

	@OneToMany(mappedBy = "court")
	private List<Reservation> reservations;

	@Override
	public Integer getId() {
		return id;
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

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
}
