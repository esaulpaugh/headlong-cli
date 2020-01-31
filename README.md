# headlong-cli
Command line interface for https://github.com/esaulpaugh/headlong ABI encoding (full ABIv2 support). Licensed under Apache 2.0 terms.

### Build

Run `gradle fatJar` which outputs headlong-cli-0.1-SNAPSHOT.jar to /build/libs

### Encode example 1

#### cmd, Git Bash

`java -jar headlong-cli-0.1-SNAPSHOT.jar -e "(uint112)" "(\"5d92d2a10d4e107b1d\")"`

#### Git Bash

`java -jar headlong-cli-0.1-SNAPSHOT.jar -e '(uint112)' '("5d92d2a10d4e107b1d")'`

Result:

`00000000000000000000000000000000000000000000005d92d2a10d4e107b1d`