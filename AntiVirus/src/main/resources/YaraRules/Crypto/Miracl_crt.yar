rule Miracl_powmod
{	meta:
		author = "Maxx"
		description = "Miracl powmod"
	strings:
		$c0 = { 53 55 56 57 E8 ?? ?? ?? ?? 8B F0 8B 86 18 02 00 00 85 C0 0F 85 EC 01 00 00 8B 56 1C 42 8B C2 89 56 1C 83 F8 18 7D 17 C7 44 86 20 12 00 00 00 8B 86 2C 02 00 00 85 C0 74 05 E8 ?? ?? ?? ?? 8B 06 8B 4E 10 3B C1 74 2E 8B 7C 24 1C 57 E8 ?? ?? ?? ?? 83 C4 04 83 F8 02 7C 33 8B 57 04 8B 0E 51 8B 02 50 E8 ?? ?? ?? ?? 83 C4 08 83 F8 01 0F 84 58 01 00 00 EB 17 8B 7C 24 1C 6A 02 57 E8 ?? ?? ?? ?? 83 C4 08 85 C0 0F 84 3F 01 00 00 8B 8E C4 01 00 00 8B 54 24 18 51 52 E8 ?? ?? ?? ?? 8B 86 CC }
	condition:
		$c0
}