**THIS REPOSITORY IS NO LONGER MAINTAINED**: Check out the RobotPy vscode plugin instead: https://github.com/robotpy/vscode-wpilib-python

RobotPy Eclipse Plugins
=======================

This is the source code for a set of experimental Eclipse plugins based on 
the WPILib Eclipse plugins. The goal is to tightly integrate RobotPy into
Eclipse using WPILib components and pydev.

This is still very much in development, please file any issues you might have
at https://github.com/robotpy/robotpy-eclipse-plugins/issues

Requirements
============

* You should have python 3.4+ installed
* You should have the latest version of [pyfrc](http://pyfrc.readthedocs.org) installed
* You should have Eclipse 4.x installed
* Eclipse must be using Java 8+ (WPILib plugin requirement, sorry)


Installation
============

Use the Eclipse update manager to install the plugins from our download site. It can
usually be found at `Help -> Install New Software`. Once there, add a new plugin site
using the 'Add' button, and use this URL:

    http://dl.bintray.com/robotpy/eclipse/

If you did it right, it should show a few checkboxes. Expand the 'WPILib Robot
Development' item, and select "Robot Python Development". That should install
all of the things you need for running the RobotPy eclipse plugins.

Usage
=====

The primary functionality that this plugin provides are launch shortcuts,
example programs, and integrated support for some of the FRC tools such as
SmartDashboard and the RIOLog plugin.

Example projects
----------------

You can install Example programs by opening `File -> New -> Project`, and select
"WPILib RobotPy Development". You can select either "RobotPy Example Project"
or "RobotPy project". The example projects that are available are similar to
the projects available in other FRC languages.

Example programs are created with a 'src' directory containing the robot code,
and a 'tests' directory that should do very basic testing for any generic 
robot. Of course, you should write your own specific tests.

Another advantage of creating a robot project from an example is that the Launch
Shortcuts shown below will work if you right click on the project itself, and
not just when you are trying to run `robot.py`.

Launch Shortcuts
----------------

When you right click on files called `robot.py` and select "Run As", you will
be shown shortcuts that will execute pyfrc commands using that robot.py file.
Here are the currently implemented shortcuts:

### RobotPy Deploy

Deploys robot to a roboRIO, running your unit tests. The first time you run it
for your robot, it will ask you for your robot's hostname in the console, so
make sure you enter it when asked.

### RobotPy Deploy (no tests)

Deploys robot to a roboRIO, without running your unit tests (not recommended)

### RobotPy Sim (pyfrc)

Runs your robot code in a simulator using pyfrc. If you want to connect to the
simulated robot using SmartDashboard, then in eclipse you can select the 
`WPILib -> Simulated SmartDashboard` menu option, and it should connect to your
robot code.

### RobotPy Tests

Runs your unit tests directly, and shows the results.

RIOLog
------

The RIOLog plugin will get installed in Eclipse, which will let you see
the netconsole output of your robot program when it's running on your robot.

For more information about RIOLog, see the [WPILib Control System docs](http://wpilib.screenstepslive.com/s/4485/m/13810/l/284333-using-riolog-to-view-console-output).

Dev Testing
===========

In order to build and test a development build of the source code, follow the steps below:

1. Uninstall previous versions of the RobotPy plugin by going to `Window > Install new Software > What is already installed?` and uninstalling the appropriate plugin.
2. Run `mvn verify` inside of the main directory for the repository: robotpy-eclipse-plugins.
3. Install the plugin by going to `Window > Install new Software > Add > Local`, select directory "YOURLOCATION/robotpy-eclipse-plugins/robotpy.updatesite/target/site"
4. Restart Eclipse and you are ready to go.

To remove old build files, run `mvn clean` in robotpy-eclipse-plugins.

License
=======

The eclipse plugins are provided under the Eclipse Public License, and were 
derived from the following Eclipse plugins:

* WPILib eclipse plugins (BSD License)
* Pydev plugins (Eclipse Public License)

<a rel="license" href="http://creativecommons.org/licenses/by-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-sa/4.0/88x31.png" /></a><br />The RobotPy logo is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-sa/4.0/">Creative Commons Attribution-ShareAlike 4.0 International License</a>.
