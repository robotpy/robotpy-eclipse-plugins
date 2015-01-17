package io.github.robotpy.plugins.robotpy.launching;

public class DeployNoTestsLaunchShortcut extends RobotLaunchShortcut {
	
	@Override
	public String getArgs() {
		return "deploy --skip-tests";
	}
}
