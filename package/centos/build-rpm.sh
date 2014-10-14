#!/bin/bash
set -e
name=voms-clients

base_dir=../../
source_dir=sources
rpmbuild_dir=$(pwd)/rpmbuild

# spec file and its source
spec_src=${name}.spec.in
spec=${name}3.spec

# determine the pom version and the rpm version
pom_version=$(grep "<version>" ${base_dir}/pom.xml | head -1 | sed -e 's/<version>//g' -e 's/<\/version>//g' -e "s/[ \t]*//g")
rpm_version=$(grep "Version:" ${spec_src} | sed -e "s/Version://g" -e "s/[ \t]*//g")

# Cleanup
./clean.sh

## Prepare spec file
sed -e "s#@@POM_VERSION@@#$pom_version#g" ${spec_src} > ${spec}

## setup RPM build dir structure
mkdir -p ${rpmbuild_dir}/BUILD \
	  ${rpmbuild_dir}/RPMS \
	  ${rpmbuild_dir}/SOURCES \
	  ${rpmbuild_dir}/SPECS \
	  ${rpmbuild_dir}/SRPMS

## Prepare sources
pushd ${base_dir}
mvn clean 
tar --transform 's,^,/voms-clients/,S' -z -c -v -f ${rpmbuild_dir}/SOURCES/${name}3-${rpm_version}.tar.gz --exclude='package/*' --exclude='package' .
popd

## Build RPMs
if [ -n "${dist}" ]; then
	rpmbuild --nodeps -v -ba ${spec} --define "_topdir ${rpmbuild_dir}"
else
	rpmbuild --nodeps -v -ba ${spec} --define "_topdir ${rpmbuild_dir}" --define "dist ${dist}"
fi
