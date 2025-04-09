# Remember to define the base_version and version_pom macros
%{!?base_version: %global base_version 0.0.0}
%{!?version_pom: %global version_pom 0.0.0}

%global orig_name voms-clients

Name: voms-clients-java
Version: %{base_version}
Release: 1%{?dist}
Summary: The Virtual Organisation Membership Service command line clients
Group: System Environment/Libraries
License: Apache-2.0
URL: https://github.com/italiangrid/voms-clients

BuildArch: noarch

BuildRequires:  maven-openjdk17
BuildRequires:  jpackage-utils

Requires:  java-17-openjdk-headless

Requires(post):    %{_bindir}/update-alternatives
Requires(postun):  %{_bindir}/update-alternatives

Provides:  voms-clients = %{version}
Provides:  voms-clients3 = %{version}
Conflicts:  voms-clients <= 2.0.11-1

%description
The Virtual Organization Membership Service (VOMS) is an attribute authority
which serves as central repository for VO user authorization information,
providing support for sorting users into group hierarchies, keeping track of
their roles and other attributes in order to issue trusted attribute
certificates and SAML assertions used in the Grid environment for
authorization purposes.

This package provides the command line clients for VOMS, voms-proxy-init, 
voms-proxy-destroy and voms-proxy-info. 

%package    javadoc
Summary:    Javadoc for the VOMS Java command line clients
Group:      Documentation
BuildArch:  noarch
Requires:   %{name} = %{version}

%description javadoc
Virtual Organization Membership Service (VOMS) Java command line clients Documentation.

%prep

%build
mvn %{?mvn_settings} -U -Dmaven.test.skip=true clean package

%install
rm -rf %{buildroot}
mkdir -p %{buildroot}%{_javadir}
mkdir -p %{buildroot}%{_javadocdir}/%{name}-%{version_pom}
mkdir -p %{buildroot}%{_sharedstatedir}/%{name}/lib

tar -C %{buildroot}/usr -xvzf target/%{orig_name}.tar.gz --strip 1 

