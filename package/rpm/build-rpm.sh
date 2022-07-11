#!/usr/bin/env bash
set -exu -o pipefail
name=voms-clients

base_dir=../../
source_dir=sources
rpmbuild_dir=$(pwd)/rpmbuild

# spec file and its source
spec_src="${name}.spec.in"
spec="${name}3.spec"

# determine the pom version and the rpm version
pom_version=$(grep "<version>" "${base_dir}/pom.xml" | head -1 | sed -e 's/<version>//g' -e 's/<\/version>//g' -e "s/[ \t]*//g")
rpm_version=$(grep "Version:" "${spec_src}" | sed -e "s/Version://g" -e "s/[ \t]*//g")

# Cleanup
./clean.sh

## Prepare spec file
sed -e "s#@@POM_VERSION@@#$pom_version#g" ${spec_src} > ${spec}

## setup RPM build dir structure
mkdir -p "${rpmbuild_dir}/BUILD" \
	  "${rpmbuild_dir}/RPMS" \
	  "${rpmbuild_dir}/SOURCES" \
	  "${rpmbuild_dir}/SPECS" \
	  "${rpmbuild_dir}/SRPMS"

## Prepare sources
## We cannot use tar --transform as the tar version in SL5 does not support it
source_tmp_dir="$(mktemp -d /tmp/voms-client-rpm.XXXXX)"

mkdir -p "${source_tmp_dir}/${name}"
cp -r "${base_dir}" "${source_tmp_dir}/${name}"
pushd "${source_tmp_dir}"
tar cvzf "${rpmbuild_dir}/SOURCES/${name}3-${rpm_version}.tar.gz" --exclude="${name}/package/*" --exclude="${name}/package" --exclude="${name}/target" .
popd

rm -rf "${source_tmp_dir}"

## Build RPMs
if [ -z "${dist}" ]; then
	rpmbuild --nodeps -v -ba "${spec}" --define "_topdir ${rpmbuild_dir}"
else
	rpmbuild --nodeps -v -ba "${spec}" --define "_topdir ${rpmbuild_dir}" --define "dist ${dist}"
fi

