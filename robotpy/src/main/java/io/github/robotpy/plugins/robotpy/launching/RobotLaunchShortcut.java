package io.github.robotpy.plugins.robotpy.launching;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.python.pydev.debug.core.Constants;
import org.python.pydev.debug.ui.launching.FileOrResource;
import org.python.pydev.debug.ui.launching.LaunchShortcut;
import org.python.pydev.editor.PyEdit;
import org.python.pydev.shared_core.io.FileUtils;

import edu.wpi.first.wpilib.plugins.core.WPILibCore;
import io.github.robotpy.plugins.robotpy.WPILibPythonPlugin;

public abstract class RobotLaunchShortcut implements ILaunchShortcut {
	//private static final int DEBUG_ATTACH_ATTEMPTS = 20;
	//private static final int DEBUG_ATTACH_RETRY_DELAY_SEC = 2;
	
	//Class constants - used to delineate types for launch shortcuts
	public static final String DEPLOY_TYPE = "edu.wpi.first.wpilib.plugins.core.deploy";
	protected LaunchShortcut pydevLauncher = new LaunchShortcut();
	
	/**
	 * Returns the launch type of the shortcut that was used, one of the constants
	 * defined in BaseLaunchShortcut
	 * @return Launch shortcut type
	 */
	public String getLaunchType() {return DEPLOY_TYPE;}

	// mostly from pydev
	public void launch(ISelection selection, String mode) {
		// Extract resource from selection
		StructuredSelection structuredSelection = (StructuredSelection)selection;
		
		if (structuredSelection.size() == 1) {
            Object object = structuredSelection.getFirstElement();
            if (object instanceof IAdaptable) {
            	
            	// selection is a file?
            	IResource resource = (IFile) ((IAdaptable) object).getAdapter(IFile.class);
                if (resource != null) {
                    launch(new FileOrResource(resource), mode);
                    return;
                }
                
                // ok, must be a project then
                IProject project = (IProject) ((IAdaptable) object).getAdapter(IProject.class);
                if (project != null) {
                	
                	// Ok. Identify robot.py. By convention, it's either at the root, or
                	// its at src/robot.py.
                	
                	IFile robotPyFile = project.getFile("src/robot.py");
                	if (!robotPyFile.exists()) {
                		robotPyFile = project.getFile("robot.py");
                		if (!robotPyFile.exists()) {
                			RobotLaunchShortcut.reportError("Could not find robot.py at 'src/robot.py' or 'robot.py'!", null);
                		}
                	}
                	
                	// TODO: Don't allow deploy icon to show up if it doesn't work!
                	launch(new FileOrResource(robotPyFile), mode);
                	return;
                }
            }
		}
		
		
	}

	// from pydev
	@Override
	public void launch(IEditorPart editor, String mode) {
		//we have an editor to run
        IEditorInput input = editor.getEditorInput();
        IFile file = (IFile) input.getAdapter(IFile.class);
        if (file != null) {
            launch(new FileOrResource(file), mode);
            return;
        }

        if (editor instanceof PyEdit) {
            PyEdit pyEdit = (PyEdit) editor;
            File editorFile = pyEdit.getEditorFile();
            if (editorFile != null) {
                launch(new FileOrResource(editorFile), mode);
                return;
            }
        }
        
		pydevLauncher.fileNotFound();
	}
	
	// from pydev
	private String getAbsPath(File f)
	{
		try {
			return f.getCanonicalPath(); 
		} catch (IOException e) {
			return f.getAbsolutePath();
		}
	}
	
	/**
	 * Runs the ant script using the correct target for the indicated mode (deploy to cRIO or just compile)
	 * @param activeProj The project that the script will be run on/from
	 * @param mode The mode it will be run in (ILaunchManager.RUN_MODE or ILaunchManager.DEBUG_MODE)
	 */
	protected void launch(FileOrResource resource, String mode){
	
		ILaunchConfigurationWorkingCopy workingCopy;
		
		try {
			ILaunchConfiguration conf = pydevLauncher.createDefaultLaunchConfigurationWithoutSaving(new FileOrResource[] { resource });
			
			workingCopy = conf.getWorkingCopy();
			
			// We have to pass the right args to robot.py AND we need to run our preexec
			// python script to make sure the environment is ok, so we can warn the user
			// -> pydev uses sitecustomize.py, but this seems easier.. 
			
			String args = "\"" + workingCopy.getAttribute(Constants.ATTR_LOCATION, "") + "\" " + getArgs();
			workingCopy.setAttribute(Constants.ATTR_PROGRAM_ARGUMENTS, args);
			
			// set to our preexec
			URL url = new URL(WPILibPythonPlugin.getDefault().getBundle().getEntry("/resources/py/"), "preexec.py");
			File file = URIUtil.toFile(FileLocator.toFileURL(url).toURI());
			
			workingCopy.setAttribute(Constants.ATTR_LOCATION, getAbsPath(file));
			
		} catch (CoreException|IOException|URISyntaxException e) {
			RobotLaunchShortcut.reportError(null, e);
			return;
		}
		
		DebugUITools.launch(workingCopy, mode);
	}
	
	protected String getHostname(IProject proj) {
		return WPILibCore.getDefault().getTargetIP(proj);
	}
	
	// from pydev
	protected static void reportError(String message, Throwable throwable)
	{
		if (message == null) {
            message = "Unexpected error";
        }

        IStatus status = null;
        if (throwable instanceof CoreException) {
            status = ((CoreException) throwable).getStatus();
        } else {
            status = new Status(IStatus.ERROR, "io.github.robotpy.plugins.robotpy", 0, message, throwable);
        }
        ErrorDialog.openError(WPILibPythonPlugin.getActiveWorkbenchWindow().getShell(), "RobotPy launch failure",
                "Python launch failed", status);
	}
	
	/**
	 * Returns the arguments to pass to robot.py
	 */
	public abstract String getArgs();
	
}
