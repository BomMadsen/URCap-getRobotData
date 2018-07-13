package com.ur.urcap.sample.getRobotData.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.ur.urcap.api.contribution.InstallationNodeService;
import com.ur.urcap.sample.getRobotData.impl.SomeInstallationService;

/**
 * Hello world activator for the OSGi bundle URCAPS contribution
 *
 */
public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		SomeInstallationService someInstallationService = new SomeInstallationService();
		
		System.out.println("Activator says Hello World!");
		bundleContext.registerService(InstallationNodeService.class, someInstallationService, null);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println("Activator says Goodbye World!");
	}
}

