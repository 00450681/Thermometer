package com.thermometer.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.thermometer.db.ThermometerDB;
import com.thermometer.db.model.BindingInfo;
import com.thermometer.db.model.Device;
import com.thermometer.db.model.Temperature;
import com.thermometer.message.ToWeChatMsg;
import com.thermometer.utility.HttpUtil;
import com.thermometer.utility.JavaLog;
import com.thermometer.utility.StringUtil;

public class WXServlet extends HttpServlet {

	private final int DEVICE_MSG_COMMAND_TEMPERATURE = 0x01;
	private final int DEVICE_MSG_COMMAND_RESULT = 1;
	public static String ACCESS_TOKEN = 
			"llntgPT5f55kxl1RAUMDXtVvxDiatg98vbPxsFnNe6IDqrsVTJSMauk65H-FFnohK8XcusPrYd5EACemHUASWw";
	private HashMap<String, Integer> shouldDisplayOnWeChatMap = new HashMap<String, Integer>();
	/**
	 * Constructor of the object.
	 */
	public WXServlet() {
		super();
		// 读取数据库，填充shouldDisplayOnWeChatMap
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
		response.getWriter().write(request.getParameter("echostr"));
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
		
		System.out.println("----------post---------------");
        try {
            InputStream is = request.getInputStream();
            // 取HTTP请求流长度
            int size = request.getContentLength();
            // 用于缓存每次读取的数据
            byte[] buffer = new byte[size];
            // 用于存放结果的数组
            byte[] xmldataByte = new byte[size];
            int count = 0;
            int rbyte = 0;
            // 循环读取
            while (count < size) { 
                // 每次实际读取长度存于rbyte中
                rbyte = is.read(buffer); 
                for(int i=0;i<rbyte;i++) {
                    xmldataByte[count + i] = buffer[i];
                }
                count += rbyte;
            }
            is.close();
            String requestStr = new String(xmldataByte, "UTF-8");
            Document doc = DocumentHelper.parseText(requestStr);
            Element rootElt = doc.getRootElement();
            String content = rootElt.elementText("Content");
            String toUserName = rootElt.elementText("ToUserName");
            String fromUserName = rootElt.elementText("FromUserName");
            String MsgType = rootElt.elementText("MsgType");
            String Event = rootElt.elementText("Event");
            //得到所有的有用数据
            
            JavaLog.log4j.info("Content : " + content + " toUserName : " + 
            toUserName + " fromUserName : " + fromUserName + " MsgType is " + 
            		MsgType + " Event is " + Event);
            
            if (/*!StringUtil.isBlank(content) && */"device_event".equals(MsgType)) {
            	JavaLog.log4j.info("device_event");
            	String event = rootElt.elementText("Event");
            	if ("bind".equals(event)) {
            		JavaLog.log4j.info("bind_event");
            		// 插入绑定信息
            		String deviceID = rootElt.elementText("DeviceID");
            		String openID = rootElt.elementText("OpenID");
            		JavaLog.log4j.info("Device ID : " + deviceID + " OpenID : " + openID);
            		BindingInfo bindingInfo = new BindingInfo();
            		bindingInfo.setDeviceID(deviceID);
            		bindingInfo.setOpenID(openID);
            		bindingInfo.setDisplayOnWeChat("1");
            		JavaLog.log4j.info("inserting binding info : " + ThermometerDB.insertBindingInfo(bindingInfo));
            		
            		String deviceType = rootElt.elementText("DeviceType");
            		Device device = new Device();
            		device.setDeviceID(deviceID);
            		device.setDeviceType(deviceType);
            		device.setDeviceMeasureInterval(3);
            		JavaLog.log4j.info("inserting device : " + ThermometerDB.insertDevice(device));
            	}else if ("unbind".equals(event)) {
            		JavaLog.log4j.info("unbind_event");
            		// 删除绑定信息
            		String deviceID = rootElt.elementText("DeviceID");
            		String openID = rootElt.elementText("OpenID");
            		JavaLog.log4j.info("Device ID : " + deviceID + " OpenID : " + openID);
            		BindingInfo bindingInfo = new BindingInfo();
            		bindingInfo.setDeviceID(deviceID);
            		bindingInfo.setOpenID(openID);
            		JavaLog.log4j.info("deleting binding info : " + ThermometerDB.deleteBindingInfo(bindingInfo));
            		
            		String deviceType = rootElt.elementText("DeviceType");
            		Device device = new Device();
            		device.setDeviceID(deviceID);
            		JavaLog.log4j.info("deleting device : " + ThermometerDB.deleteDevice(device));
            	}

            } else if (!StringUtil.isBlank(content) && "device_text".equals(MsgType)) {
            	JavaLog.log4j.info("device_text");
            	String openID = rootElt.elementText("OpenID");
            	String deviceID = rootElt.elementText("DeviceID");
            	String deviceType = rootElt.elementText("DeviceType");
            	String sessionID = rootElt.elementText("SessionID");
            	//byte []contentBytes = StringUtil.strToByteArray(content);
            	byte []contentBytes = new BASE64Decoder().decodeBuffer(content);
            	JavaLog.log4j.info("content is " + StringUtil.bytesToHexStr(contentBytes));
            	switch (contentBytes[1]) {
            	case DEVICE_MSG_COMMAND_TEMPERATURE:
            		int temp = (contentBytes[3] & 0xff) | ((contentBytes[4] & 0xff) << 8);
            		JavaLog.log4j.info("Temp is " + temp);
            		//float temperature = temp / 10;
            		// 存储在数据库中，再显示在WeChat中
            		Temperature temperature = new Temperature();
            		temperature.setOpenID(openID);
            		temperature.setDeviceID(deviceID);
            		Calendar c = Calendar.getInstance();
            		StringBuilder sb = new StringBuilder();
            		sb.append(c.get(Calendar.YEAR));
            		sb.append('-');
            		sb.append(c.get(Calendar.MONTH) + 1);
            		sb.append('-');
            		sb.append(c.get(Calendar.DAY_OF_MONTH));
            		sb.append('-');
            		sb.append(c.get(Calendar.HOUR_OF_DAY));
            		sb.append(':');
            		sb.append(c.get(Calendar.MINUTE));
            		sb.append(':');
            		sb.append(c.get(Calendar.SECOND));
            		temperature.setTime(sb.toString());
            		temperature.setTemperature(temp);
            		
            		ThermometerDB.insertTemperature(temperature);
            		
            		BindingInfo bindingInfo = new BindingInfo();
            		bindingInfo.setDeviceID(deviceID);
            		bindingInfo.setOpenID(openID);
            		BindingInfo bindingInfoDB = ThermometerDB.findBindingInfo(bindingInfo);
            		JavaLog.log4j.info("bindingInfoDB.getDisplayOnWeChat " + bindingInfoDB.getDisplayOnWeChat());
            		
            		String responseStr = "<xml>";
                    responseStr += "<ToUserName><![CDATA[" + fromUserName
                            + "]]></ToUserName>";
                    responseStr += "<FromUserName><![CDATA[" + toUserName
                            + "]]></FromUserName>";
                    responseStr += "<CreateTime>" + System.currentTimeMillis()
                            + "</CreateTime>";
                    responseStr += "<MsgType><![CDATA[device_text]]></MsgType>";
                    responseStr += "<DeviceType><![CDATA[" +deviceType + "]]></DeviceType>";
                    responseStr += "<DeviceID><![CDATA[" + deviceID + "]]></DeviceID>";
                    responseStr += "<SessionID><![CDATA[" + sessionID + "]]></SessionID>";
                    responseStr += "<Content>" + new String(new BASE64Encoder().encode(new byte []{(byte) temp}) ) + "</Content>";
                    responseStr += "</xml>";
                    JavaLog.log4j.info("responseStr " + responseStr);
                    response.getWriter().write(responseStr);
            		if (bindingInfoDB.getDisplayOnWeChat().equals("1")) {
            			
                        ToWeChatMsg msg = new ToWeChatMsg();
                        msg.openId = openID;
                        msg.content = temp / 10 + "." + temp % 10 + "";
                        msg.sendMessage();
                        
            		}
            		break;
            	/*case DEVICE_MSG_COMMAND_RESULT:
            		int result = contentBytes[4] & 0xff;
            		if (result == 0x00) {
            			// 成功,更新,通知网页
            			Device device = new Device();
            			device.setDeviceID(deviceID);
            			ThermometerDB.updateDevice(device);
            		}
            		break;*/
            	}
            } else if ("event".equals(MsgType)) {
            	// 自定义菜单
            	String event = rootElt.elementText("Event");
            	String eventKey = rootElt.elementText("EventKey");
            	if ("CLICK".equals(event)) {
            		if ("SETTING_SHOULD_SHOW_ON_WECHAT".equals(eventKey)) {
            			// fromUserName就是openID，改变shouldDisplayOnWeChat中对应openID的值，再写回数据库中
            			BindingInfo bindingInfo = new BindingInfo();
            			bindingInfo.setOpenID(fromUserName);
            			BindingInfo temp = ThermometerDB.findBindingInfo(bindingInfo);
            			bindingInfo.setDisplayOnWeChat(temp.getDisplayOnWeChat().equals("1") ? "0" : "1");
            			boolean success = ThermometerDB.updateBindingInfoWithOpenID(bindingInfo);
            			String responseContent = "";
            			if (success) {
            				responseContent = "Succeed.The temperature will " + 
            					(temp.getDisplayOnWeChat().equals("1") ? "" : "NOT " + "show up on the WeChat");
            			} else {
            				responseContent = "Failed.";
            			}
            			
            			String responseStr = "<xml>";
                        responseStr += "<ToUserName><![CDATA[" + fromUserName
                                + "]]></ToUserName>";
                        responseStr += "<FromUserName><![CDATA[" + toUserName
                                + "]]></FromUserName>";
                        responseStr += "<CreateTime>" + System.currentTimeMillis()
                                + "</CreateTime>";
                        responseStr += "<MsgType><![CDATA[text]]></MsgType>";
                        responseStr += "<Content>" + responseContent + "</Content>";
                        responseStr += "<FuncFlag>0</FuncFlag>";
                        responseStr += "</xml>";
                        response.getWriter().write(responseStr);
            		}
            	} else if ("VIEW".equals(event)) {
            		
            		/*String responseStr = "<xml>";
                    responseStr += "<ToUserName><![CDATA[" + fromUserName
                            + "]]></ToUserName>";
                    responseStr += "<FromUserName><![CDATA[" + toUserName
                            + "]]></FromUserName>";
                    responseStr += "<CreateTime>" + System.currentTimeMillis()
                            + "</CreateTime>";
                    responseStr += "<MsgType><![CDATA[text]]></MsgType>";
                    responseStr += "<Content>VIEW GOT</Content>";
                    responseStr += "<FuncFlag>0</FuncFlag>";
                    responseStr += "</xml>";
                    response.getWriter().write(responseStr);
            		
            		response.sendRedirect(eventKey);
            		
            		File file = new File("log.txt");
            		FileWriter writter = new FileWriter(file);
            		//FileOutputStream fos = new FileOutputStream("Log.txt");
            		String msg = "Got View Post";
            		System.out.println(msg);
            		writter.write(msg.toCharArray());
            		writter.flush();
            		writter.flush();*/
            	}
            }
            //文本消息
            else if (!StringUtil.isBlank(content) && "text".equals(/*content*/MsgType)) {
                String responseStr = "<xml>";
                responseStr += "<ToUserName><![CDATA[" + fromUserName
                        + "]]></ToUserName>";
                responseStr += "<FromUserName><![CDATA[" + toUserName
                        + "]]></FromUserName>";
                responseStr += "<CreateTime>" + System.currentTimeMillis()
                        + "</CreateTime>";
                responseStr += "<MsgType><![CDATA[text]]></MsgType>";
                responseStr += "<Content>text type℃</Content>";
                responseStr += "<FuncFlag>0</FuncFlag>";
                responseStr += "</xml>";
                response.getWriter().write(responseStr);
            }
            //图文消息
            else if (! StringUtil.isBlank(content) && "news".equals(/*content*/MsgType)) {
                String responseStr = "<xml>";
                responseStr += "<ToUserName><![CDATA[" + fromUserName
                        + "]]></ToUserName>";
                responseStr += "<FromUserName><![CDATA[" + toUserName
                        + "]]></FromUserName>";
                responseStr += "<CreateTime>" + System.currentTimeMillis()
                        + "</CreateTime>";
                responseStr += "<MsgType><![CDATA[news]]></MsgType>";
                responseStr += "<Content><![CDATA[]]></Content>";
 
                responseStr += "<ArticleCount>2</ArticleCount>";
 
                responseStr += "<Articles>";
                responseStr += "<item>";
                responseStr += "<Title><![CDATA[图文消息——红色石头]]></Title>";
                responseStr += "<Discription><![CDATA[图文消息正文——红色石头]]></Discription>";
                responseStr += "<PicUrl><![CDATA[http://redstones.sinaapp.com/res/images/redstones_wx_258.jpg]]></PicUrl>";
                responseStr += "<Url><![CDATA[http://redstones.sinaapp.com/]]></Url>";
                responseStr += "</item>";
 
                responseStr += "<item>";
                responseStr += "<Title><![CDATA[图文消息——红色石头]]></Title>";
                responseStr += "<Discription><![CDATA[图文消息正文——红色石头]]></Discription>";
                responseStr += "<PicUrl><![CDATA[http://redstones.sinaapp.com/res/images/redstones_wx_258.jpg]]></PicUrl>";
                responseStr += "<Url><![CDATA[http://redstones.sinaapp.com/]]></Url>";
                responseStr += "</item>";
 
                responseStr += "</Articles>";
                responseStr += "<FuncFlag>1</FuncFlag>";
                responseStr += "</xml>";
                response.getWriter().write(responseStr);
            }
            //不能识别
            else {
                String responseStr = "<xml>";
                responseStr += "<ToUserName><![CDATA[" + fromUserName
                        + "]]></ToUserName>";
                responseStr += "<FromUserName><![CDATA[" + toUserName
                        + "]]></FromUserName>";
                responseStr += "<CreateTime>" + System.currentTimeMillis()
                        + "</CreateTime>";
                responseStr += "<MsgType><![CDATA[text]]></MsgType>";
                responseStr += "<Content>unkonw type</Content>";
                responseStr += "<FuncFlag>0</FuncFlag>";
                responseStr += "</xml>";
                response.getWriter().write(responseStr);
            }
             
        } catch (Exception e) {
            e.printStackTrace();
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
