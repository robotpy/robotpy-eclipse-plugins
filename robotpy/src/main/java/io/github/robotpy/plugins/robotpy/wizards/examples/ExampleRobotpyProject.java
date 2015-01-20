package io.github.robotpy.plugins.robotpy.wizards.examples;

import java.util.List;
import java.util.Map;

import edu.wpi.first.wpilib.plugins.core.wizards.IExampleProject;
import io.github.robotpy.plugins.robotpy.wizards.newproject.RobotpyProjectType;

public class ExampleRobotpyProject extends RobotpyProjectType implements IExampleProject {
	private String name, description, world;
	private List<String> tags;
	private List<ExportFile> files;
	
	public ExampleRobotpyProject(String name, String description, List<String> tags, 
			String world, List<String> packages, List<ExportFile> files) {
		this.name = name;
		this.description = description;
		this.tags = tags;
		this.world = world;
		this.files = files;
	}

	public String getName() {
		return name;
	}
	
	public String getContent() {
		return "<h1>"+name+"</h1><p>"+description+"</p>";
	}

	public List<String> getTags() {
		return tags;
	}
	
	public String getWorld() {
		return world;
	}
	
	@Override
	public Map<String, String> getFiles(String packageName) {
		String packageDir = packageName.replace(".", "/");
		Map<String, String> files = super.getFiles(packageName);
		for (ExportFile file : this.files) {
			files.put(file.destination.replaceAll("\\$package-dir", packageDir), file.source);
		}
		return files;
	}
}
