package com.thermometer.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.thermometer.db.model.BindingInfo;
import com.thermometer.db.model.Client;
import com.thermometer.db.model.Device;
import com.thermometer.db.model.Temperature;


public class ThermometerDB {
	static private Statement connect() {
		String driver ="com.mysql.jdbc.Driver";
	    String url="jdbc:mysql://115.28.176.65:3306/thermometer";
	    String user="root";
	    String password="root";
	    try{
		    Class.forName(driver);
		    Connection con=DriverManager.getConnection(url,user,password);
	        return con.createStatement();
	    }catch(Exception e){
	        System.out.println("无法加载驱动程序" +driver);
	        return null;
	    }
	}
	
	static public boolean insertDevice(Device device) {
		if (device == null) {
			return false;
		}
		Statement statement = connect();
		Connection conn = null;
		if (statement == null) {
			return false;
		}
		try {
			conn = statement.getConnection();
			if (!conn.isClosed()) {
				String sql = "insert into " + Device.TABLE + " values(" +
						device.getDeviceID() + ", " + device.getDeviceType() +
						", " +device.getDeviceMeasureInterval() + ");";
				return statement.executeUpdate(sql) == 1 ? true : false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				statement.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;
		
	}
	static public boolean insertBindingInfo(BindingInfo bindingInfo) {
		if (bindingInfo == null) {
			return false;
		}
		Statement statement = connect();
		Connection conn = null;
		if (statement == null) {
			return false;
		}
		try {
			conn = statement.getConnection();
			if (!conn.isClosed()) {
				String sql = "insert into " + BindingInfo.TABLE + " values(" +
						bindingInfo.getDeviceID() + ", " + bindingInfo.getOpenID() +
						", " +bindingInfo.getDisplayOnWeChat() + ");";
				return statement.executeUpdate(sql) == 1 ? true : false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				statement.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;
		
	}
	static public boolean insertClient(Client client) {
		if (client == null) {
			return false;
		}
		Statement statement = connect();
		Connection conn = null;
		if (statement == null) {
			return false;
		}
		try {
			conn = statement.getConnection();
			if (!conn.isClosed()) {
				String sql = "insert into " + Client.TABLE + " values(" +
						client.getOpenID() + ");";
				return statement.executeUpdate(sql) == 1 ? true : false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				statement.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;
		
	}
	static public boolean insertTemperature(Temperature temperature) {
		if (temperature == null) {
			return false;
		}
		Statement statement = connect();
		Connection conn = null;
		if (statement == null) {
			return false;
		}
		try {
			conn = statement.getConnection();
			if (!conn.isClosed()) {
				String sql = "insert into " + Temperature.TABLE + " values(" +
						temperature.getDeviceID() + ", " + temperature.getOpenID() +
						", " +temperature.getTime() + ", " + temperature.getTemperature() +
						");";
				return statement.executeUpdate(sql) == 1 ? true : false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				statement.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;
	}
	
	public static ArrayList<Device> findDevices(Device device) {
		Statement statement = connect();
		Connection conn = null;
		if (statement == null) {
			return null;
		}
		try {
			conn = statement.getConnection();
			
			if (!conn.isClosed()) {
				String sql = "select * from " + Device.TABLE + " where 1=1";
				if (device.getDeviceID() != null && !device.getDeviceID().equals("")) {
					sql += " and " + Device.DEVICE_ID + " = " + device.getDeviceID();
				}
				if (device.getDeviceType() != null && !device.getDeviceType().equals("")) {
					sql += " and " + Device.DEVICE_TYPE + " = " + device.getDeviceType();
				}
				sql += " ;";
				ResultSet rs = statement.executeQuery(sql);
				/*ResultSetMetaData rsmd = rs.getMetaData();*/
				ArrayList<Device> devices = new ArrayList<Device>();
				while (rs.next()) {
                    Device temp = new Device();
                    temp.setDeviceID(rs.getString(Device.DEVICE_ID));
                    temp.setDeviceType(rs.getString(Device.DEVICE_TYPE));
                    temp.setDeviceMeasureInterval(Integer.parseInt(rs.getString
                    		(Device.DEVICE_MEASURE_INTERVAL)));
                    devices.add(temp);
                }
				return devices;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				statement.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	public static BindingInfo/*ArrayList<BindingInfo>*/ findBindingInfo(BindingInfo bindingInfo) {
		Statement statement = connect();
		Connection conn = null;
		if (statement == null) {
			return null;
		}
		try {
			conn = statement.getConnection();
			if (!conn.isClosed()) {
				String sql = "select * from " + BindingInfo.TABLE + " where 1=1";
				if (bindingInfo.getDeviceID() == null && !bindingInfo.getDeviceID().equals("")) {
					sql += " and " + BindingInfo.DEVICE_ID + " = " + bindingInfo.getDeviceID();
				}
				if (bindingInfo.getOpenID() != null && !bindingInfo.getOpenID().equals("")) {
					sql += " and " + BindingInfo.OPEN_ID + " = " + bindingInfo.getOpenID();
				}
				sql += " ;";
				ResultSet rs = statement.executeQuery(sql);
				/*ResultSetMetaData rsmd = rs.getMetaData();*/
				//ArrayList<BindingInfo> bindingInfos = new ArrayList<BindingInfo>();
				while (rs.next()) {
					BindingInfo temp = new BindingInfo();
                    temp.setDeviceID(rs.getString(BindingInfo.DEVICE_ID));
                    temp.setOpenID(rs.getString(BindingInfo.OPEN_ID));
                    temp.setDisplayOnWeChat(rs.getString(BindingInfo.DISPLAY_ON_WECHAT));
                    return temp;
                    //bindingInfos.add(temp);
                }
				//return bindingInfos;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				statement.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return null;
	}
	public static ArrayList<Temperature> findTemperature(Temperature temperature) {
		Statement statement = connect();
		Connection conn = null;
		if (statement == null) {
			return null;
		}
		try {
			conn = statement.getConnection();
			
			if (!conn.isClosed()) {
				String sql = "select * from " + Temperature.TABLE + " where 1=1";
				if (temperature.getDeviceID() != null && !temperature.getDeviceID().equals("")) {
					sql += " and " + Temperature.DEVICE_ID + " = " + temperature.getDeviceID();
				}
				if (temperature.getOpenID() != null && !temperature.getOpenID().equals("")) {
					sql += " and " + Temperature.OPEN_ID + " = " + temperature.getOpenID();
				}
				if (temperature.getTime() != null && !temperature.getTime().equals("")) {
					sql += " and " + Temperature.TIME + " = " + temperature.getTime();
				}
				sql += " ;";
				ResultSet rs = statement.executeQuery(sql);
				/*ResultSetMetaData rsmd = rs.getMetaData();*/
				ArrayList<Temperature> temperatures = new ArrayList<Temperature>();
				while (rs.next()) {
					Temperature temp = new Temperature();
                    temp.setDeviceID(rs.getString(Temperature.DEVICE_ID));
                    temp.setOpenID(rs.getString(Temperature.OPEN_ID));
                    temp.setTime(rs.getString(Temperature.TIME));
                    temp.setTemperature(Integer.parseInt(rs.getString(Temperature.TEMPERATURE)));
                    temperatures.add(temp);
                }
				return temperatures;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				statement.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return null;
	}
	public static ArrayList<Client> findClient(Client client) {
		Statement statement = connect();
		Connection conn = null;
		if (statement == null) {
			return null;
		}
		try {
			conn = statement.getConnection();
			
			if (!conn.isClosed()) {
				String sql = "select * from " + Client.TABLE;
				if (client.getOpenID() != null && !client.getOpenID().equals("")) {
					sql += " where " + Client.OPEN_ID + " = " + client.getOpenID();
				}
				sql += " ;";
				ResultSet rs = statement.executeQuery(sql);
				/*ResultSetMetaData rsmd = rs.getMetaData();*/
				ArrayList<Client> clients = new ArrayList<Client>();
				while (rs.next()) {
					Client temp = new Client();
                    temp.setOpenID(rs.getString(Client.OPEN_ID));
                    clients.add(temp);
                }
				return clients;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				statement.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	// 修改
	static public boolean updateDevice(Device device) {
		if (device == null) {
			return false;
		}
		Statement statement = connect();
		Connection conn = null;
		if (statement == null) {
			return false;
		}
		try {
			conn = statement.getConnection();
			if (!conn.isClosed()) {
				String sql = "update " + Device.TABLE + " set " +
						Device.DEVICE_MEASURE_INTERVAL + " =  " +
						device.getDeviceMeasureInterval() + " where " +
						Device.DEVICE_ID + " = " + device.getDeviceID() +  ");";
				return statement.executeUpdate(sql) == 1 ? true : false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				statement.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;
		
	}
	static public boolean updateBindingInfo(BindingInfo bindingInfo) {
		if (bindingInfo == null) {
			return false;
		}
		Statement statement = connect();
		Connection conn = null;
		if (statement == null) {
			return false;
		}
		try {
			conn = statement.getConnection();
			if (!conn.isClosed()) {
				String sql = "update " + BindingInfo.TABLE + " set " +
						BindingInfo.DISPLAY_ON_WECHAT + " =  " +
						bindingInfo.getDisplayOnWeChat() + " where " +
						BindingInfo.DEVICE_ID + " = " + bindingInfo.getDeviceID() +
						" and " + BindingInfo.OPEN_ID + " = " + bindingInfo.getOpenID()
						+ " ;";
				return statement.executeUpdate(sql) == 1 ? true : false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				statement.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;
		
	}
	static public boolean updateBindingInfoWithOpenID(BindingInfo bindingInfo) {
		if (bindingInfo == null) {
			return false;
		}
		Statement statement = connect();
		Connection conn = null;
		if (statement == null) {
			return false;
		}
		try {
			conn = statement.getConnection();
			if (!conn.isClosed()) {
				String sql = "update " + BindingInfo.TABLE + " set " +
						BindingInfo.DISPLAY_ON_WECHAT + " =  " +
						bindingInfo.getDisplayOnWeChat() + " where " +
						BindingInfo.OPEN_ID + " = " + bindingInfo.getOpenID()
						+ " ;";
				return statement.executeUpdate(sql) == 1 ? true : false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				statement.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;
		
	}
	/*static public boolean updateClient(Client client) {
		if (client == null) {
			return false;
		}
		Statement statement = connect();
		Connection conn = null;
		if (statement == null) {
			return false;
		}
		try {
			conn = statement.getConnection();
			if (!conn.isClosed()) {
				String sql = "insert into " + Client.TABLE + " values(" +
						client.getOpenID() + ");";
				return statement.executeUpdate(sql) == 1 ? true : false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				statement.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;
		
	}*/
	static public boolean updateTemperature(Temperature temperature) {
		if (temperature == null) {
			return false;
		}
		Statement statement = connect();
		Connection conn = null;
		if (statement == null) {
			return false;
		}
		try {
			conn = statement.getConnection();
			if (!conn.isClosed()) {
				String sql = "update " + Temperature.TABLE + " set " +
						Temperature.TIME + " =  " +
						temperature.getTime() + " , " +
						Temperature.TEMPERATURE + " = " + 
						temperature.getTemperature() + " where " +
						Temperature.DEVICE_ID + " =  " +
						temperature.getDeviceID() + " and " +
						Temperature.OPEN_ID + " = " + 
						temperature.getOpenID() + " ;";
				return statement.executeUpdate(sql) == 1 ? true : false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				statement.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;
	}
	
	static public boolean deleteBindingInfo(BindingInfo bindingInfo) {
		if (bindingInfo == null) {
			return false;
		}
		Statement statement = connect();
		Connection conn = null;
		if (statement == null) {
			return false;
		}
		try {
			conn = statement.getConnection();
			if (!conn.isClosed()) {
				String sql = "delete from " + BindingInfo.TABLE + " where " +
						BindingInfo.OPEN_ID + " = " + bindingInfo.getOpenID()
						+ " ;";
				return statement.executeUpdate(sql) == 1 ? true : false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				statement.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;
		
	}
}
