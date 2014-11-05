package com.thermometer.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javassist.bytecode.ByteArray;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import com.thermometer.db.ThermometerDB;
import com.thermometer.db.model.BindingInfo;
import com.thermometer.db.model.Device;
import com.thermometer.message.Oauth2GetAccessTokenMsg;
import com.thermometer.message.ToDeviceMsg;
import com.thermometer.message.model.Oauth2Response;
import com.thermometer.utility.StringUtil;

public class Oauth2IntervalServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Oauth2IntervalServlet() {
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
			RequestDispatcher rd = request.getRequestDispatcher("../webpage/interval_setting.jsp");
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

		/*response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();*/
		String intervalStr = request.getParameter("interval");
		String openid = request.getParameter("openid");
		
		int interval = Integer.parseInt(intervalStr);
		byte []command = new byte[5];
		command[0] = (byte) 0xfa;
		command[1] = (byte) 0xf2;
		command[2] = 0x02;
		byte []internalBytes = StringUtil.int2LittleEndianBytes(interval, 2);
		command[3] = internalBytes[0];
		command[4] = internalBytes[1];
		BindingInfo bindingInfo = new BindingInfo();
		bindingInfo.setOpenID(openid);
		BindingInfo info = ThermometerDB.findBindingInfo(bindingInfo);
		if (info != null) {
			String deviceId = info.getDeviceID();
			
			Device device = new Device();
			device.setDeviceID(deviceId);
			Device realDevice = ThermometerDB.findDevices(device);
			
			ToDeviceMsg msg = new ToDeviceMsg();
			msg.setOpenId(openid);
			msg.setDeviceId(deviceId);
			msg.setDeviceType(realDevice.getDeviceType());
			msg.setContent(command);
			if (msg.sendMessage()) {
				realDevice.setDeviceMeasureInterval(interval);
				ThermometerDB.updateDevice(realDevice);
				RequestDispatcher rd = request.getRequestDispatcher("../webpage/success.jsp");
				rd.forward(request, response);
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("../webpage/fail.jsp");
				rd.forward(request, response);
			}
		} else {
			request.setAttribute("openid", openid);
			RequestDispatcher rd = request.getRequestDispatcher("../webpage/getBindingInfoFailed.jsp");
			rd.forward(request, response);
		}
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
