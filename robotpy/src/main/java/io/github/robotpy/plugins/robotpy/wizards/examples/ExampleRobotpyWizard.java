package io.github.robotpy.plugins.robotpy.wizards.examples;

import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.wizard.IWizardPage;

import edu.wpi.first.wpilib.plugins.core.WPILibCore;
import edu.wpi.first.wpilib.plugins.core.wizards.ExampleWizard;
import edu.wpi.first.wpilib.plugins.core.wizards.IExampleProject;
import edu.wpi.first.wpilib.plugins.core.wizards.INewProjectInfo;
import edu.wpi.first.wpilib.plugins.core.wizards.IExampleProject.ExportFile;
import edu.wpi.first.wpilib.plugins.core.wizards.ProjectCreationUtils;
import io.github.robotpy.plugins.robotpy.WPILibPythonPlugin;
import io.github.robotpy.plugins.robotpy.wizards.RobotpyCreateProjectFromWizard;
import io.github.robotpy.plugins.robotpy.wizards.RobotpyProjectMainPage;
import io.github.robotpy.plugins.robotpy.wizards.newproject.WPIRobotRobotpyProjectCreator;

public class ExampleRobotpyWizard extends ExampleWizard {
	private RobotpyProjectMainPage detailsPage;

	/**
	 * Constructor for SampleNewWizard.
	 */
	public ExampleRobotpyWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	protected void doFinish(IExampleProject ex, String teamNumber) throws CoreException {
		
		RobotpyCreateProjectFromWizard.createProject(detailsPage,
													teamNumber,
													ex,
													getContainer(),
													getShell());
	}

	@Override
	protected IWizardPage getDetailsPage(INewProjectInfo info) {
		if (detailsPage != null) return detailsPage;
		detailsPage = new RobotpyProjectMainPage(selection, getTeamNumberPage(), info);
		detailsPage.setTitle("Create Example Robot Python Project");
		detailsPage.setDescription("This wizard creates a new example project based on your selection.");
		detailsPage.setShowPackage(false);
		return detailsPage;
	}

	@Override
	public IExampleProject makeExampleProject(String name, String description,
			List<String> tags, String world, List<String> folders, List<ExportFile> files) {
		return new ExampleRobotpyProject(name, description, tags, world, folders, files);
	}

	@Override
	public URL getResourceURL() {
		return WPILibPythonPlugin.getDefault().getBundle().getEntry("/resources/templates/examples");
	}

	@Override
	public String getXMLFile() {
		return "examples.xml";
	}
}
