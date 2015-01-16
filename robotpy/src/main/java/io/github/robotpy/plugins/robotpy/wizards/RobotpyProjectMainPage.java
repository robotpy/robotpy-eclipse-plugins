package io.github.robotpy.plugins.robotpy.wizards;

import io.github.robotpy.plugins.robotpy.wizards.copied.NewProjectMainPage;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.python.pydev.core.IPythonNature;
import org.python.pydev.ui.PyProjectPythonDetails;
import org.python.pydev.utils.ICallback;

import edu.wpi.first.wpilib.plugins.core.wizards.INewProjectInfo;
import edu.wpi.first.wpilib.plugins.core.wizards.TeamNumberPage;

public class RobotpyProjectMainPage extends NewProjectMainPage {

	protected PyProjectPythonDetails.ProjectInterpreterAndGrammarConfig details;
	
	public RobotpyProjectMainPage(ISelection selection,
								  TeamNumberPage teamNumberPage,
								  INewProjectInfo info) {
		super(selection, teamNumberPage, info);
	}
	
	@Override
	public void createControl(Composite parent)
	{
		super.createControl(parent);
		
		
		createProjectDetails((Composite)getControl());
		
		// TODO: hack at the project details to force the grammar to 3.0 (or remove it?)
		//       .. really only care about the interpreter selection, as that's useful
	}
	
	/**
	 * Taken verbatim from pydev
	 * 
     * @param composite
     */
    protected void createProjectDetails(Composite parent) {
        Font font = parent.getFont();
        Composite projectDetails = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        projectDetails.setLayout(layout);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
        projectDetails.setLayoutData(gd);
        projectDetails.setFont(font);

        Label projectTypeLabel = new Label(projectDetails, SWT.NONE);
        projectTypeLabel.setFont(font);
        projectTypeLabel.setText("Python Project Configuration");
        //let him choose the type of the project
        details = new PyProjectPythonDetails.ProjectInterpreterAndGrammarConfig(new ICallback() {

            //Whenever the configuration changes there, we must evaluate whether the page is complete
            public Object call(Object args) throws Exception {
            	RobotpyProjectMainPage.this.dialogChanged();
                return null;
            }
        });

        Control createdOn = details.doCreateContents(projectDetails);
        details.setDefaultSelection();
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.grabExcessHorizontalSpace = true;
        createdOn.setLayoutData(data);
    }
	
    
    @Override
    protected boolean dialogChanged() {
    	
    	if (!super.dialogChanged())
    		return false;
    	
    	if (details != null && details.getProjectInterpreter() == null) {
    		updateStatus("Project interpreter not specified");
    		return false;
    	}
    	
    	updateStatus(null);
    	return true;
    }

	public String getPyProjectType() {
		return details.getSelectedPythonOrJythonAndGrammarVersion();
	}

	public String getProjectInterpreter() {
		String projectInterpreter = details.getProjectInterpreter();
		if (projectInterpreter == null || projectInterpreter.trim().isEmpty()) {
            projectInterpreter = IPythonNature.DEFAULT_INTERPRETER;
        }
		return projectInterpreter;
	}
}
