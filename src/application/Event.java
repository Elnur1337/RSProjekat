package projekat;
import java.util.Date;
public class Event {
	
	
	private int id_;
	private String naziv_; 
	private String opis_;
	private String imgPath_;
	private Date startDate_;
	private Date endDate_;
	private double basePrice_;
	private int available_;
	private int approved_;
	private int idOrganizator_;
	private int idPodKategorija_;
	private int idLokacija_;
	private Date createdAt_;
	
	Event () {}
	
	public int getId_() {
		return id_;
	}

	public void setId_(int id_) {
		this.id_ = id_;
	}

	public String getNaziv_() {
		return naziv_;
	}

	public void setNaziv_(String naziv_) {
		this.naziv_ = naziv_;
	}

	public String getOpis_() {
		return opis_;
	}

	public void setOpis_(String opis_) {
		this.opis_ = opis_;
	}

	public String getImgPath_() {
		return imgPath_;
	}

	public void setImgPath_(String imgPath_) {
		this.imgPath_ = imgPath_;
	}

	public Date getStartDate_() {
		return startDate_;
	}

	public void setStartDate_(Date startDate_) {
		this.startDate_ = startDate_;
	}

	public Date getEndDate_() {
		return endDate_;
	}

	public void setEndDate_(Date endDate_) {
		this.endDate_ = endDate_;
	}

	public double getBasePrice_() {
		return basePrice_;
	}

	public void setBasePrice_(double basePrice_) {
		this.basePrice_ = basePrice_;
	}

	public int getAvailable_() {
		return available_;
	}

	public void setAvailable_(int available_) {
		this.available_ = available_;
	}

	public int getApproved_() {
		return approved_;
	}

	public void setApproved_(int approved_) {
		this.approved_ = approved_;
	}

	public int getIdOrganizator_() {
		return idOrganizator_;
	}

	public void setIdOrganizator_(int idOrganizator_) {
		this.idOrganizator_ = idOrganizator_;
	}

	public int getIdPodKategorija_() {
		return idPodKategorija_;
	}

	public void setIdPodKategorija_(int idPodKategorija_) {
		this.idPodKategorija_ = idPodKategorija_;
	}

	public int getIdLokacija_() {
		return idLokacija_;
	}

	public void setIdLokacija_(int idLokacija_) {
		this.idLokacija_ = idLokacija_;
	}

	public Date getCreatedAt_() {
		return createdAt_;
	}

	public void setCreatedAt_(Date createdAt_) {
		this.createdAt_ = createdAt_;
	}

	
	
	
	
	

}
