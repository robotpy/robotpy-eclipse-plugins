package io.github.robotpy.plugins.robotpy.wizards.newproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.python.pydev.plugin.nature.PythonNature;

import edu.wpi.first.wpilib.plugins.core.nature.FRCProjectNature;
import edu.wpi.first.wpilib.plugins.core.wizards.IProjectCreator;
import edu.wpi.first.wpilib.plugins.core.wizards.ProjectType;
import io.github.robotpy.plugins.robotpy.WPILibPythonPlugin;

public class WPIRobotRobotpyProjectCreator implements IProjectCreator {
	String projectName, packageName, worldName;
	ProjectType projectType;
	
	public WPIRobotRobotpyProjectCreator(String projectName, String packageName, ProjectType projectType, String worldName) {
		this.projectName = projectName;
		this.packageName = packageName;
		this.projectType = projectType;
		this.worldName = worldName;
	}

	@Override
	public String getName() {
		return projectName;
	}

	@Override
	public String getPackageName() {
		return packageName;
	}

	@Override
	public Map<String, String> getValues() {
		Map<String, String> vals = new HashMap<String, String>();
		vals.put("$project", projectName);
		vals.put("$package", packageName);
		vals.put("$world", worldName);
		return vals;
	}

	@Override
	public List<String> getNatures() {
		List<String> natures = new ArrayList<>();
		natures.add(PythonNature.PYTHON_NATURE_ID);
		natures.add(FRCProjectNature.FRC_PROJECT_NATURE);
		return natures;
	}

	@Override
	public ProjectType getProjectType() {
		return projectType;
	}

	@Override
	public void initialize(IProject project) {
		JavaCore.create(project);
	}

	@Override
	public void finalize(IProject project) throws CoreException {
		WPILibPythonPlugin.getDefault().updateVariables(project);
	}
}
