rule RsaRef2_NN_modExp
{	meta:
		author = "Maxx"
		description = "RsaRef2 NN_modExp"
	strings:
		$c0 = { 81 EC 1C 02 00 00 53 55 56 8B B4 24 30 02 00 00 57 8B BC 24 44 02 00 00 57 8D 84 24 A4 00 00 00 56 50 E8 ?? ?? ?? ?? 8B 9C 24 4C 02 00 00 57 53 8D 8C 24 B4 00 00 00 56 8D 94 24 3C 01 00 00 51 52 E8 ?? ?? ?? ?? 57 53 8D 84 24 4C 01 00 00 56 8D 8C 24 D4 01 00 00 50 51 E8 ?? ?? ?? ?? 8D 54 24 50 57 52 E8 ?? ?? ?? ?? 8B 84 24 78 02 00 00 8B B4 24 74 02 00 00 50 56 C7 44 24 60 01 00 00 00 E8 ?? ?? ?? ?? 8D 48 FF 83 C4 44 8B E9 89 4C 24 18 85 ED 0F 8C AF 00 00 00 8D 34 AE 89 74 24 }
	condition:
		any of them
}