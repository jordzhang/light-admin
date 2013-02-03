package org.lightadmin.core.config;

import org.lightadmin.core.config.management.jmx.LightAdminConfigurationMonitoringServiceMBean;
import org.lightadmin.core.config.management.rmi.DataManipulationService;
import org.lightadmin.core.config.management.rmi.GlobalConfigurationManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.util.ClassUtils;

import java.rmi.RemoteException;

@Configuration
public class LightAdminRemoteConfiguration {

	@Autowired
	private GlobalConfigurationManagementService globalConfigurationManagementService;

	@Autowired
	private DataManipulationService dataManipulationService;

	@Bean
	public RmiServiceExporter globalConfigurationManagementServiceRmiExporter() throws RemoteException {
		return createRmiServiceExporter( globalConfigurationManagementService, "GlobalConfigurationManagementService", 1199 );
	}

	@Bean
	public RmiServiceExporter dataManipulationServiceRmiExporter() throws RemoteException {
		return createRmiServiceExporter( dataManipulationService, "DataManipulationService", 1199 );
	}

	@Bean
	public AnnotationMBeanExporter annotationMBeanExporter() {
		return new AnnotationMBeanExporter();
	}

	@Bean
	public LightAdminConfigurationMonitoringServiceMBean lightAdminConfigurationMonitoringServiceMBean() {
		return new LightAdminConfigurationMonitoringServiceMBean( globalConfigurationManagementService );
	}

	private RmiServiceExporter createRmiServiceExporter( final Object service, final String serviceName, final int registryPort ) throws RemoteException {
		RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
		rmiServiceExporter.setServiceName( serviceName );
		rmiServiceExporter.setService( service );
		rmiServiceExporter.setServiceInterface( ClassUtils.getAllInterfaces( service )[0] );
		rmiServiceExporter.setRegistryPort( registryPort );
		rmiServiceExporter.afterPropertiesSet();
		return rmiServiceExporter;
	}
}