package io.github.robotpy.plugins.robotpy.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import io.github.robotpy.plugins.robotpy.WPILibPythonPlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = WPILibPythonPlugin.getDefault().getPreferenceStore();
		if (!store.contains(PreferenceConstants.LIBRARY_INSTALLED))
			store.setValue(PreferenceConstants.LIBRARY_INSTALLED, "none");
	}
}
