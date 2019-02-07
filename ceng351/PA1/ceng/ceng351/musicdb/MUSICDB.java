package ceng.ceng351.musicdb;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import com.mysql.cj.jdbc.Driver;

public class MUSICDB implements IMUSICDB {

    private String user = "e2171163";//"pa1_user";  
    private String password = "2352dd4e"; //"123456";
    private String host = "144.122.71.57"; //"localhost";
    private String database = "db2171163";//"pa1_recitation";
    private int port = 8084; // 3306;
     
    private Connection con;

    public void initialize() {
        String url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.con =  DriverManager.getConnection(url, this.user, this.password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } 
    }
   
   /**
    * Should create the necessary tables when called.
    * 
    * @return the number of tables that are created successfully.
    */
    @Override
    public int createTables() {
        int numberofTablesInserted = 0;
        String[] tables = new String[5];

        tables[0] = "create table if not exists User (" + 
                                            "userID int," + 
                                            "userName varchar(60)," + 
                                            "email varchar(30)," +
                                            "password varchar(30)," +
                                            "primary key (userID))";
        
        tables[1] = "create table if not exists Artist (" + 
                                            "artistID int," + 
                                            "artistName varchar(60)," + 
                                            "primary key (artistID))";
        
        tables[2] = "create table if not exists Album (" + 
							                "albumID int," + 
							                "title varchar(60)," +
							                "albumGenre varchar(30)," +
							                "albumRating double," +
							                "releaseDate date," +
							                "artistID int," +
							                "primary key (albumID)," +
							                "foreign key (artistID) references Artist(ArtistID) on delete cascade)";

        tables[3] = "create table if not exists Song (" + 
                                            "songID int," + 
                                            "songName varchar(60)," + 
                                            "genre varchar(30)," +
                                            "rating double," +
                                            "artistID int," +
                                            "albumID int," +
                                            "foreign key(artistID) references Artist(artistID) on delete cascade," +
                                            "foreign key(albumID) references Album(albumID) on delete cascade," +
                                            "primary key (songID))";

        

        tables[4] = "create table if not exists Listen (" + 
                                            "userID int," + 
                                            "songID int," + 
                                            "lastListenTime timestamp," +
                                            "listenCount int," +
                                            "foreign key(userID) references User(userID) on delete cascade," + 
                                            "foreign key(songID) references Song(songID) on delete cascade, " +
                                            "primary key (userID, songID))";
        for(int i = 0; i < 5; i++){
        try {
                Statement statement = this.con.createStatement();
                statement.executeUpdate(tables[i]);
                numberofTablesInserted++;
                
                //close
                statement.close();
            
            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	
	        
	    }
        return numberofTablesInserted;
    }
   
   /**
    * Should drop the tables if exists when called. 
    * 
    * @return the number of tables are dropped successfully.
    */
   public int dropTables(){
    int DroppedTableNumber = 0;

    String[] tables = {"Listen", "Song", "Album", "Artist", "User"};
    for(int i = 0; i < 5; i++){
	    try {
	
	        
	            String query = "DROP TABLE IF EXISTS " + tables[i];
	
	            Statement statement = this.con.createStatement();
	            statement.executeUpdate(query);
	            DroppedTableNumber++;
	
	            //close
	            statement.close();
	        
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
    }
  
    return DroppedTableNumber;
   }
   
   /**
    * Should insert an array of Album into the database.
    * 
    * @return Number of rows inserted successfully.
    */
   public int insertAlbum(Album[] albums) {
    int number = 0;
    
    String query = "insert into Album values(?,?,?,?,?,?)";
    for(int i = 0; i < albums.length; i++){
        try {
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setInt(1, albums[i].getAlbumID());
            ps.setString(2, albums[i].getTitle());
            ps.setString(3, albums[i].getAlbumGenre());
            ps.setDouble(4, albums[i].getAlbumRating());
            ps.setString(5, albums[i].getReleaseDate());
            ps.setInt(6, albums[i].getArtistID());
            
            ps.executeUpdate();

            //Close
            ps.close();
            number++;

        } catch (SQLException e) {
            //e.printStackTrace();
            if (e.toString().contains("SQLIntegrityConstraintViolationException")){
            } else {
            }
        }
    }
       return number;
   }

   /**
    * Should insert an array of Artist into the database.
    * 
    * @return Number of rows inserted successfully.
    */
   public int insertArtist(Artist[] artists){
    int number = 0;
    String query = "insert into Artist values(?,?)";
    for(int i = 0; i < artists.length; i++){
        try {
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setInt(1, artists[i].getArtistID());
            ps.setString(2, artists[i].getArtistName());
            ps.executeUpdate();

            //Close
            ps.close();
            number++;

        } catch (SQLException e) {
            //e.printStackTrace();
            if (e.toString().contains("SQLIntegrityConstraintViolationException")){
            	System.out.println("Already Inserted!");
            }
        }
    }
       return number;
   }

   /**
    * Should insert an array of Song into the database.
    * 
    * @return Number of rows inserted successfully.
    */
   public int insertSong(Song[] songs) {
    int number = 0;
    String query = "insert into Song values(?,?,?,?,?,?)";

    for(int i = 0; i < songs.length; i++){
        try {
        	PreparedStatement ps = this.con.prepareStatement(query);
            ps.setInt(1, songs[i].getSongID());
            ps.setString(2, songs[i].getSongName());
            ps.setString(3, songs[i].getGenre());
            ps.setDouble(4, songs[i].getRating());
            ps.setInt(5, songs[i].getArtistID());
            ps.setInt(6, songs[i].getAlbumID());
            
            ps.executeUpdate();

            //Close
            ps.close();
            number++;

        } catch (SQLException e) {
            //e.printStackTrace();
            if (e.toString().contains("SQLIntegrityConstraintViolationException")){
            	System.out.println("Already Inserted!");
            }
        }
    }
       return number;
   }

   /**
    * Should insert an array of User into the database.
    * 
    * @return Number of rows inserted successfully.
    */
   public int insertUser(User[] users){
    int number = 0;
    String query = "insert into User values(?,?,?,?)";
    for(int i = 0; i < users.length; i++){

        try {
        	PreparedStatement ps = this.con.prepareStatement(query);
            ps.setInt(1, users[i].getUserID());
            ps.setString(2, users[i].getUserName());
            ps.setString(3, users[i].getEmail());
            ps.setString(4, users[i].getPassword());
            
            ps.executeUpdate();

            //Close
            ps.close();
            number++;
        } catch (SQLException e) {
            //e.printStackTrace();
            if (e.toString().contains("SQLIntegrityConstraintViolationException")){
            	System.out.println("Already Inserted!");
            }
        }
    }
    
       return number;
   }
   
   /**
    * Should insert an array of Listen into the database.
    * 
    * @return Number of rows inserted successfully.
    */
   public int insertListen(Listen[] listens){
    int number = 0;
    String query = "insert into Listen values(?,?,?,?)";
    for(int i = 0; i < listens.length; i++){
        try {
        	PreparedStatement ps = this.con.prepareStatement(query);
            ps.setInt(1, listens[i].getUserID());
            ps.setInt(2, listens[i].getSongID());
            ps.setTimestamp(3, listens[i].getLastListenTime());
            ps.setInt(4, listens[i].getListenCount());
            
            ps.executeUpdate();

            //Close
            ps.close();
            number++;
        } catch (SQLException e) {
            //e.printStackTrace();
            if (e.toString().contains("SQLIntegrityConstraintViolationException")){
            	System.out.println("Already Inserted!");
            }
        }
    }
       return number;
}
   
   /**
    * Should return songs with the highest rating
    * 
    * @return ArtistNameSongNameGenreRatingResult[]
    */
   public QueryResult.ArtistNameSongNameGenreRatingResult[] getHighestRatedSongs() {
       ResultSet rs;
       QueryResult.ArtistNameSongNameGenreRatingResult song;
       QueryResult.ArtistNameSongNameGenreRatingResult[] result = null;
       int i = 0;
       int size = 0;
       String query = "select a.artistName, s.songName, s.genre, s.rating from Song s, Artist a " +
    		   		   "where a.artistID = s.artistID and s.rating >= ALL (select s2.rating from Song s2) order by a.artistName ASC";

       try {
           Statement st = this.con.createStatement();
           rs = st.executeQuery(query);
           
           while(rs.next()) {
        	   size++;
           }
           
           result = new QueryResult.ArtistNameSongNameGenreRatingResult[size];
           rs = st.executeQuery(query);
           while(rs.next()) {
               String m_artistname = rs.getString("artistName");
               String m_songName = rs.getString("songName");
               String m_genre = rs.getString("genre");
               double m_rating = rs.getDouble("rating");
               
               song = new QueryResult.ArtistNameSongNameGenreRatingResult(m_artistname,m_songName, m_genre, m_rating);
               result[i] = song;
               i++;
           }
        
           //Close
           st.close();
       } catch (SQLException e) {
           e.printStackTrace();
       }
 
       return result;
   }
   
   /**
    * Should return the most recent album for the given artistName
    * 
    * @return TitleReleaseDateRatingResult
    */
   public QueryResult.TitleReleaseDateRatingResult getMostRecentAlbum(String artistName){
	   ResultSet rs;
	   QueryResult.TitleReleaseDateRatingResult album = null;
       String query = "select distinct al1.title, al1.releaseDate, al1.albumRating " + 
    		   		  "from Album al1, Artist art1 " +
    		   		  "where art1.artistName=? and art1.artistID = al1.artistID and " +
    		   		  "al1.releaseDate >= ALL (select al2.releaseDate from Album al2, Artist art2 " +
    		   		  "where art2.artistName=? and art2.artistID = al2.artistID)";
       
       
       try {
    	   PreparedStatement ps = this.con.prepareStatement(query);
    	   ps.setString(1, artistName);
           ps.setString(2, artistName);
           rs = ps.executeQuery();
           
           
           //Statement st = this.con.createStatement();
           //rs = st.executeQuery(query);	

           rs.next();
           String m_title = rs.getString("title");
           String m_releasedate = rs.getString("releaseDate");
           double m_rating = rs.getDouble("albumRating");
           
           album = new QueryResult.TitleReleaseDateRatingResult(m_title, m_releasedate, m_rating);
           
           //Close
           ps.close();
           

       } catch (SQLException e) {
           e.printStackTrace();
       }
       
       return album;
   }
   
   /**
    * Should return songs that are listened by both userName1 & userName2
    * 
    * @return ArtistNameSongNameGenreRatingResult[]
    */
   public QueryResult.ArtistNameSongNameGenreRatingResult[] getCommonSongs(String userName1, String userName2) {
	   ResultSet rs;
       QueryResult.ArtistNameSongNameGenreRatingResult song;
       QueryResult.ArtistNameSongNameGenreRatingResult[] result = new QueryResult.ArtistNameSongNameGenreRatingResult[99];
       int i = 0;
       int size = 0;
       String query = "select distinct a1.artistName, s1.songName, s1.genre, s1.rating from Song s1, Artist a1, User u1, Listen l1 " +
    		   		  "where u1.userName =? and u1.userID=l1.userID and l1.songID=s1.songID and s1.artistID=a1.artistID and s1.songID in "+
    		   		  "(select s2.songID from Song s2, User u2, Listen l2 " +
  		   		  	  "where u2.userName =? and u2.userID=l2.userID and l2.songID=s2.songID) "+
    		   		  "order by s1.rating DESC";

       try {
    	   
    	   PreparedStatement ps = this.con.prepareStatement(query);
    	   ps.setString(1, userName1);
           ps.setString(2, userName2);
           rs = ps.executeQuery();
           
    	   while(rs.next()) {
        	   size++;
           }
           
    	   result = new QueryResult.ArtistNameSongNameGenreRatingResult[size] ;
           rs = ps.executeQuery();
           while(rs.next()) {
               String m_artistname = rs.getString("artistName");
               String m_songName = rs.getString("songName");
               String m_genre = rs.getString("genre");
               double m_rating = rs.getDouble("rating");
               
               song = new QueryResult.ArtistNameSongNameGenreRatingResult(m_artistname,m_songName, m_genre, m_rating);
               result[i] = song;
               i++;
           }
        
           //Close
           ps.close();
       } catch (SQLException e) {
           e.printStackTrace();
       }

       return result;
   }
   
   /**
    * Should return artists whose songs are listened by the given user  
    * and number of times his/her songs have been listened by the given user 
    * 
    * @return ArtistNameNumberOfSongsResult[]
    */
   public QueryResult.ArtistNameNumberOfSongsResult[] getNumberOfTimesSongsListenedByUser(String userName){
	   ResultSet rs;
	   QueryResult.ArtistNameNumberOfSongsResult artist;
	   QueryResult.ArtistNameNumberOfSongsResult[] result = null;
       int i = 0;
       int size = 0;
       String query = "select distinct a.artistName, sum(l.listenCount) as numberOfSongs from Artist a, User u, Listen l, Song s "+
    		   		  "where u.userName=? and u.userID = l.userID and l.songID = s.songID and s.artistID = a.artistID " +
    		   		  "group by a.artistName order by a.artistName ASC";

       try {
    	   PreparedStatement ps = this.con.prepareStatement(query);
    	   ps.setString(1, userName);
           rs = ps.executeQuery();
           
           while(rs.next()) {
        	   size++;
           }
           
    	   result = new QueryResult.ArtistNameNumberOfSongsResult[size] ;
           rs = ps.executeQuery();

           while(rs.next()) {
               String m_artistname = rs.getString("artistName");
               int m_numberofsongs = rs.getInt("numberOfSongs");
               
               artist = new QueryResult.ArtistNameNumberOfSongsResult(m_artistname,m_numberofsongs);
               result[i] = artist;
               i++;
           }
        
           //Close
           ps.close();
       } catch (SQLException e) {
           e.printStackTrace();
       }

       return result;
   }
   
   /**
    * Should return users who have listened all songs of a given artist
    * 
    * @return User[]
    */
   public User[] getUsersWhoListenedAllSongs(String artistName) {
	  
	   ResultSet rs;
       User[] result = null;
       int i = 0;
       int size = 0;
       
       String query = "select distinct u.userID, u.userName, u.email, u.password from User u "+
		   		  "where not exists (select s.songID from Artist a, Song s where a.artistName=? and s.artistID = a.artistID " +
		   		  "and s.songID not in "+
		   		  "(select l.songID from Listen l where l.userID=u.userID)) order by u.userID ASC";

       try {
    	   PreparedStatement ps = this.con.prepareStatement(query);
    	   ps.setString(1, artistName);
           rs = ps.executeQuery();
           
           while(rs.next()) {
        	   size++;
           }
           
           result = new User[size];
           rs = ps.executeQuery();
           while(rs.next()) {
               int m_uid = rs.getInt("userID");
               String m_uname = rs.getString("userName");
               String m_email = rs.getString("email");
               String m_password = rs.getString("password");
               
               result[i]  = new User(m_uid, m_uname, m_email, m_password);
               i++;
           }
        
           //Close
           ps.close();
       } catch (SQLException e) {
           e.printStackTrace();
       }
 
       return result;
   }
   
   /**
    * Should return the userID, userName and number of songs listened by this user such that 
    * none of these songs are listened by any other user.
    * 
    * @return QueryResult.UserIdUserNameNumberOfSongsResult[]
    */
   public QueryResult.UserIdUserNameNumberOfSongsResult[]  getUserIDUserNameNumberOfSongsNotListenedByAnyone(){
	   ResultSet rs;
	   QueryResult.UserIdUserNameNumberOfSongsResult[] result = null;
       int i = 0;
       int size = 0;
       String query = "select u.userID, u.userName, count(l.songID) as songNo from User u, Listen l where u.userID = l.userID and l.songID not in (select l2.songID from Listen l2 where l2.userID != l.userID and l2.songID = l.songID) group by l.userID order by u.userID ASC";

       try {
    	   
    	   PreparedStatement ps = this.con.prepareStatement(query);
           rs = ps.executeQuery();
           
    	   while(rs.next()) {
        	   size++;
           }
           
    	   result = new QueryResult.UserIdUserNameNumberOfSongsResult[size] ;
           rs = ps.executeQuery();
           while(rs.next()) {
        	   int m_uid = rs.getInt("userID");
               String m_userName = rs.getString("userName");
               int m_songNo = rs.getInt("songNo");
               
               result[i] = new QueryResult.UserIdUserNameNumberOfSongsResult(m_uid,m_userName,m_songNo);
               i++;
           }
        
           //Close
           ps.close();
       } catch (SQLException e) {
           e.printStackTrace();
       }

       return result;
   }
   
   /**
    * Should return artists who have sung pop music at least once and whose average rating of
    * all songs is greater than the given rating
    * 
    * @return Artist[]
    */
   public Artist[] getArtistSingingPopGreaterAverageRating(double rating) {
	   ResultSet rs;
	   Artist[] result = null;
       int i = 0;
       int size = 0;
       String query = "select a.artistID, a.artistName from Artist a, Song s where s.artistID = a.artistID and a.artistID in (select s2.artistID from Song s2 where s2.genre = 'Pop')\n" + 
       		"group by a.artistID having avg(s.rating) > ? order by a.artistID ASC";
       try {
    	   
    	   PreparedStatement ps = this.con.prepareStatement(query);
    	   ps.setDouble(1, rating);
           rs = ps.executeQuery();
           
    	   while(rs.next()) {
        	   size++;
           }
           
    	   result = new Artist[size] ;
           rs = ps.executeQuery();
           while(rs.next()) {
        	   int m_aid = rs.getInt("artistID");
               String m_aName = rs.getString("artistName");
               
               result[i] = new Artist(m_aid,m_aName);
               i++;
           }
        
           //Close
           ps.close();
       } catch (SQLException e) {
           e.printStackTrace();
       }

       return result;
   }
   
   /**
    * Retrieve songs with the lowest rating in pop category, in case of more than one song exist, 
    * retrieve the least listened ones
    * 
    * @return Song[]
    */
   public Song[] retrieveLowestRatedAndLeastNumberOfListenedSongs() {
	   ResultSet rs;
	   Song[] result = null;
       int i = 0;
       int size = 0;
       String query = "select distinct s.songID, s.songName, s.rating, s.genre, s.artistID, s.albumID  from (select * from Song s where s.genre='Pop' and \n" + 
       		"s.rating <= ALL(select s2.rating from Song s2 where s2.genre = 'Pop')) as s, Listen l\n" + 
       		"where l.songID = s.songID group by l.songID having sum(l.listenCount) <= ALL (select sum(l2.listenCount) from Listen l2, (select * from Song s where s.genre='Pop' and \n" + 
       		"s.rating <= ALL(select s2.rating from Song s2 where s2.genre = 'Pop')) as s2 where s2.songID = l2.songID group by l2.songID) order by s.songID ASC";
       try {
    	   
    	   PreparedStatement ps = this.con.prepareStatement(query);
           rs = ps.executeQuery();
           
    	   while(rs.next()) {
        	   size++;
           }
           
    	   result = new Song[size] ;
           rs = ps.executeQuery();
           while(rs.next()) {
        	   int m_sid = rs.getInt("songID");
               String m_sName = rs.getString("songName");
               double m_rating = rs.getDouble("rating");
               String m_genre = rs.getString("genre");
               int m_artid = rs.getInt("artistID");
               int m_alid = rs.getInt("albumID");
               
               result[i] = new Song(m_sid,m_sName, m_genre, m_rating, m_artid, m_alid);
               i++;
           }
        
           //Close
           ps.close();
       } catch (SQLException e) {
           e.printStackTrace();
       }

       return result;
   }
   
   /**
    * Multiply the rating of the album by 1.5 whose releaseDate is after for a given date
    * 
    * @return the row count for SQL Data Manipulation Language (DML) statements
    */
   public int multiplyRatingOfAlbum(String releaseDate) {
	   int rs = 1;
       String query = "update Album set albumRating= 1.5*albumRating where releaseDate > ?";

       try {
    	   PreparedStatement ps = this.con.prepareStatement(query);
           ps.setString(1, releaseDate);
           rs = ps.executeUpdate();
           /*
           while(rs == 1) {
               i++;
               rs = ps.executeUpdate();
           } */
        
           //Close
           ps.close();
       } catch (SQLException e) {
           e.printStackTrace();
       }

       return rs;
   }
   
   /**
    * Delete the song for the given songName
    * 
    * @return Song
    */
   public Song deleteSong(String songName) {
	   ResultSet rs1;
	   String query1 = "select * from Song s where songName=?";
	   String query2 = "delete from Song where songName=?";
	   Song song = null;
	   
	   try {
		   PreparedStatement ps1 = this.con.prepareStatement(query1);
		   ps1.setString(1, songName);
           rs1 = ps1.executeQuery();

           rs1.next();
           int m_id = rs1.getInt("songID");
           String m_name = rs1.getString("songName");
           String m_genre = rs1.getString("genre");
           double m_rating = rs1.getDouble("rating");
           int m_artist = rs1.getInt("artistID");
           int m_album = rs1.getInt("albumID");
           
           song = new Song(m_id, m_name, m_genre, m_rating, m_artist,m_album);
           
           //Close
           ps1.close();
           
           PreparedStatement ps2 = this.con.prepareStatement(query2);
		   ps2.setString(1, songName);
           ps2.executeUpdate();
           ps2.close();
           

       } catch (SQLException e) {
           e.printStackTrace();
       }
	   
	   return song;
   }
}