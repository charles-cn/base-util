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
 * JAX-WS ���ù��߰�. <br>
 * <p>
 * Copyright: Copyright (c) 2009-6-18 ����04:58:07
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
	 * �����������ɿͻ���ʵ��. <br>
	 * 
	 * <pre>
	 * // Service��
	 * Class&lt;? extends Service&gt; serviceClass = ContentTraceSeedService_Service.class;
	 * // ����ӿ���
	 * Class&lt;?&gt; clientClass = ContentTraceSeedService.class;
	 * // WSDL�ļ���
	 * String wsdlFilename = &quot;./wsdl/ContentTraceSeedService.wsdl&quot;;
	 * // ����˵�ַ
	 * String serverURL = &quot;http://61.155.107.63:9980/seedService&quot;;
	 * // �ͻ���
	 * ContentTraceSeedService client;
	 * // ���ɿͻ���ʵ��
	 * client = (ContentTraceSeedService) JAXWSUtils.getClient(wsdlFilename, serverURL, serviceClass, clientClass);
	 * // �ͻ��˴������
	 * client.regMmsSeed(...);
	 * </pre>
	 * 
	 * @param wsdlLocation
	 *            wsdl�ļ���ַ��<br>
	 *            ����дΪnull����ʹ�÷���˵�wsdl��<br>
	 *            ����д�����ļ�����ͻ��˲�����ʷ���˻�ȡWSDL�ļ���������ٶȡ�<br>
	 *            ����дURL����ͻ���ʹ�ø�URL��ȡ WSDL�ļ���
	 * @param serverURL
	 *            �����URL ��ַ��<br>
	 *            ����дnull����ʹ��WSDL�������ĵ�ַ��
	 * @param serviceClass
	 *            Service��
	 * @param interfaceClass
	 *            ����ӿ���
	 * @return ���ɵĿͻ���
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
