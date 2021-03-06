package com.wyndhamlewis.utilities.pks;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class LocalInfoContributor implements InfoContributor {
 
    @Override
	public void contribute(Builder builder) {
    	try {
			builder.withDetail("hostname", InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			builder.withDetail("hostname", "Failed to get from InetAddress.getLocalHost().getHostName()");
		}
    	
    	
		try(final DatagramSocket socket = new DatagramSocket()){
		  socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
		  builder.withDetail("IP Address", socket.getLocalAddress().getHostAddress());
		} catch (SocketException | UnknownHostException e) {
			builder.withDetail("hostname", "Failed to get from DatagramSocket.getLocalAddress().getHostAddress()");
		}
		
		Map<String, String> javaDetails = new HashMap<String, String>();
		javaDetails.put("alpine-version", getEnvItem("JAVA_ALPINE_VERSION"));
		javaDetails.put("java-version", getEnvItem("JAVA_VERSION"));
		javaDetails.put("java-home", getEnvItem("JAVA_HOME"));
		builder.withDetail("java-info", javaDetails);
		
		builder.withDetail("tomcat-port", getEnvItem("TOMCAT_PORT"));
		
		Map<String, String> appDetails = new HashMap<String, String>();
		appDetails.put("service-port-web", getEnvItem("PKSUPGRADETESTER_SERVICE_PORT_WEB"));
		appDetails.put("service-port", getEnvItem("PKSUPGRADETESTER_SERVICE_PORT"));
		appDetails.put("service-host", getEnvItem("PKSUPGRADETESTER_SERVICE_HOST"));
		appDetails.put("k8s-port", getEnvItem("PKSUPGRADETESTER_PORT"));
		appDetails.put("port-http-tcp", getEnvItem("PKSUPGRADETESTER_PORT_80_TCP"));
		appDetails.put("port-http-proto", getEnvItem("PKSUPGRADETESTER_PORT_80_TCP_PROTO"));
		appDetails.put("port-http-addr", getEnvItem("PKSUPGRADETESTER_PORT_80_TCP_ADDR"));
		appDetails.put("port-http-addr", getEnvItem("PKSUPGRADETESTER_PORT_80_TCP_PORT"));
        builder.withDetail("app-info", appDetails);

		
		Map<String, String> podDetails = new HashMap<String, String>();
		podDetails.put("kube-version", getEnvItem("KUBE_VERSION"));
		podDetails.put("service-host", getEnvItem("KUBERNETES_SERVICE_HOST"));
		podDetails.put("service-port", getEnvItem("KUBERNETES_SERVICE_PORT"));
		podDetails.put("service-port-https", getEnvItem("KUBERNETES_SERVICE_PORT_HTTPS"));
		podDetails.put("k8s-port", getEnvItem("KUBERNETES_PORT"));
		podDetails.put("hostname", getEnvItem("HOSTNAME"));
		podDetails.put("port-443-tcp", getEnvItem("KUBERNETES_PORT_443_TCP"));
		podDetails.put("port-443-proto", getEnvItem("KUBERNETES_PORT_443_TCP_PROTO"));
		podDetails.put("port-443-addr", getEnvItem("KUBERNETES_PORT_443_TCP_ADDR"));
		podDetails.put("port-443-addr", getEnvItem("KUBERNETES_PORT_443_TCP_PORT"));
        builder.withDetail("kube-info", podDetails);
	}
    
    private String getEnvItem(String propertyName) {
    	String val = System.getenv(propertyName);
    	return val == null||val.length()==0?"Not set":val;
    }
}
