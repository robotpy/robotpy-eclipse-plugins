package io.github.robotpy.plugins.robotpy.wizards.newproject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilib.plugins.core.wizards.ProjectType;
import io.github.robotpy.plugins.robotpy.WPILibPythonPlugin;

public class RobotpyProjectType implements ProjectType {
	static ProjectType SAMPLE = new RobotpyProjectType() {
		@Override public Map<String, String> getFiles(String packageName) {
			Map<String, String> files = super.getFiles(packageName);
			files.put("src/robot.py", "sample/robot.py");
			return files;
		}
	};
	static ProjectType ITERATIVE = new RobotpyProjectType() {
		@Override public Map<String, String> getFiles(String packageName) {
			Map<String, String> files = super.getFiles(packageName);
			files.put("src/robot.py", "iterative/robot.py");
			return files;
		}
	};
	static ProjectType COMMAND_BASED = new RobotpyProjectType() {
		@Override public String[] getFolders(String packageName) {
			String[] paths = {"src",
					"src/commands",
					"src/subsystems",
					"src/triggers",
					"tests"};
			return paths;
		}
		@Override public Map<String, String> getFiles(String packageName) {
			Map<String, String> files = super.getFiles(packageName);
			files.put("src/robot.py", "command-based/robot.py");
			files.put("src/robot_map.py", "command-based/robot_map.py");
			files.put("src/oi.py", "command-based/oi.py");
			files.put("src/commands/example_command.py", "command-based/example_command.py");
			files.put("src/subsystems/example_subsystem.py", "command-based/example_subsystem.py");
			return files;
		}
	};
	@SuppressWarnings("serial")
	static Map<String, ProjectType> TYPES = new HashMap<String, ProjectType>() {{
		put(ProjectType.SAMPLE, SAMPLE);
		put(ProjectType.ITERATIVE, ITERATIVE);
		put(ProjectType.COMMAND_BASED, COMMAND_BASED);
	}};

	@Override
	public String[] getFolders(String packageName) {
		String[] paths = {"src/", "tests/"};
		return paths;
	}

	@Override
	public Map<String, String> getFiles(String packageName) {
		HashMap<String, String> files = new HashMap<String, String>();
		// add the pyfrc tests to everything
		files.put("tests/pyfrc_test.py", "pyfrc_tests/pyfrc_test.py");
		return files;
	}

	@Override
	public URL getBaseURL() {
		return WPILibPythonPlugin.getDefault().getBundle().getEntry("/resources/templates/");
	}
}
