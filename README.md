# Java Scoped Template Variables

This project is a plug-in for Eclipse. It adds project scoped variables
to the Eclipse Java preferences. These variables can be used in Java
code templates to add the name of the team that is responsible for the
project or the year the project was started. It also allows you to
define your own project scoped variables.

The plug-in was inspired by waffel's Weblog,
[Use your own variable in eclipse code templates](https://thomaswabner.wordpress.com/2009/08/21/use-your-own-variable-in-eclipse-code-templates/).

## Using the Variables

* Open **Window - Preferences - Java - Scoped Variables** to maintain the variables.
* Assign a value to a scope. A scope is a plug-in ID or a plug-in ID
  prefix that ends with an asterisk, e.g. `de.tools400.*`.
* Use the variables in **Java - Code Style - Code Templates**,
  e.g. `${project_team}` or `${project_year}`.

The value of a variable is resolved from the plug-in ID of the project
that contains the code. Hence the variables are only resolved in plug-in
projects.

## Projects

* **Java Scoped Template Variables Plugin**: Sources of plug-in `de.tools400.java.scoped.variables.core`.
* **Java Scoped Template Variables Feature**: Feature of the plug-in.
* **Java Scoped Template Variables Update Site**: Update site definition.
* **Java Scoped Template Variables Build**: Ant script for updating the
  version number and for packaging the update site.

## Launching a Test Application

Follow these steps to launch a test application:
* Clone the repository to your PC.
* Import the projects into Eclipse.
* Open file `plugin.xml` of project **Java Scoped Template Variables Plugin**.
* Select the **Overview** tab.
* Click the **Launch an Eclipse application in Debug mode** link below **Testing**.

## Building the Plug-in

The Ant script `build/build.xml` of project **Java Scoped Template
Variables Build** must run in the same JRE as the workspace, to make
the `eclipse.refreshLocal` task work properly.

Follow these steps to build an archived update site:

* Open file `build/build.properties` of project **Java Scoped Template Variables Build**.
* Increment the version number of property `build.version`.
* Execute target `build`. It updates the version number of the following files:
  * Java Scoped Template Variables Plugin: `META-INF/MANIFEST.MF`
  * Java Scoped Template Variables Feature: `feature.xml`
  * Java Scoped Template Variables Update Site: `site.xml`
* Right click `site.xml` of project **Java Scoped Template Variables Update Site**
  and select option **PDE Tools - Build Site**.
* Execute target `createSourceForgeFiles`. It creates the archived update site
  `Java_Scoped_Template_Variables_v<version>.zip` in folder `upload/sf-files`.
* Commit the changes and create a version tag, e.g. `v1.2.2.r`.

The version number format is `major.minor.micro.qualifier`, where the
qualifier is `r` for a release version or `bnnn` for a beta version.

## License

The plug-in is made available under the terms of the Common Public
License v1.0, see [LICENSE](LICENSE).

The sources in `src/org/json` of project **Java Scoped Template Variables
Plugin** are Copyright (c) 2002 JSON.org and are made available under the
terms of the JSON License, see the header of each file.
