package com.thermometer.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.thermometer.db.ThermometerDB;
import com.thermometer.db.model.BindingInfo;
import com.thermometer.db.model.Device;
import com.thermometer.db.model.Temperature;
import com.thermometer.message.ToDeviceMsg;
import com.thermometer.utility.StringUtil;

public class WXServlet extends HttpServlet {

	private final int DEVICE_MSG_COMMAND_TEMPERATURE = 0;
	private final int DEVICE_MSG_COMMAND_RESULT = 1;
	public static final String ACCESS_TOKEN = 
			"llntgPT5f55kxl1RAUMDXtVvxDiatg98vbPxsFnNe6IDqrsVTJSMauk65H-FFnohK8XcusPrYd5EACemHUASWw";
	private HashMap<String, Integer> shouldDisplayOnWeChatMap = new HashMap<String, Integer>();
	/**
	 * Constructor of the object.
	 */
	public WXServlet() {
		super();
		// ��ȡ���ݿ⣬���shouldDisplayOnWeChatMap
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
            // ȡHTTP����������
            int size = request.getContentLength();
            // ���ڻ���ÿ�ζ�ȡ������
            byte[] buffer = new byte[size];
            // ���ڴ�Ž��������
            byte[] xmldataByte = new byte[size];
            int count = 0;
            int rbyte = 0;
            // ѭ����ȡ
            while (count < size) { 
                // ÿ��ʵ�ʶ�ȡ���ȴ���rbyte��
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
            //�õ����е���������
            System.out.println(content+ ":" + toUserName + ":" + fromUserName +
            		" MsgType is " + MsgType);
            if (!StringUtil.isBlank(content) && "device_event".equals(MsgType)) {
            	String event = rootElt.elementText("Event");
            	if ("bind".equals(event)) {
            		// �������Ϣ
            		String deviceID = rootElt.elementText("DeviceID");
            		String openID = rootElt.elementText("OpenID");
            		BindingInfo bindingInfo = new BindingInfo();
            		bindingInfo.setDeviceID(deviceID);
            		bindingInfo.setOpenID(openID);
            		bindingInfo.setDisplayOnWeChat("1");
            		ThermometerDB.insertBindingInfo(bindingInfo);
            		
            		
            	}else if ("unbind".equals(event)) {
            		// ɾ������Ϣ
            		String deviceID = rootElt.elementText("DeviceID");
            		String openID = rootElt.elementText("OpenID");
            		BindingInfo bindingInfo = new BindingInfo();
            		bindingInfo.setDeviceID(deviceID);
            		bindingInfo.setOpenID(openID);
            		ThermometerDB.deleteBindingInfo(bindingInfo);
            	}

            } else if (!StringUtil.isBlank(content) && "device_text".equals(MsgType)) {
            	String openID = rootElt.elementText("OpenID");
            	String deviceID = rootElt.elementText("DeviceID");
            	String deviceType = rootElt.elementText("DeviceType");
            	byte []contentBytes = StringUtil.strToByteArray(content);
            	switch (contentBytes[2]) {
            	case DEVICE_MSG_COMMAND_TEMPERATURE:
            		int temp = (contentBytes[4] & 0xff) | ((contentBytes[5] & 0xff) << 8);
            		//float temperature = temp / 10;
            		// �洢�����ݿ��У�����ʾ��WeChat��
            		Temperature temperature = new Temperature();
            		temperature.setOpenID(openID);
            		temperature.setDeviceID(deviceID);
            		Calendar c = Calendar.getInstance();
            		StringBuilder sb = new StringBuilder();
            		sb.append(c.get(Calendar.YEAR));
            		sb.append(c.get(Calendar.MONTH));
            		sb.append(c.get(Calendar.DAY_OF_MONTH));
            		sb.append(c.get(Calendar.HOUR_OF_DAY));
            		sb.append(c.get(Calendar.MINUTE));
            		sb.append(c.get(Calendar.SECOND));
            		temperature.setTime(sb.toString());
            		temperature.setTemperature(temp);
            		
            		ThermometerDB.insertTemperature(temperature);
            		
            		BindingInfo bindingInfo = new BindingInfo();
            		bindingInfo.setDeviceID(deviceID);
            		bindingInfo.setOpenID(openID);
            		BindingInfo bindingInfoDB = ThermometerDB.findBindingInfo(bindingInfo);
            		if (bindingInfoDB.getDisplayOnWeChat().equals("1")) {
	            		ToDeviceMsg msg = new ToDeviceMsg();
	            		msg.setContent(temp / 10.0 + "");
	            		msg.setDeviceId(deviceID);
	            		msg.setDeviceType(deviceType);
	            		msg.setOpenId(openID);
	            		msg.sendMessage();
            		}
            		break;
            	case DEVICE_MSG_COMMAND_RESULT:
            		int result = contentBytes[4] & 0xff;
            		if (result == 0x00) {
            			// �ɹ�,����,֪ͨ��ҳ
            			Device device = new Device();
            			device.setDeviceID(deviceID);
            			ThermometerDB.updateDevice(device);
            		}
            		break;
            	}
            } else if ("event".equals(MsgType)) {
            	// �Զ���˵�
            	String event = rootElt.elementText("Event");
            	String eventKey = rootElt.elementText("EventKey");
            	if ("CLICK".equals(event)) {
            		if ("SETTING_SHOULD_SHOW_ON_WECHAT".equals(eventKey)) {
            			// fromUserName����openID���ı�shouldDisplayOnWeChat�ж�ӦopenID��ֵ����д�����ݿ���
            			BindingInfo bindingInfo = new BindingInfo();
            			bindingInfo.setOpenID(fromUserName);
            			boolean success = ThermometerDB.updateBindingInfoWithOpenID(bindingInfo);
            			String responseContent = "";
            			if (success) {
            				responseContent = "Succeed.";
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
            	}/* else if ("VIEW".equals(event)) {
            		response.sendRedirect(eventKey);
            	}*/
            }
            //�ı���Ϣ
            else if (!StringUtil.isBlank(content) && "text".equals(/*content*/MsgType)) {
                String responseStr = "<xml>";
                responseStr += "<ToUserName><![CDATA[" + fromUserName
                        + "]]></ToUserName>";
                responseStr += "<FromUserName><![CDATA[" + toUserName
                        + "]]></FromUserName>";
                responseStr += "<CreateTime>" + System.currentTimeMillis()
                        + "</CreateTime>";
                responseStr += "<MsgType><![CDATA[text]]></MsgType>";
                responseStr += "<Content>text type</Content>";
                responseStr += "<FuncFlag>0</FuncFlag>";
                responseStr += "</xml>";
                response.getWriter().write(responseStr);
            }
            //ͼ����Ϣ
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
                responseStr += "<Title><![CDATA[ͼ����Ϣ������ɫʯͷ]]></Title>";
                responseStr += "<Discription><![CDATA[ͼ����Ϣ���ġ�����ɫʯͷ]]></Discription>";
                responseStr += "<PicUrl><![CDATA[http://redstones.sinaapp.com/res/images/redstones_wx_258.jpg]]></PicUrl>";
                responseStr += "<Url><![CDATA[http://redstones.sinaapp.com/]]></Url>";
                responseStr += "</item>";
 
                responseStr += "<item>";
                responseStr += "<Title><![CDATA[ͼ����Ϣ������ɫʯͷ]]></Title>";
                responseStr += "<Discription><![CDATA[ͼ����Ϣ���ġ�����ɫʯͷ]]></Discription>";
                responseStr += "<PicUrl><![CDATA[http://redstones.sinaapp.com/res/images/redstones_wx_258.jpg]]></PicUrl>";
                responseStr += "<Url><![CDATA[http://redstones.sinaapp.com/]]></Url>";
                responseStr += "</item>";
 
                responseStr += "</Articles>";
                responseStr += "<FuncFlag>1</FuncFlag>";
                responseStr += "</xml>";
                response.getWriter().write(responseStr);
            }
            //����ʶ��
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
