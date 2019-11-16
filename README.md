# headlong-cli
Command line interface for headlong

###Build

Run gradle task fatJar. Outputs headlong-cli-0.1-SNAPSHOT.jar to /build/libs

###Example usage:

java -jar headlong-cli-0.1-SNAPSHOT.jar -n '(uint112)' '[{"type":"string","value":"0x5d92d2a10d4e107b1d"}]'

Result: 00000000000000000000000000000000000000000000005d92d2a10d4e107b1d