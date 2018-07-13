package com.ur.urcap.sample.getRobotData.impl;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.InstallationNodeService;
import com.ur.urcap.api.domain.URCapAPI;

import java.io.InputStream;

import com.ur.urcap.api.domain.data.DataModel;

public class SomeInstallationService implements InstallationNodeService {

	public SomeInstallationService() { }

	@Override
	public InstallationNodeContribution createInstallationNode(URCapAPI api, DataModel model) {
		return new SomeInstallationContribution(api, model);
	}

	@Override
	public String getTitle() {
		return "Realtime Message";
	}

	@Override
	public InputStream getHTML() {
		InputStream is = this.getClass().getResourceAsStream("/com/ur/urcap/sample/getRobotData/impl/installation.html");
		return is;
	}
}
