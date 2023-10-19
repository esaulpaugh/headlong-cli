# headlong-cli
Command line interface for https://github.com/esaulpaugh/headlong ABI and RLP codecs. Depends on headlong v10.0.0 and gson v2.10.1. Licensed under Apache 2.0 terms.

## Build

Run `gradle fatJar` which produces `build/libs/headlong-cli-1.1-SNAPSHOT.jar`

Or `mvn package` which produces `target/headlong-cli-1.1-SNAPSHOT.jar`

## Usage (cmd, Git Bash, Windows PowerShell)

#### Encode tuple

Command: `java -jar headlong-cli-1.1-SNAPSHOT.jar -e "(uint112)" "('5d92d2a10d4e107b1d')"`

Result: `00000000000000000000000000000000000000000000005d92d2a10d4e107b1d`

#### Encode function with human-friendly syntax

Command: `java -jar headlong-cli-1.1-SNAPSHOT.jar -ef "sam(bytes,bool,uint[])" "(u'dave', b'true', [ d'1', d'2', d'3' ])"`

Result: `a5643bf20000000000000000000000000000000000000000000000000000000000000060000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000a0000000000000000000000000000000000000000000000000000000000000000464617665000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000003`

Prepending a human-friendly code to an item affects how it is interpreted:

`u` for UTF-8 (e.g. `u'Yeehaw'`)

`b` for boolean (e.g. `b'false'`)

`a` for address (e.g. `a'0x000000000000F9087ABcDEf00CafeBaBE86244AA'`)

`d` for decimal (e.g. `d'255'`)

#### More examples

Command: `java -jar headlong-cli-1.1-SNAPSHOT.jar -e "(function[2][][],bytes24,string[1][1],address[],uint72,(uint8),(int16)[2][][1],(int32)[],uint40,(int48)[],(uint),bool,string,bool[2],int24[],uint40[1])" "( [ [ [ '191c766e29a65787b7155dd05f41292438467db93420cade', '191c766e29a65787b7155dd05f41292438467db93420cade' ] ] ], '191c766e29a65787b7155dd05f41292438467db93420cade', [ [ '7a' ] ], [ 'ff00ee01dd02cc03cafebabe9906880777086609' ], 'fdfffffffffffffe04', [ '07' ], [ [ [ [ '09' ], [ 'fffffff5' ] ] ] ], [ [ '11' ], [ 'ffffffed' ] ], 'fca527923b', [ [ '7e' ], [ 'ffffffffffffff82' ] ], [ '0a' ], '01', '6661726f7574', [ '01', '01' ], [ '03', '14', 'fffffa' ], [ 'fffffffe' ])"`

Result: `0000000000000000000000000000000000000000000000000000000000000220191c766e29a65787b7155dd05f41292438467db93420cade000000000000000000000000000000000000000000000000000000000000000000000000000002c000000000000000000000000000000000000000000000000000000000000003400000000000000000000000000000000000000000000000fdfffffffffffffe04000000000000000000000000000000000000000000000000000000000000000700000000000000000000000000000000000000000000000000000000000003800000000000000000000000000000000000000000000000000000000000000400000000000000000000000000000000000000000000000000000000fca527923b0000000000000000000000000000000000000000000000000000000000000460000000000000000000000000000000000000000000000000000000000000000a000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000004c000000000000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000000000000000000000000000000001000000000000000000000000000000000000000000000000000000000000050000000000000000000000000000000000000000000000000000000000fffffffe000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000001191c766e29a65787b7155dd05f41292438467db93420cade0000000000000000191c766e29a65787b7155dd05f41292438467db93420cade00000000000000000000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000017a000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001000000000000000000000000ff00ee01dd02cc03cafebabe9906880777086609000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000000000000000000000000000000009fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff500000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000011ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffed0000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000007effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8200000000000000000000000000000000000000000000000000000000000000066661726f75740000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000300000000000000000000000000000000000000000000000000000000000000030000000000000000000000000000000000000000000000000000000000000014fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffa`

#### Decode

