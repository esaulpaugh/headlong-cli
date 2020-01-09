# headlong-cli
Command line interface for https://github.com/esaulpaugh/headlong ABI encoding (full ABIv2 support). Licensed under Apache 2.0 terms.

### Build

Run `gradle fatJar` which outputs headlong-cli-0.1-SNAPSHOT.jar to /build/libs

### Encode example 1

#### cmd, Git Bash

`java -jar headlong-cli-0.1-SNAPSHOT.jar -e -n "(uint112)" "[{"type":"string","value":"0x5d92d2a10d4e107b1d"}]"`

#### Windows Power Shell, Git Bash

`java -jar headlong-cli-0.1-SNAPSHOT.jar -e -n '(uint112)' '[{"type":"string","value":"0x5d92d2a10d4e107b1d"}]'`

Result:

`00000000000000000000000000000000000000000000005d92d2a10d4e107b1d`

### Encode example 2 (cmd, Git Bash)

`java -jar headlong-cli-0.1-SNAPSHOT.jar -e -a "[\"tuple(tuple(bytes30,tuple(tuple(tuple(int168,bytes,string))),bytes)[],address)[3]\",\"bytes14\",\"address\"]" "[[{\"type\":\"tuple\",\"value\":[[{\"type\":\"tuple\",\"value\":[{\"type\":\"buffer\",\"value\":\"0x8df4683536fd6103d3c19a560bf5f15e03a364e6f5b9b939bb907ba09e03\"},{\"type\":\"tuple\",\"value\":[{\"type\":\"tuple\",\"value\":[{\"type\":\"tuple\",\"value\":[{\"type\":\"number\",\"value\":\"3921882435194190759439980220076233830451453157\"},{\"type\":\"buffer\",\"value\":\"0xf7e1cb8ea3b9b7d86cfbadd1fd959f6533cabe8118d2484b5567b111836fef223d33a319c77560d36499ff3d11837f5f31499169992e4d8b3dca6e0d1eb5b4\"},{\"type\":\"string\",\"value\":\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exerci\"}]}]}]},{\"type\":\"buffer\",\"value\":\"0x29ca871067f1dba7822f32d8753e94c55bb05cf82605e7c531428f6f6dd88ccec5f04ef820059391a67a9050464e86c91ed14e8ec7b0eea5746e6e\"}]},{\"type\":\"tuple\",\"value\":[{\"type\":\"buffer\",\"value\":\"0x8df4683536fd6103d3c19a560bf5f15e03a364e6f5b9b939bb907ba09e03\"},{\"type\":\"tuple\",\"value\":[{\"type\":\"tuple\",\"value\":[{\"type\":\"tuple\",\"value\":[{\"type\":\"number\",\"value\":\"3921882435194190759439980220076233830451453157\"},{\"type\":\"buffer\",\"value\":\"0xf7e1cb8ea3b9b7d86cfbadd1fd959f6533cabe8118d2484b5567b111836fef223d33a319c77560d36499ff3d11837f5f31499169992e4d8b3dca6e0d1eb5b4\"},{\"type\":\"string\",\"value\":\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exerci\"}]}]}]},{\"type\":\"buffer\",\"value\":\"0x29ca871067f1dba7822f32d8753e94c55bb05cf82605e7c531428f6f6dd88ccec5f04ef820059391a67a9050464e86c91ed14e8ec7b0eea5746e6e\"}]}],{\"type\":\"string\",\"value\":\"0x2C31162FeA49C10Be4e8a6745352DC13c3FA332f\"}]},{\"type\":\"tuple\",\"value\":[[{\"type\":\"tuple\",\"value\":[{\"type\":\"buffer\",\"value\":\"0x8df4683536fd6103d3c19a560bf5f15e03a364e6f5b9b939bb907ba09e03\"},{\"type\":\"tuple\",\"value\":[{\"type\":\"tuple\",\"value\":[{\"type\":\"tuple\",\"value\":[{\"type\":\"number\",\"value\":\"3921882435194190759439980220076233830451453157\"},{\"type\":\"buffer\",\"value\":\"0xf7e1cb8ea3b9b7d86cfbadd1fd959f6533cabe8118d2484b5567b111836fef223d33a319c77560d36499ff3d11837f5f31499169992e4d8b3dca6e0d1eb5b4\"},{\"type\":\"string\",\"value\":\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exerci\"}]}]}]},{\"type\":\"buffer\",\"value\":\"0x29ca871067f1dba7822f32d8753e94c55bb05cf82605e7c531428f6f6dd88ccec5f04ef820059391a67a9050464e86c91ed14e8ec7b0eea5746e6e\"}]},{\"type\":\"tuple\",\"value\":[{\"type\":\"buffer\",\"value\":\"0x8df4683536fd6103d3c19a560bf5f15e03a364e6f5b9b939bb907ba09e03\"},{\"type\":\"tuple\",\"value\":[{\"type\":\"tuple\",\"value\":[{\"type\":\"tuple\",\"value\":[{\"type\":\"number\",\"value\":\"3921882435194190759439980220076233830451453157\"},{\"type\":\"buffer\",\"value\":\"0xf7e1cb8ea3b9b7d86cfbadd1fd959f6533cabe8118d2484b5567b111836fef223d33a319c77560d36499ff3d11837f5f31499169992e4d8b3dca6e0d1eb5b4\"},{\"type\":\"string\",\"value\":\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exerci\"}]}]}]},{\"type\":\"buffer\",\"value\":\"0x29ca871067f1dba7822f32d8753e94c55bb05cf82605e7c531428f6f6dd88ccec5f04ef820059391a67a9050464e86c91ed14e8ec7b0eea5746e6e\"}]}],{\"type\":\"string\",\"value\":\"0x2C31162FeA49C10Be4e8a6745352DC13c3FA332f\"}]},{\"type\":\"tuple\",\"value\":[[{\"type\":\"tuple\",\"value\":[{\"type\":\"buffer\",\"value\":\"0x8df4683536fd6103d3c19a560bf5f15e03a364e6f5b9b939bb907ba09e03\"},{\"type\":\"tuple\",\"value\":[{\"type\":\"tuple\",\"value\":[{\"type\":\"tuple\",\"value\":[{\"type\":\"number\",\"value\":\"3921882435194190759439980220076233830451453157\"},{\"type\":\"buffer\",\"value\":\"0xf7e1cb8ea3b9b7d86cfbadd1fd959f6533cabe8118d2484b5567b111836fef223d33a319c77560d36499ff3d11837f5f31499169992e4d8b3dca6e0d1eb5b4\"},{\"type\":\"string\",\"value\":\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exerci\"}]}]}]},{\"type\":\"buffer\",\"value\":\"0x29ca871067f1dba7822f32d8753e94c55bb05cf82605e7c531428f6f6dd88ccec5f04ef820059391a67a9050464e86c91ed14e8ec7b0eea5746e6e\"}]},{\"type\":\"tuple\",\"value\":[{\"type\":\"buffer\",\"value\":\"0x8df4683536fd6103d3c19a560bf5f15e03a364e6f5b9b939bb907ba09e03\"},{\"type\":\"tuple\",\"value\":[{\"type\":\"tuple\",\"value\":[{\"type\":\"tuple\",\"value\":[{\"type\":\"number\",\"value\":\"3921882435194190759439980220076233830451453157\"},{\"type\":\"buffer\",\"value\":\"0xf7e1cb8ea3b9b7d86cfbadd1fd959f6533cabe8118d2484b5567b111836fef223d33a319c77560d36499ff3d11837f5f31499169992e4d8b3dca6e0d1eb5b4\"},{\"type\":\"string\",\"value\":\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exerci\"}]}]}]},{\"type\":\"buffer\",\"value\":\"0x29ca871067f1dba7822f32d8753e94c55bb05cf82605e7c531428f6f6dd88ccec5f04ef820059391a67a9050464e86c91ed14e8ec7b0eea5746e6e\"}]}],{\"type\":\"string\",\"value\":\"0x2C31162FeA49C10Be4e8a6745352DC13c3FA332f\"}]}],{\"type\":\"buffer\",\"value\":\"0x5e0c0d1d0b8c31f9169ba498edf5\"},{\"type\":\"string\",\"value\":\"0xeC61270DBe62871a4e2Ca6EB0C075dE6eD515198\"}]"`

