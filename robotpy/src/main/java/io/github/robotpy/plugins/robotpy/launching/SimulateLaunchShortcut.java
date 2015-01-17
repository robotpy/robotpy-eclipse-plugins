package io.github.robotpy.plugins.robotpy.launching;

import org.eclipse.core.resources.IProject;

public class SimulateLaunchShortcut extends RobotLaunchShortcut {

	@Override
	public String getArgs() {
		return "";
	}
	
	protected String getHostname(IProject proj) {
		return "localhost";
	}
}