Command: `java -jar headlong-cli-1.1-SNAPSHOT.jar -d "(function[2][][],bytes24,string[1][1],address[],uint72,(uint8),(int16)[2][][1],(int32)[],uint40,(int48)[],(uint),bool,string,bool[2],int24[],uint40[1])" "0000000000000000000000000000000000000000000000000000000000000220191c766e29a65787b7155dd05f41292438467db93420cade000000000000000000000000000000000000000000000000000000000000000000000000000002c000000000000000000000000000000000000000000000000000000000000003400000000000000000000000000000000000000000000000fdfffffffffffffe04000000000000000000000000000000000000000000000000000000000000000700000000000000000000000000000000000000000000000000000000000003800000000000000000000000000000000000000000000000000000000000000400000000000000000000000000000000000000000000000000000000fca527923b0000000000000000000000000000000000000000000000000000000000000460000000000000000000000000000000000000000000000000000000000000000a000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000004c000000000000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000000000000000000000000000000001000000000000000000000000000000000000000000000000000000000000050000000000000000000000000000000000000000000000000000000000fffffffe000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000001191c766e29a65787b7155dd05f41292438467db93420cade0000000000000000191c766e29a65787b7155dd05f41292438467db93420cade00000000000000000000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000017a000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001000000000000000000000000ff00ee01dd02cc03cafebabe9906880777086609000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000000000000000000000000000000009fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff500000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000011ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffed0000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000007effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8200000000000000000000000000000000000000000000000000000000000000066661726f75740000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000300000000000000000000000000000000000000000000000000000000000000030000000000000000000000000000000000000000000000000000000000000014fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffa"`

Result (RLP Object Notation):

```
(
  [ [ [ '191c766e29a65787b7155dd05f41292438467db93420cade', '191c766e29a65787b7155dd05f41292438467db93420cade' ] ] ],
  '191c766e29a65787b7155dd05f41292438467db93420cade',
  [ [ '7a' ] ],
  [ 'ff00ee01dd02cc03cafebabe9906880777086609' ],
  'fdfffffffffffffe04',
  [ '07' ],
  [ [ [ [ '09' ], [ 'fff5' ] ] ] ],
  [ [ '11' ], [ 'ffffffed' ] ],
  'fca527923b',
  [ [ '7e' ], [ 'ffffffffff82' ] ],
  [ '0a' ],
  '01',
  '6661726f7574',
  [ '01', '01' ],
  [ '03', '14', 'fffffa' ],
  [ 'fffffffe' ]
)
```

### Packed Encoding

Encode packed: `java -jar headlong-cli-1.1-SNAPSHOT.jar -ep "(int16,bytes1,uint16,string)" "('ffff', '42', '03', '48656c6c6f2c20776f726c6421')"`

Result: `ffff42000348656c6c6f2c20776f726c6421`

Decode packed: `java -jar headlong-cli-1.1-SNAPSHOT.jar -dp "(int16,bytes1,uint16,string)" "ffff42000348656c6c6f2c20776f726c6421"`

Result:
```
(
  'ffff',
  '42',
  '03',
  '48656c6c6f2c20776f726c6421'
)
```

### Other commands

Use `-help` for a full list.

UTF-8 to (compact) hexadecimal: `java -jar headlong-cli-1.1-SNAPSHOT.jar -utfhexc "Hello, world!"`

Result: `('48656c6c6f2c20776f726c6421')`

### Machine-to-machine interface

#### Encode

Machine interface encode `-me` expects hex-encoded RLP data:

`java -jar headlong-cli-1.1-SNAPSHOT.jar -me "(function[2][][],bytes24,string[1][1],address[],uint72,(uint8),(int16)[2][][1],(int32)[],uint40,(int48)[],(uint),bool,string,bool[2],int24[],uint40[1])" "f4f3f298191c766e29a65787b7155dd05f41292438467db93420cade98191c766e29a65787b7155dd05f41292438467db93420cade98191c766e29a65787b7155dd05f41292438467db93420cadec2c17ad594ff00ee01dd02cc03cafebabe990688077708660989fdfffffffffffffe04c107c8c7c6c109c382fff5c8c111c584ffffffed85fca527923bcac17ec786ffffffffff82c10a01866661726f7574c20101c6031483fffffac584fffffffe"`

#### Decode

Machine interface decode `-md` results in hex-encoded RLP data:

`f4f3f298191c766e29a65787b7155dd05f41292438467db93420cade98191c766e29a65787b7155dd05f41292438467db93420cade98191c766e29a65787b7155dd05f41292438467db93420cadec2c17ad594ff00ee01dd02cc03cafebabe990688077708660989fdfffffffffffffe04c107c8c7c6c109c382fff5c8c111c584ffffffed85fca527923bcac17ec786ffffffffff82c10a01866661726f7574c20101c6031483fffffac584fffffffe`
