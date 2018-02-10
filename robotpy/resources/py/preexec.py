#
# This is a bit of code that will execute prior to executing robot code. This
# code does things like checking its environment and making sure that the user
# receives helpful error messages when they do something they shouldn't have.
#

import marshal
import sys
import os.path
import tokenize
import traceback
import types

from distutils.version import LooseVersion

is_windows = hasattr(sys, 'getwindowsversion')

# setup minimum requirements here
PYFRC_MIN_VERSION = LooseVersion("2018.0.9")
WPILIB_MIN_VERSION = LooseVersion("2018.0.11")
NETWORKTABLES_MIN_VERSION = LooseVersion("2018.0.1")

if is_windows:
    PIP_CMD = "py -3 -m pip"
else:
    PIP_CMD = "pip3"

def err(msg):
    sys.stderr.write("ERROR: %s" % msg)
    exit(1)
    
def version_check(name, need, has, pkg):
    if has == 'master':
        return
    
    if LooseVersion(has) < need:
        
        msg = "%s %s or greater must be installed (found version %s)\n" % (name, need, has) + \
              "\nTo upgrade: Open a terminal, and run '%s install -U %s'" % (PIP_CMD, pkg)
            
        if not is_windows:
            msg += '\n   Note: An upgrade may require sudo if not using virtualenv'
        
        err(msg)

def preexec_main():
    
    if sys.version_info[0] < 3 or \
       sys.version_info[1] < 5:
        err("Your interpreter isn't set up properly for python 3.5.x (RobotPy requires python 3.5.x)\n" +
            "-> Found python %s" % sys.version)
    
    try:
        import wpilib
    except ImportError:
        err("The 'wpilib' module could not be imported -- did you install pyfrc?\n" +
            "-> See http://pyfrc.readthedocs.org/en/latest/install.html")
    
    version_check('wpilib', WPILIB_MIN_VERSION, wpilib.__version__, 'pyfrc')
    
    try:
        import networktables
    except ImportError:
        err("The 'networktables' module could not be imported -- did you install pyfrc?\n" +
            "-> See http://pyfrc.readthedocs.org/en/latest/install.html")
    
    version_check('pynetworktables', NETWORKTABLES_MIN_VERSION, networktables.__version__, 'pynetworktables')
    
    try:
        import pyfrc
    except ImportError:
        err("The 'pyfrc' module could not be imported -- did you install pyfrc?\n" +
            "-> See http://pyfrc.readthedocs.org/en/latest/install.html")
    
    version_check('pyfrc', PYFRC_MIN_VERSION, pyfrc.__version__, 'pyfrc')
    
    # delete all the modules
    mods_to_delete = []

    for k, v in sys.modules.items():
        if k in ['wpilib', 'pyfrc', 'networktables'] or \
           k.startswith('wpilib.') or \
           k.startswith('pyfrc.') or \
           k.startswith('networktables.'):

           mods_to_delete.append(k)

    for k in mods_to_delete:
        del sys.modules[k]

    # Finally run the thing
    filename = sys.argv[1]
    args = [filename] + list(sys.argv[2:])
    
    # Fix up sys.path before we run it though
    sys.path[0] = os.path.abspath(os.path.dirname(filename))
    
    run_python_file(filename, args)
    
    # But, make sure that the user actually called wpilib.run, otherwise they
    # will just be presented with a blank console and not know why
    
    

#######################################################################
#
# This code swiped from the 'coverage' module, released under a BSD license
# by Ned Batchelder
#
#######################################################################

class DummyLoader(object):
    """A shim for the pep302 __loader__, emulating pkgutil.ImpLoader.

    Currently only implements the .fullname attribute
    """
    def __init__(self, fullname, *_args):
        self.fullname = fullname


def run_python_file(filename, args, package=None, modulename=None):
    """Run a Python file as if it were the main program on the command line.

    `filename` is the path to the file to execute, it need not be a .py file.
    `args` is the argument array to present as sys.argv, including the first
    element naming the file being executed.  `package` is the name of the
    enclosing package, if any.

    `modulename` is the name of the module the file was run as.

    """
    if modulename is None and sys.version_info >= (3, 3):
        modulename = '__main__'

    # Create a module to serve as __main__
    old_main_mod = sys.modules['__main__']
    main_mod = types.ModuleType('__main__')
    sys.modules['__main__'] = main_mod
    main_mod.__file__ = filename
    if package:
        main_mod.__package__ = package
    if modulename:
        main_mod.__loader__ = DummyLoader(modulename)

    main_mod.__builtins__ = sys.modules['builtins']

    # Set sys.argv properly.
    old_argv = sys.argv
    sys.argv = args

    try:
        # Make a code object somehow.
        if filename.endswith((".pyc", ".pyo")):
            code = make_code_from_pyc(filename)
        else:
            code = make_code_from_py(filename)

        # Execute the code object.
        try:
            exec(code, main_mod.__dict__)
        except SystemExit:
            # The user called sys.exit().  Just pass it along to the upper
            # layers, where it will be handled.
            raise
        except:
            # Something went wrong while executing the user code.
            # Get the exc_info, and pack them into an exception that we can
            # throw up to the outer loop.  We peel one layer off the traceback
            # so that the coverage.py code doesn't appear in the final printed
            # traceback.
            typ, err, tb = sys.exc_info()

            # PyPy3 weirdness.  If I don't access __context__, then somehow it
            # is non-None when the exception is reported at the upper layer,
            # and a nested exception is shown to the user.  This getattr fixes
            # it somehow? https://bitbucket.org/pypy/pypy/issue/1903
            getattr(err, '__context__', None)

            # Just print the traceback, that's all the user really wants
            traceback.print_exception(typ, err, tb.tb_next)
    finally:
        # Restore the old __main__
        sys.modules['__main__'] = old_main_mod

        # Restore the old argv and path
        sys.argv = old_argv

def make_code_from_py(filename):
    """Get source from `filename` and make a code object of it."""
    # Open the source file.
    try:
        source = read_python_source(filename)
    except (IOError, NoSource):
        raise NoSource("No file to run: '%s'" % filename)

    code = compile(source, filename, "exec")
    return code

# coverage: python.py

def read_python_source(filename):
    """Read the Python source text from `filename`.

    Returns a str: unicode on Python 3, bytes on Python 2.

    """
    # Python 3.2 provides `tokenize.open`, the best way to open source files.
    if sys.version_info >= (3, 2):
        f = tokenize.open(filename)
    else:
        f = open(filename, "rU")

    with f:
        return f.read()

if __name__ == '__main__':
    preexec_main()
