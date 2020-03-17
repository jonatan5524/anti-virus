rule DCP_BLOWFISH_Init {
	meta:
		author = "_pusher_"
		description = "Look for DCP Blowfish Init"
		date = "2016-07"
	strings:
		$c0 = { 53 56 57 55 8B F2 8B F8 8B CF B2 01 A1 ?? ?? ?? ?? E8 ?? ?? ?? ?? 8B D8 8B C3 8B 10 FF 52 34 8B C6 E8 ?? ?? ?? ?? 50 8B C6 E8 ?? ?? ?? ?? 8B D0 8B C3 59 8B 30 FF 56 3C 8B 43 3C 85 C0 79 03 83 C0 07 C1 F8 03 E8 ?? ?? ?? ?? 8B F0 8B D6 8B C3 8B 08 FF 51 40 8B 47 40 8B 6B 3C 3B C5 7D 0F 6A 00 8B C8 8B D6 8B C7 8B 38 FF 57 30 EB 0D 6A 00 8B D6 8B CD 8B C7 8B 38 FF 57 30 8B 53 3C 85 D2 79 03 83 C2 07 C1 FA 03 8B C6 B9 FF 00 00 00 E8 ?? ?? ?? ?? 8B 53 3C 85 D2 79 03 83 C2 07 C1 FA 03 8B C6 E8 ?? ?? ?? ?? 8B C3 E8 ?? ?? ?? ?? 5D 5F 5E 5B C3 }
	condition:
		$c0
}