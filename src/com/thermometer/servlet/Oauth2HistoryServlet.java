package com.thermometer.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;


import com.thermometer.db.ThermometerDB;
import com.thermometer.db.model.BindingInfo;
import com.thermometer.db.model.Client;
import com.thermometer.db.model.Device;
import com.thermometer.db.model.Temperature;
import com.thermometer.message.Oauth2GetAccessTokenMsg;
import com.thermometer.message.ToDeviceMsg;
import com.thermometer.message.model.Oauth2Response;

public class Oauth2HistoryServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Oauth2HistoryServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();*/
		String code=request.getParameter("code");
		this.log("code is " + code);
		Oauth2GetAccessTokenMsg msg = new Oauth2GetAccessTokenMsg();
		msg.setCode(code);
		if (msg.sendMessage()) {
			if (msg.getResponse().getErrcode() != null && !msg.getResponse().getErrcode().equals("")) {
				request.setAttribute("code", code);
				RequestDispatcher rd = request.getRequestDispatcher("../webpage/getOpenIDFailed.jsp");
				rd.forward(request, response);
				return;
			}
			Oauth2Response oauth2Response = msg.getResponse();
			this.log("openid is " + oauth2Response.getOpenid());
			request.setAttribute("openid", oauth2Response.getOpenid());
			RequestDispatcher rd = request.getRequestDispatcher("../webpage/history.jsp");
			rd.forward(request, response);
		} else {
			request.setAttribute("code", code);
			RequestDispatcher rd = request.getRequestDispatcher("../webpage/getOpenIDFailed.jsp");
			rd.forward(request, response);
			//response.sendRedirect(arg0)
		}
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//String openid = request.getParameter("openid");
		String openid = "o3hXIjtu6EXiSEdFyxQWMT202ySw";// 测试时设置为这个而已
		String dateStr = request.getParameter("searchDate");
		dateStr = dateStr.trim();
		
		BindingInfo bindingInfo = new BindingInfo();
		bindingInfo.setOpenID(openid);
		BindingInfo info = ThermometerDB.findBindingInfo(bindingInfo);
		if (info != null) {
			String deviceId = info.getDeviceID();
			
			Device device = new Device();
			device.setDeviceID(deviceId);
			Device realDevice = ThermometerDB.findDevices(device);
			
			Temperature temperature = new Temperature();
			temperature.setOpenID(openid);
			temperature.setDeviceID(realDevice.getDeviceID());
			ArrayList<Temperature> temperatures =  ThermometerDB.findTemperature(temperature);
			
			Map<String, ArrayList> map = new HashMap<String, ArrayList>();
			
			ArrayList<Float> times = new ArrayList<Float>();
			ArrayList<Integer> temperaturesDuringTime = new ArrayList<Integer>();
			for (Temperature temp : temperatures) {
				if (temp.getTime().contains(dateStr)) {
					String []time = temp.getTime().split("-");
					//map.put(time[3], temp.getTemperature());
					times.add(calculateTime(time[3]));
					temperaturesDuringTime.add(temp.getTemperature());
				}
			}
			map.put("time", times);
			map.put("temperature", temperaturesDuringTime);
			JSONArray jsonRespArray = JSONArray.fromObject( map );
			response.getWriter().write(jsonRespArray.toString());
			
		} else {
			request.setAttribute("openid", openid);
			RequestDispatcher rd = request.getRequestDispatcher("../webpage/getBindingInfoFailed.jsp");
			rd.forward(request, response);
		}
		/*Map<String, ArrayList> map = new HashMap<String, ArrayList>();
		
		ArrayList times = new ArrayList();
		ArrayList<Integer> temperaturesDuringTime = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			times.add(7.1 + i);
			temperaturesDuringTime.add(340 + 10 * i);
		}
		map.put("time", times);
		map.put("temperature", temperaturesDuringTime);
		JSONArray jsonRespArray = JSONArray.fromObject( map );
		response.getWriter().write(jsonRespArray.toString());*/
	}
		

	private float calculateTime (String time) {
		String []times = time.split(":");
		return (float) (Float.valueOf(times[0]).floatValue() + Float.valueOf(times[1]).floatValue() / 60.0 + Float.valueOf(times[2]).floatValue() / 3600.0);
	}
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
