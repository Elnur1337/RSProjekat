package main.model;

public class Lokacija {

	private int id_mjesto_;
	private String naziv_;
	private String img_path_;
	
    Lokacija(){}
	
	Lokacija(int id_mjesto,String naziv)
	{
		id_mjesto_=id_mjesto;
		naziv_=naziv;
	}
	
	
	public int getId_mjesto_() {
		return id_mjesto_;
	}

	public void setId_mjesto_(int id_mjesto_) {
		this.id_mjesto_ = id_mjesto_;
	}

	public String getNaziv_() {
		return naziv_;
	}

	public void setNaziv_(String naziv_) {
		this.naziv_ = naziv_;
	}

	public String getImg_path_() {
		return img_path_;
	}

	public void setImg_path_(String img_path_) {
		this.img_path_ = img_path_;
	}

	
	
	
	

	
	
	
	
}
