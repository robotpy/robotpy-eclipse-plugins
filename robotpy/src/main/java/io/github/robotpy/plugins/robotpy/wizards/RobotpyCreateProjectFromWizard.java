package io.github.robotpy.plugins.robotpy.wizards;

import io.github.robotpy.plugins.robotpy.WPILibPythonPlugin;
import io.github.robotpy.plugins.robotpy.wizards.copied.ProjectCreationUtils;
import io.github.robotpy.plugins.robotpy.wizards.newproject.WPIRobotRobotpyProjectCreator;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import edu.wpi.first.wpilib.plugins.core.WPILibCore;
import edu.wpi.first.wpilib.plugins.core.wizards.ProjectType;

public class RobotpyCreateProjectFromWizard {

	static public boolean createProject(RobotpyProjectMainPage page,
							     		final String teamNumber,
							     		final ProjectType projectType,
							     		IWizardContainer container,
							     		Shell shell) {
		
		final String projectName = page.getProjectName();
		final String worldName = page.getWorld();
		WPILibPythonPlugin.logInfo("Project: "+projectName+" Project Type: "+projectType);
		
		final String pyProjectType = page.getPyProjectType();
        final String projectInterpreter = page.getProjectInterpreter();
		
		// define the operation to create a new project
        WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
            @Override
            protected void execute(IProgressMonitor monitor) throws CoreException {
            	doFinish(projectName, teamNumber, projectType, worldName,
            			pyProjectType, projectInterpreter, monitor);
            }
        };
		
		try {
			container.run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(shell, "Error", realException.getMessage());
			return false;
		}
		
		return true;
	}
	
	/**
	 * The worker method. It will find the container, create the
	 * file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 */

	private static void doFinish(String projectName, String teamNumber, ProjectType projectType, String worldName,
			String pyProjectType, String projectInterpreter, IProgressMonitor monitor) throws CoreException {
    	Properties props = WPILibCore.getDefault().getProjectProperties(null);
    	props.setProperty("team-number", teamNumber);
    	WPILibCore.getDefault().saveGlobalProperties(props);
		createProject(new WPIRobotRobotpyProjectCreator(projectName, projectType, worldName,
				pyProjectType, projectInterpreter, monitor));
	}
	
	// copied from ProjectCreationUtils.createProject
	public static IProject createProject(WPIRobotRobotpyProjectCreator creator) throws CoreException {
		//IProject project = createBaseProject(creator.getName(), null);
		
		IProject project = creator.initializeBase();
		
		try {
			creator.initialize(null);
			for (String nature : creator.getNatures()) {
				ProjectCreationUtils.addNature(project, nature);	
			}
			ProjectCreationUtils.addToProjectStructure(project, creator);
			ProjectCreationUtils.addFilesToProject(project, creator);
			creator.finalize(project);
		} catch (CoreException e) {
            WPILibCore.logError("Error creating project "+creator.getName(), e);
            project = null;
        }
		
        return project;
	}
	
	
	
}