mv %{buildroot}%{_javadir}/%{orig_name}/*.jar $RPM_BUILD_ROOT%{_varlib}/%{name}/lib

ln -s %{_varlib}/%{name}/lib/%{orig_name}-%{pom_version}.jar %{buildroot}%{_javadir}/%{orig_name}.jar
ln -s %{_varlib}/%{name}/lib/%{orig_name}-%{pom_version}.jar %{buildroot}%{_javadir}/%{name}.jar

# Rename to voms-proxy-*3 to avoid clashes with old C clients
mv %{buildroot}/%{_bindir}/voms-proxy-init %{buildroot}/%{_bindir}/voms-proxy-init3
mv %{buildroot}/%{_bindir}/voms-proxy-info %{buildroot}/%{_bindir}/voms-proxy-info3
mv %{buildroot}/%{_bindir}/voms-proxy-destroy %{buildroot}/%{_bindir}/voms-proxy-destroy3

# Rename manpages
mv %{buildroot}/%{_mandir}/man1/voms-proxy-init.1 %{buildroot}/%{_mandir}/man1/voms-proxy-init3.1
mv %{buildroot}/%{_mandir}/man1/voms-proxy-info.1 %{buildroot}/%{_mandir}/man1/voms-proxy-info3.1
mv %{buildroot}/%{_mandir}/man1/voms-proxy-destroy.1 %{buildroot}/%{_mandir}/man1/voms-proxy-destroy3.1

# Needed by alternatives. See http://fedoraproject.org/wiki/Packaging:Alternatives
touch %{buildroot}/%{_bindir}/voms-proxy-init
touch %{buildroot}/%{_bindir}/voms-proxy-info
touch %{buildroot}/%{_bindir}/voms-proxy-destroy

%clean
rm -rf %{buildroot}

%files
%defattr(-,root,root,-)

%ghost %{_bindir}/voms-proxy-init
%ghost %{_bindir}/voms-proxy-info
%ghost %{_bindir}/voms-proxy-destroy

%{_bindir}/voms-proxy-init3
%{_bindir}/voms-proxy-info3
%{_bindir}/voms-proxy-destroy3

%{_mandir}/man1/voms-proxy-init3.1.gz
%{_mandir}/man1/voms-proxy-info3.1.gz
%{_mandir}/man1/voms-proxy-destroy3.1.gz
%{_mandir}/man5/vomses.5.gz
%{_mandir}/man5/vomsdir.5.gz

%{_javadir}/%{name}.jar
%{_javadir}/%{orig_name}.jar

%dir %{_sharedstatedir}/%{name}/lib
%{_sharedstatedir}/%{name}/lib/*.jar

%pre

if [ $1 -eq 2 ] ; then
  ## Package upgrade, cleanup embedded dependencies
  if [ -d "%{_sharedstatedir}/%{name}/lib" ]; then
    rm -f %{_sharedstatedir}/%{name}/lib/*.jar
  fi

  ## Remove scripts if not managed with alternatives (pre v. 3.0.5)
  for c in voms-proxy-init voms-proxy-info voms-proxy-destroy; do
    if [[ -x %{_bindir}/$c && ! -L %{_bindir}/$c ]]; then
      rm -f %{_bindir}/$c
    fi
  done
fi

%preun

if [ $1 -eq 0 ] ; then
    rm -f %{_sharedstatedir}/%{name}/lib/*.jar
    rm -rf %{_sharedstatedir}/%{name}
fi

%post

%{_sbindir}/update-alternatives --install %{_bindir}/voms-proxy-init \
   voms-proxy-init %{_bindir}/voms-proxy-init3 90 \
   --slave %{_mandir}/man1/voms-proxy-init.1.gz voms-proxy-init-man %{_mandir}/man1/voms-proxy-init3.1.gz

%{_sbindir}/update-alternatives --install %{_bindir}/voms-proxy-info \
   voms-proxy-info %{_bindir}/voms-proxy-info3 90 \
   --slave %{_mandir}/man1/voms-proxy-info.1.gz voms-proxy-info-man %{_mandir}/man1/voms-proxy-info3.1.gz 

%{_sbindir}/update-alternatives --install %{_bindir}/voms-proxy-destroy \
   voms-proxy-destroy %{_bindir}/voms-proxy-destroy3 90 \
   --slave %{_mandir}/man1/voms-proxy-destroy.1.gz voms-proxy-destroy-man %{_mandir}/man1/voms-proxy-destroy3.1.gz 

%postun

if [ $1 -eq 0 ] ; then
    %{_sbindir}/update-alternatives  --remove voms-proxy-init %{_bindir}/voms-proxy-init3
    %{_sbindir}/update-alternatives  --remove voms-proxy-info %{_bindir}/voms-proxy-info3
    %{_sbindir}/update-alternatives  --remove voms-proxy-destroy %{_bindir}/voms-proxy-destroy3
fi

%changelog
* Tue Mar 25 2025 Enrico Vianello <enrico.vianello at cnaf.infn.it> - 3.3.5-0
- Packaging 3.3.5-0 version

* Fri Aug 2 2024 Enrico Vianello <enrico.vianello at cnaf.infn.it> - 3.3.4
- Packaging 3.3.4-1 version

* Fri Jun 21 2024 Enrico Vianello <enrico.vianello at cnaf.infn.it> - 3.3.3-1
- 3.3.3 packaging for EL9

* Fri May 13 2022 Enrico Vianello <enrivo.vianello at cnaf.infn.it> - 3.3.3-0
- Packaging 3.3.3 version

* Fri Mar 9 2018 Andrea Ceccanti <andrea.ceccanti at cnaf.infn.it> - 3.3.2-0
- Packaging 3.3.2 version

* Mon Dec 4 2017 Andrea Ceccanti <andrea.ceccanti at cnaf.infn.it> - 3.3.0-0
- Packaging 3.3.0 version

* Wed Sep 7 2016 Andrea Ceccanti <andrea.ceccanti at cnaf.infn.it> - 3.0.7-1
- Packaging 3.0.7 version

* Wed Dec 10 2014 Andrea Ceccanti <andrea.ceccanti at cnaf.infn.it> - 3.0.6-1
- Packaging 3.0.6 version

* Mon Jan 27 2014 Andrea Ceccanti <andrea.ceccanti at cnaf.infn.it> - 3.0.5-1
- Fix for https://issues.infn.it/jira/browse/VOMS-495. New packaging installable
  together with with voms-clients 2.x package.

* Thu Sep 26 2013 Andrea Ceccanti <andrea.ceccanti at cnaf.infn.it> - 3.0.4-1
- Fix for https://issues.infn.it/jira/browse/VOMS-423
- Move to VOMS Java APIs v. 3.0.2

* Thu Jul 11 2013 Andrea Ceccanti <andrea.ceccanti at cnaf.infn.it> - 3.0.3-1
- Fix for https://issues.infn.it/jira/browse/VOMS-361

* Tue Jul 2 2013 Valerio Venturi <valerio.venturi at cnaf.infn.it> - 3.0.2-1
- Fix for https://issues.infn.it/jira/browse/VOMS-307
- Fix for https://issues.infn.it/jira/browse/VOMS-296

* Thu Apr 11 2013 Andrea Ceccanti <andrea.ceccanti at cnaf.infn.it> - 3.0.1-1
- Fix for https://issues.infn.it/browse/VOMS-256

* Wed Nov 14 2012 Valerio Venturi <valerio.venturi at cnaf.infn.it> - 3.0.0-1
- New version of VOMS command line clients

