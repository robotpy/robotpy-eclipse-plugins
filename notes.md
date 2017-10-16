Directories
===========

robotpy
-------

Robotpy eclipse plugin source


robotpy.feature
---------------

Defines the plugin as an eclipse feature, because Eclipse developers really
like indirection.

robotpy.updatesite
------------------

Generates an Eclipse compatible update site.

public.updatesite
-----------------

This points at multiple public repositories so that the robotpy plugins can
depend on pydev and FRC plugins without requiring users to set it up manually

Deployment
==========

I'm deploying these to bintray, so I chose to use the script at 
https://github.com/vogellacompany/bintray-publish-p2-updatesite for uploading
artifacts to bintray.

Now I have it set up such that when a tag is pushed, travis-ci will
automatically push a new build to bintray.
