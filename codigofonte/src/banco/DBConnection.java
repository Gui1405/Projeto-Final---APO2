package banco;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

private String host;
private String port;
private String schema;
private String user;
private String password;

private Connection connection = null;

public DBConnection(String host, String port, String schema, String user, String password) {
this.setHost(host);
this.setPort(port);
this.setSchema(schema);
this.setUser(user);
this.setPassword(password);
this.doConnection();
}

public DBConnection() {
this.setHost("localhost");
this.setPort("3306");
this.setSchema("cinema");
this.setUser("root");
this.setPassword("");
this.doConnection();
}

private void doConnection() {
String timezone = "&useTimezone=true&serverTimezone=UTC";
String url = "jdbc:mysql://" + this.host + ":" + port + "/" + this.schema + "?user=" + this.user + "&password=" + this.password + timezone;

try {
Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
this.connection = DriverManager.getConnection(url);
}

catch (Exception e) {
e.printStackTrace();
}
}

public Connection getConnection() {
return (this.connection);
}

public void setHost(String host) {
this.host = (host.isEmpty() ? "localhost" : host);
}

public void setPort(String port) {
this.port = (port.isEmpty() ? "3306" : port);
}

public void setSchema(String schema) {
this.schema = schema;
}

public void setUser(String user) {
this.user = user;
}

public void setPassword(String password) {
this.password = password;
}
}