Result:

`00000000000000000000000000000000000000000000000000000000000000605e0c0d1d0b8c31f9169ba498edf5000000000000000000000000000000000000000000000000000000000000ec61270dbe62871a4e2ca6eb0c075de6ed515198000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000006400000000000000000000000000000000000000000000000000000000000000c2000000000000000000000000000000000000000000000000000000000000000400000000000000000000000002c31162fea49c10be4e8a6745352dc13c3fa332f0000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000004000000000000000000000000000000000000000000000000000000000000002e08df4683536fd6103d3c19a560bf5f15e03a364e6f5b9b939bb907ba09e030000000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000002400000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000afdd009e2eaeb7da84c4e49746b4d2555d1ce5000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000000c0000000000000000000000000000000000000000000000000000000000000003ff7e1cb8ea3b9b7d86cfbadd1fd959f6533cabe8118d2484b5567b111836fef223d33a319c77560d36499ff3d11837f5f31499169992e4d8b3dca6e0d1eb5b40000000000000000000000000000000000000000000000000000000000000000a84c6f72656d20697073756d20646f6c6f722073697420616d65742c20636f6e73656374657475722061646970697363696e6720656c69742c2073656420646f20656975736d6f642074656d706f7220696e6369646964756e74207574206c61626f726520657420646f6c6f7265206d61676e6120616c697175612e20557420656e696d206164206d696e696d2076656e69616d2c2071756973206e6f737472756420657865726369000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003b29ca871067f1dba7822f32d8753e94c55bb05cf82605e7c531428f6f6dd88ccec5f04ef820059391a67a9050464e86c91ed14e8ec7b0eea5746e6e00000000008df4683536fd6103d3c19a560bf5f15e03a364e6f5b9b939bb907ba09e030000000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000002400000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000afdd009e2eaeb7da84c4e49746b4d2555d1ce5000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000000c0000000000000000000000000000000000000000000000000000000000000003ff7e1cb8ea3b9b7d86cfbadd1fd959f6533cabe8118d2484b5567b111836fef223d33a319c77560d36499ff3d11837f5f31499169992e4d8b3dca6e0d1eb5b40000000000000000000000000000000000000000000000000000000000000000a84c6f72656d20697073756d20646f6c6f722073697420616d65742c20636f6e73656374657475722061646970697363696e6720656c69742c2073656420646f20656975736d6f642074656d706f7220696e6369646964756e74207574206c61626f726520657420646f6c6f7265206d61676e6120616c697175612e20557420656e696d206164206d696e696d2076656e69616d2c2071756973206e6f737472756420657865726369000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003b29ca871067f1dba7822f32d8753e94c55bb05cf82605e7c531428f6f6dd88ccec5f04ef820059391a67a9050464e86c91ed14e8ec7b0eea5746e6e000000000000000000000000000000000000000000000000000000000000000000000000400000000000000000000000002c31162fea49c10be4e8a6745352dc13c3fa332f0000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000004000000000000000000000000000000000000000000000000000000000000002e08df4683536fd6103d3c19a560bf5f15e03a364e6f5b9b939bb907ba09e030000000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000002400000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000afdd009e2eaeb7da84c4e49746b4d2555d1ce5000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000000c0000000000000000000000000000000000000000000000000000000000000003ff7e1cb8ea3b9b7d86cfbadd1fd959f6533cabe8118d2484b5567b111836fef223d33a319c77560d36499ff3d11837f5f31499169992e4d8b3dca6e0d1eb5b40000000000000000000000000000000000000000000000000000000000000000a84c6f72656d20697073756d20646f6c6f722073697420616d65742c20636f6e73656374657475722061646970697363696e6720656c69742c2073656420646f20656975736d6f642074656d706f7220696e6369646964756e74207574206c61626f726520657420646f6c6f7265206d61676e6120616c697175612e20557420656e696d206164206d696e696d2076656e69616d2c2071756973206e6f737472756420657865726369000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003b29ca871067f1dba7822f32d8753e94c55bb05cf82605e7c531428f6f6dd88ccec5f04ef820059391a67a9050464e86c91ed14e8ec7b0eea5746e6e00000000008df4683536fd6103d3c19a560bf5f15e03a364e6f5b9b939bb907ba09e030000000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000002400000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000afdd009e2eaeb7da84c4e49746b4d2555d1ce5000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000000c0000000000000000000000000000000000000000000000000000000000000003ff7e1cb8ea3b9b7d86cfbadd1fd959f6533cabe8118d2484b5567b111836fef223d33a319c77560d36499ff3d11837f5f31499169992e4d8b3dca6e0d1eb5b40000000000000000000000000000000000000000000000000000000000000000a84c6f72656d20697073756d20646f6c6f722073697420616d65742c20636f6e73656374657475722061646970697363696e6720656c69742c2073656420646f20656975736d6f642074656d706f7220696e6369646964756e74207574206c61626f726520657420646f6c6f7265206d61676e6120616c697175612e20557420656e696d206164206d696e696d2076656e69616d2c2071756973206e6f737472756420657865726369000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003b29ca871067f1dba7822f32d8753e94c55bb05cf82605e7c531428f6f6dd88ccec5f04ef820059391a67a9050464e86c91ed14e8ec7b0eea5746e6e000000000000000000000000000000000000000000000000000000000000000000000000400000000000000000000000002c31162fea49c10be4e8a6745352dc13c3fa332f0000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000004000000000000000000000000000000000000000000000000000000000000002e08df4683536fd6103d3c19a560bf5f15e03a364e6f5b9b939bb907ba09e030000000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000002400000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000afdd009e2eaeb7da84c4e49746b4d2555d1ce5000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000000c0000000000000000000000000000000000000000000000000000000000000003ff7e1cb8ea3b9b7d86cfbadd1fd959f6533cabe8118d2484b5567b111836fef223d33a319c77560d36499ff3d11837f5f31499169992e4d8b3dca6e0d1eb5b40000000000000000000000000000000000000000000000000000000000000000a84c6f72656d20697073756d20646f6c6f722073697420616d65742c20636f6e73656374657475722061646970697363696e6720656c69742c2073656420646f20656975736d6f642074656d706f7220696e6369646964756e74207574206c61626f726520657420646f6c6f7265206d61676e6120616c697175612e20557420656e696d206164206d696e696d2076656e69616d2c2071756973206e6f737472756420657865726369000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003b29ca871067f1dba7822f32d8753e94c55bb05cf82605e7c531428f6f6dd88ccec5f04ef820059391a67a9050464e86c91ed14e8ec7b0eea5746e6e00000000008df4683536fd6103d3c19a560bf5f15e03a364e6f5b9b939bb907ba09e030000000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000002400000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000afdd009e2eaeb7da84c4e49746b4d2555d1ce5000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000000c0000000000000000000000000000000000000000000000000000000000000003ff7e1cb8ea3b9b7d86cfbadd1fd959f6533cabe8118d2484b5567b111836fef223d33a319c77560d36499ff3d11837f5f31499169992e4d8b3dca6e0d1eb5b40000000000000000000000000000000000000000000000000000000000000000a84c6f72656d20697073756d20646f6c6f722073697420616d65742c20636f6e73656374657475722061646970697363696e6720656c69742c2073656420646f20656975736d6f642074656d706f7220696e6369646964756e74207574206c61626f726520657420646f6c6f7265206d61676e6120616c697175612e20557420656e696d206164206d696e696d2076656e69616d2c2071756973206e6f737472756420657865726369000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003b29ca871067f1dba7822f32d8753e94c55bb05cf82605e7c531428f6f6dd88ccec5f04ef820059391a67a9050464e86c91ed14e8ec7b0eea5746e6e0000000000`

### Decode example 1 (cmd, Git Bash)

`java -jar headlong-cli-0.1-SNAPSHOT.jar -d -n "(uint112)" "00000000000000000000000000000000000000000000005d92d2a10d4e107b1d"`

Result:

`[{"type":"string","value":"0x5d92d2a10d4e107b1d"}]`

### Decode example 2 (cmd, Git Bash)

`java -jar headlong-cli-0.1-SNAPSHOT.jar -d -f "foo(uint112)" "3ce760f500000000000000000000000000000000000000000000005d92d2a10d4e107b1d"`

Result:

`[{"type":"string","value":"0x5d92d2a10d4e107b1d"}]`

### Formatting

Format of values is the same as https://github.com/esaulpaugh/headlong/blob/master/src/test/resources/tests/ethers-io/tests/tests/contract-interface-abi2.json ( https://github.com/ethers-io/ethers.js/blob/master/tests/tests/contract-interface-abi2.json.gz )
