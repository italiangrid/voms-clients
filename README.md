# VOMS Clients

[![Build Status](https://travis-ci.org/italiangrid/voms-clients.svg)](https://travis-ci.org/italiangrid/voms-clients)

Command-line clients for Virtual Organization Membership Service (VOMS), i.e.:

- *voms-proxy-init*: creates a VOMS proxy containing VOMS attributes
- *voms-proxy-info*: shows information about a created VOMS proxy certificate
- *voms-proxy-destroy*: destroys a VOMS proxy certificate

## Build instructions

Clone this github repo. 
Build packages with:

```bash
mvn package
```

## Build

You will need maven and Java >= 6.
To build the clients, type:

  mvn package

To build RPM packages, see instructions in the `package/rpm` directory.

## Install

A tarball containing the clients can be found in the `target` directory under the source tree.
Untar the tarball to a directory of your choice.

You may want to add the bin directory to your path to be able to directly call voms-proxy-* from you shell

```bash
export PATH=$PATH:<install_dir>/bin
```

### Install from RPMs packages

RPMs for the latest stable release can be found on the EMI repository:

- [CentOS/SL 6](http://emisoft.web.cern.ch/emisoft/dist/EMI/3/sl6/x86_64/updates/repoview/voms-clients3.html)
- [CentOS/SL 5](http://emisoft.web.cern.ch/emisoft/dist/EMI/3/sl5/x86_64/updates/repoview/voms-clients3.html)

RPMs for the latest development build can be found at the following URLs:

- [CentOS/SL 6](http://radiohead.cnaf.infn.it:9999/job/voms-clients-rpm_develop_SL6/lastSuccessfulBuild)
- [CentOS/SL 5](http://radiohead.cnaf.infn.it:9999/job/voms-clients-rpm_develop_SL5/lastSuccessfulBuild)

## Quickstart

### Credentials

User credentials typically live in the 
```bash
  $HOME/.globus
```
directory.

PKCS12 and PEM X.509 credentials are both supported. For PKCS12 the credential file should be
named:

```bash
  $HOME/.globus/usercred.p12
```

PEM credential files should be named:

```bash  
  $HOME/.globus/usercert.pem
  $HOME/.globus/userkey.pem
```

In case both PEM and PKCS12 formats are present, PEM takes precedence.

Please setup your user credentials so that the private key is only readable by the user owning 
the credentials (i.e., unix mode 0400).

### Configuring VOMS servers locations

VOMS server contact information is typically maintained in `/etc/vomses` or in `$HOME/.glite/vomses` directory.

For more information on the format of these files and what information should be there, consult the `vomses`
man page.

### Configuring trust for VOMS servers

VOMS server trust information in typically maintained in the `/etc/grid-security/vomsdir` directory,
or in the directory pointed by the `X509_VOMS_DIR` environment variable.

For more information on the format of this directory and what information should be there, 
consult the `vomsdir` man page.

### Creating a VOMS proxy certificate

`voms-proxy-init` is the command that creates VOMS proxies which can be used for authorization purposes on the Grid. 

Its basic syntax is:
```bash
$ voms-proxy-init --voms <test>
```
where `test` is the name of the VO to which the user belongs. This command will create a proxy containing all the
groups to which the user belongs, but none of the roles. The -voms option may be speciﬁed multiple times to request
multiple attributes or attributes from multiple VOs.

VOMS roles are conditional attributes which are included in a VOMS attribute certificate only when explicitly
requested. Roles can be requested using a command like the following one:
```bash
voms-proxy-init -voms atlas:/atlas/Role=pilot
```
If the `-voms` option is not specified, a proxy without VOMS extension will be created.

### Validity constraints

By default, voms-proxy-init will generate a proxy valid for 12 hours including a VOMS extension valid for the same time (if requested).
These time periods can be changed using the *-valid* option, which will set the validity of both the proxy and
the AC. Note that the validity of the AC can only be "proposed" by voms-proxy-init, as the AC validity is set by the VOMS server
and its maximum value is limited by local VOMS server configuration (typically the maximum value is 24 hours).

More information can be found in the `voms-proxy-init` man page.

### Displayng information embedded in a VOMS proxy certificate 

Once a proxy has been created, the `voms-proxy-info` command allows the user to print the information
cointained in it. 
```bash
subject   : /C=IT/O=INFN/OU=Personal Certificate/L=CNAF/CN=Andrea Ceccanti/CN=proxy
issuer    : /C=IT/O=INFN/OU=Personal Certificate/L=CNAF/CN=Andrea Ceccanti
identity  : /C=IT/O=INFN/OU=Personal Certificate/L=CNAF/CN=Andrea Ceccanti
type      : proxy
strength  : 1024
path      : /tmp/x509up_u501
timeleft  : 11:59:48
key usage : digitalSignature keyEncipherment dataEncipherment
```

Information about VOMS attributes can be printed passing the `-all` option to the command:

```bash
$ voms-proxy-info --all
subject   : /C=IT/O=INFN/OU=Personal Certificate/L=CNAF/CN=Andrea Ceccanti/CN=proxy
issuer    : /C=IT/O=INFN/OU=Personal Certificate/L=CNAF/CN=Andrea Ceccanti
identity  : /C=IT/O=INFN/OU=Personal Certificate/L=CNAF/CN=Andrea Ceccanti
type      : proxy
strength  : 1024
path      : /tmp/x509up_u501
timeleft  : 12:04:48
key usage : digitalSignature keyEncipherment dataEncipherment 
=== VO testers.eu-emi.eu extension information ===
VO        : testers.eu-emi.eu
subject   : CN=Andrea Ceccanti,L=CNAF,OU=Personal Certificate,O=INFN,C=IT
issuer    : CN=emitestbed07.cnaf.infn.it,L=CNAF,OU=Host,O=INFN,C=IT
attribute : /testers.eu-emi.eu/Role=NULL/Capability=NULL
attribute : /testers.eu-emi.eu/pseudotest1/Role=NULL/Capability=NULL
attribute : /testers.eu-emi.eu/test/Role=NULL/Capability=NULL
attribute : /testers.eu-emi.eu/test1/Role=NULL/Capability=NULL
attribute : test_ga = val (/testers.eu-emi.eu/test)
attribute : default-group = camaghe (testers.eu-emi.eu)
timeleft  : 11:59:18
uri       : emitestbed07.cnaf.infn.it:15002
```

More information can be found in the `voms-proxy-info` man page.

### Destroying a proxy certificate

The `voms-proxy-destroy` command erases an existing proxy from the system. Its basic use is:

```bash
$ voms-proxy-destroy
```
More information can be found in the `voms-proxy-destroy` man page.
