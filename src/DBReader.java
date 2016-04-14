import java.sql.*;

import javax.websocket.Session;

public class DBReader extends Thread{
	Session session;
	String key = null;
	String app = null;
	boolean isRunning = true;
	int lastIndex = 0;

	public DBReader(Session session){
		this.session = session;
	}
	
	
	public void setFilter(String key){
		this.key = key;
		if (key.isEmpty()) this.key = null;
		this.app = null;
		lastIndex = 0;
	}

	public void setApp(String app){
		this.app = app;
		if (app.isEmpty()) this.app = null;
		this.key = null;
		lastIndex = 0;
	}

	public void kill(){
		isRunning = false;
	}
	

	public void run(){
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		String DB_URL = "jdbc:mysql://tweets.cmhwhoq4ms5s.us-east-1.rds.amazonaws.com:3306/Tweet";
		String USER = "somdeep";
		String PASS = "somdeep10";
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			
				sql = "SELECT * from tweet1";
				if (key != null){
					sql += " AND text like '%"+key+"%'";
				}
				if (app != null){
					sql += " AND source like '"+app+"'";
				}
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()){
					processRow(rs);
				}
				rs.close();
			
		} catch (Exception e){
			e.printStackTrace();
		} finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void processRow(ResultSet rs) throws Exception{
		String s = "";
		if (session != null && session.isOpen()){
			s += rs.getString("latitude")+","+rs.getString("longitude")+",";
			//s += rs.getString("place")+","+rs.getString("source");
			s += rs.getString("score");
			//System.out.println(s);
			session.getBasicRemote().sendText(s);
		}
		else {
			s +=  rs.getString("id") + ") ";
			s += "lat: "+rs.getString("latitude")+", ";
			s += "lng: "+rs.getString("longitude")+", ";
			s += "app: "+rs.getString("source")+", ";
			s += "ctr: "+rs.getString("place")+", ";
			s += "text: "+rs.getString("text")+", ";
			s += "score: "+rs.getString("score");
			System.out.println(s);
		}

	}

	///**
	public static void main(String[] args) throws InterruptedException{
		DBReader reader = new DBReader(null);
		reader.start();
		/**
		System.out.println("---------start----------");
		reader.start();
		Thread.sleep(3000);

		System.out.println("--------fashion---------");
		reader.setFilter("fashion");
		Thread.sleep(3000);

		System.out.println("--------instagram-------");
		reader.setApp("instagram");
		Thread.sleep(3000);

		System.out.println("--------no filter-------");
		reader.setFilter("");
		Thread.sleep(3000);

		System.out.println("---------twitter--------");
		reader.setApp("twitter");
		Thread.sleep(3000);

		System.out.println("----------kill----------");
		reader.kill();
		*/
	}
	//*/

}
