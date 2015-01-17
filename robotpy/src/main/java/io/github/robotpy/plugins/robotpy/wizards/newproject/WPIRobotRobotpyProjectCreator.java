package io.github.robotpy.plugins.robotpy.wizards.newproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.python.pydev.plugin.PyStructureConfigHelpers;
import org.python.pydev.shared_core.callbacks.ICallback;

import edu.wpi.first.wpilib.plugins.core.nature.FRCProjectNature;
import edu.wpi.first.wpilib.plugins.core.wizards.IProjectCreator;
import edu.wpi.first.wpilib.plugins.core.wizards.ProjectType;
import io.github.robotpy.plugins.robotpy.WPILibPythonPlugin;

public class WPIRobotRobotpyProjectCreator implements IProjectCreator {
	String projectName, worldName;
	ProjectType projectType;
	String pyProjectType, projectInterpreter;
	IProgressMonitor monitor;
	
	public WPIRobotRobotpyProjectCreator(String projectName, ProjectType projectType, String worldName,
			String pyProjectType, String projectInterpreter, IProgressMonitor monitor) {
		this.projectName = projectName;
		this.projectType = projectType;
		this.worldName = worldName;
		this.pyProjectType = pyProjectType;
		this.projectInterpreter = projectInterpreter;
		this.monitor = monitor;
	}

	@Override
	public String getName() {
		return projectName;
	}

	@Override
	public String getPackageName() {
		return "";
	}

	@Override
	public Map<String, String> getValues() {
		Map<String, String> vals = new HashMap<String, String>();
		vals.put("$project", projectName);
		vals.put("$world", worldName);
		return vals;
	}

	@Override
	public List<String> getNatures() {
		List<String> natures = new ArrayList<>();
		natures.add(FRCProjectNature.FRC_PROJECT_NATURE);
		return natures;
	}

	@Override
	public ProjectType getProjectType() {
		return projectType;
	}

	@Override
	public void initialize(final IProject project) throws CoreException {
		// not used
	}
	
	public IProject initializeBase() throws CoreException
	{
		//IProject project = null;;
		return PyStructureConfigHelpers.createPydevProject(
				projectName, null, null,
				monitor, pyProjectType, projectInterpreter,
				getSourceFolderHandlesCallback, null, null,
				null);
	}

	@Override
	public void finalize(IProject project) throws CoreException {
		WPILibPythonPlugin.getDefault().updateVariables(project);
	}
	
	//
	// Pydev stuffs
	//
	
	protected ICallback<List<IContainer>, IProject> getSourceFolderHandlesCallback = new ICallback<List<IContainer>, IProject>() {
        public List<IContainer> call(IProject projectHandle) {
            List<IContainer> ret = new ArrayList<IContainer>();
            
            // default to 'src' is fine
            IContainer folder = projectHandle.getFolder("src");
            ret = new ArrayList<IContainer>();
            ret.add(folder);
            return ret;
        }
    };
}
