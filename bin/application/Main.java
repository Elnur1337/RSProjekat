package projekat;
import java.sql.*;
import javax.sql.*;


public class Main {
	
	
	
	
	
	public static void main(String[]args) 
	{
		
		String url="jdbc:mysql://localhost:3306/rsprojekat";
		String username="root";
		//////////////////////////////////////////	//////////////////////////////////////////
		String password=" "; // UNIJETI password	
		//////////////////////////////////////////	//////////////////////////////////////////
		
		User user = new User(1002, "Damir", "Muminovic", "damirmuminovic@fet.ba", "123456", 500, 1, 1, Date.valueOf("2023-05-19"), 1, "2023-05-19");
     
		
		user.createUser(user); 
		
		try {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con=DriverManager.getConnection(url,username,password);
		
		Statement statement=con.createStatement();
		
		ResultSet resultSet=statement.executeQuery("SELECT *FROM korisnik");
		
		
		//ispis podataka o korisniku
		while (resultSet.next()) 
		{
			System.out.print("ID:"+resultSet.getString(1)+", Ime: "+resultSet.getString(2)+", Prezime: "+resultSet.getString(3));
			
		}
				}
		catch(Exception e)
		{
			System.out.println(e);
			
		}
		

		
		
	}
	

}
