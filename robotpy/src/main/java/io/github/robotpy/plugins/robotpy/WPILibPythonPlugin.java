package io.github.robotpy.plugins.robotpy;

import java.io.File;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import edu.wpi.first.wpilib.plugins.core.WPILibCore;
import edu.wpi.first.wpilib.plugins.core.ant.AntPropertiesParser;

/**
 * The activator class controls the plug-in life cycle
 */
public class WPILibPythonPlugin extends AbstractUIPlugin implements IStartup {

	// The plug-in ID
	public static final String PLUGIN_ID = "io.github.robotpy.plugins.robotpy"; //$NON-NLS-1$

	// The shared instance
	private static WPILibPythonPlugin plugin;
	
	/**
	 * The constructor
	 */
	public WPILibPythonPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static WPILibPythonPlugin getDefault() {
		return plugin;
	}

	public String getCurrentVersion() {
		try {
			Properties props = new AntPropertiesParser(WPILibPythonPlugin.class.getResourceAsStream("/resources/configuration.properties")).getProperties();
			if (props.getProperty("version").startsWith("$")) {
				return "DEVELOPMENT";
			} else {
				return props.getProperty("version");
			}
		} catch (CoreException e) {
            WPILibPythonPlugin.logError("Error getting properties.", e);
			return "DEVELOPMENT";
		}
	}
	public String getSimPath() {
		return WPILibCore.getDefault().getWPILibBaseDir()
				+ File.separator + "sim" + File.separator + "current";
	}
	
	public void updateProjects() {
		// Get the root of the workspace
	    IWorkspace workspace = ResourcesPlugin.getWorkspace();
	    IWorkspaceRoot root = workspace.getRoot();
	    // Get all projects in the workspace
	    IProject[] projects = root.getProjects();
	    // Loop over all projects
	    for (IProject project : projects) {
			  try {
				  if(project.hasNature("edu.wpi.first.wpilib.plugins.core.nature.FRCProjectNature")){
					WPILibPythonPlugin.logInfo("Updating project");
					updateVariables(project);
				  } else {
				  }
			  } catch (CoreException e) {
				WPILibPythonPlugin.logError("Error updating projects.", e);
			  }
	    }
	}
	
	public void updateVariables(IProject project) throws CoreException {
		// right now we don't need to do anything like this in RobotPy.. but you never know
		/*Properties props = WPILibPythonPlugin.getDefault().getProjectProperties(project);

		try {
			JavaCore.setClasspathVariable("wpilib", new Path(props.getProperty("wpilib.jar")), null);
			JavaCore.setClasspathVariable("wpilib.sources", new Path(props.getProperty("wpilib.sources")), null);
			JavaCore.setClasspathVariable("networktables", new Path(props.getProperty("networktables.jar")), null);
			JavaCore.setClasspathVariable("networktables.sources", new Path(props.getProperty("networktables.sources")), null);
		} catch (JavaModelException e) {
            // Classpath variables didn't get set
            WPILibPythonPlugin.logError("Error setting classpath..", e);
		}
		*/
	}

	@Override
	public void earlyStartup() {
		// causes errors for now
		//new SimInstaller(getCurrentVersion()).installIfNecessary();
	}

	public static void logInfo(String msg) {
		getDefault().getLog().log(new Status(Status.INFO, PLUGIN_ID, Status.OK, msg, null));
	}

	public static void logError(String msg, Exception e) {
		getDefault().getLog().log(new Status(Status.ERROR, PLUGIN_ID, Status.OK, msg, e));
	}
	
	/**
     * Returns the active workbench window or <code>null</code> if none
     */
    public static IWorkbenchWindow getActiveWorkbenchWindow() {
        return getDefault().getWorkbench().getActiveWorkbenchWindow();
    }
}
