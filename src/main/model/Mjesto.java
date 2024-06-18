package main.model;

public class Mjesto {
	
	private int id_;
	private String naziv_;
	
	Mjesto(){}
	Mjesto(int id,String naziv)
	{
		naziv_=naziv;
		id_=id;
	}

	
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
	
}
