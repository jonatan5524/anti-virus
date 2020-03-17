
rule DCP_BLOWFISH_EncryptCBC {
	meta:
		author = "_pusher_"
		description = "Look for DCP Blowfish EncryptCBC"
		date = "2016-07"
	strings:
		$c0 = { 55 8B EC 83 C4 F0 53 56 57 89 4D F8 89 55 FC 8B D8 80 7B 34 00 75 16 B9 ?? ?? ?? ?? B2 01 A1 ?? ?? ?? ?? E8 ?? ?? ?? ?? E8 ?? ?? ?? ?? 8B 7D 08 85 FF 79 03 83 C7 07 C1 FF 03 85 FF 7E 56 BE 01 00 00 00 6A 08 8B 45 FC 8B D6 4A C1 E2 03 03 C2 8D 4D F0 8D 53 54 E8 ?? ?? ?? ?? 8D 4D F0 8D 55 F0 8B C3 E8 ?? ?? ?? ?? 8B 55 F8 8B C6 48 C1 E0 03 03 D0 8D 45 F0 B9 08 00 00 00 E8 ?? ?? ?? ?? 8D 53 54 8D 45 F0 B9 08 00 00 00 E8 ?? ?? ?? ?? 46 4F 75 AF 8B 75 08 81 E6 07 00 00 80 79 05 4E 83 CE F8 46 85 F6 74 26 8D 4D F0 8D 53 54 8B C3 E8 ?? ?? ?? ?? 56 8B 4D F8 03 4D 08 2B CE 8B 55 FC 03 55 08 2B D6 8D 45 F0 E8 ?? ?? ?? ?? 8D 45 F0 B9 FF 00 00 00 BA 08 00 00 00 E8 ?? ?? ?? ?? 5F 5E 5B 8B E5 5D C2 04 00 }
	condition:
		$c0
}