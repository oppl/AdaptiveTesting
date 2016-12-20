AdaptiveTesting
==============

Web-based platform for delivering computerized adaptive tests (CAT). The major design goal is maximum flexibility in item types and testing algorithms. Relies on [RCaller](https://github.com/jbytecode/rcaller) (compatible library version included as a binary) and [Vaadin](http://www.vaadin.com).

The details are summarized and scientifically grounded in a [working paper](https://scawomo.wordpress.com/2016/06/01/scawomo-wp-02/), of which an updated version was accepted for publication in the [International Journal of Educational Technologies in Higher Education](https://educationaltechnologyjournal.springeropen.com). 

## Usage

The committed files contain the complete maven configuration for the project. It does not rely on any external libraries aside maven. Furthermore, the configuration files for the IntelliJ IDE are included - you might want to dismiss them, if you use something else. 

The plattform relies on the availability of a local installation of [R](https://www.r-project.org). The R binary needs to be available in "C:\Program Files\R" for Windows servers and in "/usr/local/bin" for Unix-systems (incl. Mac). While these are default paths, they should be made configurable at some point in time. 

The platform loads question items in XML-format from a path specified in the web.xml file (parameter-name at.jku.ce.adaptivetesting.questionfolder). The XML-format is determined by the implementations of the domain-specific classes holding item data (via JAXP-bindings). Currently, we only load questions in a single item pool - features for handling several tests simultanously still need to be implemented.

## Branching Policy

New features are developed in separate branches that are eventually merged with the master branch as soon as a stable and tested version is available. The master branch always only contains a version that can be deployed in real-world workshops.

If you want to contribute, you might want to have a look at the open issues and projects specified in this repository. While [issues](https://github.com/win-ce/AdaptiveTesting2/issues) point at concrete bugs or potential enhancements that have been identified during testing and real-world deployment, the [projects](https://github.com/win-ce/AdaptiveTesting2/projects) outline the general directions of further development. Feel free to contact [Stefan Oppl](https://github.com/oppl), if you have any questions on any of the projects, or simply comment on any of the open issues. 

## License

This software is provided under the [LGPL 3.0 license](https://github.com/win-ce/AdaptiveTesting2/blob/master/LICENSE.md).
