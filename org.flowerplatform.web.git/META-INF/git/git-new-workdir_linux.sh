#!/bin/sh

usage () {
	echo "usage:" $@
	exit 1
}

die3 () {
	echo $@
	exit 3
}

die4 () {
	echo $@
	exit 4
}

die5 () {
	echo $@
	exit 5
}

die6 () {
	echo $@
	exit 6
}

if test $# -lt 2 || test $# -gt 3
then
	usage "$0 <repository> <new_workdir> [<branch>]"
fi

orig_git=$1
new_workdir=$2
branch=$3

# want to make sure that what is pointed to has a .git directory ...
git_dir=$(cd "$orig_git" 2>/dev/null &&
  git rev-parse --git-dir 2>/dev/null) ||
  
  die3 "Not a git repository: \"$orig_git\""

case "$git_dir" in
.git)
	git_dir="$orig_git/.git"
	;;
.)
	git_dir=$orig_git
	;;
esac

# don't link to a configured bare repository
isbare=$(git --git-dir="$git_dir" config --bool --get core.bare)
if test ztrue = z$isbare
then
	die4 "\"$git_dir\" has core.bare set to true," \
		" remove from \"$git_dir/config\" to use $0"
fi

# don't recreate a workdir over an existing repository
if test -e "$new_workdir"
then
	die5 "destination directory '$new_workdir' already exists."
fi

# make sure the links use full paths
git_dir=$(cd "$git_dir"; pwd)

# create the workdir
mkdir -p "$new_workdir/.git" || die6 "unable to create \"$new_workdir\"!"

# create the links to the original repo.  explicitly exclude index, HEAD and
# logs/HEAD from the list since they are purely related to the current working
# directory, and should not be shared.
for x in config refs logs/refs objects info hooks packed-refs remotes rr-cache svn
do
	case $x in
	*/*)
		mkdir -p "$(dirname "$new_workdir/.git/$x")"
		;;
	esac
	ln -s "$git_dir/$x" "$new_workdir/.git/$x"
done

# now setup the workdir
cd "$new_workdir"
# copy the HEAD from the original repository as a default branch
cp "$git_dir/HEAD" .git/HEAD
# checkout the branch (either the same as HEAD from the original repository, or
# the one that was asked for)
git checkout -f $branch

exit 0