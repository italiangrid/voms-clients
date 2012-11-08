VOMS Clients
============

Command-line clients for Virtual Organization Membership Service (VOMS).

## Install

The last stable build can be downloaded [here](http://radiohead.cnaf.infn.it:9999/job/voms-clients_3_0-SNAPSHOT/lastStableBuild/org.italiangrid$voms-clients/artifact/org.italiangrid/voms-clients/3.0-SNAPSHOT/voms-clients-3.0-SNAPSHOT.tar.gz).
Untar the tarball to a directory of your choice.

You may want to add the bin directory to your path to be able to directly call voms-proxy-* from you shell

```bash
export PATH=$PATH:<install_dir>/bin
```

## Quickstart

### Credentials

While user credentials may be put anywhere, and then their location passed to voms-proxy-init via the
appropriate options, there are obviously default values. User credentials should be posted in the 
.globus subdirectory. Both PKCS12 and PEM formatted credentials are okay. The default name for the PKCS12
are usercert.p12 or usercred.p12, while usercert.pem and userkey.pem are the default names for the PEM 
formatted one. In case both the PEM and PKCS12 formats are present, PEM takes precedence.

The user certiﬁcate MUST not be writable but by the owner (i.e. mode 0600), while the user key MUST not be
writable (i.e. mode 0400).

### Configuring VOMS servers locations

Be sure that in “/etc/vomses” or in “$HOME/.voms/vomses” you have put a copy of the vomses ﬁle distributed 
by all the VOMS servers you wish to contact. This subtree will be recursed into to examine all pertinent ﬁles. 

The format of a vomses ﬁle is a set of lines, each with the following format: “nick” “hostname” “port”
“server’s DN” “alias” “globus version” where nick is the nickname which will be used as the VO name for the 
voms-proxy-init command, hostname is the host on which the server is running, port is the port on which it
is listening, server’s DN is the DN of the server’s certiﬁcate, alias is ignored for now, but should be put
equal to nick, and ﬁnally globus version is optional, and unnecessary if the server is running voms server 
2.0 or later, but to keep compatibility with VOMS servers <= 1.8 it should encoded, in the same way as for
the -globus option of voms-proxy-init, the version of globus on which the server is running.
It is possible to assign the same nick to several servers. This is interpreted by voms-proxy-init as declaring
the servers as replicas of each other, in which case they will be contacted in random order until one
succeeds or all fail.

### Configuring trust for VOMS servers

The X509_VOMS_DIR environment variable must point to a directory where a copy of all the certiﬁcates
of the supported VOMS servers are kept. If the X509_VOMS_DIR variable is not speciﬁed or is empty, 
than it defaults to “/etc/grid-security/vomsdir”. As a backwards compatibility feature, if a server certiﬁcate 
is not found in the $X509_VOMS_DIR/<VO> directory, then it is searched for also in $X509_VOMS_DIR. This is 
temporary, and will eventually be removed, so do not rely on this working.

In this directory there should be a subdirectory for each VO, named as the VO itseld, and all certiﬁcates
belonging to a server for that VO should be placed in the subdir. Alternatively, a ﬁle named <hostname>.lsc 
can be placed in the directory. If so, its format should ba a list of subject/issuer DN couples detailing 
the exact certiﬁcation path, up to and including the CA, of a certiﬁcate authorized to sign ACs for the speciﬁc VO. 
More then one such path can be speciﬁed by separating the paths with a ’—— NEXT CHAIN ——’ line. 

In case both the .lsc and the certiﬁcates are present in a directory, the .lsc ﬁle supercedes the certiﬁcates.

### Creating a proxy certificate

voms-proxy-init is the command that should be used to create a proxy for usage on the grid. Its basic
syntax is:
```bash
$ voms-proxy-init --voms test
```
where voname is the name of the VO to which the user belongs. This will create a proxy containing all the
groups to which the user belongs, but none of the roles. Also, the -voms option may be speciﬁed multiple
times in cas the user belongs to more than one VO.

It is also possible to omit the –voms option entirely. This will however result in the creation of a completely
globus-standard proxy, and is not advised since such proxies will not be usable under gLite 3.0.0 and
beyond.

No roles are ever include in proxy by default. In case they are needed, they must be
explicitly requested. For example, to request the role sgm in the /test/italian group, the following
syntax should be used:
```bash
voms-proxy-init --voms test:/test/italian/Role=sgm
```
thus obtaining a role that will be included in the AC, in addition to all the other information that will be
normally present. In case multiple roles are needed, the -voms option may be used several times.

By default, all FQANs explicitly requested on the command line will be present in the returned credentials,
if they were granted, and in the exact order speciﬁed, with all other FQANs following in an unspeciﬁed
ordering. If a speciﬁc order is needed, it should be explicitly requested via the -order option. For
example, the following command line:
```bash
$ voms-proxy-init --voms test:/test/Role=sgm --order /test
```

Asks for the Role sgm in the root group, and speciﬁes that the resulting AC should begin with membership
in the root group instead, while posing no requirements on the ordering of the remaining FQANs. This
also means that with the above command line there is no guarantee that the role will end up as the second
FQAN. If this is desired, use the following command line instead:
```bash
$ voms-proxy-init --voms test:/test/Role=sgm --order /test --order /test/Role=sgm
```

The validity of an AC created by VOMS will generally be as long as the proxy which contains it. However,
this cannot always be true. For starters, the VOMS server is conﬁgured with a maximum validity for all
the ACs it will create, and a request to exceed it will simply be ignored. If this happens, the output of
voms-proxy-init will indicate the fact. For example, in the following output 
(slightly reformatted for a shorter line then on screen):
```bash
$ voms-proxy-init --voms test --vomslife 50:15
Enter GRID pass phrase:
Your identity: /C=IT/O=INFN/OU=Personal Certificate/L=CNAF/CN=Vincenzo Ciaschini
Creating temporary proxy .................................... Done
Contacting datatag6.cnaf.infn.it:50002
[/C=IT/O=INFN/OU=Host/L=CNAF/CN=datatag6.cnaf.infn.it] "test" Done
Warning: datatag6.cnaf.infn.it:50002:
The validity of this VOMS AC in your proxy is shortened to 86400 seconds!
Creating proxy ......................................... Done
Your proxy is valid until Fri Sep 8 01:55:34 2006
```
You can see that the life of the voms AC has been clearly shortened to 24 hours, even though 50 hours
and 15 minutes had been requested.

If your certiﬁcate is not in the default place, you may specify it explicitly by using the –cert and –key
options, like in the following example:
```bash
$ voms-proxy-init --voms test --cert \$HOME/usercert.pem --key \$HOME/userkey.pem
```

Finally, in case several options have to be speciﬁed several times, proﬁles can be created. For examples:
```bash
$ cat voms.profile
--voms=test
--lifetime=50:15
--cert=$HOME/usercert.pem
--key=$HOME/userkey.pem
--order=/test/aGroup
```
followed by:
```bash
$ voms-proxy-init --conf voms.profile
```
is equivalent to:
```bash
$ voms-proxy-init --voms test --lifetime 50:15 --cert $HOME/usercert.pem \
--key $HOME/userkey.pem --order /test/aGroup
```
with the obvious advantages of being much less error-prone.

### Reading a proxy certificate

Once a proxy has been created, the voms-proxy-info command allowes the user to retrieve several
information from it. The two most basic uses are:
```bash
$ voms-proxy-info
subject : /C=IT/O=INFN/OU=Personal Certificate/L=CNAF/CN=Vincenzo Ciaschini/CN=proxy
issuer : /C=IT/O=INFN/OU=Personal Certificate/L=CNAF/CN=Vincenzo Ciaschini
identity : /C=IT/O=INFN/OU=Personal Certificate/L=CNAF/CN=Vincenzo Ciaschini
type : proxy
strength : 512 bits
path : /tmp/x509up_u502
timeleft : 10:33:52
```
which, as you can see, prints the same information that would be printed by a plain grid-proxy-info,
and then there is:
```bash
$ voms-proxy-info --all
subject : /C=IT/O=INFN/OU=Personal Certificate/L=CNAF/CN=Vincenzo Ciaschini/CN=proxy
issuer : /C=IT/O=INFN/OU=Personal Certificate/L=CNAF/CN=Vincenzo Ciaschini
identity : /C=IT/O=INFN/OU=Personal Certificate/L=CNAF/CN=Vincenzo Ciaschini
type : proxy
strength : 512 bits
path : /tmp/x509up_u502
timeleft : 11:59:59
=== VO test extension information ===
VO : test
subject : /C=IT/O=INFN/OU=Personal Certificate/L=CNAF/CN=Vincenzo Ciaschini
issuer : /C=IT/O=INFN/OU=Host/L=CNAF/CN=datatag6.cnaf.infn.it
attribute : /test
attribute : /test/aGroup
attribute : /test/anotherGroup
attribute : attributeOne = anAttribute (test)
attribute : attributeTwo = AnotherAttribute (test)
timeleft : 11:59:59
uri : datatag6.cnaf.infn.it:15000
which prints everything that there is to know about the proxy and the included ACs. Several options enable
the user to select just a subset of the information shown here.
```

### Destroying a proxy certificate

The voms-proxy-destroy command erases an existing proxy from the system. Its basic use is:
```bash
$ voms-proxy-destroy
```
As can be seen, no output is given in case of a successful usage

## When you are sent RTFM

Describe in details the options for the three commands.