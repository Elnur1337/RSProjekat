package projekat;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Date;
public class User {
	
	private int id_;
	private String ime_;
	private String prezime_;
	private String email_;
	private String pass_;
	private double wallet_;
	private int admin_;
	private int organizator_;
	private Date datum_;
	private int approved_;
	private String created_at_;
	
	
	
	public User() {}
	public User(int id,String ime,String prezime,String email,String pass,
			double wallet,int admin,int organizator,Date datum,int approved,String created_at) 
	{  
		id_=id;
		ime_=ime;
		prezime_=prezime;
		email_=email;
		pass_=pass;
		wallet_=wallet;
		admin_=admin;
		organizator_=organizator;
		datum_=datum;
		approved_=approved;
		created_at_=created_at;
	}
	
	public void createUser(User user) 
	{
		
		String url = "jdbc:mysql://localhost:3306/rsprojekat";

	String username="root";
	//////////////////////////////////////////	//////////////////////////////////////////
	String password=""; /// UNIJETI SIFRU
	//////////////////////////////////////////	//////////////////////////////////////////
	
		String naredba="INSERT INTO Korisnik(id,ime,prezime,email,pass,wallet,admin,organizator,datum_rod,"
				+ "approved,created_at) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		
		try 
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection(url,username,password);
			PreparedStatement statement=con.prepareStatement(naredba);
			
			
			statement.setInt(1, id_);
			statement.setString(2, ime_);
			statement.setString(3, prezime_);
			statement.setString(4, email_);
			statement.setString(5,pass_);
			statement.setDouble(6,wallet_);
			statement.setInt(7,admin_);
			statement.setInt(8,organizator_);
			statement.setDate(9, datum_);
			statement.setInt(10, approved_);
			statement.setString(11, created_at_);
			
			
			
			int rowsAffected=statement.executeUpdate();
			if(rowsAffected>0) 
			{
				System.out.println("Korisnik je usjpesno dodan");
				
				
			}
			
			
		}
		catch(Exception e) 
		{
			e.printStackTrace();
			
		}
		
		
	}
	

}
