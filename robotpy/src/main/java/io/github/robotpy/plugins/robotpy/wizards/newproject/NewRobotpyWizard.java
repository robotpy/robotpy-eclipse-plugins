package io.github.robotpy.plugins.robotpy.wizards.newproject;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

import edu.wpi.first.wpilib.plugins.core.wizards.INewProjectInfo;
import edu.wpi.first.wpilib.plugins.core.wizards.TeamNumberPage;
import io.github.robotpy.plugins.robotpy.wizards.RobotpyCreateProjectFromWizard;
import io.github.robotpy.plugins.robotpy.wizards.RobotpyProjectMainPage;

/**
 * 
 * Example Docs:
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "mpe". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class NewRobotpyWizard extends Wizard implements INewWizard {
	private TeamNumberPage teamNumberPage;
	private RobotpyProjectMainPage page;
	private ISelection selection;

	/**
	 * Constructor for SampleNewWizard.
	 */
	public NewRobotpyWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new RobotpyProjectMainPage(selection, teamNumberPage, INewProjectInfo.Null);
		page.setProjectTypes(RobotpyProjectType.TYPES);
		page.setShowPackage(false);
		page.setTitle("Create New Robot Python Project");
		page.setDescription("This wizard creates a new Robot Python Project configured to use WPILib for programming FRC robots.");
		page.setShowPackage(false);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		
		return RobotpyCreateProjectFromWizard.createProject(
			page,
			TeamNumberPage.getTeamNumberFromPage(teamNumberPage),
			page.getProjectType(),
			getContainer(),
			getShell()
		);
		
		// TODO: stuff from pydev: Switch to default perspective (will ask before changing)
        //BasicNewProjectResourceWizard.updatePerspective(fConfigElement);
        //BasicNewResourceWizard.selectAndReveal(createdProject, workbench.getActiveWorkbenchWindow());
	}

	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}
