# Parse json r4 using JSONLD-JAVA 1.1 from a give directory into JSONLD.
# This will create a subdirectory of the given directory called jsonld.
#
# Parse the JSONLD in the gven folder into ttl.  This will create
# a subdirectory of the given directory called ttl.
#
# Example ./runProcessor.sh
# Example ./runProcessor.sh /jsonld_4r_directory
#

java -cp ./core/target/jsonld-java-0.13.1-SNAPSHOT.jar:./core/target/dependency/* com.github.jsonldjava.RDFGEnerator  $@
