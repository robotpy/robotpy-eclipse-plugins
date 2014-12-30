package io.github.robotpy.plugins.robotpy.installer;

import java.io.InputStream;
import org.eclipse.jface.preference.IPreferenceStore;

import edu.wpi.first.wpilib.plugins.core.installer.AbstractInstaller;
import io.github.robotpy.plugins.robotpy.WPILibPythonPlugin;
import io.github.robotpy.plugins.robotpy.preferences.PreferenceConstants;

/**
 * Installs the given version of WPILib Simulation Tools into the correct location. Where the
 * install directory is usually ~/wpilib/sim/version.
 * 
 * @author alex
 */
public class SimInstaller extends AbstractInstaller {

	public SimInstaller(String version) {
		super(version, 
				WPILibPythonPlugin.getDefault().getPreferenceStore().getString(PreferenceConstants.LIBRARY_INSTALLED),
				WPILibPythonPlugin.getDefault().getSimPath());
	}

	@Override
	protected String getFeatureName() {
		return "sim";
	}

	@Override
	protected void updateInstalledVersion(String version) {
		IPreferenceStore prefs = WPILibPythonPlugin.getDefault().getPreferenceStore();
			prefs.setValue(PreferenceConstants.LIBRARY_INSTALLED, version);
	}

	@Override
	protected InputStream getInstallResourceStream() {
		return SimInstaller.class.getResourceAsStream("/resources/sim.zip");
	}
}
