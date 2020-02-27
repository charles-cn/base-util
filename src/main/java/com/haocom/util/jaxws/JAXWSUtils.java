package com.haocom.util.jaxws;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceClient;

import com.sun.xml.internal.ws.developer.WSBindingProvider;

/**
 * JAX-WS 常用工具包. <br>
 * <p>
 * Copyright: Copyright (c) 2009-6-18 下午04:58:07
 * <p>
 * Company: 
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 */
public class JAXWSUtils {

	/**
	 * 依据设置生成客户端实例. <br>
	 * 
	 * <pre>
	 * // Service类
	 * Class&lt;? extends Service&gt; serviceClass = ContentTraceSeedService_Service.class;
	 * // 服务接口类
	 * Class&lt;?&gt; clientClass = ContentTraceSeedService.class;
	 * // WSDL文件名
	 * String wsdlFilename = &quot;./wsdl/ContentTraceSeedService.wsdl&quot;;
	 * // 服务端地址
	 * String serverURL = &quot;http://61.155.107.63:9980/seedService&quot;;
	 * // 客户端
	 * ContentTraceSeedService client;
	 * // 生成客户端实例
	 * client = (ContentTraceSeedService) JAXWSUtils.getClient(wsdlFilename, serverURL, serviceClass, clientClass);
	 * // 客户端代码调用
	 * client.regMmsSeed(...);
	 * </pre>
	 * 
	 * @param wsdlLocation
	 *            wsdl文件地址。<br>
	 *            如填写为null，则使用服务端的wsdl。<br>
	 *            如填写本地文件，则客户端不会访问服务端获取WSDL文件，可提高速度。<br>
	 *            如填写URL，则客户端使用该URL获取 WSDL文件。
	 * @param serverURL
	 *            服务端URL 地址。<br>
	 *            如填写null，则使用WSDL中声明的地址。
	 * @param serviceClass
	 *            Service类
	 * @param interfaceClass
	 *            服务接口类
	 * @return 生成的客户端
	 * @throws Exception
	 */
	public static Object getClient(String wsdlLocation, String serverURL, Class<? extends Service> serviceClass, Class<?> interfaceClass)
	        throws Exception {
		Service service;
		if (wsdlLocation == null && serverURL != null) {
			wsdlLocation = serverURL + "?wsdl";
		}
		if (wsdlLocation != null) {
			WebServiceClient serviceClient = serviceClass.getAnnotation(WebServiceClient.class);
			String targetNamespace = serviceClient.targetNamespace();
			String name = serviceClient.name();
			URL wsdlURL = null;
			if (new File(wsdlLocation).exists()) {
				wsdlURL = new File(wsdlLocation).toURI().toURL();
			} else {
				wsdlURL = new URL(wsdlLocation);
			}
			Constructor<? extends Service> constructor = serviceClass.getConstructor(URL.class, QName.class);
			service = constructor.newInstance(wsdlURL, new QName(targetNamespace, name));
		} else {
			Constructor<? extends Service> constructor = serviceClass.getConstructor();
			service = constructor.newInstance();
		}
		Object client = service.getPort(interfaceClass);
		if (serverURL != null) {
			WSBindingProvider provider = (WSBindingProvider) client;
			provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serverURL);
		}
		return client;
	}
}
